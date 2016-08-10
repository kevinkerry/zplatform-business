package com.zlebank.zplatform.business.individual.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.acc.bean.BusiAcctQuery;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.exception.IllegalEntryRequestException;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.acc.service.entry.EntryEvent;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.business.individual.bean.enums.WechatType;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.InvalidBindIdException;
import com.zlebank.zplatform.business.individual.exception.PayPwdVerifyFailException;
import com.zlebank.zplatform.business.individual.exception.SmsCodeVerifyFailException;
import com.zlebank.zplatform.business.individual.exception.UnCheckedSystemException;
import com.zlebank.zplatform.business.individual.exception.UnknowPayWayException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.service.OrderService;
import com.zlebank.zplatform.business.individual.utils.Constants;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.dao.CardBinDao;
import com.zlebank.zplatform.commons.dao.pojo.BusiTypeEnum;
import com.zlebank.zplatform.commons.enums.BusinessCodeEnum;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.dao.CoopInstiDAO;
import com.zlebank.zplatform.member.dao.MemberDAO;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.pojo.PojoCoopInsti;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberBankCardService;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.adapter.quickpay.IQuickPayTrade;
import com.zlebank.zplatform.trade.bean.ReaPayResultBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.gateway.OrderAsynRespBean;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.dao.ConfigInfoDAO;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.exception.AbstractTradeDescribeException;
import com.zlebank.zplatform.trade.exception.BalanceNotEnoughException;
import com.zlebank.zplatform.trade.exception.FailToGetAccountInfoException;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.factory.TradeAdapterFactory;
import com.zlebank.zplatform.trade.model.ConfigInfoModel;
import com.zlebank.zplatform.trade.model.QuickpayCustModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsNotifyTaskModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.IQuickpayCustService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsNotifyTaskService;
import com.zlebank.zplatform.trade.service.ITxnsQuickpayService;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.ObjectDynamic;
import com.zlebank.zplatform.trade.utils.OrderNumber;
import com.zlebank.zplatform.wechat.qr.service.WeChatQRService;
import com.zlebank.zplatform.wechat.security.AESUtil;
import com.zlebank.zplatform.wechat.service.WeChatService;
import com.zlebank.zplatform.wechat.wx.common.SignUtil;
import com.zlebank.zplatform.wechat.wx.common.WXConfigure;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Log log = LogFactory.getLog(OrderServiceImpl.class);
    @Autowired
    private IGateWayService gateWayService;
    @Autowired
    private IOrderValidator orderValidator;
    @Autowired
    private MemberOperationService memberOperationServiceImpl;
    @Autowired
    private MemberService memberServiceImpl;
    @Autowired
    private com.zlebank.zplatform.member.service.MemberAccountService memberAccountServiceImpl;
    @Autowired
    private ISMSService smsService;
    @Autowired
    private IQuickpayCustService quickpayCustService;
    @Autowired
    CoopInstiDAO coopInstiDAO;
    @Autowired
	private MemberDAO memberDAO;
    @Autowired
	private MemberBankCardService memberBankCardService;
    @Autowired
    private CardBinDao cardBinDao;
    @Autowired
    private ConfigInfoDAO configInfoDAO;

    @Autowired
    private AccEntryService accEntryService;
    @Autowired
    private ITxnsLogService txnsLogService;
    @Autowired
    private WeChatService weChatService;
    @Autowired
    private ITxnsQuickpayService txnsQuickpayService;
    @Autowired
    private ITxnsOrderinfoDAO txnsOrderinfoDAO;
    @Autowired
    private ITxnsNotifyTaskService txnsNotifyTaskService;
    @Autowired
	private WeChatQRService weChatQRService;
    
	/**
	 *
	 * @param memberId
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public PagedResult<Order> queryOrderList(String memberId, Date startDate, Date endDate,int page, int pageSize){
		List<Map<String, Object>> orderinfoList = gateWayService.queryOrderInfo(memberId, DateUtil.formatDateTime(startDate), DateUtil.formatDateTime(endDate), page, pageSize);
		List<Order> orderList = new ArrayList<Order>();
		//ORDERNO,ORDERAMT,ORDERCOMMITIME,STATUS,ORDERDESC,TN,CURRENCYCODE
		for(Map<String, Object> valueMap:orderinfoList){
			Order order = new Order();
			order.setOrderId(valueMap.get("ORDERNO")+"");
			BigDecimal txnAmt = (BigDecimal) valueMap.get("ORDERAMT");
			order.setTxnAmt(txnAmt.longValue()+"");
			order.setTxnTime(valueMap.get("ORDERCOMMITIME")+"");
			order.setStatus(OrderStatus.fromValue(String.valueOf(valueMap.get("STATUS"))));
			order.setOrderDesc(valueMap.get("ORDERDESC")+"");
			order.setCurrencyCode(valueMap.get("CURRENCYCODE")+"");
			order.setTn(valueMap.get("TN")+"");
			orderList.add(order);
		}
		return new DefaultPageResult<Order>(orderList,gateWayService.queryOrderInfoCount(memberId, DateUtil.formatDateTime(startDate), DateUtil.formatDateTime(endDate)));
	}

	/**
	 *
	 * @param memberId
	 * @param orderNo
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public Order queryOrder(String memberId, String tn) {
		TxnsOrderinfoModel orderinfoModel = gateWayService.getOrderinfoByTN(tn);
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfoModel.getRelatetradetxn());
		Order order = new Order();
		order.setMerId(orderinfoModel.getFirmemberno());
		order.setMerName(orderinfoModel.getFirmembername());
		order.setMerAbbr(orderinfoModel.getFirmembershortname());
		order.setOrderId(orderinfoModel.getOrderno());
		order.setTxnAmt(orderinfoModel.getOrderamt()+"");
		order.setTxnTime(orderinfoModel.getOrdercommitime());
		order.setOrderTime(txnsLog.getTxndate()+txnsLog.getTxntime());
		order.setStatus(OrderStatus.fromValue(orderinfoModel.getStatus()));
		order.setOrderDesc(orderinfoModel.getOrderdesc());
		order.setCurrencyCode(orderinfoModel.getCurrencycode());
		order.setTn(orderinfoModel.getTn());
		order.setBankCode(txnsLog.getCardinstino());
		BusiTypeEnum busitype = BusiTypeEnum.fromValue(txnsLog.getBusitype());
		String code=OrderType.UNKNOW.getCode();
		if(busitype.equals(BusiTypeEnum.consumption)){
			code=OrderType.CONSUME.getCode();
		}else if(busitype.equals(BusiTypeEnum.refund)){
			code=OrderType.REFUND.getCode();
		}else if(busitype.equals(BusiTypeEnum.charge)){
			code=OrderType.RECHARGE.getCode();
		}else if(busitype.equals(BusiTypeEnum.withdrawal)){
			code=OrderType.WITHDRAW.getCode();
		}
		order.setOrderType( OrderType.fromValue(code));
		//交易类型
		order.setBusiType(txnsLog.getBusitype());
		if(ChannelEnmu.REAPAY==ChannelEnmu.fromValue(txnsLog.getPayinst())){//支付渠道为融宝时
			if(OrderStatus.fromValue(orderinfoModel.getStatus())==OrderStatus.PAYING){//订单状态为正在支付
				//调用融宝查询方法
				IQuickPayTrade quickPayTrade = null;
		        try {
		            quickPayTrade = TradeAdapterFactory.getInstance()
		                    .getQuickPayTrade(ChannelEnmu.REAPAY.getChnlcode());
		        } catch (TradeException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        } catch (ClassNotFoundException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        } catch (InstantiationException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        } catch (IllegalAccessException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        String reapayOrderNo = txnsQuickpayService.getReapayOrderNo(orderinfoModel.getRelatetradetxn());
		        TradeBean trade = new TradeBean();
		        trade.setReaPayOrderNo(reapayOrderNo);
		        ResultBean queryResultBean = null;
		        ReaPayResultBean payResult = null;
		        for (int i = 0; i < 5; i++) {
		        	txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfoModel.getRelatetradetxn());
		        	if("0000".equals(txnsLog.getPayretcode())||"3006".equals(txnsLog.getPayretcode())||"3053".equals(txnsLog.getPayretcode())||"3054".equals(txnsLog.getPayretcode())||
	                        "3056".equals(txnsLog.getPayretcode())||"3083".equals(txnsLog.getPayretcode())||"3081".equals(txnsLog.getPayretcode())){
	                    //返回这些信息时，表示融宝已经接受到交易请求，但是没有同步处理，等待异步通知
		        		
		        		try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                queryResultBean = quickPayTrade.queryTrade(trade);
		                payResult = (ReaPayResultBean) queryResultBean.getResultObj();
		                if ("completed".equalsIgnoreCase(payResult.getStatus())) {//交易完成
		                	order.setStatus(OrderStatus.SUCCESS);
		                    break;
		                }
		                if ("failed".equalsIgnoreCase(payResult.getStatus())) {//交易失败
		                	order.setStatus(OrderStatus.PAYFAILED);
		                    break;
		                }
		                if ("wait".equalsIgnoreCase(payResult.getStatus())) {//等待支付，也就是未支付，比如验证码错误，或者交易金额超限等错误，此时状态为支付失败
		                	order.setStatus(OrderStatus.PAYFAILED);
		                    break;
		                }
		                if ("processing".equalsIgnoreCase(payResult.getStatus())) {
		                    
		                }
	                }else{
	                    //订单状态更新为失败
	                	break;
	                }
	                
		        }
			}
		}
		
		
		return order;
	}

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String createOrder(Order order) throws ValidateOrderException,
            TradeException, AbstractIndividualBusinessException {
        String tn = null;
        fullNonWalletData(order);

        Map<String, String> validateResult = orderValidator
                .validateOrder(order);
        String retCode = validateResult.get(IOrderValidator.RET_CODE);
        if (retCode != null
                && !retCode.equals(IOrderValidator.RET_CODE_SUCCESS)) {
            throw new ValidateOrderException(
                    validateResult.get(IOrderValidator.RET_CODE),
                    validateResult.get(IOrderValidator.RET_MESSAGE));
        }
        try {
            tn = gateWayService.dealWithWapOrder(order);
        } catch (TradeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnCheckedSystemException();
        }
        if (tn == null || tn.equals("")) {
            throw new UnCheckedSystemException();
        }
        return tn;
    }

    private Order fullNonWalletData(Order order) {
        /*
         * 钱包接口中没有的参数,但是web收银台接口必须传入的参数
         */
        long orderTimeOutMill = 30 * 60 * 1000;
        order.setOrderTimeout(String.valueOf(orderTimeOutMill));
        // 风险信息域，非空,必须按照此格式
        order.setRiskRateInfo("merUserId=" + order.getMemberId()
                + "&commodityQty=0&commodityUnitPrice=0&");
        // 前台通知地址，非空
        if(StringUtil.isEmpty(order.getFrontUrl())){
        	order.setFrontUrl(Constants.WALLET_MISSING_FIELD_STR);
        }else{
        	order.setFrontUrl(order.getFrontUrl());
        }
        // 后台通知地址，非空
        if(StringUtil.isEmpty(order.getBackUrl())){
        	 order.setBackUrl(Constants.WALLET_MISSING_FIELD_STR);
        }else{
            order.setBackUrl(order.getBackUrl());
        }
        // 证书ID，非空
        order.setCertId(String.valueOf(Constants.WALLET_MISSING_FIELD_INT));
        return order;
    }

    @Override
    public OrderStatus pay(String order,
            String smsCode,
            String payPwd,
            PayWay payWay) throws AbstractTradeDescribeException,
            AbstractIndividualBusinessException, TradeException {
        Order orderObj = JSON.parseObject(order, Order.class);
        String memberId = orderObj.getMemberId();
        PojoMember member = memberServiceImpl.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
        if (member == null) {// 资金账户不存在
            throw new FailToGetAccountInfoException();
        }

        List<BusiAcctQuery> busiAcctList = memberServiceImpl
                .getAllBusiByMId(memberId);
        BusiAcctQuery basicFund = null;
        for (BusiAcctQuery busiAcct : busiAcctList) {
            if (busiAcct.getUsage() == Usage.BASICPAY) {
                basicFund = busiAcct;
                break;
            }
        }

        if (basicFund == null) {// 资金账户不存在
            throw new FailToGetAccountInfoException();
        }
        try {
            MemberBean memberBean = new MemberBean();
            memberBean.setLoginName(member.getLoginName());
            memberBean.setInstiId(member.getInstiId());
            memberBean.setPhone(member.getPhone());
            memberBean.setPaypwd(payPwd);
            // 校验支付密码
            if (!memberOperationServiceImpl.verifyPayPwd(MemberType.INDIVIDUAL,
                    memberBean)) {
                throw new PayPwdVerifyFailException();
            }
        } catch (DataCheckFailedException e) {
            PayPwdVerifyFailException pe = new PayPwdVerifyFailException();
            pe.initCause(e);
            throw pe;
        }

        String phoneNo = "";
        if (payWay == PayWay.ACCOUNT) {
            phoneNo = member.getPhone();
        } else if (payWay == PayWay.QUICK){
            QuickpayCustModel card = quickpayCustService.getCardByBindId(orderObj.getBindId());
            phoneNo = card.getPhone();
        }
        switch (payWay) {
            case ACCOUNT :
            	// 校验手机短信验证码
                if (smsService.verifyCode(phoneNo,orderObj.getTn(),smsCode)!=1) {
                    throw new SmsCodeVerifyFailException();
                }
                Money amount = Money.valueOf(new BigDecimal(orderObj
                        .getTxnAmt()));
                if (basicFund.getBalance().minus(amount).compareTo(Money.ZERO) < 0) {// 余额不足
                    throw new BalanceNotEnoughException();
                }
                gateWayService.accountPay(order);
                updateAnonOrderToMemberOrder(orderObj);
                break;
            case QUICK :
                QuickpayCustModel card = quickpayCustService
                        .getCardByBindId(orderObj.getBindId());
                if (card == null) {
                    throw new InvalidBindIdException();
                }
                gateWayService.submitPay(order);
                updateAnonOrderToMemberOrder(orderObj);
                break;
            default :
                throw new UnknowPayWayException();
        }

        Order orderRet = queryOrder(member.getMemberId(),orderObj.getTn());
        return orderRet.getStatus();
    }
    
    /**
     * 更新匿名订单但是登陆支付的订单信息
     * @param order
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void updateAnonOrderToMemberOrder(Order order){
    	log.info("order json:"+JSON.toJSONString(order));
    	TxnsOrderinfoModel orderinfo = gateWayService.getOrderinfoByTN(order.getTn());
    	//TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
    	String memberId = order.getMemberId();
    	String old_order_member = orderinfo.getMemberid();
    	if("999999999999999".equals(old_order_member)){
    		log.info("memberId:"+memberId);
    		log.info("old_order_member:"+old_order_member);
    		/*//更新交易流水
    		txnsLog.setAccmemberid(memberId);
    		txnsLogService.updateTxnsLog(txnsLog);
    		//更新交易订单
    		orderinfo.setMemberid(memberId);
    		txnsOrderinfoDAO.updateOrderinfo(orderinfo);*/
    		txnsLogService.updateAnonOrderToMemberOrder(orderinfo.getRelatetradetxn(), memberId);
    		
    	}
    	
    	
    	
    }
    
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public OrderStatus anonymousPay(String order,String smsCode) throws AbstractTradeDescribeException,
            AbstractIndividualBusinessException, TradeException {
        Order orderObj = JSON.parseObject(order, Order.class);
        String memberId = "999999999999999";
        String bindId = orderObj.getBindId();
        String cardNo = orderObj.getCardNo();
        String phoneNo = "";
        QuickpayCustModel card = null;
        if(StringUtil.isNotEmpty(bindId)&&StringUtil.isNotEmpty(cardNo)){//绑卡标示和卡信息同时不为空时
        	card = quickpayCustService.getCardByBindId(orderObj.getBindId());
        	if (card == null) {
                throw new InvalidBindIdException();
            }
        	//绑卡表中的卡信息和参数卡信息进行比较
        	if(!card.getAccname().equals(orderObj.getAccNo())||!card.getCardno().equals(orderObj.getCardNo())||
        			!card.getIdnum().equals(orderObj.getCertifId())||!card.getPhone().equals(orderObj.getPhoneNo())){
        		throw new InvalidBindIdException();
        	}
        	phoneNo = card.getPhone();
        }else if(StringUtil.isNotEmpty(bindId)){
        	card = quickpayCustService.getCardByBindId(orderObj.getBindId());
        	phoneNo = card.getPhone();
        }else if(StringUtil.isNotEmpty(cardNo)){
        	List<QuickpayCustModel> cardList =this.quickpayCustService.getCardList(cardNo,orderObj.getCustomerNm(),orderObj.getPhoneNo(),orderObj.getCertifId(),memberId);
        			//(List<QuickpayCustModel>) quickpayCustService.queryByHQL("from QuickpayCustModel where cardno=? and accname = ? and phone = ? and idnum = ? and relatememberno = ? and status = ?", new Object[]{cardNo,orderObj.getCustomerNm(),orderObj.getPhoneNo(),orderObj.getCertifId(),memberId,"00"});
        	if(cardList.size()>0){
        		orderObj.setBindId(cardList.get(0).getId()+"");
        	}else{
        		throw new InvalidBindIdException();
        	}
        }
        // 校验手机短信验证码
        /*if (smsService.verifyCode(phoneNo,orderObj.getTn(),smsCode)!=1) {
            throw new SmsCodeVerifyFailException();
        }*/
        orderObj.setSmsCode(smsCode);
        gateWayService.submitPay(JSON.toJSONString(orderObj));
        Order orderRet = queryOrder(memberId,orderObj.getTn());
        return orderRet.getStatus();
    }
    
    /**
     * 匿名支付 实名认证
     * @param individualRealInfo 卡实名认证信息
     * @param memberId 商户号
     * @return
     * @throws DataCheckFailedException
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public ResultBean anonymousRealName(IndividualRealInfo individualRealInfo,String memberId) {
    	ResultBean resultBean = null;
    	try {
			PojoMember pm = memberDAO.getMemberByMemberId(memberId, null);
			// 【实名认证】
			PojoCoopInsti pojoCoopInsti = coopInstiDAO.get(pm.getInstiId());
			WapCardBean cardBean = new WapCardBean(individualRealInfo.getCardNo(), individualRealInfo.getCardType(), individualRealInfo.getCustomerName(), 
			        individualRealInfo.getCertifType(), individualRealInfo.getCertifNo(), individualRealInfo.getPhoneNo(), individualRealInfo.getCvn2(), 
			        individualRealInfo.getExpired());
			resultBean = gateWayService.bindingBankCard(pojoCoopInsti.getInstiCode(), memberId, cardBean);
			if(resultBean.isResultBool()){
			    //保存绑卡信息
			    QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
			    quickpayCustBean.setCustomerno(pojoCoopInsti.getInstiCode());
			    quickpayCustBean.setCardno(individualRealInfo.getCardNo());
			    quickpayCustBean.setCardtype(individualRealInfo.getCardType());
			    quickpayCustBean.setAccname(individualRealInfo.getCustomerName());
			    quickpayCustBean.setPhone(individualRealInfo.getPhoneNo());
			    quickpayCustBean.setIdtype(individualRealInfo.getCertifType());
			    quickpayCustBean.setIdnum(individualRealInfo.getCertifNo());
			    quickpayCustBean.setCvv2(individualRealInfo.getCvn2());
			    quickpayCustBean.setValidtime(individualRealInfo.getExpired());
			    quickpayCustBean.setRelatememberno("999999999999999");
			    //新增设备ID支持匿名支付
			    quickpayCustBean.setDevId(individualRealInfo.getDevId());
			    CardBin cardBin = cardBinDao.getCard(individualRealInfo.getCardNo());
			    quickpayCustBean.setBankcode(cardBin.getBankCode());
			    quickpayCustBean.setBankname(cardBin.getBankName());
			    long bindId = memberBankCardService.saveQuickPayCust(quickpayCustBean);
			    
			    boolean isCost = false;
			    BusinessCodeEnum busiCode = BusinessCodeEnum.REALNAME_AUTH_COST;
			    //记录实名认证手续费
			    ConfigInfoModel startTime = configInfoDAO.getConfigByParaName("REALNAME_AUTH_PRICE");
			    // 记录分录流水
			    TradeInfo tradeInfo = new TradeInfo();
			    tradeInfo.setTxnseqno(OrderNumber.getInstance().generateTxnseqno("8000"));
			    tradeInfo.setAmount(BigDecimal.ZERO);
			    tradeInfo.setCommission(BigDecimal.ZERO);
			    tradeInfo.setAmountD(BigDecimal.ZERO);
			    tradeInfo.setAmountE(BigDecimal.ZERO);
			    tradeInfo.setBusiCode(busiCode.getBusiCode());
			    tradeInfo.setPayMemberId(memberId);
			    tradeInfo.setCharge(new BigDecimal(startTime.getPara()));// 手续费
			    
			    tradeInfo.setChannelId(ChannelEnmu.CMBCWITHHOLDING.getChnlcode());
			    tradeInfo.setCoopInstCode(pojoCoopInsti.getInstiCode());
			    accEntryService.accEntryProcess(tradeInfo,EntryEvent.TRADE_SUCCESS);
			    resultBean = new ResultBean(bindId);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			resultBean = new ResultBean("", "数字格式化异常");
		} catch (AccBussinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultBean = new ResultBean(e.getCode(), e.getMessage());
		} catch (AbstractBusiAcctException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultBean = new ResultBean(e.getCode(), e.getMessage());
		} catch (IllegalEntryRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultBean;
	}
    
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String createRefundOrder(Order order) throws ValidateOrderException,
    TradeException, AbstractIndividualBusinessException{
    	
    	String tn = null;
        fullNonWalletData(order);

        Map<String, String> validateResult = orderValidator
                .validateOrder(order);
        String retCode = validateResult.get(IOrderValidator.RET_CODE);
        if (retCode != null
                && !retCode.equals(IOrderValidator.RET_CODE_SUCCESS)) {
            throw new ValidateOrderException(
                    validateResult.get(IOrderValidator.RET_CODE),
                    validateResult.get(IOrderValidator.RET_MESSAGE));
        }
        try {
            tn = gateWayService.refund(JSON.toJSONString(order));
        } catch (TradeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnCheckedSystemException();
        }
        if (tn == null || tn.equals("")) {
            throw new UnCheckedSystemException();
        }
        return tn;
    }
    
    public JSONObject createWeChatOrder(String tn){
    	return weChatService.creatOrder(tn);
    }
    
    public Long getRefundFee(String txnseqno,String merchNo,String txnAmt,String busicode){
    	return gateWayService.getRefundFee(txnseqno, merchNo, txnAmt, busicode);
    }

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public ResultBean queryWechatOrder(String tn) {
		TradeBean trade = new TradeBean();
		trade.setTn(tn);
		return this.weChatService.queryWechatOrder(trade);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public ResultBean cashPay(Order orderObj,String smsCode,String memberId)  {
        ResultBean result= null;
        Map<String,Object> map = new HashMap<String, Object>();
        TxnsOrderinfoModel orderinfoModel = gateWayService.getOrderinfoByTN(orderObj.getTn());
        if(orderinfoModel==null){
        	result =new ResultBean("","无效的交易信息");
        	return result;
        }
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfoModel.getRelatetradetxn());
		if(txnsLog == null){
			result =new ResultBean("","无效的交易信息");
			return result;
		}
		map.put("fronturl", orderinfoModel.getFronturl());
        memberId = null==memberId?"999999999999999":memberId;
        String bindId = orderObj.getBindId();
        String cardNo = orderObj.getCardNo();
        String phoneNo = "";
        QuickpayCustModel card = null;
        if(StringUtil.isNotEmpty(bindId)&&StringUtil.isNotEmpty(cardNo)){//绑卡标示和卡信息同时不为空时
        	card = quickpayCustService.getCardByBindId(orderObj.getBindId());
        	if (card == null) {
                //throw new InvalidBindIdException();
        		result =new ResultBean("", "无效的卡信息");
        		result.setResultObj(map);
        		return result;
            }
        	//绑卡表中的卡信息和参数卡信息进行比较
        	if(!card.getAccname().equals(orderObj.getAccNo())||!card.getCardno().equals(orderObj.getCardNo())||
        			!card.getIdnum().equals(orderObj.getCertifId())||!card.getPhone().equals(orderObj.getPhoneNo())){
        		result =new ResultBean("", "请检查输入的卡信息");
        		result.setResultObj(map);
        		return result;
        	}
        	phoneNo = card.getPhone();
        }else if(StringUtil.isNotEmpty(bindId)){
        	card = quickpayCustService.getCardByBindId(orderObj.getBindId());
        	phoneNo = card.getPhone();
        }else if(StringUtil.isNotEmpty(cardNo)){
        	List<QuickpayCustModel> cardList =this.quickpayCustService.getCardList(cardNo,orderObj.getCustomerNm(),orderObj.getPhoneNo(),orderObj.getCertifId(),memberId);
        	if(cardList.size()>0){
        		orderObj.setBindId(cardList.get(0).getId()+"");
        	}else{
        		result =new ResultBean("", "请检查输入的卡信息");
        		result.setResultObj(map);
        		return result;
        	}
        }
        orderObj.setSmsCode(smsCode);
        try {
			gateWayService.submitPay(JSON.toJSONString(orderObj));
		} catch (TradeException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			result =new ResultBean(e.getCode(), e.getMessage());
    		result.setResultObj(map);
    		return result;
		}
        Order orderRet = queryOrder(memberId,orderObj.getTn());
        map.put("orderStatus", orderRet.getStatus());
        if(orderRet.getStatus().equals(OrderStatus.SUCCESS)){
        	ResultBean orderResp = gateWayService.generateAsyncRespMessage(txnsLog.getTxnseqno());
        	OrderAsynRespBean respBean = (OrderAsynRespBean) orderResp.getResultObj();
        	TxnsNotifyTaskModel task;
			try {
				task = new TxnsNotifyTaskModel(
						orderinfoModel.getFirmemberno(),
						orderinfoModel.getRelatetradetxn(), 1, 1,
				        ObjectDynamic.generateReturnParamer(respBean, false, null),
				        "00", "200", orderinfoModel.getFronturl(), "2");
				txnsNotifyTaskService.saveTask(task);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				result =new ResultBean("","保存前台通知失败");
	    		result.setResultObj(map);
	    		return result;
			}
             
        }
        //返回url及状态
        return new ResultBean(map);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public JSONObject createWechatOrder(String tn, String typeId) throws TradeException {
		JSONObject result = null;
		if(StringUtil.isEmpty(typeId)||StringUtil.isEmpty(tn)){
			throw new TradeException("", "tn或typeId不能为空！");
		}
		//微信APP支付
		if(typeId.equals(WechatType.APP.getTypeId())){
			result= weChatService.creatOrder(tn);
		//扫码支付	
		}else if(typeId.equals(WechatType.RQCODE.getTypeId())){
			result=this.weChatQRService.creatOrder(tn);
		}
		return result;
		
	}

	@Override
	public ResultBean queryWechatOrder(String tn, String typeId)throws TradeException {
		ResultBean result = null;
		if(StringUtil.isEmpty(typeId)||StringUtil.isEmpty(tn)){
			throw new TradeException("", "tn或typeId不能为空！");
		}
		TradeBean trade = new TradeBean();
		trade.setTn(tn);
		//微信APP支付
		if(typeId.equals(WechatType.APP.getTypeId())){
			result =this.weChatService.queryWechatOrder(trade);
		//扫码支付	
		}else if(typeId.equals(WechatType.RQCODE.getTypeId())){
			result=this.weChatQRService.queryWechatOrder(trade);
		}
		
		return result;
	}
}
