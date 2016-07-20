package com.zlebank.zplatform.business.individual.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.common.page.PageVo;
import com.zlebank.zplatform.trade.exception.AbstractTradeDescribeException;
import com.zlebank.zplatform.trade.exception.TradeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/*.xml")
public class GatewayTest {
	private static String tn="160719002700054080";
	@Autowired
	private OrderService orderSerivce;
	@Autowired
	private MemberCardService  memberCardService;
	@Autowired
	private SmsService  smsService;
	@Test
	public void test_getOrderInfo(){
		Order order = orderSerivce.queryOrder(null, tn);
		System.out.println(JSON.toJSONString(order));
	}
	
	@Test
	public void test_getCardList(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paytype", "01");
		map.put("status", "00");
		PageVo<SupportedBankCardType> pageVo=memberCardService.queryCardList(map, 3, 10);
		System.out.println(pageVo.getTotal());
		List<SupportedBankCardType> list= pageVo.getList();
		int row=0;
		for (SupportedBankCardType item :list){
			System.out.println(JSON.toJSONString(item));
			row++;
		}
		System.out.println("------"+row+"--------");
	}
	
	@Test
	public void test_getCardBin(){
		CardBin card = this.memberCardService.queryCardBin("6225768749734008");
		System.out.println(JSON.toJSONString(card));
	}
	
	@Test
	public void test_bindCard(){
		JSONObject json= new JSONObject();
		json.put("tn", tn);
		json.put("cardNo", "6225768749734008");
		json.put("cardType", "2");
		json.put("customerNm", "刘守梅");
		json.put("certifTp", "");
		json.put("certifId", "410726198801032462");
		json.put("phoneNo", "18210457410");
		json.put("cvn2", "510");
		json.put("expired", "0520");
		json.put("devId", "");
	  //  json.put("bindFlag", "1");
		ResultBean result =this.memberCardService.anonymousBindCard(json.toString());
		if(result.isResultBool()){
			System.out.println("bindId："+result.getResultObj());
		}
	}
	@Test
	public void test_sendSms(){
		JSONObject json= new JSONObject();
		json.put("tn", tn);
		json.put("bindId", "173");
		json.put("cardNo", "6225768749734008");
		json.put("cardType", "2");
		json.put("customerNm", "刘守梅");
		json.put("certifTp", "");
		json.put("certifId", "410726198801032462");
		json.put("phoneNo", "18210457410");
		json.put("cvn2", "510");
		json.put("expired", "0520");
		json.put("devId", "");
		Boolean flag =this.smsService.sendSmsCode(json.toString(), ModuleTypeEnum.ANONYMOUSPAY);
		System.out.println(flag);
	}
	
	@Test
	public void test_submitPay(){
		JSONObject json= new JSONObject();
		json.put("tn", tn);
		//json.put("bindId", "");
		json.put("cardNo", "6225768749734008");
		json.put("cardType", "2");
		json.put("customerNm", "刘守梅");
		json.put("certifTp", "");
		json.put("certifId", "410726198801032462");
		json.put("phoneNo", "18210457410");
		json.put("cvn2", "510");
		json.put("expired", "0520");
		json.put("devId", "");
		json.put("txnAmt", "1");
		try {
			OrderStatus orderStatus = this.orderSerivce.anonymousPay(json.toString(), "183590");
			System.out.println(orderStatus.getCode());
		} catch (AbstractTradeDescribeException
				| AbstractIndividualBusinessException | TradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
