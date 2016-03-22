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
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.InvalidBindIdException;
import com.zlebank.zplatform.business.individual.exception.PayPwdVerifyFailException;
import com.zlebank.zplatform.business.individual.exception.SmsCodeVerifyFailException;
import com.zlebank.zplatform.business.individual.exception.UnCheckedSystemException;
import com.zlebank.zplatform.business.individual.exception.UnknowPayWayException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.service.OrderService;
import com.zlebank.zplatform.business.individual.utils.Constants;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.dao.CoopInstiDAO;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.exception.AbstractTradeDescribeException;
import com.zlebank.zplatform.trade.exception.BalanceNotEnoughException;
import com.zlebank.zplatform.trade.exception.FailToGetAccountInfoException;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.QuickpayCustModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.IQuickpayCustService;

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
        order.setFrontUrl(Constants.WALLET_MISSING_FIELD_STR);
        // 后台通知地址，非空
        order.setBackUrl(Constants.WALLET_MISSING_FIELD_STR);
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

                break;
            case QUICK :
                QuickpayCustModel card = quickpayCustService
                        .getCardByBindId(orderObj.getBindId());
                if (card == null) {
                    throw new InvalidBindIdException();
                }
                gateWayService.submitPay(order);
                break;
            default :
                throw new UnknowPayWayException();
        }

        Order orderRet = queryOrder(member.getMemberId(),orderObj.getTn());
        return orderRet.getStatus();
    }
}
