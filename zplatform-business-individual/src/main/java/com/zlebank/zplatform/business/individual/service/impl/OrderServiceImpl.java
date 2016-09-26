package com.zlebank.zplatform.business.individual.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.acc.bean.BusiAcctQuery;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.acc.service.entry.EntryEvent;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.ExcepitonTypeEnum;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.business.individual.bean.enums.WechatType;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.business.individual.service.OrderService;
import com.zlebank.zplatform.business.individual.utils.Constants;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.dao.pojo.BusiTypeEnum;
import com.zlebank.zplatform.commons.enums.BusinessCodeEnum;
import com.zlebank.zplatform.member.bean.CoopInsti;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.rmi.acc.IAccEntryService;
import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.rmi.member.ICoopInstiService;
import com.zlebank.zplatform.rmi.member.IMemberAccountService;
import com.zlebank.zplatform.rmi.member.IMemberBankCardService;
import com.zlebank.zplatform.rmi.member.IMemberOperationService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.trade.CardBinServiceProxy;
import com.zlebank.zplatform.rmi.trade.ConfigInfoServiceProxy;
import com.zlebank.zplatform.rmi.trade.GateWayServiceProxy;
import com.zlebank.zplatform.rmi.trade.TradeQueryServiceProxy;
import com.zlebank.zplatform.rmi.trade.TxnsLogServiceProxy;
import com.zlebank.zplatform.rmi.trade.TxnsNotifyTaskServiceProxy;
import com.zlebank.zplatform.rmi.trade.TxnsQuickpayServiceProxy;
import com.zlebank.zplatform.rmi.trade.WeChatQRServiceProxy;
import com.zlebank.zplatform.rmi.trade.WeChatServiceProxy;
import com.zlebank.zplatform.trade.bean.CardBinBean;
import com.zlebank.zplatform.trade.bean.ReaPayResultBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.OrderStatusEnum;
import com.zlebank.zplatform.trade.bean.gateway.OrderAsynRespBean;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.model.ConfigInfoModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.trade.utils.StringUtil;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Log log = LogFactory.getLog(OrderServiceImpl.class);
    @Autowired
    private GateWayServiceProxy gateWayService;
    @Autowired
    private IOrderValidator orderValidator;
    @Autowired
    private IMemberOperationService memberOperationServiceImpl;
    @Autowired
    private IMemberService memberServiceImpl;
    @Autowired
    private IMemberAccountService memberAccountServiceImpl;
    @Autowired
    private SMSServiceProxy smsService;
   // @Autowired
   //private IQuickpayCustService quickpayCustService;
   
    /*@Autowired
	private MemberDAO memberDAO;*/
   
    
    @Autowired
    private IMemberService memberService;
    
    @Autowired
	private IMemberBankCardService memberBankCardService;
    @Autowired
    //private CardBinDao cardBinDao;
    private CardBinServiceProxy cardBinService;
    @Autowired
    //private ConfigInfoDAO configInfoDAO;
    private ConfigInfoServiceProxy configInfoService;
    @Autowired
    private IAccEntryService accEntryService;
    @Autowired
    private TxnsLogServiceProxy txnsLogService;
    @Autowired
    private WeChatServiceProxy weChatService;
    @Autowired
    private TxnsQuickpayServiceProxy txnsQuickpayService;
    
    @Autowired
    private TxnsNotifyTaskServiceProxy txnsNotifyTaskService;
    @Autowired
    private WeChatQRServiceProxy weChatQRService;
    @Autowired
    private ICoopInstiService coopInstiService;
    @Autowired
    private TradeQueryServiceProxy tradeQueryServiceProxy;
    @Autowired
    private ConfigInfoServiceProxy configInfoServiceProxy;
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
		 ResultBean queryResultBean = null;
	    ReaPayResultBean payResult = null;
		if(ChannelEnmu.REAPAY==ChannelEnmu.fromValue(txnsLog.getPayinst())){//支付渠道为融宝时
			if(OrderStatus.fromValue(orderinfoModel.getStatus())==OrderStatus.PAYING){//订单状态为正在支付
				OrderStatusEnum queryTradeResult = tradeQueryServiceProxy.queryTradeResult(orderinfoModel.getRelatetradetxn());
				order.setStatus(OrderStatus.fromValue(queryTradeResult.getStatus()));
		}
		}
		return order;
	}

    @Override
    public String createOrder(Order order) throws CommonException{
        String tn = null;
        fullNonWalletData(order);

        Map<String, String> validateResult = orderValidator
                .validateOrder(order);
        String retCode = validateResult.get(IOrderValidator.RET_CODE);
        if (retCode != null
                && !retCode.equals(IOrderValidator.RET_CODE_SUCCESS)) {
            throw new CommonException(
            		ExcepitonTypeEnum.ORDER.getCode(),
                    validateResult.get(IOrderValidator.RET_MESSAGE));
        }
        try {
            tn = gateWayService.dealWithWapOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            //throw new UnCheckedSystemException();
            throw new CommonException(ExcepitonTypeEnum.TRADE.getCode(),e.getMessage());
        }
        if (tn == null || tn.equals("")) {
            //throw new UnCheckedSystemException();
            throw new CommonException(ExcepitonTypeEnum.TRADE.getCode(),"订单生成失败");
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
            PayWay payWay) throws 
            CommonException {
        Order orderObj = JSON.parseObject(order, Order.class);
        String memberId = orderObj.getMemberId();
        PojoMember member = memberServiceImpl.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
        if (member == null) {// 资金账户不存在
            throw new CommonException(ExcepitonTypeEnum.MEMBER_ACCOUNT.getCode(),"资金账户不存在");
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
        	throw new CommonException(ExcepitonTypeEnum.MEMBER_ACCOUNT.getCode(),"资金账户不存在");
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
                //throw new PayPwdVerifyFailException();
            	throw new CommonException(ExcepitonTypeEnum.PASSWORD.getCode(),"支付密码错误");
            }
        } catch (Exception e) {
            /*PayPwdVerifyFailException pe = new PayPwdVerifyFailException();
            pe.initCause(e);
            throw pe;*/
        	throw new CommonException(ExcepitonTypeEnum.MEMBER_ACCOUNT.getCode(),e.getMessage());
        }

        String phoneNo = "";
        if (payWay == PayWay.ACCOUNT) {
            phoneNo = member.getPhone();
        } else if (payWay == PayWay.QUICK){
            //QuickpayCustModel card = quickpayCustService.getCardByBindId(orderObj.getBindId());
        	QuickpayCustBean memberBankCard = memberBankCardService.getMemberBankCardById(Long.valueOf(orderObj.getBindId()));
        			//quickpayCustService.getCardByBindId(orderObj.getBindId());
            phoneNo = memberBankCard.getPhone();
        }
        switch (payWay) {
            case ACCOUNT :
            	// 校验手机短信验证码
                if (smsService.verifyCode(phoneNo,orderObj.getTn(),smsCode)!=1) {
                   // throw new SmsCodeVerifyFailException();
                	throw new CommonException(ExcepitonTypeEnum.PASSWORD.getCode(),"短信验证码错误");
                }
                Money amount = Money.valueOf(new BigDecimal(orderObj
                        .getTxnAmt()));
                if (basicFund.getBalance().minus(amount).compareTo(Money.ZERO) < 0) {// 余额不足
                    throw new CommonException(ExcepitonTypeEnum.MEMBER_ACCOUNT.getCode(),"余额不足");
                }
                gateWayService.accountPay(order);
                updateAnonOrderToMemberOrder(orderObj);
                break;
            case QUICK :
                //QuickpayCustModel card = quickpayCustService.getCardByBindId(orderObj.getBindId());
                QuickpayCustBean memberBankCard = memberBankCardService.getMemberBankCardById(Long.valueOf(orderObj.getBindId()));
                if (memberBankCard == null) {
                   // throw new InvalidBindIdException();
                	throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),"银行卡不存在");
                }
                gateWayService.submitPay(order);
                updateAnonOrderToMemberOrder(orderObj);
                break;
            default :
                //throw new UnknowPayWayException();
            	throw new CommonException(ExcepitonTypeEnum.TRADE.getCode(),"未知支付类型");
        }

        Order orderRet = queryOrder(member.getMemberId(),orderObj.getTn());
        return orderRet.getStatus();
    }
    
    /**
     * 更新匿名订单但是登陆支付的订单信息
     * @param order
     */
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
    
    public OrderStatus anonymousPay(String order,String smsCode) throws 
             CommonException {
        Order orderObj = JSON.parseObject(order, Order.class);
        String memberId = "999999999999999";
        String bindId = orderObj.getBindId();
        String cardNo = orderObj.getCardNo();
        String phoneNo = "";
        QuickpayCustBean memberBankCard = null;
        if(StringUtil.isNotEmpty(bindId)&&StringUtil.isNotEmpty(cardNo)){//绑卡标示和卡信息同时不为空时
        	 memberBankCard = memberBankCardService.getMemberBankCardById(Long.valueOf(orderObj.getBindId()));
        	if (memberBankCard == null) {
                //throw new InvalidBindIdException();
        		throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),"银行卡不存在");
            }
        	//绑卡表中的卡信息和参数卡信息进行比较
        	if(!memberBankCard.getAccname().equals(orderObj.getAccNo())||!memberBankCard.getCardno().equals(orderObj.getCardNo())||
        			!memberBankCard.getIdnum().equals(orderObj.getCertifId())||!memberBankCard.getPhone().equals(orderObj.getPhoneNo())){
        		//throw new InvalidBindIdException();
        		throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),"银行卡信息校验失败");
        	}
        	phoneNo = memberBankCard.getPhone();
        }else if(StringUtil.isNotEmpty(bindId)){
        	memberBankCard = memberBankCardService.getMemberBankCardById(Long.valueOf(orderObj.getBindId()));
        	phoneNo = memberBankCard.getPhone();
        }else if(StringUtil.isNotEmpty(cardNo)){
        	//List<QuickpayCustModel> cardList =this.quickpayCustService.getCardList(cardNo,orderObj.getCustomerNm(),orderObj.getPhoneNo(),orderObj.getCertifId(),memberId);
        			//(List<QuickpayCustModel>) quickpayCustService.queryByHQL("from QuickpayCustModel where cardno=? and accname = ? and phone = ? and idnum = ? and relatememberno = ? and status = ?", new Object[]{cardNo,orderObj.getCustomerNm(),orderObj.getPhoneNo(),orderObj.getCertifId(),memberId,"00"});
        	QuickpayCustBean quickpayCustBean = memberBankCardService.getCardList(cardNo,orderObj.getCustomerNm(), orderObj.getPhoneNo(), orderObj.getCertifId(), memberId);
        	if(quickpayCustBean!=null){
        		orderObj.setBindId(quickpayCustBean.getId()+"");
        	}else{
        		//throw new InvalidBindIdException();
        		throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),"绑定银行卡失败");
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
    
	public ResultBean anonymousRealName(IndividualRealInfo individualRealInfo,String memberId) {
    	ResultBean resultBean = null;
    	try {
			//PojoMember pm = memberDAO.getMemberByMemberId(memberId, null);
			PojoMember pm = memberService.getMbmberByMemberId(memberId, null);
			
			// 【实名认证】
			//PojoCoopInsti pojoCoopInsti = coopInstiDAO.get(pm.getInstiId());
			CoopInsti coopInsti = coopInstiService.getInstiByInstiID(pm.getInstiId());
			
			
			
			WapCardBean cardBean = new WapCardBean(individualRealInfo.getCardNo(), individualRealInfo.getCardType(), individualRealInfo.getCustomerName(), 
			        individualRealInfo.getCertifType(), individualRealInfo.getCertifNo(), individualRealInfo.getPhoneNo(), individualRealInfo.getCvn2(), 
			        individualRealInfo.getExpired());
			resultBean = gateWayService.bindingBankCard(coopInsti.getInstiCode(), memberId, cardBean);
			if(resultBean.isResultBool()){
			    //保存绑卡信息
			    QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
			    quickpayCustBean.setCustomerno(coopInsti.getInstiCode());
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
			    CardBinBean cardBin = cardBinService.getCard(individualRealInfo.getCardNo());
			    quickpayCustBean.setBankcode(cardBin.getBankCode());
			    quickpayCustBean.setBankname(cardBin.getBankName());
			    long bindId = memberBankCardService.saveQuickPayCust(quickpayCustBean);
			    
			    boolean isCost = false;
			    BusinessCodeEnum busiCode = BusinessCodeEnum.REALNAME_AUTH_COST;
			    //记录实名认证手续费
			    ConfigInfoModel startTime = configInfoService.getConfigByParaName("REALNAME_AUTH_PRICE");
			    // 记录分录流水
			    TradeInfo tradeInfo = new TradeInfo();
			    tradeInfo.setTxnseqno(getTradeNum());
			    tradeInfo.setAmount(BigDecimal.ZERO);
			    tradeInfo.setCommission(BigDecimal.ZERO);
			    tradeInfo.setAmountD(BigDecimal.ZERO);
			    tradeInfo.setAmountE(BigDecimal.ZERO);
			    tradeInfo.setBusiCode(busiCode.getBusiCode());
			    tradeInfo.setPayMemberId(memberId);
			    tradeInfo.setCharge(new BigDecimal(startTime.getPara()));// 手续费
			    
			    tradeInfo.setChannelId(ChannelEnmu.CMBCWITHHOLDING.getChnlcode());
			    tradeInfo.setCoopInstCode(coopInsti.getInstiCode());
			    accEntryService.accEntryProcess(tradeInfo,EntryEvent.TRADE_SUCCESS);
			    resultBean = new ResultBean(bindId);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			resultBean = new ResultBean("", "数字格式化异常");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultBean = new ResultBean("", e.getMessage());
		}
		return resultBean;
	}
    
    private String getTradeNum(){
    	long sequence = configInfoServiceProxy.getSequence("SEQ_TXNSEQNO");
    	 DecimalFormat df = new DecimalFormat("00000000");
         SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
         String tradeNum = sdf.format(new Date())+ df.format(sequence);
         return tradeNum.substring(0,6)+"99"+tradeNum.substring(6);
    }
    
    public String createRefundOrder(Order order) throws CommonException{
    	
    	String tn = null;
        fullNonWalletData(order);

        Map<String, String> validateResult = orderValidator
                .validateOrder(order);
        String retCode = validateResult.get(IOrderValidator.RET_CODE);
        if (retCode != null
                && !retCode.equals(IOrderValidator.RET_CODE_SUCCESS)) {
            throw new CommonException(
                    ExcepitonTypeEnum.ORDER.getCode(),
                    validateResult.get(IOrderValidator.RET_MESSAGE));
        }
        try {
            tn = gateWayService.refund(JSON.toJSONString(order));
        } catch (Exception e) {
            e.printStackTrace();
            //throw new UnCheckedSystemException();
            throw new CommonException(ExcepitonTypeEnum.TRADE.getCode(),e.getMessage());
        }
        if (tn == null || tn.equals("")) {
           // throw new UnCheckedSystemException();
        	throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),"退款订单提交失败");
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
	public ResultBean queryWechatOrder(String tn) {
		TradeBean trade = new TradeBean();
		trade.setTn(tn);
		return this.weChatService.queryWechatOrder(trade);
	}

	@Override
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
        QuickpayCustBean  memberBankCard = null;
        if(StringUtil.isNotEmpty(bindId)&&StringUtil.isNotEmpty(cardNo)){//绑卡标示和卡信息同时不为空时
        	memberBankCard = memberBankCardService.getMemberBankCardById(Long.valueOf(orderObj.getBindId()));
        	if (memberBankCard == null) {
                //throw new InvalidBindIdException();
        		result =new ResultBean("", "无效的卡信息");
        		result.setResultObj(map);
        		return result;
            }
        	//绑卡表中的卡信息和参数卡信息进行比较
        	if(!memberBankCard.getAccname().equals(orderObj.getAccNo())||!memberBankCard.getCardno().equals(orderObj.getCardNo())||
        			!memberBankCard.getIdnum().equals(orderObj.getCertifId())||!memberBankCard.getPhone().equals(orderObj.getPhoneNo())){
        		result =new ResultBean("", "请检查输入的卡信息");
        		result.setResultObj(map);
        		return result;
        	}
        	phoneNo = memberBankCard.getPhone();
        }else if(StringUtil.isNotEmpty(bindId)){
        	memberBankCard = memberBankCardService.getMemberBankCardById(Long.valueOf(orderObj.getBindId()));
        	phoneNo = memberBankCard.getPhone();
        }else if(StringUtil.isNotEmpty(cardNo)){
        	//List<QuickpayCustModel> cardList =this.quickpayCustService.getCardList(cardNo,orderObj.getCustomerNm(),orderObj.getPhoneNo(),orderObj.getCertifId(),memberId);
        	QuickpayCustBean quickpayCustBean = memberBankCardService.getCardList(cardNo,orderObj.getCustomerNm(), orderObj.getPhoneNo(), orderObj.getCertifId(), memberId);
        	if(quickpayCustBean!=null){
        		orderObj.setBindId(quickpayCustBean.getId()+"");
        	}else{
        		result =new ResultBean("", "请检查输入的卡信息");
        		result.setResultObj(map);
        		return result;
        	}
        }
        orderObj.setSmsCode(smsCode);
        try {
			gateWayService.submitPay(JSON.toJSONString(orderObj));
		} catch (Exception e) {
			e.printStackTrace();
			result =new ResultBean("", e.getMessage());
    		result.setResultObj(map);
    		return result;
		}
        Order orderRet = queryOrder(memberId,orderObj.getTn());
        map.put("orderStatus", orderRet.getStatus());
        if(orderRet.getStatus().equals(OrderStatus.SUCCESS)){
        	
        	
        	ResultBean orderResp = gateWayService.generateAsyncRespMessage(txnsLog.getTxnseqno());
        	OrderAsynRespBean respBean = (OrderAsynRespBean) orderResp.getResultObj();
        	//同步通知有问题
        	/*TxnsNotifyTaskModel task;
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
			}*/
             
        }
        //返回url及状态
        return new ResultBean(map);
	}


	/**
	 *
	 * @param tn
	 * @return
	 */
	@Override
	public JSONObject createWeChatOrderQR(String tn) {
		// TODO Auto-generated method stub
		JSONObject creatOrder = null;
		try {
			creatOrder = weChatQRService.creatOrder(tn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return creatOrder;
	}
	@Override
	public JSONObject createWechatOrder(String tn, String typeId) throws CommonException {
		JSONObject result = null;
		if(StringUtil.isEmpty(typeId)||StringUtil.isEmpty(tn)){
			throw new CommonException(ExcepitonTypeEnum.ORDER.getCode(),"tn或typeId不能为空！");
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
	public ResultBean queryWechatOrder(String tn, String typeId)throws CommonException{
		ResultBean result = null;
		if(StringUtil.isEmpty(typeId)||StringUtil.isEmpty(tn)){
			throw new CommonException(ExcepitonTypeEnum.ORDER.getCode(),"tn或typeId不能为空！");
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

