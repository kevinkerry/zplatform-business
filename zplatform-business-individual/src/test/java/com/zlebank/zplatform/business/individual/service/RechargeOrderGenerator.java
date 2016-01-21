package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;

public class RechargeOrderGenerator extends OrderGenerator {
    
    private ConsumeOrderGenerator consumeOrderGenerator;
    
    public RechargeOrderGenerator(){
        consumeOrderGenerator = new ConsumeOrderGenerator();
    }
    
    @Override
    public Order generate(boolean isAnonymous) {
         Order order = consumeOrderGenerator.generate(isAnonymous);
         order.setOrderType(OrderType.RECHARGE);
         order.setMerId("");
        return order;
    }

}
