package com.zlebank.zplatform.business.individual.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.business.individual.utils.Constants;
import com.zlebank.zplatform.trade.exception.TradeException;

public class MemberAccountServiceImplTest extends ApplicationContextAbled {

    private final String version = "v1.0";
    private final String encoding = "1";// 1-utf-8
    private final String txnType = "17";//
    private final String txnSubType = "00";
    private final String bizType = "000204";
    private final String channelType = "08";
    private final String accessType = "0";
    private final String coopInstiCode = "300000000000027";
    private final String individualMemberId = "100000000000564";
    private final String merchId = "200000000000568";
    private final String signMethod = "01";// 01-RSA
    @Test
    public void testRecharge() {
        MemberAccountService memberAccountService = (MemberAccountService) getContext()
                .getBean("busiMemberAccountServiceImpl");

        String tn = null;
        try {
            // test non anonymous
            tn = memberAccountService.recharge(generateRechargeOrder(false));
            System.out.println(tn);
            Assert.assertNotNull(tn);

        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (AbstractIndividualBusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (ValidateOrderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        try {
            // test anonymous
            tn = memberAccountService.recharge(generateRechargeOrder(true));
        } catch (ValidateOrderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), true);
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
    
    private Order generateRechargeOrder(boolean isAnonymous) {
        Order order = new Order();
        order.setAccessType(accessType);
        order.setCoopInstiId(coopInstiCode);
        if (!isAnonymous) {
            order.setMemberId(individualMemberId);
        } else {
            order.setMemberId(Constants.ANONYMOUS_MERCH_ID);
        }
        order.setTxnType(txnType);
        order.setTxnSubType(txnSubType);
        order.setBizType(bizType);
        order.setChannelType(channelType);
        order.setMerId(merchId);
        order.setOrderId(randomAlphabetic() + randomNumber(17));
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHmmss");
        Calendar calendar = Calendar.getInstance();
        order.setTxnTime(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MINUTE, 45);
        order.setPayTimeout(new SimpleDateFormat("YYYYMMDDHHmmss")
                .format(calendar.getTime()));
        order.setTxnAmt(randomNumber(4));
        order.setCurrencyCode("156");
        order.setOrderDesc("充值测试" + randomNumber(9));
        order.setSignature("12313123123");
        order.setVersion(version);
        order.setEncoding(encoding);
        order.setSignMethod(signMethod);
        order.setOrderType(OrderType.RECHARGE);
        /*
         * 钱包接口中没有的参数
         */
        // 接收超时时间
        long orderTimeOutMill = 30 * 60 * 1000;
        order.setOrderTimeout(String.valueOf(orderTimeOutMill));
        // 风险信息域，非空,必须按照此格式
        order.setRiskRateInfo("merUserId=" + individualMemberId
                + "&commodityQty=0&commodityUnitPrice=0&");
        // 前台通知地址，非空
        order.setFrontUrl("123");
        // 后台通知地址，非空
        order.setBackUrl("123");
        // 证书ID，非空
        order.setCertId("123");
        return order;
    }

    private String randomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String randomAlphabetic() {
        Random random = new Random();
        int length = random.nextInt(15);
        String name = RandomStringUtils.randomAlphabetic(length);

        return name;
    }
    @Test
    @Ignore
    public void test1() {
        String s = "merUserId=12321312&";
        String s1 = StringUtils.substringBetween(s, "merUserId=", "&");
        Assert.assertEquals("12321312", s1);
    }
}
