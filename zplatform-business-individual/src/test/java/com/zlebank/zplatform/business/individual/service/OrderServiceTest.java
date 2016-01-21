package com.zlebank.zplatform.business.individual.service;

import org.junit.Assert;
import org.junit.Test;

import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.trade.exception.TradeException;

public class OrderServiceTest extends ApplicationContextAbled {
    @Test
    public void testCreateOrder() {
        OrderService orderService = (OrderService) getContext().getBean(
                "orderServiceImpl");
        String tn = null;
        OrderGenerator orderGenerator = null;
        try {
            // test recharge order
            orderGenerator = new RechargeOrderGenerator();
            tn = orderService.createOrder(orderGenerator.generate(false));
            System.out.println(tn);
            Assert.assertNotNull(tn);
            // test consume non anonymous order
            orderGenerator = new ConsumeOrderGenerator();
            tn = orderService.createOrder(orderGenerator.generate(false));
            System.out.println(tn);
            Assert.assertNotNull(tn);
            // test consume non anonymous order
            orderGenerator = new ConsumeOrderGenerator();
            tn = orderService.createOrder(orderGenerator.generate(true));
            System.out.println(tn);
            Assert.assertNotNull(tn);
        } catch (TradeException e) {
            Assert.fail(e.getMessage());
        } catch (AbstractIndividualBusinessException e) {
            Assert.fail(e.getMessage());
        } catch (ValidateOrderException e) {
            Assert.fail(e.getMessage());
        }
    }
}
