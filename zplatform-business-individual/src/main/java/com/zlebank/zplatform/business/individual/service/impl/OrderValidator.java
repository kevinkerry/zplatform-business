package com.zlebank.zplatform.business.individual.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;
import com.zlebank.zplatform.business.individual.utils.Constants;
import com.zlebank.zplatform.trade.analyzer.GateWayTradeAnalyzer;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.IMemberService;
import com.zlebank.zplatform.trade.service.IProdCaseService;

@Service
public class OrderValidator implements IOrderValidator{
    @Autowired
    private IGateWayService gateWayService;
    @Autowired
    private IProdCaseService prodCaseService;
    @Autowired
    private IMemberService memberService;
    
    public Map<String, String> validateOrder(Order order) {
        // 验证订单数据有效性，用hibernate validator处理
        Map<String, String> resultMap = new HashMap<String,String>();
        ResultBean resultBean = GateWayTradeAnalyzer.validateOrder(order);
        if (!resultBean.isResultBool()) {
            // 订单信息长度和非空验证未通过
            resultMap.put(RET_MESSAGE, resultBean.getErrMsg());
            resultMap.put(RET_CODE, resultBean.getErrCode());
            return resultMap;
        }
        // 检验风控信息是否符合要求
        ResultBean riskResultBean = GateWayTradeAnalyzer.generateRiskBean(order
                .getRiskRateInfo());
        if (!riskResultBean.isResultBool()) {// 验证风控信息的正确性
            resultMap.put(RET_MESSAGE, riskResultBean.getErrMsg());
            resultMap.put(RET_CODE, riskResultBean.getErrCode());
            return resultMap;
        }
        //钱包数据与web收银台数据不一致，验签不能通过，验签暂时关闭
       /* ResultBean signResultBean = gateWayService.verifyOrder(order);
        if (!signResultBean.isResultBool()) {
            // 订单信息验签未通过
            resultMap.put(RET_MESSAGE, signResultBean.getErrMsg());
            resultMap.put(RET_CODE, signResultBean.getErrCode());
            return resultMap;
        }*/

        // 验证订单号是否重复
        try {
            /*String memberId = ((RiskRateInfoBean) riskResultBean.getResultObj())
                    .getMerUserId();*/
            gateWayService.verifyRepeatWebOrder(order.getOrderId(),
                    order.getTxnTime(), order.getTxnAmt(), order.getMerId(),
                    order.getMemberId());
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resultMap.put(RET_MESSAGE, e.getMessage());
            resultMap.put(RET_CODE, e.getCode());
            return resultMap;
        }

        // 验证商户产品版本中是否有对应的业务
        ResultBean busiResultBean = prodCaseService.verifyBusiness(order);
        if (!busiResultBean.isResultBool()) {
            resultMap.put(RET_MESSAGE, busiResultBean.getErrMsg());
            resultMap.put(RET_CODE, busiResultBean.getErrCode());
            return resultMap;
        }

        // 检验机构和商户有效性
        /*if (StringUtil.isNotEmpty(order.getCoopInstiId())) {
            MemberBaseModel subMember = memberService.getMemberByMemberId(order.getCoopInstiId());
            if (subMember == null) {
                resultMap.put(RET_MESSAGE,"商户信息不存在");
                resultMap.put(RET_CODE, "RC10");
                return resultMap;
            }不需要校验二级商户号
            if (order.getSubMerId().startsWith("2")) {// 对于商户会员需要进行检查
                ResultBean memberResultBean = memberService.verifySubMerch(
                        order.getMerId(), order.getSubMerId());
                if (!memberResultBean.isResultBool()) {
                    resultMap.put(RET_MESSAGE, memberResultBean.getErrMsg());
                    resultMap.put(RET_CODE, memberResultBean.getErrCode());
                    return resultMap;
                }
            }

        }*/

        // 业务验证
        // 充值业务，如果memberId为空，或者为999999999999999时为非法订单
        ResultBean memberBusiResultBean = null;
        if(order.getOrderType()!=OrderType.CONSUME){
            if(StringUtils.isEmpty(order.getMemberId())||order.getMemberId().equals(Constants.ANONYMOUS_MERCH_ID)){
                memberBusiResultBean = new ResultBean("GW19", "非消费类订单不支持匿名支付");
                resultMap.put(RET_MESSAGE, memberBusiResultBean.getErrMsg());
                resultMap.put(RET_CODE, memberBusiResultBean.getErrCode());
                return resultMap;
            }
        }
         
        resultMap.put(RET_MESSAGE, RET_MESSAGE_SUCCESS);
        resultMap.put(RET_CODE, RET_CODE_SUCCESS);
        return resultMap;
    }
}
