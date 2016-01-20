package com.zlebank.zplatform.business.individual.service;

import java.util.List;

import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.MemberBean;
/**
 * Member bank card service
 * 
 * @author yangying
 * @version
 * @date 2016年1月19日 下午3:15:20
 * @since
 */
public interface MemberCardService {
    /**
     * Query list of bank card which the member can use
     * 
     * @param memberId
     *            member id
     * @return
     */
    List<BankCardInfo> queryBankCard(String memberId);
    /**
     * Query bank card info by bank card no.
     * 
     * @param bankCardNo
     * @return {@link CardBin}
     */
    CardBin queryCardBin(String bankCardNo);
    /**
     * Bind a bank card to member
     * 
     * @param individualMember
     * @param bankCardInfo
     * @param smsCode
     * @return
     */
    String bindBankCard(MemberBean individualMember,
            BankCardInfo bankCardInfo,
            String smsCode);
    /**
     * Unbind a bank card
     * 
     * @param memberId
     * @param bindcardid
     * @param payPwd
     * @return the bindid
     */
    String UnbindBankCard(String memberId, String bindcardid, String payPwd);
    /**
     * Query list of bank card type
     * 
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult<SupportedBankCardType> queryBank(int page, int pageSize);
}
