package com.gatewayExample.gatewaytraining.config;

import com.gatewayExample.gatewaytraining.Repositories.AllTransactionRepository;
import com.gatewayExample.gatewaytraining.Services.AllTransactionServiceTemplate;
import com.gatewayExample.gatewaytraining.Services.AllTransactionsService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextBridgeService implements SpringContextBridgeTemplate, ApplicationContextAware {
    private static ApplicationContext applicationContext;


    @Autowired
    private AllTransactionServiceTemplate allTransactionServiceTemplate;

    @Autowired
    AllTransactionRepository transactionRepository;

    @Override
    public AllTransactionServiceTemplate getAllTransactionServiceTemplate(){
    return allTransactionServiceTemplate;
}
    @Override
    public AllTransactionRepository getAllTransactionsRepo() {
        return transactionRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextBridgeService.applicationContext = context;
    }

    public static SpringContextBridgeTemplate services(){
        return applicationContext.getBean(SpringContextBridgeTemplate.class);
    }
}
