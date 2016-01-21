/* 
 * OrderServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年1月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.service.OrderService;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.IGateWayService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月21日 上午9:03:13
 * @since 
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService{

	@Autowired
	private IGateWayService gateWayService;
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
			order.setStatus(valueMap.get("STATUS")+"");
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
	public Order queryOrder(String memberId, String orderNo) {
		TxnsOrderinfoModel orderinfoModel = gateWayService.getPersonOrder(orderNo, memberId);
		Order order = new Order();
		order.setMerId(orderinfoModel.getFirmemberno());
		order.setMerName(orderinfoModel.getFirmembername());
		order.setMerAbbr(orderinfoModel.getFirmembershortname());
		order.setOrderId(orderinfoModel.getOrderno());
		order.setTxnAmt(orderinfoModel.getOrderamt()+"");
		order.setTxnTime(orderinfoModel.getOrdercommitime());
		order.setStatus(orderinfoModel.getStatus());
		order.setOrderDesc(orderinfoModel.getOrderdesc());
		order.setCurrencyCode(orderinfoModel.getCurrencycode());
		order.setTn(orderinfoModel.getTn());
		return order;
	}

	/**
	 *
	 * @param order
	 * @return
	 */
	@Override
	public String createOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param order
	 * @param smsCode
	 * @param payPwd
	 * @return
	 */
	@Override
	public OrderStatus pay(Order order, String smsCode, String payPwd) {
		
		return null;
	}

	
	
}
