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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.business.individual.exception.DataBaseErrorException;
import com.zlebank.zplatform.business.individual.exception.MerchMKNotExistException;
import com.zlebank.zplatform.business.individual.exception.MerchWhiteListNotExistException;
import com.zlebank.zplatform.business.individual.service.MerchBusinessService;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.dao.MemberDAO;
import com.zlebank.zplatform.member.dao.MerchMKDAO;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.pojo.PojoMerchMK;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.dao.MerchWhiteListDAO;
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
	private MerchMKDAO merchMKDAO;
	@Autowired
	private MerchWhiteListDAO merchWhiteListDAO;
	@Autowired
	private MemberOperationService memberOperationService;
	@Autowired
	private MemberDAO memberDAO;

	/**
	 *
	 * @param memberId
	 * @param pub_key
	 * @return
	 * @throws MerchMKNotExistException,DataBaseErrorException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public boolean updateMerchPubKey(String memberId, String pub_key) throws MerchMKNotExistException,DataBaseErrorException {
		// TODO Auto-generated method stub
		
		PojoMerchMK merchMK = merchMKDAO.get(memberId);
		if(merchMK==null){
			//商户秘钥不存在异常
			throw new MerchMKNotExistException();
		}
		merchMK.setMemberPubKey(pub_key);
		try {
			merchMKDAO.update(merchMK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataBaseErrorException();
		}
		return true;
	}

	/**
	 *
	 * @param merchWhiteList
	 * @return
	 * @throws DataBaseErrorException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public boolean saveWhiteList(PojoMerchWhiteList merchWhiteList) throws DataBaseErrorException {
		// TODO Auto-generated method stub
		try {
			merchWhiteListDAO.merge(merchWhiteList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataBaseErrorException();
		}
		return true;
	}

	/**
	 *
	 * @param merchWhiteList
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public boolean updateWhiteList(PojoMerchWhiteList merchWhiteList)  throws DataBaseErrorException,MerchWhiteListNotExistException{
		PojoMerchWhiteList merchWhite = merchWhiteListDAO.getMerchWhiteListById(merchWhiteList.getId());
		if(merchWhite==null){
			throw new MerchWhiteListNotExistException();
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
			merchWhiteListDAO.update(merchWhite);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataBaseErrorException();
		}
		return true;
	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws MerchWhiteListNotExistException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public boolean deleteWhiteList(Long id)  throws DataBaseErrorException, MerchWhiteListNotExistException{
		// TODO Auto-generated method stub
		PojoMerchWhiteList merchWhiteList = merchWhiteListDAO.getMerchWhiteListById(id);
		if(merchWhiteList==null){
			throw new MerchWhiteListNotExistException();
		}
		merchWhiteList.setStatus("9");
		try {
			merchWhiteListDAO.merge(merchWhiteList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataBaseErrorException();
		}
		return true;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public boolean resetPayPwd(String memberId, String payPwd) throws DataCheckFailedException {
		PojoMember pm = memberDAO.getMemberByMemberId(memberId, MemberType.ENTERPRISE);
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
