/* 
 * SMSSendService.java  
 * 
 * version TODO
 *
 * 2016年1月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.business.individual.service.SmsService;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.rmi.member.IMemberBankCardService;
import com.zlebank.zplatform.rmi.trade.CardBinServiceProxy;
import com.zlebank.zplatform.rmi.trade.GateWayServiceProxy;
import com.zlebank.zplatform.rmi.trade.TxnsLogServiceProxy;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.trade.bean.CardBinBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.utils.StringUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月21日 上午10:57:00
 * @since 
 */
@Service("smsSendService")
public class SMSSendService implements SmsService{
	private static final Log log = LogFactory.getLog(SMSSendService.class);
	@Autowired
	private SMSServiceProxy smsSendService;
	@Autowired 
	private GateWayServiceProxy gateWayService;
	//@Autowired
    
	
	@Autowired
	//private CardBinDao cardBinDao;
	private CardBinServiceProxy cardBinService;
	@Autowired
	private IMemberBankCardService memberBankCardService;
	@Autowired
	private TxnsLogServiceProxy txnsLogService;
	/**
	 *
	 * @param memberId
	 * @param phone
	 * @param moduleType
	 * @return
	 */
	@Override
	public boolean sendSmsCode(String memberId, String phone,
			ModuleTypeEnum moduleType) {
		int retcode = smsSendService.sendSMS(moduleType.getCode(), phone, "", "");
		if(retcode==100||retcode==105){
			return true;
		}
		return false;
	}
	/**
	 *
	 * @param json
	 * @param moduleType
	 * @return
	 */
	@Override
	public boolean sendSmsCode(String json, ModuleTypeEnum moduleType) {
		JSONObject jsonObject =  JSON.parseObject(json);
		int retcode = 999;
		String phoneNo = null;
		switch (moduleType) {
			case BINDCARD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, "", "");
				break;
			case CHANGELOGINPWD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, "", "");
				break;
			case CHANGEPAYPWD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, "", "");
				break;
			case PAY:
				//需要bindId tn
				try {
					gateWayService.sendSMSMessage(json);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return true;
			case REGISTER:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, "", "");
				break;
			case ACCOUNTPAY:
				phoneNo = jsonObject.get("phoneNo").toString();
				String tn = jsonObject.get("tn").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, tn, "");
				break;
			case ENTERPRISEREALNAME://1.5.0 新增企业实名认证
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, jsonObject.get("tn").toString(), "");
				break;
			case UNBINDPHONE://1.5.0 新增解绑手机
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, "", phoneNo.substring(7));
				break;
			case RESETPAYPWD://1.5.0 新增重置支付密码
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, "", "");
				break;
			case BINDPHONE://1.5.0 新增绑定手机
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType.getCode(), phoneNo, "",  phoneNo.substring(7));
				break;
			case ANONYMOUSPAY:
				try {
					String tn_=jsonObject.get("tn").toString();
					 String cardNo=jsonObject.get("cardNo").toString();
					 String cardType=jsonObject.get("cardType").toString();
					 String customerNm=jsonObject.get("customerNm").toString();
					 String certifTp=jsonObject.get("certifTp").toString();
					 String certifId=jsonObject.get("certifId").toString();
					 phoneNo=jsonObject.get("phoneNo").toString();
					 String cvn2=jsonObject.get("cvn2")+"";
					 String expired=jsonObject.get("expired")+"";
					 String bindFlag = jsonObject.get("bindFlag")+"";
					 String instiCode = jsonObject.get("instiCode")+"";
					 String devId = jsonObject.get("devId")+"";
					 QuickpayCustBean custBean = null;
					 if(devId!=null&&!"".equals(devId)){
						 custBean = memberBankCardService.getCardList(cardNo, customerNm, phoneNo, certifId, "999999999999999",devId);
					 }else{
						 custBean = memberBankCardService.getCardList(cardNo, customerNm, phoneNo, certifId, "999999999999999");
					 }
		        	if(custBean!=null){//已绑卡
		        		Map<String, Object> resultMap = new HashMap<String, Object>();
		        		resultMap.put("tn", tn_);
		        		resultMap.put("bindId", custBean.getId());
		        		try {
							gateWayService.sendSMSMessage(JSON.toJSONString(resultMap));
							return true;
						} catch (Exception e) {
							e.printStackTrace();
							log.error("发送短信失败"+e.getMessage());
							return false;
						}
		        	}else{
		        		if("1".equals(bindFlag)){//需要进行绑卡签约
		        			 if(instiCode!=null&&!"".equals(instiCode)){
		        				 TxnsOrderinfoModel orderinfo = gateWayService.getOrderinfoByTN(tn_);
		        				 TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
		        				 instiCode = txnsLog.getAcccoopinstino();
		        			 }
		        			 TxnsOrderinfoModel orderinfo = gateWayService.getOrderinfoByTN(tn_);
	        				 TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
	        				 instiCode = txnsLog.getAcccoopinstino();
		        			 String merId = orderinfo.getSecmemberno()!=null ?   orderinfo.getSecmemberno():orderinfo.getFirmemberno();
		        	        WapCardBean cardBean = new WapCardBean(cardNo,cardType , customerNm,certifTp, certifId, phoneNo, cvn2, expired);
		        	        cardBean.setTn(tn_);
		        	        
		        	        ResultBean resultBean = gateWayService.bindingBankCard(merId, "999999999999999", cardBean);
		        	        if(resultBean==null){
		        	        	log.error("绑卡签约失败"+JSON.toJSONString(cardBean));
		        	        	return false;
		        	        }
		        	        //绑卡签约成功则已发送短信，因此去掉此逻辑
		        	       if(resultBean.isResultBool()){
		        	    	 //保存绑卡信息
		        	            QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		        	            quickpayCustBean.setCustomerno(instiCode);
		        	            quickpayCustBean.setCardno(cardNo);
		        	            quickpayCustBean.setCardtype(cardType);
		        	            quickpayCustBean.setAccname(customerNm);
		        	            quickpayCustBean.setPhone(phoneNo);
		        	            quickpayCustBean.setIdtype(certifTp);
		        	            quickpayCustBean.setIdnum(certifId);
		        	            quickpayCustBean.setCvv2(cvn2);
		        	            quickpayCustBean.setValidtime(expired);
		        	            quickpayCustBean.setRelatememberno("999999999999999");
		        	            //新增设备ID支持匿名支付
		        	            quickpayCustBean.setDevId(devId);
		        	            CardBinBean cardBin = cardBinService.getCard(cardNo);
		        	            quickpayCustBean.setBankcode(cardBin.getBankCode());
		        	            quickpayCustBean.setBankname(cardBin.getBankName());
		        	            long bindId = memberBankCardService.saveQuickPayCustExt(quickpayCustBean);
		        	            Map<String, Object> resultMap = new HashMap<String, Object>();
		    	        		resultMap.put("tn", tn_);
		    	        		resultMap.put("bindId", bindId+"");
		    	        		try {
		    						gateWayService.sendSMSMessage(JSON.toJSONString(resultMap));
		    						return true;
		    					} catch (Exception e) {
		    						e.printStackTrace();
		    						log.error("发送短信失败"+e.getMessage());
		    						return false;
		    					}
		        	            //memberBankCardService.saveQuickPayCustExt(quickpayCustBean);
			        			//return true;
		        	        }else{
		        	        	log.error("发送短信失败 :resultBean error  msg "+resultBean.getErrCode()+resultBean.getErrMsg());
			        			return false;
		        	        }
		        		}else{
		        			log.error("发送短信失败 :bindFlag is null");
		        			return false;
		        		}
		        	}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("发送短信失败"+e.getMessage());
					return false;
				}
				 
		}
		return false;
	}

	
	public String generateSmsCode(String json, ModuleTypeEnum moduleType) {
		JSONObject jsonObject =  JSON.parseObject(json);
		String retcode = "";
		String phoneNo = null;
		switch (moduleType) {
			case BINDCARD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case CHANGELOGINPWD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case CHANGEPAYPWD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case PAY:
				//需要bindId tn
				try {
					gateWayService.sendSMSMessage(json);
				} catch (Exception e) {
					e.printStackTrace();
					return "false";
				}
				return "true";
			case REGISTER:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case ACCOUNTPAY:
				phoneNo = jsonObject.get("phoneNo").toString();
				String tn = jsonObject.get("tn").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, tn);
				break;
			case ENTERPRISEREALNAME://1.5.0 新增企业实名认证
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case UNBINDPHONE://1.5.0 新增解绑手机
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case RESETPAYPWD://1.5.0 新增重置支付密码
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case BINDPHONE://1.5.0 新增绑定手机
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.generateCode(moduleType.getCode(), phoneNo, "");
				break;
			case ANONYMOUSPAY:
				try {
					String tn_=jsonObject.get("tn").toString();
					 String cardNo=jsonObject.get("cardNo").toString();
					 String cardType=jsonObject.get("cardType").toString();
					 String customerNm=jsonObject.get("customerNm").toString();
					 String certifTp=jsonObject.get("certifTp").toString();
					 String certifId=jsonObject.get("certifId").toString();
					 phoneNo=jsonObject.get("phoneNo").toString();
					 String cvn2=jsonObject.get("cvn2")+"";
					 String expired=jsonObject.get("expired")+"";
					 String bindFlag = jsonObject.get("bindFlag")+"";
					 String instiCode = jsonObject.get("instiCode")+"";
					 String devId = jsonObject.get("devId")+"";
					 String merId="";
					 QuickpayCustBean custBean = null;
					 if(devId!=null&&!"".equals(devId)){
						 custBean = memberBankCardService.getCardList(cardNo, customerNm, phoneNo, certifId, "999999999999999",devId);
					 }else{
						 custBean = memberBankCardService.getCardList(cardNo, customerNm, phoneNo, certifId, "999999999999999");
					 }
		        	if(custBean!=null){//已绑卡
		        		Map<String, Object> resultMap = new HashMap<String, Object>();
		        		resultMap.put("tn", tn_);
		        		resultMap.put("bindId", custBean.getId());
		        		try {
							gateWayService.sendSMSMessage(JSON.toJSONString(resultMap));
							return "true";
						} catch (Exception e) {
							e.printStackTrace();
							log.error("发送短信失败"+e.getMessage());
							return null;
						}
		        	}else{
		        		if("1".equals(bindFlag)){//需要进行绑卡签约
		        			TxnsOrderinfoModel orderinfo = gateWayService.getOrderinfoByTN(tn_);
	        				 TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
		        			 if(StringUtil.isEmpty(instiCode)){
		        				 instiCode = txnsLog.getAcccoopinstino();
		        			 }
		        			 merId=txnsLog.getAccsecmerno()==null?txnsLog.getAccsecmerno():instiCode;
		        	        WapCardBean cardBean = new WapCardBean(cardNo,cardType , customerNm,certifTp, certifId, phoneNo, cvn2, expired);
		        	        cardBean.setTn(tn_);
		        	        ResultBean resultBean = gateWayService.bindingBankCard(merId, "999999999999999", cardBean);
		        	        if(resultBean==null){
		        	        	log.error("绑卡签约失败"+JSON.toJSONString(cardBean));
		        	        	return null;
		        	        }
		        	        if(resultBean.isResultBool()){
		        	        	//保存绑卡信息
		        	            QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		        	            quickpayCustBean.setCustomerno(instiCode);
		        	            quickpayCustBean.setCardno(cardNo);
		        	            quickpayCustBean.setCardtype(cardType);
		        	            quickpayCustBean.setAccname(customerNm);
		        	            quickpayCustBean.setPhone(phoneNo);
		        	            quickpayCustBean.setIdtype(certifTp);
		        	            quickpayCustBean.setIdnum(certifId);
		        	            quickpayCustBean.setCvv2(cvn2);
		        	            quickpayCustBean.setValidtime(expired);
		        	            quickpayCustBean.setRelatememberno("999999999999999");
		        	            //新增设备ID支持匿名支付
		        	            quickpayCustBean.setDevId(devId);
		        	            CardBinBean cardBin = cardBinService.getCard(cardNo);
		        	            quickpayCustBean.setBankcode(cardBin.getBankCode());
		        	            quickpayCustBean.setBankname(cardBin.getBankName());
		        	            long bindId = memberBankCardService.saveQuickPayCustExt(quickpayCustBean);
		        	            Map<String, Object> resultMap = new HashMap<String, Object>();
		    	        		resultMap.put("tn", tn_);
		    	        		resultMap.put("bindId", bindId+"");
		    	        		try {
		    						gateWayService.sendSMSMessage(JSON.toJSONString(resultMap));
		    						return "true";
		    					} catch (Exception e) {
		    						e.printStackTrace();
		    						log.error("发送短信失败"+e.getMessage());
		    						return null;
		    					}
		        	            
		        	        }
		        		}else{
		        			log.error("发送短信失败 :bindFlag is null");
		        			return null;
		        		}
		        	}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("发送短信失败"+e.getMessage());
					return null;
				}
				 
		}
		
		return retcode;
	}
	public static void main(String[] args) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("tn", "123");
		resultMap.put("bindId", "123456");
		
		System.out.println(JSON.toJSONString(resultMap));
	}
}
