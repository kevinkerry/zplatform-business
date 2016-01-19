package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.member.bean.MemberBean;

public interface MemberInfoService {
    /**
     * Register a individual member
     * 
     * @param registerIndividual
     * @param smsCode
     * @return member id
     */
    String register(MemberBean registerIndividual, String smsCode);
    /**
     * Query a member information
     * 
     * @param loginName
     * @param coopInstiCode
     * @return
     */
    MemberBean queryMember(String loginName, String coopInstiCode);
    /**
     * login with login name,login password
     * 
     * @param loginName
     * @param pwd
     * @param coopInstiCode
     * @return true if login success,else false
     */
    boolean login(String loginName, String pwd, String coopInstiCode);
    /**
     * Do a real name authenticate
     * 
     * @param individualRealInfo
     *            real name information
     * @param smsCode
     * @param coopInstiCode
     * @return
     */
    boolean realName(IndividualRealInfo individualRealInfo,
            String smsCode,
            String coopInstiCode);
    /**
     * Verify the pay password
     * 
     * @param memberId
     * @param payPwd
     * @return true if pay password match
     */
    boolean vaildatePayPwd(String memberId, String payPwd);
    /**
     * Modify the login password.<p>Note:The up layer which invoker the method must make
     * sure that member has login</p>
     * 
     * @param memberId
     * @param orgPwd
     *            the old login password
     * @param pwd
     *            the new login password to be set
     * @return true if modify success,else false
     */
    boolean modifyPwd(String memberId, String orgPwd, String pwd);
    /**
     * Modify the pay password.<p>Note:The up layer which invoker the method must make
     * sure that member has login</p>
     * 
     * @param memberId
     * @param orgPwd
     *            the old pay password
     * @param pwd
     *            the new pay password to be set
     * @return true if modify success,else false
     */
    boolean modifyPayPwd(String memberId, String orgPayPwd, String payPwd);
    /**
     * Reset login password.No need to make sure that member has login
     * @param memberId
     * @param pwd the new login password
     * @param smsCode
     * @return
     */
    boolean resetPwd(String memberId, String pwd, String smsCode);
    /**
     * Reset pay password.No need to make sure that member has login
     * @param memberId
     * @param payPwd the new pay password
     * @param smsCode
     * @return
     */
    boolean resetPayPwd(String memberId, String payPwd, String smsCode);
}
