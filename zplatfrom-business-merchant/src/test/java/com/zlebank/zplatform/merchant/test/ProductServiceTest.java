/* 
 * ProductServiceTest.java  
 * 
 * version TODO
 *
 * 2016年8月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.merchant.test;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.merchant.enterprise.bean.ProductBalanceBean;
import com.zlebank.zplatform.business.merchant.product.service.ProductService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月30日 上午11:51:42
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/remoting-client.xml") 
public class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	@Test
	@Ignore
	public void test_async_product(){
		boolean openProduct = productService.openProduct("100000001", "测试产品1", "200000000000855", "200000000000853");
		System.out.println(openProduct);
	}
	
	@Test
	//@Ignore
	public void test_query(){
		try {
			ProductBalanceBean queryBalance = productService.queryBalance("100000001");
			System.out.println(JSON.toJSONString(queryBalance));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
