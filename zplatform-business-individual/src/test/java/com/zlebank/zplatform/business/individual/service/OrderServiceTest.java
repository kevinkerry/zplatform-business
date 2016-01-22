
package com.zlebank.zplatform.business.individual.service;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.utils.DateUtil;
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
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
    public void test_queryOrder(){
		OrderService orderService = (OrderService) getContext().getBean("orderService");
		Order order = orderService.queryOrder("100000000000564", "dGVWwxSCU68267726142181273");
		System.out.println(JSON.toJSONString(order));
	}
	@Test
	public void test_queryOrderList(){
		OrderService orderService = (OrderService) getContext().getBean("orderService");
		PagedResult<Order> orderList = null;
		try {
			orderList = orderService.queryOrderList("100000000000564", DateUtil.convertToDate("20160120112643", "yyyyMMddHHmmss"), DateUtil.convertToDate("20160121122643", "yyyyMMddHHmmss"), 1, 10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(JSON.toJSONString(orderList));
	}
}