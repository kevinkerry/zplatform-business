/* 
 * MemberCardService.java  
 * 
 * version TODO
 *
 * 2016年1月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.bean.enums.ExcepitonTypeEnum;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.business.individual.service.MemberCardService;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.CoopInsti;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.RealNameBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.rmi.member.ICoopInstiService;
import com.zlebank.zplatform.rmi.member.IMemberBankCardService;
import com.zlebank.zplatform.rmi.member.IMemberOperationService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.trade.CardBinServiceProxy;
import com.zlebank.zplatform.rmi.trade.CashBankServiceProxy;
import com.zlebank.zplatform.rmi.trade.GateWayServiceProxy;
import com.zlebank.zplatform.rmi.trade.TxnsLogServiceProxy;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.trade.bean.CardBinBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.common.page.PageVo;
import com.zlebank.zplatform.trade.model.CashBankModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月20日 下午1:50:40
 * @since 
 */
@Service("memberCardService")
public class MemberCardServiceImpl implements MemberCardService{
	private static final Log log = LogFactory.getLog(MemberCardServiceImpl.class);
	@Autowired
	private GateWayServiceProxy gateWayService;
	
	@Autowired
	private IMemberBankCardService memberBankCardService;
	@Autowired
	//private CardBinDao cardBinDao;
	private CardBinServiceProxy cardBinService;
	@Autowired
	private CashBankServiceProxy cashBankService;
	@Autowired
	private SMSServiceProxy smsService;
    @Autowired
    //CoopInstiDAO coopInstiDAO;
    private ICoopInstiService coopInstiService;
    @Autowired
    private IMemberService memberServiceImpl;
    @Autowired
    private IMemberOperationService memberOperationServiceImpl;
    //@Autowired
    //private IQuickpayCustService quickpayCustService;
	@Autowired
	private TxnsLogServiceProxy txnsLogService;
	
	/**
	 *
	 * @param memberId
	 * @return
	 * @throws IllegalAccessException 
	 */
	@Override
	public PagedResult<BankCardInfo> queryBankCard(String memberId,String cardType,String devId,int page,int pageSize) throws IllegalAccessException {
		PagedResult<QuickpayCustBean> pagedResult = memberBankCardService.queryMemberBankCard(memberId, cardType,devId, page, pageSize);
		List<BankCardInfo> bankCardList = new ArrayList<BankCardInfo>();
		for(QuickpayCustBean custBean :pagedResult.getPagedResult()){
			BankCardInfo bankCardInfo = new BankCardInfo();
			bankCardInfo.setBindcardid(custBean.getId()+"");
			Bank bank = new Bank();
			bank.setBankCode(custBean.getBankcode());
			bank.setBankName(custBean.getBankname());
			bankCardInfo.setBank(bank);
			IndividualRealInfo individualRealInfo = new IndividualRealInfo();
			individualRealInfo.setCardNo(custBean.getCardno());
			individualRealInfo.setCardType(custBean.getCardtype());
			individualRealInfo.setCustomerName(custBean.getAccname());
			individualRealInfo.setCertifType(custBean.getIdtype());
			individualRealInfo.setCertifNo(custBean.getIdnum());
			individualRealInfo.setPhoneNo(custBean.getPhone());
			individualRealInfo.setCvn2(custBean.getCvv2());
			individualRealInfo.setExpired(custBean.getValidtime());
			individualRealInfo.setDevId(custBean.getDevId());
			bankCardInfo.setBankCardInfo(individualRealInfo);
			bankCardList.add(bankCardInfo);
		}
		return new DefaultPageResult<BankCardInfo>(bankCardList,pagedResult.getTotal());
	}

	/**
	 *
	 * @param bankCardNo
	 * @return
	 */
	@Override
	public CardBinBean queryCardBin(String bankCardNo) {
		CardBinBean cardBin = cardBinService.getCard(bankCardNo);
		if (cardBin == null)
		    return null;
		cardBin.setBankCode(cardBin.getBankCode()+"0000");
		return cardBin;
	}

