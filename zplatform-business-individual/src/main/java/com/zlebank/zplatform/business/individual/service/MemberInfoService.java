package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.enums.RealNameTypeEnum;
import com.zlebank.zplatform.business.individual.exception.CommonException;


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
	public String register(Member registerMemberInfo,String smsCode) throws CommonException;
	/**
	 * 会员信息查询 Query a member information
	 * @param loginName 登录名
	 * @param coopInstiCode 合作机构代码
	 * @return
	 */
	public Member queryMember(String loginName,String coopInstiCode);
	/**
	 * 会员登录 login with login name,login password
	 * @param loginName 登录名
	 * @param pwd 登录密码
	 * @param coopInstiCode 合作机构号
	 * @return 会员号
	 * @throws LoginFailedException 
	 * @throws DataCheckFailedException 
	 */
	public String login(String loginName,String pwd,String coopInstiCode) throws CommonException;
	/**
	 * 实名认证 Do a real name authenticate
	 * @param individualRealInfo 实名认证信息
	 * @param smsCode 短信验证码
	 * @param 会员号 
	 * @param realNameFlag 实名认证标示，
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean realName(IndividualRealInfo individualRealInfo,
            String smsCode,
            String payPwd,
            String memberId,RealNameTypeEnum realNameTypeEnum) throws CommonException;
	/**
	 * 实名认证 Do a real name authenticate
	 * @param individualRealInfo 实名认证信息
	 * @param smsCode 短信验证码
	 * @param 会员号 
	 * @param realNameFlag 实名认证标示，
	 * @return bindId 绑卡标示
	 * @throws DataCheckFailedException 
	 */
	public Long realName(IndividualRealInfo individualRealInfo,
            String smsCode,
            String memberId,RealNameTypeEnum realNameTypeEnum) throws CommonException;
	/**
	 * 验证支付密码  Verify the pay password
	 * @param memberId 会员号
	 * @param payPwd 支付密码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean vaildatePayPwd(String memberId,String payPwd) throws CommonException;
	
	/**
	 * 验证登录密码  Verify the login password
	 * @param memberId
	 * @param pwd
	 * @return
	 * @throws DataCheckFailedException
	 * @throws LoginFailedException 
	 */
	public boolean vaildatePwd(String memberId,String pwd) throws CommonException;
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
	public boolean modifyPwd(String memberId,String orgPwd,String pwd) throws CommonException;
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
	public boolean modifyPayPwd(String memberId,String orgPayPwd,String payPwd) throws CommonException;
	/**
	 * 重置登录密码 Reset login password.No need to make sure that member has login
	 * @param memberId 会员号
	 * @param pwd 登录密码 
	 * @param smsCode 短信验证码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean resetPwd(String memberId,String pwd,String smsCode) throws CommonException;
	/**
	 * 重置支付密码 Reset pay password.No need to make sure that member has login
	 * @param memberId 会员号
	 * @param payPwd 支付密码
	 * @param smsCode 短信验证码
	 * @return
	 * @throws DataCheckFailedException 
	 */
	public boolean resetPayPwd(String memberId,String payPwd,String smsCode) throws CommonException;
	
	/**
	 * 校验解绑手机信息
	 * @param memberId 会员号
	 * @param phone 原手机号
	 * @param payPwd 支付密码
	 * @param smsCode 短信验证码
	 * @return
	 * @throws CommonException
	 * @throws Exception
	 */
	public boolean vaildateUnbindPhone(String memberId,String phone,String payPwd,String smsCode) throws CommonException;
	
	/**
	 * 校验并更新会员绑定的手机号
	 * @param memberId 会员号
	 * @param phone 新手机号
	 * @param smsCode 短信验证码
	 * @throws CommonException
	 */
	public void modifyPhone(String memberId,String phone,String smsCode) throws CommonException;
	/**
	 * 校验绑卡信息和支付密码（修改手机号而原手机号不可用）
	 * @param memberId 会员号
	 * @param bindId  绑卡标示
	 * @param cardNo 银行卡号
	 * @param certNo 身份证号
	 * @param payPwd 支付密码
	 * @throws CommonException
	 * @throws Exception
	 */
	public void vaildateBankCardForModifyPhone(String memberId,long bindId,String cardNo,String certNo,String payPwd)throws CommonException;
	
	/**
	 * 校验绑卡信息和短信验证码（实名后重置支付密码）
	 * @param memberId 会员号
	 * @param phone 手机号
	 * @param smsCode 短信验证码
	 * @param bindId 绑卡标示
	 * @param cardNo 银行卡号
	 * @throws CommonException
	 */
	public void vaildateBankCardForResetPwd(String memberId,String phone,String smsCode,long bindId,String cardNo) throws CommonException;
	
	/**
	 * 查询会员信息（个人会员）
	 * @param memberId
	 * @return
	 */
	public Member queryPersonMember(String memberId);
}
