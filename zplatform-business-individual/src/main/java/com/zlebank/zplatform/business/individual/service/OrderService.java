package com.zlebank.zplatform.business.individual.service;

import java.util.Date;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.trade.exception.AbstractTradeDescribeException;
import com.zlebank.zplatform.trade.exception.TradeException;

public interface OrderService {
    /**
     * Query order list belongs to member
     * 
     * @param memberId
     * @param startDate
     * @param endDate
     * @param page
     * @param pageSize
     * @return
     */
    Object queryOrderList(String memberId,
            Date startDate,
            Date endDate,
            int page,
            int pageSize);
    /**
     * Query a order detail
     * 
     * @param memberId
     * @param orderNo
     * @return
     */
    Order queryOrder(String memberId, String orderNo);
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
}
