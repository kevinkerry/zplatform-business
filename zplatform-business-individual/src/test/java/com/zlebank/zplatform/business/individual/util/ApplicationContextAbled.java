package com.zlebank.zplatform.business.individual.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class ApplicationContextAbled {
    
    private ApplicationContext context;
    
    protected ApplicationContext getContext(){
        context =  new ClassPathXmlApplicationContext(new String[]{"spring.xml","spring-hibernate.xml"});
        return context;
    }
}
