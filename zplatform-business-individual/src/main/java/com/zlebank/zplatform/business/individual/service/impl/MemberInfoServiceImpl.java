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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.service.MemberInfoService;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.enums.BusinessActorType;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.dao.MemberDAO;
import com.zlebank.zplatform.member.exception.CreateBusiAcctFailedException;
import com.zlebank.zplatform.member.exception.CreateMemberFailedException;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.InvalidMemberDataException;
import com.zlebank.zplatform.member.exception.LoginFailedException;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberBankCardService;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.service.IGateWayService;

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
	private MemberOperationService memberOperationService;
	@Autowired
	private ISMSService smsService;
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private MemberBankCardService memberBankCardService;
	@Autowired
	private IGateWayService gateWayService;
	
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
			throws InvalidMemberDataException, CreateMemberFailedException,
			CreateBusiAcctFailedException {
		int retCode = smsService.verifyCode(ModuleTypeEnum.REGISTER,
				registerMemberInfo.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
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
	public Member queryMemebr(String loginName, String coopInstiCode) {
		PojoMember pm = memberDAO.getMemberByLoginNameAndCoopInsti(loginName,
				coopInstiCode);
		if(pm==null){
			return null;
		}
		Member member = new Member();
		long memid = pm.getMemid();
		String memberId = pm.getMemberId();
		String instiCode=pm.getInstiCode();
		String memberName=pm.getMemberName();
		String pwd=pm.getPwd();
		String paypwd=pm.getPayPwd();
		String realnameLv=pm.getRealnameLv();
		String phone=pm.getPhone();
		String email=pm.getEmail();
		String memberType=pm.getMemberType().getCode();
		String memberStatus=pm.getStatus().getCode();
		String registerIdent=pm.getRegisterIdent();
		member.setMemid(memid+"");
		member.setMemberId(memberId);
		member.setInstiCode(instiCode);
		member.setMemberName(memberName);
		member.setPwd(pwd);
		member.setPaypwd(paypwd);
		member.setRealnameLv(realnameLv);
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
	public boolean login(String loginName, String pwd, String coopInstiCode)
			throws DataCheckFailedException, LoginFailedException {
		// TODO Auto-generated method stub
		MemberBean member = new MemberBean();
		member.setLoginName(loginName);
		member.setPwd(pwd);
		member.setInstiCode(coopInstiCode);
		String memberId = memberOperationService.login(MemberType.INDIVIDUAL,
				member);
		if (StringUtil.isNotEmpty(memberId)) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param individualRealInfo
	 * @param smsCode
	 * @param coopInstiCode
	 * @return
	 */
	@Override
	public boolean realName(IndividualRealInfo individualRealInfo,
			String smsCode, String memberId) {
		
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
	public boolean vaildatePayPwd(String memberId, String payPwd) throws DataCheckFailedException {
		PojoMember pm = memberDAO.getMbmberByMemberId(memberId, BusinessActorType.INDIVIDUAL);
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiCode(pm.getInstiCode());
		member.setPhone(pm.getPhone());
		member.setPaypwd(payPwd);
		memberOperationService.verifyPayPwd(MemberType.INDIVIDUAL, member);
		return false;
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
	public boolean modifyPwd(String memberId, String orgPwd, String pwd) throws DataCheckFailedException {
		PojoMember pm = memberDAO.getMbmberByMemberId(memberId, BusinessActorType.INDIVIDUAL);
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiCode(pm.getInstiCode());
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
	public boolean modifyPayPwd(String memberId, String orgPayPwd, String payPwd) throws DataCheckFailedException {
		PojoMember pm = memberDAO.getMbmberByMemberId(memberId, BusinessActorType.INDIVIDUAL);
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiCode(pm.getInstiCode());
		member.setPhone(pm.getPhone());
		member.setPaypwd(orgPayPwd);
		return memberOperationService.resetPayPwd(MemberType.INDIVIDUAL, member, payPwd, true);
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
	public boolean resetPwd(String memberId, String pwd, String smsCode) throws DataCheckFailedException {
		PojoMember pm = memberDAO.getMbmberByMemberId(memberId, BusinessActorType.INDIVIDUAL);
		int retCode = smsService.verifyCode(ModuleTypeEnum.REGISTER,
				pm.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiCode(pm.getInstiCode());
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
	public boolean resetPayPwd(String memberId, String payPwd, String smsCode) throws DataCheckFailedException {
		PojoMember pm = memberDAO.getMbmberByMemberId(memberId, BusinessActorType.INDIVIDUAL);
		int retCode = smsService.verifyCode(ModuleTypeEnum.REGISTER,
				pm.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiCode(pm.getInstiCode());
		member.setPhone(pm.getPhone());
		return memberOperationService.resetPayPwd(MemberType.INDIVIDUAL, member, payPwd, false);
	}

}
