package com.zlebank.zplatform.business.individual.service;

import java.util.Date;

import net.sf.json.JSONObject;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.exception.AbstractTradeDescribeException;
import com.zlebank.zplatform.trade.exception.TradeException;


public interface OrderService {
	/**
	 * 订单查询列表
	 * @param memberId 会员号
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param page 页数
	 * @param pageSize 当前页行数
	 * @return
	 */
	public PagedResult<Order> queryOrderList(String memberId,Date startDate,Date endDate,int page,int pageSize);
	/**
	 * 订单明细信息<br/> Query a order detail
	 * @param memberId
	 * @param orderNo
	 * @return
	 */
	public Order queryOrder(String memberId,String orderNo);
    /**
     * create a consume order
     * 
     * @param order
     * @return tn - zplatform accept order no
     * @throws ValidateOrderException
     * @throws TradeException
     * @throws AbstractIndividualBusinessException
     */
    String createOrder(Order order) throws ValidateOrderException,
            TradeException, AbstractIndividualBusinessException;
    /**
     * Pay a order with account balance
     * 
     * @param order
     * @param smsCode
     * @param payPwd
     * @return {@link OrderStatus}
     */
    OrderStatus pay(String order, String smsCode, String payPwd, PayWay payWay)
            throws AbstractTradeDescribeException,
            AbstractIndividualBusinessException,TradeException;

    /**
     * 匿名支付
     * @param order
     * @param smsCode
     * @param payPwd
     * @return
     * @throws AbstractTradeDescribeException
     * @throws AbstractIndividualBusinessException
     * @throws TradeException
     */
    public OrderStatus anonymousPay(String order,
            String smsCode
            ) throws AbstractTradeDescribeException,
            AbstractIndividualBusinessException, TradeException;
    
    /**
     * 匿名支付 实名认证
     * @param individualRealInfo
     * @param memberId
     * @return
     */
    public ResultBean anonymousRealName(IndividualRealInfo individualRealInfo,String memberId);
    
    /**
     * 创建退款交易订单
     * @param order
     * @return
     * @throws ValidateOrderException
     * @throws TradeException
     * @throws AbstractIndividualBusinessException
     */
    public String createRefundOrder(Order order) throws ValidateOrderException,
    TradeException, AbstractIndividualBusinessException;
    
    /**
     * 创建微信订单
     * @param tn
     * @return
     */
    public JSONObject createWeChatOrder(String tn);
    
    /**
     * 获取退款手续费
     * @param txnseqno
     * @param merchNo
     * @param txnAmt
     * @param busicode
     * @return
     */
    public Long getRefundFee(String txnseqno,String merchNo,String txnAmt,String busicode);
    /***
     * 查询微信订单
     * @param tn
     * @return
     */
    public ResultBean queryWechatOrder(String tn);
}