	/**
	 *
	 * @param individualMember
	 * @param bankCardInfo
	 * @param smsCode
	 * @return
	 * @throws CommonException 
	 */
	@Override
	public String bindBankCard(Member individualMember,
			BankCardInfo bankCardInfo, String smsCode) throws CommonException {
		int retCode = smsService.verifyCode(ModuleTypeEnum.BINDCARD.getCode(),
				individualMember.getPhone(), smsCode);
		if (retCode != 1) {
			 throw new CommonException(ExcepitonTypeEnum.PASSWORD.getCode(),"验证码错误");
		}
		// 查询实名认证信息
		RealNameBean bean = new RealNameBean();
		bean.setMemberId(individualMember.getMemberId());
        RealNameBean realNameInfo = memberBankCardService.queryRealNameInfo(bean );
        String realName = ""; // 真实姓名
        if ( realNameInfo != null ) 
            realName = realNameInfo.getRealname();
        String cardName = bankCardInfo.getBankCardInfo().getCustomerName(); // 绑卡真实姓名
        if (!realName.equals(cardName)) {
        	 throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),"绑卡姓名和实名信息不一致");
        }
		QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		CoopInsti pojoCoopInsti = coopInstiService.getInstiByInstiCode(individualMember.getInstiCode());
		quickpayCustBean.setCustomerno(pojoCoopInsti.getInstiCode());
		quickpayCustBean.setCardno(bankCardInfo.getBankCardInfo().getCardNo());
		quickpayCustBean.setCardtype(bankCardInfo.getBankCardInfo().getCardType());
		quickpayCustBean.setAccname(bankCardInfo.getBankCardInfo().getCustomerName());
		quickpayCustBean.setPhone(bankCardInfo.getBankCardInfo().getPhoneNo());
		quickpayCustBean.setIdtype(bankCardInfo.getBankCardInfo().getCertifType());
		quickpayCustBean.setIdnum(bankCardInfo.getBankCardInfo().getCertifNo());
		quickpayCustBean.setCvv2(bankCardInfo.getBankCardInfo().getCvn2());
		quickpayCustBean.setValidtime(bankCardInfo.getBankCardInfo().getExpired());
		quickpayCustBean.setRelatememberno(individualMember.getMemberId());
		quickpayCustBean.setBankcode(bankCardInfo.getBank().getBankCode());
		quickpayCustBean.setBankname(bankCardInfo.getBank().getBankName());
		long bindId=memberBankCardService.saveQuickPayCust(quickpayCustBean);
		
		return bindId+"";
	}

	/**
	 *
	 * @param memberId
	 * @param bindcardid
	 * @param payPwd
	 * @return
	 * @throws UnbindBankFailedException 
	 * @throws DataCheckFailedException 
	 */
	@Override
	public boolean unbindBankCard(String memberId, String bindcardid,
			String payPwd) throws CommonException {
		//校验支付密码
	    PojoMember member = memberServiceImpl.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
        if (member == null) {// 资金账户不存在
        	 throw new CommonException(ExcepitonTypeEnum.MEMBER_INFO.getCode(),"会员不存在");
        }
	    MemberBean memberBean = new MemberBean();
        memberBean.setLoginName(member.getLoginName());
        memberBean.setInstiId(member.getInstiId());
        memberBean.setPhone(member.getPhone());
        memberBean.setPaypwd(payPwd);
        // 校验支付密码
        try {
			if (!memberOperationServiceImpl.verifyPayPwd(MemberType.INDIVIDUAL,  memberBean)) {
				throw new CommonException(ExcepitonTypeEnum.PASSWORD.getCode(),"支付密码错误");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),e.getMessage());
		}
		QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		quickpayCustBean.setId(Long.valueOf(bindcardid));
		quickpayCustBean.setRelatememberno(memberId);
		try {
			memberBankCardService.unbindQuickPayCust(quickpayCustBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommonException(ExcepitonTypeEnum.MEMBER_CARD.getCode(),e.getMessage());
		}
		
		return true;
	}

	/**
	 *
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Override
	public PagedResult<SupportedBankCardType> queryBank(int page, int pageSize) {
		List<SupportedBankCardType> supportBankList = new ArrayList<SupportedBankCardType>();
		List<CashBankModel> bankList = cashBankService.findBankPage(page, pageSize);
		for(CashBankModel casebank:bankList){
			SupportedBankCardType supportedBankCardType = new SupportedBankCardType();
			supportedBankCardType.setCardType(casebank.getCardtype());
			Bank bank = new Bank();
			bank.setBankCode(casebank.getBankcode());
			bank.setBankName(casebank.getBankname());
			supportedBankCardType.setBank(bank);
			supportBankList.add(supportedBankCardType);
		}
		PagedResult<SupportedBankCardType> pagedResult = new DefaultPageResult<SupportedBankCardType>(supportBankList, cashBankService.findBankCount());
		return pagedResult;
	}

	@Override
	public PageVo<SupportedBankCardType> queryCardList(Map<String, Object> map,
			Integer pageNo, Integer pageSize) {
		PageVo<CashBankModel>  pagevo = this.cashBankService.getCardList(map, pageNo, pageSize);
		List<CashBankModel>  bankList = pagevo.getList();
		List<SupportedBankCardType> supportBankList = new ArrayList<SupportedBankCardType>();
		for(CashBankModel casebank:bankList){
			SupportedBankCardType supportedBankCardType = new SupportedBankCardType();
			supportedBankCardType.setCardType(casebank.getCardtype());
			Bank bank = new Bank();
			bank.setBankCode(casebank.getBankcode());
			bank.setBankName(casebank.getBankname());
			bank.setBankIcon(casebank.getIco());
			supportedBankCardType.setBank(bank);
			supportBankList.add(supportedBankCardType);
		}
		PageVo<SupportedBankCardType>  returnPage= new PageVo<SupportedBankCardType>();
		returnPage.setList(supportBankList);
		returnPage.setTotal(pagevo.getTotal());
		return returnPage;
	}

	@Override
	public ResultBean anonymousBindCard(String json) {
		JSONObject jsonObject =  JSON.parseObject(json);
		//需要进行绑卡签约
		 String tn_=jsonObject.get("tn").toString();
		 String cardNo=jsonObject.get("cardNo").toString();
		 String cardType=jsonObject.get("cardType").toString();
		 String customerNm=jsonObject.get("customerNm").toString();
		 String certifTp=jsonObject.get("certifTp").toString();
		 String certifId=jsonObject.get("certifId").toString();
		 String phoneNo=jsonObject.get("phoneNo").toString();
		 String cvn2=jsonObject.get("cvn2")+"";
		 String expired=jsonObject.get("expired")+"";
		 String devId = jsonObject.get("devId")+"";  
		 TxnsOrderinfoModel orderinfo = gateWayService.getOrderinfoByTN(tn_);
		 TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
		 String instiCode = txnsLog.getAcccoopinstino();
		 WapCardBean cardBean = new WapCardBean(cardNo,cardType , customerNm,certifTp, certifId, phoneNo, cvn2, expired);
          ResultBean resultBean = gateWayService.bindingBankCard(instiCode, "999999999999999", cardBean);
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
            long bindId = memberBankCardService.saveQuickPayCust(quickpayCustBean);
            Map<String, Object> resultMap = new HashMap<String, Object>();
    		resultMap.put("tn", tn_);
    		resultMap.put("bindId", bindId+"");
    		try {
				gateWayService.sendSMSMessage(JSON.toJSONString(resultMap));
				return new ResultBean(bindId);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultBean("",e.getMessage());
			}
            
        }else{
        	return resultBean;
        }
		
	}
}

