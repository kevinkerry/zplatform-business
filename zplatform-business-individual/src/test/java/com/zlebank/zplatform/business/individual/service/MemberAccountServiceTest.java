package com.zlebank.zplatform.business.individual.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.business.individual.bean.MemInAndExDetail;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.MemberAccountBean;

public class MemberAccountServiceTest extends ApplicationContextAbled {

    private MemberAccountService memberAccountService = (MemberAccountService) getContext()
            .getBean("busiMemberAccountServiceImpl");
    private final String individualMemberId = "100000000000564";
    private final String payPwd = "e10adc3949ba59abbe56e057f20f883e";
    @Test
    @Ignore
    public void testRecharge() {

        String tn = null;
        OrderGenerator orderGenerator = new RechargeOrderGenerator();
        try {
            // test non anonymous
            tn = memberAccountService.recharge(orderGenerator.generate(false));
            System.out.println(tn);
            Assert.assertNotNull(tn);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            // test anonymous
            tn = memberAccountService.recharge(orderGenerator.generate(true));
            Assert.fail();
        } catch (ValidateOrderException e) {
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void testWithdraw() {
        OrderGenerator orderGenerator = new RechargeOrderGenerator();
        Order order = orderGenerator.generate(false);
        String json = JSON.toJSONString(order);
        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.remove("txnAmt");
        jsonObject.put("amount", order.getTxnAmt());
        json = JSON.toJSONString(jsonObject);
        System.out.println(json);
        try {
            memberAccountService.withdraw(json, payPwd);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    @Test
    @Ignore
    public void testQueryMemberFuns() {
        try {
            MemberAccountBean memberAccountBean = memberAccountService
                    .queryMemberFuns(individualMemberId);
            Assert.assertEquals("6010101100000000000564",
                    memberAccountBean.getBusiCode());
            Assert.assertEquals("00", memberAccountBean.getStatus());
            Assert.assertEquals(Usage.BASICPAY, memberAccountBean.getUsage());
            Assert.assertEquals(BigDecimal.ZERO, memberAccountBean.getBalance());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void testqueryAccInAndExDetail() throws Exception {
        try {
            PagedResult<MemInAndExDetail> result = (PagedResult<MemInAndExDetail>) memberAccountService
                    .queryAccInAndExDetail(individualMemberId, 1, 10);
            Assert.assertEquals(0, result.getTotal());

            result = (PagedResult<MemInAndExDetail>) memberAccountService.queryAccInAndExDetail(
                    "100000000000435", 2, 14);
            Assert.assertEquals(1054, result.getTotal());
            List<MemInAndExDetail> memInAndExDetails = result.getPagedResult();
            for (MemInAndExDetail memInAndExDetail : memInAndExDetails) {
                System.out.println(memInAndExDetail.getBudgetType() + "|"
                        + memInAndExDetail.getTxnTime() + "|"
                        + memInAndExDetail.getTxnAmt());
            }
        }catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void test1() {
        String s = "merUserId=12321312&";
        String s1 = StringUtils.substringBetween(s, "merUserId=", "&");
        Assert.assertEquals("12321312", s1);
    }
}
