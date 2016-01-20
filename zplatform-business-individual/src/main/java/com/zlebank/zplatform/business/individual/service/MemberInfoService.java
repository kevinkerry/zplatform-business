package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;

import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.member.exception.CreateBusiAcctFailedException;
import com.zlebank.zplatform.member.exception.CreateMemberFailedException;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.InvalidMemberDataException;
import com.zlebank.zplatform.member.exception.LoginFailedException;

import com.zlebank.zplatform.member.bean.MemberBean;


public interface MemberInfoService {
	/**
	 * 会员注册 Register a individual member
	 * @param registerMemberInfo 会员信息
	 * @param smsCode 短信验证码
	 * @return
	 * @throws CreateBusiAcctFailedException 
	 * @throws CreateMemberFailedException 
	 * @throws InvalidMemberDataException 
	 */
	public String register(Member registerMemberInfo,String smsCode) throws InvalidMemberDataException, CreateMemberFailedException, CreateBusiAcctFailedException;
	/**
	 * 会员信息查询 Query a member information
	 * @param loginName 登录名
	 * @param coopInstiCode 合作机构代码
	 * @return
	 */
	public Member queryMemebr(String loginName,String coopInstiCode);
	/**
	 * 会员登录 login with login name,login password
	 * @param loginName 登录名
	 * @param pwd 登录密码
	 * @param coopInstiCode 合作机构号
	 * @return
	 * @throws LoginFailedException 
	 * @throws DataCheckFailedException 
	 */
	public boolean login(String loginName,String pwd,String coopInstiCode) throws DataCheckFailedException, LoginFailedException;
	/**
	 * 实名认证 Do a real name authenticate
	 * @param individualRealInfo 实名认证信息
	 * @param smsCode 短信验证码
	 * @param 会员号 
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean realName(IndividualRealInfo individualRealInfo,
            String smsCode,
            String payPwd,
            String memberId) throws DataCheckFailedException;
	/**
	 * 验证支付密码  Verify the pay password
	 * @param memberId 会员号
	 * @param payPwd 支付密码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean vaildatePayPwd(String memberId,String payPwd) throws DataCheckFailedException;
	/**
	 * Modify the login password.<p>Note:The up layer which invoker the method must make
     * sure that member has login</p>
	 * 修改登录密码
	 * @param memberId 会员号
	 * @param orgPwd 原始密码
	 * @param pwd 新密码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean modifyPwd(String memberId,String orgPwd,String pwd) throws DataCheckFailedException;
	/**
     * Modify the pay password.<p>Note:The up layer which invoker the method must make
     * sure that member has login</p>
	 * 修改支付密码
	 * @param memberId 会员号
	 * @param orgPayPwd 原始支付密码
	 * @param payPwd 新支付密码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean modifyPayPwd(String memberId,String orgPayPwd,String payPwd) throws DataCheckFailedException;
	/**
	 * 重置登录密码 Reset login password.No need to make sure that member has login
	 * @param memberId 会员号
	 * @param pwd 登录密码 
	 * @param smsCode 短信验证码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean resetPwd(String memberId,String pwd,String smsCode) throws DataCheckFailedException;
	/**
	 * 重置支付密码 Reset pay password.No need to make sure that member has login
	 * @param memberId 会员号
	 * @param payPwd 支付密码
	 * @param smsCode 短信验证码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean resetPayPwd(String memberId,String payPwd,String smsCode) throws DataCheckFailedException;
}
