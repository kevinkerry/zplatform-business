/* 
 * AppUpdateServiceImpl.java  
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
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.business.individual.service.AppUpdateService;
import com.zlebank.zplatform.trade.dao.AppUpdateDAO;
import com.zlebank.zplatform.trade.model.PojoAppUpdate;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年6月24日 下午4:26:34
 * @since 
 */
@Service("appUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService{

	@Autowired
	private AppUpdateDAO appUpdateDAO;
	/**
	 *
	 * @param appVersion
	 * @param appChannelId
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public PojoAppUpdate getAppUpdate(String appVersion, String appChannelId) {
		// TODO Auto-generated method stub
		return appUpdateDAO.getAppUpdate(appVersion, appChannelId);
	}

}
