package com.gatewayExample.gatewaytraining.Wrappers;

import lombok.Data;

/**
 * @Aurthor Nelson Mose
 * @Date 2020:08:11
 */

@Data
public class BalanceInquiryRequest {
    private String apikey;
    private String custumerNumber;
}
