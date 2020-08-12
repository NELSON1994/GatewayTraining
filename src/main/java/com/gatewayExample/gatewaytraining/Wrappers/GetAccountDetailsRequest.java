package com.gatewayExample.gatewaytraining.Wrappers;

import lombok.Data;

@Data
public class GetAccountDetailsRequest {
    private String apikey;
    private String custumerNumber;
}
