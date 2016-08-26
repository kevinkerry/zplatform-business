/* 
 * MerchServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年6月24日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.business.individual.service.MerchBusinessService;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.MerchMK;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.rmi.member.IMemberOperationService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.member.IMerchMKService;
import com.zlebank.zplatform.rmi.trade.MerchWhiteListServiceProxy;
import com.zlebank.zplatform.trade.model.PojoMerchWhiteList;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年6月24日 上午10:17:48
 * @since 
 */
@Service("merchBusinessService")
public class MerchBusinessServiceImpl implements MerchBusinessService{
	
	@Autowired
	//private MerchMKDAO merchMKDAO;
	private IMerchMKService merchMKService;
	@Autowired
	//private MerchWhiteListDAO merchWhiteListDAO;
	private MerchWhiteListServiceProxy merchWhiteListService;
	@Autowired
	private IMemberOperationService memberOperationService;
	@Autowired
	//private MemberDAO memberDAO;
	private IMemberService memberService;

	/**
	 *
	 * @param memberId
	 * @param pub_key
	 * @return
	 * @throws MerchMKNotExistException,DataBaseErrorException 
	 */
	public boolean updateMerchPubKey(String memberId, String pub_key) throws Exception {
		// TODO Auto-generated method stub
		
		MerchMK merchMK = merchMKService.get(memberId);
		if(merchMK==null){
			//商户秘钥不存在异常
			//throw new MerchMKNotExistException();
			throw new Exception();
		}
		merchMK.setMemberPubKey(pub_key);
		try {
			//merchMKDAO.update(merchMK);
			merchMKService.updateMerchMK(merchMK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new DataBaseErrorException();
			throw new Exception();
		}
		return true;
	}

	/**
	 *
	 * @param merchWhiteList
	 * @return
	 * @throws DataBaseErrorException 
	 */
	public boolean saveWhiteList(PojoMerchWhiteList merchWhiteList) throws Exception {
		// TODO Auto-generated method stub
		try {
			merchWhiteListService.merge(merchWhiteList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new DataBaseErrorException();
			throw new Exception();
		}
		return true;
	}

	/**
	 *
	 * @param merchWhiteList
	 * @return
	 */
	public boolean updateWhiteList(PojoMerchWhiteList merchWhiteList)  throws Exception{
		PojoMerchWhiteList merchWhite = merchWhiteListService.getMerchWhiteListById(merchWhiteList.getId());
		if(merchWhite==null){
		//	throw new MerchWhiteListNotExistException();
			throw new Exception();
		}
		try {
			merchWhite.setAccName(merchWhiteList.getAccName());
			merchWhite.setCardNo(merchWhiteList.getCardNo());
			merchWhite.setId(merchWhiteList.getId());
			merchWhite.setMerchId(merchWhiteList.getMerchId());
			merchWhite.setNotes(merchWhiteList.getNotes());
			merchWhite.setStatus(merchWhiteList.getStatus());
			merchWhite.setUptime(merchWhiteList.getUptime());
			merchWhite.setUpuser(merchWhiteList.getUpuser());
			merchWhiteListService.merge(merchWhite);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new DataBaseErrorException();
			throw new Exception();
		}
		return true;
	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws MerchWhiteListNotExistException 
	 */
	public boolean deleteWhiteList(Long id)  throws Exception{
		// TODO Auto-generated method stub
		PojoMerchWhiteList merchWhiteList = merchWhiteListService.getMerchWhiteListById(id);
		if(merchWhiteList==null){
			//throw new MerchWhiteListNotExistException();
			throw new Exception();
		}
		merchWhiteList.setStatus("9");
		try {
			merchWhiteListService.merge(merchWhiteList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new DataBaseErrorException();
			throw new Exception();
		}
		return true;
	}

	public boolean resetPayPwd(String memberId, String payPwd) throws Exception {
		PojoMember pm = memberService.getMbmberByMemberId(memberId, MemberType.ENTERPRISE);
		if(pm==null){
			return false;
		}
		MemberBean member = new MemberBean();
		member.setLoginName(pm.getLoginName());
		member.setInstiId(pm.getInstiId());
		member.setPhone(pm.getPhone());
		return memberOperationService.resetPayPwd(MemberType.ENTERPRISE, member, payPwd, false);
	}
}
