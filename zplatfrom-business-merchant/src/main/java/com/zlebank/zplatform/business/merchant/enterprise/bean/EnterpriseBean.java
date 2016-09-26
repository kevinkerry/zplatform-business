/* 
 * EnterpriseBean.java  
 * 
 * version TODO
 *
 * 2016年3月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.enterprise.bean;

import java.io.Serializable;

import com.zlebank.zplatform.member.bean.enums.CardType;
import com.zlebank.zplatform.member.bean.enums.IndustryType;

/**
 * 企业 Bean
 * (如果需要更多信息，可以自行添加)
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月21日 下午2:08:04
 * @since 
 */
public class EnterpriseBean implements Serializable{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2640101753501197219L;
    /**会员号**/
    private String enterpriseMemberId;
    /** 企业名称 **/
    private String enterpriseName;
    /**合作机构id**/
    private Long coopInstiId;
    /**所属行业**/
    private String mcc;
    /**所属行业子类别**/
    private String mccList;
    /**企业所属机构**/
    private Long enterpriseInsti;
    /**企业所属省**/
    private Long province;
    /**企业所属市**/
    private Long city;
    /**企业所属县**/
    private Long street;
    /**企业行政地区代码（由商户所属县得到）**/
    private String zoneCode;
    /**企业邮编**/
    private String postCode;
    /**企业地址**/
    private String address;
    /**企业邮箱**/
    private String email;
    /**企业网址**/
    private String website;
    /**企业证件类型**/
    private CardType cardType;
    /**税务登记证号**/
    private String taxNo;
    /**营业执照号**/
    private String licenceNo;
    /**组织机构代码证**/
    private String orgCode;
    /**法人**/
    private String corporation;
    /**法人证件号码**/
    private String corpNo;
    /**法人身份证正面路径**/
    private String corpFile;
    /**法人身份证背面路径**/
    private String corpFileOpp;
    /**税务登记证文件路径**/
    private String taxFile;
    /**营业执照文件路径**/
    private String licenceFile;
    /**组织机构文件路径**/
    private String orgCodeFile;
    /**联系人**/
    private String contact;
    /**联系人电话**/
    private String contPhone;
    /**联系人职位**/
    private String contTitle;
    /**联系人邮箱**/
    private String contEmail;
    /**联系人地址**/
    private String contAddress;
    /**联系人邮编**/
    private String contPost;
    /**客户来源**/
    private String custFrom;
    /**客户经理**/
    private String custMgr;
    /**客户经理部门**/
    private String custMgrDept;
    /**是否授权人办理1-是0-否**/
    private Long isDelegation;
    /**委托人**/
    private String signatory;
    /**委托人身份证**/
    private String signCertNo;
    /**委托人证件照正面路径**/
    private String signCertFile;
    /**委托人证件照背面路径**/
    private String signCertFileOpp;
    /**状态**/
    private String enterpriseStatus;
    /**
     * 企业手机号
     */
    private String cellPhoneNo;
	/**
	 * 主营业务
	 */
	private IndustryType primaryBusiness;
    /**
	 * @return the cellPhoneNo
	 */
	public String getCellPhoneNo() {
		return cellPhoneNo;
	}

	/**
	 * @param cellPhoneNo the cellPhoneNo to set
	 */
	public void setCellPhoneNo(String cellPhoneNo) {
		this.cellPhoneNo = cellPhoneNo;
	}

	public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

	/**
	 * @return the enterpriseMemberId
	 */
	public String getEnterpriseMemberId() {
		return enterpriseMemberId;
	}

	/**
	 * @param enterpriseMemberId the enterpriseMemberId to set
	 */
	public void setEnterpriseMemberId(String enterpriseMemberId) {
		this.enterpriseMemberId = enterpriseMemberId;
	}

	/**
	 * @return the coopInstiId
	 */
	public Long getCoopInstiId() {
		return coopInstiId;
	}

	/**
	 * @param coopInstiId the coopInstiId to set
	 */
	public void setCoopInstiId(Long coopInstiId) {
		this.coopInstiId = coopInstiId;
	}

	/**
	 * @return the mcc
	 */
	public String getMcc() {
		return mcc;
	}

	/**
	 * @param mcc the mcc to set
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the mccList
	 */
	public String getMccList() {
		return mccList;
	}

	/**
	 * @param mccList the mccList to set
	 */
	public void setMccList(String mccList) {
		this.mccList = mccList;
	}

	/**
	 * @return the enterpriseInsti
	 */
	public Long getEnterpriseInsti() {
		return enterpriseInsti;
	}

	/**
	 * @param enterpriseInsti the enterpriseInsti to set
	 */
	public void setEnterpriseInsti(Long enterpriseInsti) {
		this.enterpriseInsti = enterpriseInsti;
	}

	/**
	 * @return the province
	 */
	public Long getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(Long province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public Long getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(Long city) {
		this.city = city;
	}

	/**
	 * @return the street
	 */
	public Long getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(Long street) {
		this.street = street;
	}

	/**
	 * @return the zoneCode
	 */
	public String getZoneCode() {
		return zoneCode;
	}

	/**
	 * @param zoneCode the zoneCode to set
	 */
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the cardType
	 */
	public CardType getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the taxNo
	 */
	public String getTaxNo() {
		return taxNo;
	}

	/**
	 * @param taxNo the taxNo to set
	 */
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	/**
	 * @return the licenceNo
	 */
	public String getLicenceNo() {
		return licenceNo;
	}

	/**
	 * @param licenceNo the licenceNo to set
	 */
	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * @return the corporation
	 */
	public String getCorporation() {
		return corporation;
	}

	/**
	 * @param corporation the corporation to set
	 */
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}

	/**
	 * @return the corpNo
	 */
	public String getCorpNo() {
		return corpNo;
	}

