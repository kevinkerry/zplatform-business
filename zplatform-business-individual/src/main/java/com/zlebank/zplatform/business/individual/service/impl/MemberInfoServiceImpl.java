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
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.dao.MemberDAO;
import com.zlebank.zplatform.member.exception.CreateBusiAcctFailedException;
import com.zlebank.zplatform.member.exception.CreateMemberFailedException;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.InvalidMemberDataException;
import com.zlebank.zplatform.member.exception.LoginFailedException;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.sms.service.ISMSService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月19日 下午3:18:39
 * @since 
 */
@Service("memberInfoService")
public class MemberInfoServiceImpl implements MemberInfoService{
	
	@Autowired
	private MemberOperationService memberOperationService;
	@Autowired
	private ISMSService smsService;
	@Autowired
	private MemberDAO memberDAO;

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
	public String register(Member registerMemberInfo, String smsCode) throws InvalidMemberDataException, CreateMemberFailedException, CreateBusiAcctFailedException {
		int retCode = smsService.verifyCode(ModuleTypeEnum.REGISTER, registerMemberInfo.getPhone(), smsCode);
		if(retCode!=1){
			throw new RuntimeException("验证码错误");
		}
		String memberId = memberOperationService.registMember(MemberType.INDIVIDUAL, registerMemberInfo);
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
		PojoMember pm = memberDAO.getMemberByLoginNameAndCoopInsti(loginName, coopInstiCode);
		
		
		return null;
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
	public boolean login(String loginName, String pwd, String coopInstiCode) throws DataCheckFailedException, LoginFailedException {
		// TODO Auto-generated method stub
		MemberBean member = new MemberBean();
		member.setLoginName(loginName);
		member.setPwd(pwd);
		member.setInstiCode(coopInstiCode);
		String memberId = memberOperationService.login(MemberType.INDIVIDUAL, member);
		if(StringUtil.isNotEmpty(memberId)){
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
			String smsCode, String coopInstiCode) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 *
	 * @param memberId
	 * @param payPwd
	 * @return
	 */
	@Override
	public boolean vaildatePayPwd(String memberId, String payPwd) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 *
	 * @param memberId
	 * @param orgPwd
	 * @param pwd
	 * @return
	 */
	@Override
	public boolean modifyPwd(String memberId, String orgPwd, String pwd) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 *
	 * @param memberId
	 * @param orgPayPwd
	 * @param payPwd
	 * @return
	 */
	@Override
	public boolean modifyPayPwd(String memberId, String orgPayPwd, String payPwd) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 *
	 * @param memberId
	 * @param pwd
	 * @param smsCode
	 * @return
	 */
	@Override
	public boolean resetPwd(String memberId, String pwd, String smsCode) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 *
	 * @param memberId
	 * @param payPwd
	 * @param smsCode
	 * @return
	 */
	@Override
	public boolean resetPayPwd(String memberId, String payPwd, String smsCode) {
		// TODO Auto-generated method stub
		return false;
	}

}
