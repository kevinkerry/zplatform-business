/* 
 * MerchBusinessServiceTest.java  
 * 
 * version TODO
 *
 * 2016年6月24日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zlebank.zplatform.business.individual.exception.DataBaseErrorException;
import com.zlebank.zplatform.business.individual.exception.MerchMKNotExistException;
import com.zlebank.zplatform.business.individual.exception.MerchWhiteListNotExistException;
import com.zlebank.zplatform.trade.model.PojoMerchWhiteList;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年6月24日 下午5:08:46
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/*.xml")
public class MerchBusinessServiceTest {

	@Autowired
	private MerchBusinessService merchBusinessService;
	
	@Test
	@Ignore
	public void test_updateMerchPubKey(){
		try {
			merchBusinessService.updateMerchPubKey("200000000000001", "1234566777");
		} catch (MerchMKNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataBaseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	@Ignore
	public void test_saveWhiteList(){
		try {
			PojoMerchWhiteList merchWhiteList = new PojoMerchWhiteList();
			merchWhiteList.setAccName("郭佳");
			merchWhiteList.setMerchId("200000000000613");
			merchWhiteList.setCardNo("6228480018543668976");
			merchWhiteList.setStatus("0");
			merchBusinessService.saveWhiteList(merchWhiteList);
		} catch (DataBaseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test_updateWhiteList(){
		try {
			PojoMerchWhiteList merchWhiteList = new PojoMerchWhiteList();
			merchWhiteList.setId(19L);
			merchWhiteList.setAccName("郭佳__");
			merchWhiteList.setMerchId("200000000000613");
			merchWhiteList.setCardNo("6228480018543661200");
			merchWhiteList.setStatus("0");
			merchBusinessService.updateWhiteList(merchWhiteList);
		} catch (DataBaseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MerchWhiteListNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_deleteWhiteList(){
		try {
			merchBusinessService.deleteWhiteList(19L);
		} catch (DataBaseErrorException | MerchWhiteListNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
