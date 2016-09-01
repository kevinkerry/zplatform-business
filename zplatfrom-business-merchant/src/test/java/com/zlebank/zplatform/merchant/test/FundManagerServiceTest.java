/* 
 * FundManagerService.java  
 * 
 * version TODO
 *
 * 2016年8月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.merchant.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zlebank.zplatform.business.merchant.fund.service.FundManagerService;
import com.zlebank.zplatform.trade.bean.FinancierReimbursementBean;
import com.zlebank.zplatform.trade.bean.MerchantReimbursementBean;
import com.zlebank.zplatform.trade.bean.OffLineChargeBean;
import com.zlebank.zplatform.trade.bean.RaiseMoneyTransferBean;
import com.zlebank.zplatform.trade.bean.ReimbursementDetailBean;
import com.zlebank.zplatform.trade.utils.DateUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月30日 上午10:45:03
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/remoting-client.xml")  
public class FundManagerServiceTest {

	@Autowired
	private FundManagerService fundManagerService;
	
	@Test
	@Ignore
	public void test_offLine_charge(){
		OffLineChargeBean offLineChargeBean = new OffLineChargeBean();
		offLineChargeBean.setMemberId("200000000000683");
		offLineChargeBean.setBackURL("www.baidu.com");
		offLineChargeBean.setBizType("000207");
		offLineChargeBean.setChannelType("00");
		offLineChargeBean.setChargeCode("123455");
		offLineChargeBean.setCoopInsti("300000000000027");
		offLineChargeBean.setOrderId("M"+new Date().getTime());
		offLineChargeBean.setTxnAmt("10000");
		offLineChargeBean.setTxnSubType("00");
		offLineChargeBean.setTxnType("25");
		String tn = fundManagerService.offLineCharge(offLineChargeBean);
		System.out.println("【受理订单号】"+tn);
	}
	
	@Test
	//@Ignore
	public void test_financierReimbursement(){
		/**
		 *  22
			00
			000207
			00
		 *   txnType;// 交易类型
			 txnSubType;// 交易子类
			 bizType;// 产品类型
			 channelType;// 渠道类型
			 coopInsti;// 合作结构
			 memberId;//	融资人ID
			 orderId;//	还款单号
			 totalAmt;//	还款总金额
			 productCode;//	产品代码
		 */
		FinancierReimbursementBean financierReimbursementBean = new FinancierReimbursementBean();
		financierReimbursementBean.setTxnType("22");
		financierReimbursementBean.setTxnSubType("00");
		financierReimbursementBean.setBizType("000207"); 
		financierReimbursementBean.setChannelType("00");
		financierReimbursementBean.setCoopInsti("300000000000027");
		financierReimbursementBean.setMemberId("200000000000597");
		financierReimbursementBean.setOrderId("M0"+DateUtil.getCurrentDateTime());
		financierReimbursementBean.setTotalAmt("10");
		financierReimbursementBean.setProductCode("100000001");
		
		String tn = fundManagerService.financierReimbursement(financierReimbursementBean);
		System.out.println("【融资人还款】"+tn);
	}
	
	@Test
	@Ignore
	public void test_raiseMoneyTransfer(){
		RaiseMoneyTransferBean raiseMoneyTransferBean = new RaiseMoneyTransferBean();
		
		/**
		 *  txnType;// 交易类型
			txnSubType;// 交易子类
			bizTypeprivate;// 产品类型
			channelType;// 渠道类型
			OrderId;// 订单号
			memberId;// 商户ID
			financingId;// 融资人ID
			productCode;// 产品代码
		 */
		
		raiseMoneyTransferBean.setTxnType("23");
		raiseMoneyTransferBean.setTxnSubType("00");
		raiseMoneyTransferBean.setBizType("000207"); 
		raiseMoneyTransferBean.setChannelType("00");
		raiseMoneyTransferBean.setOrderId("MP"+DateUtil.getCurrentDateTime());
		raiseMoneyTransferBean.setMemberId("200000000000597");
		raiseMoneyTransferBean.setFinancingId("200000000000683");
		raiseMoneyTransferBean.setProductCode("100000001");
		raiseMoneyTransferBean.setCoopInsti("300000000000027");
		
		String tn = fundManagerService.raiseMoneyTransfer(raiseMoneyTransferBean);
		System.out.println("【募集款划转】"+tn);
	}
	
	@Test
	@Ignore
	public void test_merchanReimbursement(){
		MerchantReimbursementBean merchanReimbursementBean = new MerchantReimbursementBean();
		merchanReimbursementBean.setBatchNo("BP"+DateUtil.getCurrentDateTime());
		merchanReimbursementBean.setMemberId("200000000000597");
		merchanReimbursementBean.setProductCode("100000001");
		merchanReimbursementBean.setTotalAmt("10");
		List<ReimbursementDetailBean> detaiList = new ArrayList<ReimbursementDetailBean>();
		ReimbursementDetailBean detailBean = new ReimbursementDetailBean();
		detailBean.setOrderId("MO"+DateUtil.getCurrentDateTime());
		detailBean.setTxnAmt("10");
		detailBean.setInterest("200000000000597");
		detailBean.setInvestors("200000000000597");
		detaiList.add(detailBean);
		
		merchanReimbursementBean.setDetaiList(detaiList);
		try {
			String tn = fundManagerService.merchanReimbursement(merchanReimbursementBean);
			System.out.println("【商户还款】"+tn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
