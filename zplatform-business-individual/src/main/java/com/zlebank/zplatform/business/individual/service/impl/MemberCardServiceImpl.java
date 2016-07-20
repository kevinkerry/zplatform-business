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

import javax.management.RuntimeErrorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.service.MemberCardService;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.dao.CardBinDao;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.RealNameBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.dao.CoopInstiDAO;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.UnbindBankFailedException;
import com.zlebank.zplatform.member.pojo.PojoCoopInsti;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberBankCardService;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.common.page.PageVo;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.CashBankModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.ICashBankService;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.IWebGateWayService;
import com.zlebank.zplatform.trade.utils.AmountUtil;

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
	private IGateWayService gateWayService;
	@Autowired
	private IWebGateWayService  webGateWayService;
	@Autowired
	private MemberBankCardService memberBankCardService;
	@Autowired
	private CardBinDao cardBinDao;
	@Autowired
	private ICashBankService cashBankService;
	@Autowired
	private ISMSService smsService;
    @Autowired
    CoopInstiDAO coopInstiDAO;
    @Autowired
    private MemberService memberServiceImpl;
    @Autowired
    private MemberOperationService memberOperationServiceImpl;
	@Autowired
	private ITxnsOrderinfoDAO orderDao;
	@Autowired
	private ITxnsLogService txnsLogService;
	
	/**
	 *
	 * @param memberId
	 * @return
	 * @throws IllegalAccessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public PagedResult<BankCardInfo> queryBankCard(String memberId,String cardType,String devId,int page,int pageSize) throws IllegalAccessException {
		PagedResult<QuickpayCustBean> pagedResult = memberBankCardService.queryMemberBankCard(memberId, cardType,devId, page, pageSize);
		System.out.println(JSON.toJSON(pagedResult.getPagedResult()));
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
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public CardBin queryCardBin(String bankCardNo) {
		CardBin cardBin = cardBinDao.getCard(bankCardNo);
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
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String bindBankCard(Member individualMember,
			BankCardInfo bankCardInfo, String smsCode) {
		int retCode = smsService.verifyCode(ModuleTypeEnum.BINDCARD,
				individualMember.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		// 查询实名认证信息
		RealNameBean bean = new RealNameBean();
		bean.setMemberId(individualMember.getMemberId());
        RealNameBean realNameInfo = memberBankCardService.queryRealNameInfo(bean );
        String realName = ""; // 真实姓名
        if ( realNameInfo != null ) 
            realName = realNameInfo.getRealname();
        String cardName = bankCardInfo.getBankCardInfo().getCustomerName(); // 绑卡真实姓名
        if (!realName.equals(cardName)) 
            throw new RuntimeException("绑卡姓名和实名信息不一致");
		QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		PojoCoopInsti pojoCoopInsti = coopInstiDAO.getByInstiCode(individualMember.getInstiCode());
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
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public boolean unbindBankCard(String memberId, String bindcardid,
			String payPwd) throws DataCheckFailedException, UnbindBankFailedException {
		//校验支付密码
	    PojoMember member = memberServiceImpl.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
        if (member == null) {// 资金账户不存在
            throw new UnbindBankFailedException("会员不存在");
        }
	    MemberBean memberBean = new MemberBean();
        memberBean.setLoginName(member.getLoginName());
        memberBean.setInstiId(member.getInstiId());
        memberBean.setPhone(member.getPhone());
        memberBean.setPaypwd(payPwd);
        // 校验支付密码
        if (!memberOperationServiceImpl.verifyPayPwd(MemberType.INDIVIDUAL,  memberBean)) {
            throw new UnbindBankFailedException("支付密码不对");
        }
		QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		quickpayCustBean.setId(Long.valueOf(bindcardid));
		quickpayCustBean.setRelatememberno(memberId);
		memberBankCardService.unbindQuickPayCust(quickpayCustBean);
		return true;
	}

	/**
	 *
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
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
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
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
			supportedBankCardType.setBank(bank);
			supportBankList.add(supportedBankCardType);
		}
		PageVo<SupportedBankCardType>  returnPage= new PageVo<SupportedBankCardType>();
		returnPage.setList(supportBankList);
		returnPage.setTotal(pagevo.getTotal());
		return returnPage;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
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
            CardBin cardBin = cardBinDao.getCard(cardNo);
            quickpayCustBean.setBankcode(cardBin.getBankCode());
            quickpayCustBean.setBankname(cardBin.getBankName());
            long bindId = memberBankCardService.saveQuickPayCust(quickpayCustBean);
            Map<String, Object> resultMap = new HashMap<String, Object>();
    		resultMap.put("tn", tn_);
    		resultMap.put("bindId", bindId+"");
    		try {
				gateWayService.sendSMSMessage(JSON.toJSONString(resultMap));
				return new ResultBean(bindId);
			} catch (TradeException e) {
				e.printStackTrace();
				return new ResultBean(e.getCode(),e.getMessage());
			}
            
        }else{
        	return resultBean;
        }
		
	}
}

