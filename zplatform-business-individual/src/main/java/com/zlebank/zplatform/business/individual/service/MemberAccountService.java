package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.MemInAndExDetail;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.MemberAccountBean;
/**
 * 
 * Individual member account service
 *
 * @author yangying
 * @version
 * @date 2016年1月19日 下午2:58:11
 * @since
 */
public interface MemberAccountService {

    // Object queryMemberAccount();
    /**
     * Member account recharge.Just create a recharge order.Invoke
     * {@code  OrderService.pay()} later if recharge success.
     * 
     * @param order
     * @return tn - zplatform accept order no
     * @throws ValidateOrderException
     *             if validate order failed,invoke getCode() to see retCode and
     *             getMessage to see more information.
     * @throws TradeException
     *             if create order error
     * @throws AbstractIndividualBusinessException
     * @see OrderService
     */
    String recharge(Order order) throws CommonException;
    /**
     * Member account withdraw.Just create a withdraw order,it will be processed
     * by background program. Member can check bank account later if background
     * program handle success.
     * 
     * @param order
     * @param payPwd
     * @param smsCode
     * @return tn - zplatform accept order no
     * @throws ValidateOrderException
     *             if validate order failed,invoke getCode() to see retCode and
     *             getMessage to see more information.
     * @throws TradeException
     *             if create order error
     * @throws AbstractIndividualBusinessException
     */
    String withdraw(String json, String payPwd)
            throws CommonException;
    /**
     * query member basic funds account
     * 
     * @param memberId
     * @return
     * @throws AbstractTradeDescribeException
     *             if member funs account not exist
     */
    MemberAccountBean queryMemberFuns(String memberId)
            throws CommonException;
    /**
     * query member income and express detail
     * 
     * @param memberId
     * @param page
     * @param pageSize
     * @return
     * @throws AbstractTradeDescribeException
     * @throws IllegalAccessException
     */
    PagedResult<MemInAndExDetail> queryAccInAndExDetail(String memberId,
            int page,
            int pageSize) throws CommonException;
}
