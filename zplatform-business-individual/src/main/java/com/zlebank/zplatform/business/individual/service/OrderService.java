package com.zlebank.zplatform.business.individual.service;

import java.util.Date;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;

public interface OrderService {
	 /**
	  * Query order list belongs to member
	  * @param memberId
	  * @param startDate
	  * @param endDate
	  * @param page
	  * @param pageSize
	  * @return
	  */
	Object queryOrderList(String memberId,Date startDate,Date endDate,int page,int pageSize);
	/**
	 * Query a order detail
	 * @param memberId
	 * @param orderNo
	 * @return
	 */
	Order queryOrder(String memberId,String orderNo);
	/**
	 * create a consume order
	 * @param order
	 * @return tn
	 */
	String createOrder(Order order);
	/**
	 * Pay a order with account balance
	 * @param order
	 * @param smsCode
	 * @param payPwd
	 * @return {@link OrderStatus}
	 */
	OrderStatus accountPay(Order order,String smsCode,String payPwd);
	/**
	 * Pay a order with account balance
	 * 
	 * @param order
	 * @param smsCode
	 * @param payPwd
	 * @return
	 */
	OrderStatus quickPay(Order order,String smsCode,String payPwd);
}
