package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.MemInAndExDetail;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.MemberAccountBean;
/**
 * 
 * individual member account service
 *
 * @author yangying
 * @version
 * @date 2016年1月19日 下午2:58:11
 * @since
 */
public interface MemberAccountService {

    // Object queryMemberAccount();
    /**
     * member account recharge.Just create a recharge order.Invoker
     * {@code  OrderService.pay()} later if recharge success.
     * 
     * @param order
     * @return tn - a order number represent platform accept recharge.
     * @see OrderService
     */
    String recharge(Order order);
    /**
     * member account withdraw.Just create a withdraw order,it will be processed
     * by background program. Member can check bank account later if background
     * program handle success.
     * 
     * @param order
     * @return tn - a order number represent platform accept withdraw.
     */
    String withdraw(Order order);
     /**
      * query member basic funds account
      * @param memberId
      * @return
      */
    MemberAccountBean queryMemberFuns(String memberId);
    /**
     * query member income and express detail
     * @param memberId
     * @return
     */
    PagedResult<MemInAndExDetail> queryAccInAndExDetail(String memberId);
}
