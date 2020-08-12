package com.gatewayExample.gatewaytraining.Wrappers;

import lombok.Data;

@Data
public class TransferRequest {
    private String apikey;
    private String accountFrom;
    private String accountTo;
    private String amount;
    private String remarks;
    private String type;
}
