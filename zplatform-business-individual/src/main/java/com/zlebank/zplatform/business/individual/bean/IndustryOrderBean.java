/* 
 * IndustryOrderBean.java  
 * 
 * version TODO
 *
 * 2016年9月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.bean;

import java.io.Serializable;

import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;

/**
 * 行业专户订单信息
 *
 * @author guojia
 * @version
 * @date 2016年9月29日 下午2:52:15
 * @since
 */
public class IndustryOrderBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1368427218746428311L;
	
	
	private OrderStatus status;
	private OrderType orderType;
	private String tn;
}
