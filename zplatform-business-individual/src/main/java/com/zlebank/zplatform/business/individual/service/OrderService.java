package com.zlebank.zplatform.business.individual.service;

import java.util.Date;

import com.zlebank.zplatform.business.individual.bean.Order;

public interface OrderService {
	 
	Object queryOrderList(String memberId,Date startDate,Date endDate,int page,int pageSize);
	
	Order queryOrder(String memberId,String orderNo);
	/**
	 * 生成消费订单
	 * @param order
	 * @return tn
	 */
	String createOrder(Order order);
	
	String pay(Order order,String smsCode,String payPwd);
}
