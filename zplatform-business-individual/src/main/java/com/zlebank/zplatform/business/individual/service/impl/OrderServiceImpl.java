package com.zlebank.zplatform.business.individual.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.acc.service.entry.EntryEvent;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.business.individual.bean.enums.RealNameTypeEnum;
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
import com.zlebank.zplatform.commons.enums.BusinessCodeEnum;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.RealNameBean;
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
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.dao.ConfigInfoDAO;
import com.zlebank.zplatform.trade.exception.AbstractTradeDescribeException;
import com.zlebank.zplatform.trade.exception.BalanceNotEnoughException;
import com.zlebank.zplatform.trade.exception.FailToGetAccountInfoException;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.ConfigInfoModel;
import com.zlebank.zplatform.trade.model.QuickpayCustModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.IQuickpayCustService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.OrderNumber;

@Service
public class OrderServiceImpl implements OrderService {

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
		order.setStatus(OrderStatus.fromValue(orderinfoModel.getStatus()));
		order.setOrderDesc(orderinfoModel.getOrderdesc());
		order.setCurrencyCode(orderinfoModel.getCurrencycode());
		order.setTn(orderinfoModel.getTn());
		order.setBankCode(txnsLog.getCardinstino());
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

        // 校验手机短信验证码
        if (smsService.verifyCode(phoneNo,orderObj.getTn(),smsCode)!=1) {
            throw new SmsCodeVerifyFailException();
        }

        switch (payWay) {
            case ACCOUNT :
                Money amount = Money.valueOf(new BigDecimal(orderObj
                        .getTxnAmt()));
                if (basicFund.getBalance().minus(amount).compareTo(Money.ZERO) < 0) {// 余额不足
                    throw new BalanceNotEnoughException();
                }

                gateWayService.accountPay(order);
                updateAnonOrderToMemberOrder( orderObj);
                break;
            case QUICK :
                QuickpayCustModel card = quickpayCustService
                        .getCardByBindId(orderObj.getBindId());
                if (card == null) {
                    throw new InvalidBindIdException();
                }
                gateWayService.submitPay(order);
                updateAnonOrderToMemberOrder( orderObj);
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
    private void updateAnonOrderToMemberOrder(Order order){
    	TxnsOrderinfoModel orderinfo = gateWayService.getOrderinfoByTN(order.getTn());
    	TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
    	String memberId = order.getMemberId();
    	String old_order_member = txnsLog.getAccmemberid();
    	if("999999999999999".equals(old_order_member)){
    		txnsLog.setAccmemberid(memberId);
    		txnsLogService.update(txnsLog);
    	}
    	
    	
    	
    }
    
    
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
        	List<QuickpayCustModel> cardList = (List<QuickpayCustModel>) quickpayCustService.queryByHQL("from QuickpayCustModel where cardno=? and accname = ? and phone = ? and idnum = ? and relatememberno = ? and status = ?", new Object[]{cardNo,orderObj.getCustomerNm(),orderObj.getPhoneNo(),orderObj.getCertifId(),memberId,"00"});
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
			    BusinessCodeEnum busiCode = isCost ? BusinessCodeEnum.REALNAME_AUTH_COST : BusinessCodeEnum.REALNAME_AUTH_NO_COST;
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
}
