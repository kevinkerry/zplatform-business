package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;

public interface MemberInfoService {
	String register(Member registerMemberInfo,String smsCode);
	Member queryMemebr(String loginName,String coopInstiCode);
	boolean login(String loginName,String pwd,String coopInstiCode);
	boolean realName(IndividualRealInfo individualRealInfo,String smsCode,String coopInstiCode);
	boolean vaildatePayPwd(String memberId,String payPwd);
	boolean modifyPwd(String memberId,String orgPwd,String pwd);
	boolean modifyPayPwd(String memberId,String orgPayPwd,String payPwd);
	boolean resetPwd(String memberId,String pwd,String smsCode);
	boolean resetPayPwd(String memberId,String payPwd,String smsCode);
}
