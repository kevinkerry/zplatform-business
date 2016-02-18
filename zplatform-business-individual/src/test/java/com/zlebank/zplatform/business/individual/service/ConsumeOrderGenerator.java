package com.zlebank.zplatform.business.individual.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;
import com.zlebank.zplatform.business.individual.utils.Constants;

public class ConsumeOrderGenerator extends OrderGenerator {
    private final String txnType = "17";//
    private final String txnSubType = "00";
    private final String bizType = "000204";
    @Override
    public Order generate(boolean isAnonymous) {
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
        order.setTxnAmt(randomNumber(3));
        order.setCurrencyCode("156");
        order.setOrderDesc("充值测试" + randomNumber(9));
        order.setSignature("12313123123");
        order.setVersion(version);
        order.setEncoding(encoding);
        order.setSignMethod(signMethod);
        order.setOrderType(OrderType.CONSUME);
        return order;
    }

}
