package com.gatewayExample.gatewaytraining.config;

import com.gatewayExample.gatewaytraining.Repositories.AllTransactionRepository;
import com.gatewayExample.gatewaytraining.Services.AllTransactionServiceTemplate;

public interface SpringContextBridgeTemplate {
    AllTransactionServiceTemplate getAllTransactionServiceTemplate();
    AllTransactionRepository getAllTransactionsRepo();
}