	/**
	 * @param corpNo the corpNo to set
	 */
	public void setCorpNo(String corpNo) {
		this.corpNo = corpNo;
	}

	/**
	 * @return the corpFile
	 */
	public String getCorpFile() {
		return corpFile;
	}

	/**
	 * @param corpFile the corpFile to set
	 */
	public void setCorpFile(String corpFile) {
		this.corpFile = corpFile;
	}

	/**
	 * @return the corpFileOpp
	 */
	public String getCorpFileOpp() {
		return corpFileOpp;
	}

	/**
	 * @param corpFileOpp the corpFileOpp to set
	 */
	public void setCorpFileOpp(String corpFileOpp) {
		this.corpFileOpp = corpFileOpp;
	}

	/**
	 * @return the taxFile
	 */
	public String getTaxFile() {
		return taxFile;
	}

	/**
	 * @param taxFile the taxFile to set
	 */
	public void setTaxFile(String taxFile) {
		this.taxFile = taxFile;
	}

	/**
	 * @return the licenceFile
	 */
	public String getLicenceFile() {
		return licenceFile;
	}

	/**
	 * @param licenceFile the licenceFile to set
	 */
	public void setLicenceFile(String licenceFile) {
		this.licenceFile = licenceFile;
	}

	/**
	 * @return the orgCodeFile
	 */
	public String getOrgCodeFile() {
		return orgCodeFile;
	}

	/**
	 * @param orgCodeFile the orgCodeFile to set
	 */
	public void setOrgCodeFile(String orgCodeFile) {
		this.orgCodeFile = orgCodeFile;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the contPhone
	 */
	public String getContPhone() {
		return contPhone;
	}

	/**
	 * @param contPhone the contPhone to set
	 */
	public void setContPhone(String contPhone) {
		this.contPhone = contPhone;
	}

	/**
	 * @return the contTitle
	 */
	public String getContTitle() {
		return contTitle;
	}

	/**
	 * @param contTitle the contTitle to set
	 */
	public void setContTitle(String contTitle) {
		this.contTitle = contTitle;
	}

	/**
	 * @return the contEmail
	 */
	public String getContEmail() {
		return contEmail;
	}

	/**
	 * @param contEmail the contEmail to set
	 */
	public void setContEmail(String contEmail) {
		this.contEmail = contEmail;
	}

	/**
	 * @return the contAddress
	 */
	public String getContAddress() {
		return contAddress;
	}

	/**
	 * @param contAddress the contAddress to set
	 */
	public void setContAddress(String contAddress) {
		this.contAddress = contAddress;
	}

	/**
	 * @return the contPost
	 */
	public String getContPost() {
		return contPost;
	}

	/**
	 * @param contPost the contPost to set
	 */
	public void setContPost(String contPost) {
		this.contPost = contPost;
	}

	/**
	 * @return the custFrom
	 */
	public String getCustFrom() {
		return custFrom;
	}

	/**
	 * @param custFrom the custFrom to set
	 */
	public void setCustFrom(String custFrom) {
		this.custFrom = custFrom;
	}

	/**
	 * @return the custMgr
	 */
	public String getCustMgr() {
		return custMgr;
	}

	/**
	 * @param custMgr the custMgr to set
	 */
	public void setCustMgr(String custMgr) {
		this.custMgr = custMgr;
	}

	/**
	 * @return the custMgrDept
	 */
	public String getCustMgrDept() {
		return custMgrDept;
	}

	/**
	 * @param custMgrDept the custMgrDept to set
	 */
	public void setCustMgrDept(String custMgrDept) {
		this.custMgrDept = custMgrDept;
	}

	/**
	 * @return the isDelegation
	 */
	public Long getIsDelegation() {
		return isDelegation;
	}

	/**
	 * @param isDelegation the isDelegation to set
	 */
	public void setIsDelegation(Long isDelegation) {
		this.isDelegation = isDelegation;
	}

	/**
	 * @return the signatory
	 */
	public String getSignatory() {
		return signatory;
	}

	/**
	 * @param signatory the signatory to set
	 */
	public void setSignatory(String signatory) {
		this.signatory = signatory;
	}

	/**
	 * @return the signCertNo
	 */
	public String getSignCertNo() {
		return signCertNo;
	}

	/**
	 * @param signCertNo the signCertNo to set
	 */
	public void setSignCertNo(String signCertNo) {
		this.signCertNo = signCertNo;
	}

	/**
	 * @return the signCertFile
	 */
	public String getSignCertFile() {
		return signCertFile;
	}

	/**
	 * @param signCertFile the signCertFile to set
	 */
	public void setSignCertFile(String signCertFile) {
		this.signCertFile = signCertFile;
	}

	/**
	 * @return the signCertFileOpp
	 */
	public String getSignCertFileOpp() {
		return signCertFileOpp;
	}

	/**
	 * @param signCertFileOpp the signCertFileOpp to set
	 */
	public void setSignCertFileOpp(String signCertFileOpp) {
		this.signCertFileOpp = signCertFileOpp;
	}

	/**
	 * @return the enterpriseStatus
	 */
	public String getEnterpriseStatus() {
		return enterpriseStatus;
	}

	/**
	 * @param enterpriseStatus the enterpriseStatus to set
	 */
	public void setEnterpriseStatus(String enterpriseStatus) {
		this.enterpriseStatus = enterpriseStatus;
	}

	/**
	 * @return the primaryBusiness
	 */
	public IndustryType getPrimaryBusiness() {
		return primaryBusiness;
	}

	/**
	 * @param primaryBusiness the primaryBusiness to set
	 */
	public void setPrimaryBusiness(IndustryType primaryBusiness) {
		this.primaryBusiness = primaryBusiness;
	}
    
    
}
