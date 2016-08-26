/* 
 * MemberInfoServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年1月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.enums.RealNameTypeEnum;
import com.zlebank.zplatform.business.individual.service.MemberInfoService;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.CoopInsti;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.RealNameBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.bean.enums.RealNameLvType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.rmi.member.ICoopInstiService;
import com.zlebank.zplatform.rmi.member.IMemberBankCardService;
import com.zlebank.zplatform.rmi.member.IMemberOperationService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.trade.CardBinServiceProxy;
import com.zlebank.zplatform.rmi.trade.GateWayServiceProxy;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.trade.bean.CardBinBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.utils.StringUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月19日 下午3:18:39
 * @since
 */
@Service("memberInfoService")
public class MemberInfoServiceImpl implements MemberInfoService {

	@Autowired
	private IMemberOperationService memberOperationService;
	@Autowired
	private SMSServiceProxy smsService;
	@Autowired
	//private MemberDAO memberDAO;
	private IMemberService memberService;
	@Autowired
	private IMemberBankCardService memberBankCardService;
	@Autowired
	private GateWayServiceProxy gateWayService;
	@Autowired
	//private CardBinDao cardBinDao;
	private CardBinServiceProxy cardBinService;
    @Autowired
    //CoopInstiDAO coopInstiDAO;
    private ICoopInstiService coopInstiService;
	/**
	 *
	 * @param registerMemberInfo
	 * @param smsCode
	 * @return
	 * @throws CreateBusiAcctFailedException
	 * @throws CreateMemberFailedException
	 * @throws InvalidMemberDataException
	 */
	@Override
	public String register(Member registerMemberInfo, String smsCode)
			throws Exception {
		int retCode = smsService.verifyCode(ModuleTypeEnum.REGISTER,
				registerMemberInfo.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		// 机构号转换为机构ID
		CoopInsti coopInsti = coopInstiService.getInstiByInstiCode(registerMemberInfo.getInstiCode());//getByInstiCode(registerMemberInfo.getInstiCode());
		registerMemberInfo.setInstiCode(coopInsti.getInstiCode());
		registerMemberInfo.setInstiId(coopInsti.getId());
		String memberId = memberOperationService.registMember(
				MemberType.INDIVIDUAL, registerMemberInfo);
		return memberId;
	}

	/**
	 *
	 * @param loginName
	 * @param coopInstiCode
	 * @return
	 */
	@Override
	public Member queryMember(String loginName, String coopInstiCode) {
	    CoopInsti coopInsti = coopInstiService.getInstiByInstiCode(coopInstiCode);
		PojoMember pm = memberService.getMemberByLoginNameAndCoopInsti(loginName, coopInsti.getId());
		if(pm==null){
			return null;
		}
		Member member = new Member();
		long memid = pm.getMemId();
		String memberId = pm.getMemberId();
		String memberName=pm.getMemberName();
		String pwd=pm.getPwd();
		String paypwd=pm.getPayPwd();
		RealNameLvType realnameLv=pm.getRealnameLv();
		String phone=pm.getPhone();
		String email=pm.getEmail();
		String memberType=pm.getMemberType().getCode();
		String memberStatus=pm.getStatus().getCode();
		String registerIdent=pm.getRegisterIdent();
		member.setMemid(memid+"");
		member.setMemberId(memberId);
		member.setInstiCode(coopInstiCode);
		member.setMemberName(memberName);
		member.setPwd(pwd);
		member.setPaypwd(paypwd);
		member.setRealnameLv(realnameLv.getCode());
		member.setPhone(phone);
		member.setEmail(email);
		member.setMemberType(memberType);
		member.setMemberStatus(memberStatus);
		member.setRegisterIdent(registerIdent);
		return member;
	}

	/**
	 *
	 * @param loginName
	 * @param pwd
	 * @param coopInstiCode
	 * @return
	 * @throws LoginFailedException
	 * @throws DataCheckFailedException
	 */
	@Override
	public String login(String loginName, String pwd, String coopInstiCode)
			throws Exception {
		MemberBean member = new MemberBean();
		member.setLoginName(loginName);
		member.setPwd(pwd);
		CoopInsti coopInsti = coopInstiService.getInstiByInstiCode(coopInstiCode);
		member.setInstiId(coopInsti.getId());
		String memberId = memberOperationService.login(MemberType.INDIVIDUAL,member);
		if (memberId!=null&&!"".equals(memberId)) {
			return memberId;
		}
		return null;
	}

	/**
	 *
	 * @param individualRealInfo
	 * @param smsCode
	 * @param coopInstiCode
	 * @return
	 * @throws DataCheckFailedException 
	 * @throws UnbindBankFailedException 
	 */
	@Override
	public boolean realName(IndividualRealInfo individualRealInfo,
            String smsCode,
            String payPwd,
            String memberId,RealNameTypeEnum realNameTypeEnum) throws Exception {
		//校验短信验证码
		PojoMember pm = memberService.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
		if(realNameTypeEnum!=RealNameTypeEnum.CARDREALNAME){
			if(pm==null){
				return false;
			}
			int retCode = smsService.verifyCode(ModuleTypeEnum.BINDCARD,individualRealInfo.getPhoneNo(), smsCode);
			if(retCode != 1) {
				throw new RuntimeException("验证码错误");
			}
		}
        
        // 查询当前操作是【实名认证】还是【重置密码】
        RealNameBean bean = new RealNameBean();
        bean.setMemberId(memberId);
        RealNameBean realNameInfo = memberBankCardService.queryRealNameInfo(bean );
        // 【重置密码】时
        if (realNameInfo!=null) {
            if (!realNameInfo.getIdentiNum().equals(individualRealInfo.getCertifNo()) ||
                    !realNameInfo.getRealname().equals(individualRealInfo.getCustomerName())) {
                throw new RuntimeException("输入的实名信息不对");
            }
          // 检查输入银行卡信息
          if(realNameTypeEnum != RealNameTypeEnum.CARDREALNAME){// 发送验证码时不解绑
              PagedResult<QuickpayCustBean> queryMemberBankCard = memberBankCardService.queryMemberBankCard(memberId, "0", individualRealInfo.getDevId(),0, 100);
              try {
                    List<QuickpayCustBean> pagedResult = queryMemberBankCard.getPagedResult();
                    QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
                    for (QuickpayCustBean pojo : pagedResult) {
                        // 解绑所有的银行卡
                        quickpayCustBean.setId(Long.valueOf(pojo.getId()));
                        quickpayCustBean.setRelatememberno(memberId);
                        try {
                            memberBankCardService.unbindQuickPayCust(quickpayCustBean);
                        } catch (Exception e) {
                            throw new Exception("解绑银行卡失败");
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
          }
        }
        
        // 【实名认证】【重置密码】
        CoopInsti pojoCoopInsti = coopInstiService.getInstiByInstiID(pm.getInstiId());
        WapCardBean cardBean = new WapCardBean(individualRealInfo.getCardNo(), individualRealInfo.getCardType(), individualRealInfo.getCustomerName(), 
                individualRealInfo.getCertifType(), individualRealInfo.getCertifNo(), individualRealInfo.getPhoneNo(), individualRealInfo.getCvn2(), 
                individualRealInfo.getExpired());
        ResultBean resultBean = gateWayService.bindingBankCard(pojoCoopInsti.getInstiCode(), memberId, cardBean);
        if(realNameTypeEnum==RealNameTypeEnum.CARDREALNAME){
            return resultBean.isResultBool();
        }
        if(resultBean.isResultBool()){
            //保存实名认证信息
            RealNameBean realNameBean = new RealNameBean();
            realNameBean.setMemberId(memberId);
            realNameBean.setRealname(individualRealInfo.getCustomerName());
            realNameBean.setIdentiType(individualRealInfo.getCertifType());
            realNameBean.setIdentiNum(individualRealInfo.getCertifNo());
            realNameBean.setStatus("00");
            RealNameBean nameBean = memberBankCardService.queryRealNameInfo(realNameBean);
            if(nameBean==null){
                memberBankCardService.saveRealNameInfo(realNameBean);
            }
            //保存绑卡信息
            QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
            quickpayCustBean.setCustomerno(pojoCoopInsti.getInstiCode());
            quickpayCustBean.setCardno(individualRealInfo.getCardNo());
            quickpayCustBean.setCardtype(individualRealInfo.getCardType());
            quickpayCustBean.setAccname(individualRealInfo.getCustomerName());
            quickpayCustBean.setPhone(individualRealInfo.getPhoneNo());
            quickpayCustBean.setIdtype(individualRealInfo.getCertifType());
            quickpayCustBean.setIdnum(individualRealInfo.getCertifNo());
            quickpayCustBean.setCvv2(individualRealInfo.getCvn2());
            quickpayCustBean.setValidtime(individualRealInfo.getExpired());
            quickpayCustBean.setRelatememberno(memberId);
            //新增设备ID支持匿名支付
            quickpayCustBean.setDevId(individualRealInfo.getDevId());
            CardBinBean cardBin = cardBinService.getCard(individualRealInfo.getCardNo());
            quickpayCustBean.setBankcode(cardBin.getBankCode());
            quickpayCustBean.setBankname(cardBin.getBankName());
            memberBankCardService.saveQuickPayCust(quickpayCustBean);
            //重置支付密码
            MemberBean member = new MemberBean();
            member.setLoginName(pm.getLoginName());
            member.setInstiId(pojoCoopInsti.getId());
            member.setPhone(pm.getPhone());
            return memberOperationService.resetPayPwd(MemberType.INDIVIDUAL, member, payPwd, false);
        }
		return false;
	}

	/**
	 *
	 * @param memberId
	 * @param payPwd
	 * @return
	 * @throws DataCheckFailedException 
	 */
	@Override
	public boolean vaildatePayPwd(String memberId, String payPwd) throws Exception {
		PojoMember pm = memberService.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
		if(pm==null){
			return false;
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiId(pm.getInstiId());
		member.setPhone(pm.getPhone());
		member.setPaypwd(payPwd);
		return memberOperationService.verifyPayPwd(MemberType.INDIVIDUAL, member);
	}

	/**
	 *
	 * @param memberId
	 * @param orgPwd
	 * @param pwd
	 * @return
	 * @throws DataCheckFailedException 
	 */
	@Override
	public boolean modifyPwd(String memberId, String orgPwd, String pwd) throws Exception {
		PojoMember pm = memberService.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
		if(pm==null){
			return false;
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiId(pm.getInstiId());
		member.setPhone(pm.getPhone());
		member.setPwd(orgPwd);
		return memberOperationService.resetLoginPwd(MemberType.INDIVIDUAL, member, pwd, true);
	}

	/**
	 *
	 * @param memberId
	 * @param orgPayPwd
	 * @param payPwd
	 * @return
	 * @throws DataCheckFailedException 
	 */
	@Override
	public boolean modifyPayPwd(String memberId, String orgPayPwd, String payPwd) throws Exception {
		PojoMember pm = memberService.getMbmberByMemberId(memberId, null);
		if(pm==null){
			return false;
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiId(pm.getInstiId());
		member.setPhone(pm.getPhone());
		member.setPaypwd(orgPayPwd);

		if(orgPayPwd!=null&&!"".equals(orgPayPwd)){
			return memberOperationService.resetPayPwd(MemberType.INDIVIDUAL, member, payPwd, false);	
		} else {
			return memberOperationService.resetPayPwd(MemberType.INDIVIDUAL, member, payPwd, true);
		}

	}

	/**
	 *
	 * @param memberId
	 * @param pwd
	 * @param smsCode
	 * @return
	 * @throws DataCheckFailedException 
	 */
	@Override
	public boolean resetPwd(String memberId, String pwd, String smsCode) throws Exception {
		PojoMember pm = memberService.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
		if(pm==null){
			return false;
		}
		int retCode = smsService.verifyCode(ModuleTypeEnum.CHANGELOGINPWD,
				pm.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiId(pm.getInstiId());
		member.setPhone(pm.getPhone());
		return memberOperationService.resetLoginPwd(MemberType.INDIVIDUAL, member, pwd, false);
	}

	/**
	 *
	 * @param memberId
	 * @param payPwd
	 * @param smsCode
	 * @return
	 * @throws DataCheckFailedException 
	 */
	@Override
	public boolean resetPayPwd(String memberId, String payPwd, String smsCode) throws Exception {
		PojoMember pm = memberService.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
		if(pm==null){
			return false;
		}
		int retCode = smsService.verifyCode(ModuleTypeEnum.RESETPAYPWD,
				pm.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiId(pm.getInstiId());
		member.setPhone(pm.getPhone());
		return memberOperationService.resetPayPwd(MemberType.INDIVIDUAL, member, payPwd, false);
	}

	/**
	 *
	 * @param memberId
	 * @param pwd
	 * @return
	 * @throws DataCheckFailedException
	 * @throws LoginFailedException 
	 */
	@Override
	public boolean vaildatePwd(String memberId, String pwd)
			throws Exception {
		PojoMember pm = memberService.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
		if(pm==null){
			return false;
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiId(pm.getInstiId());
		member.setPhone(pm.getPhone());
		member.setPwd(pwd);
		if(StringUtil.isNotEmpty(memberOperationService.login(MemberType.INDIVIDUAL, member))){
			return true;
		}
		return false;
	}
	
	

}
