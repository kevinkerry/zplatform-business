/* 
 * OrderServiceTest.java  
 * 
 * version TODO
 *
 * 2016年1月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.utils.DateUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月21日 下午4:30:03
 * @since 
 */
public class OrderServiceTest extends ApplicationContextAbled{
//100000000000564
	
	public void test_queryOrder(){
		OrderService orderService = (OrderService) getContext().getBean("orderService");
		Order order = orderService.queryOrder("100000000000564", "dGVWwxSCU68267726142181273");
		System.out.println(JSON.toJSONString(order));
	}
	@Test
	public void test_queryOrderList(){
		OrderService orderService = (OrderService) getContext().getBean("orderService");
		PagedResult<Order> orderList = null;
		try {
			orderList = orderService.queryOrderList("100000000000564", DateUtil.convertToDate("20160120112643", "yyyyMMddHHmmss"), DateUtil.convertToDate("20160121122643", "yyyyMMddHHmmss"), 1, 10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(JSON.toJSONString(orderList));
	}
}
