package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.member.exception.CreateBusiAcctFailedException;
import com.zlebank.zplatform.member.exception.CreateMemberFailedException;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.InvalidMemberDataException;
import com.zlebank.zplatform.member.exception.LoginFailedException;

public interface MemberInfoService {
	/**
	 * 会员注册
	 * @param registerMemberInfo 会员信息
	 * @param smsCode 短信验证码
	 * @return
	 * @throws CreateBusiAcctFailedException 
	 * @throws CreateMemberFailedException 
	 * @throws InvalidMemberDataException 
	 */
	public String register(Member registerMemberInfo,String smsCode) throws InvalidMemberDataException, CreateMemberFailedException, CreateBusiAcctFailedException;
	/**
	 * 会员信息查询
	 * @param loginName 登录名
	 * @param coopInstiCode 合作机构代码
	 * @return
	 */
	public Member queryMemebr(String loginName,String coopInstiCode);
	/**
	 * 会员登录
	 * @param loginName 登录名
	 * @param pwd 登录密码
	 * @param coopInstiCode 合作机构号
	 * @return
	 * @throws LoginFailedException 
	 * @throws DataCheckFailedException 
	 */
	public boolean login(String loginName,String pwd,String coopInstiCode) throws DataCheckFailedException, LoginFailedException;
	/**
	 * 实名认证
	 * @param individualRealInfo 实名认证信息
	 * @param smsCode 短信验证码
	 * @param coopInstiCode 合作机构号
	 * @return
	 */
	public boolean realName(IndividualRealInfo individualRealInfo,String smsCode,String coopInstiCode);
	/**
	 * 验证支付密码
	 * @param memberId 会员号
	 * @param payPwd 支付密码
	 * @return
	 */
	public boolean vaildatePayPwd(String memberId,String payPwd);
	/**
	 * 修改登录密码
	 * @param memberId 会员号
	 * @param orgPwd 原始密码
	 * @param pwd 新密码
	 * @return
	 */
	public boolean modifyPwd(String memberId,String orgPwd,String pwd);
	/**
	 * 修改支付密码
	 * @param memberId 会员号
	 * @param orgPayPwd 原始支付密码
	 * @param payPwd 新支付密码
	 * @return
	 */
	public boolean modifyPayPwd(String memberId,String orgPayPwd,String payPwd);
	/**
	 * 重置登录密码
	 * @param memberId 会员号
	 * @param pwd 登录密码 
	 * @param smsCode 短信验证码
	 * @return
	 */
	public boolean resetPwd(String memberId,String pwd,String smsCode);
	/**
	 * 重置支付密码
	 * @param memberId 会员号
	 * @param payPwd 支付密码
	 * @param smsCode 短信验证码
	 * @return
	 */
	public boolean resetPayPwd(String memberId,String payPwd,String smsCode);
}
