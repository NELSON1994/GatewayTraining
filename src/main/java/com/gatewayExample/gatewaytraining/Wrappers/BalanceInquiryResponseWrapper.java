package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Aurthor Nelson Mose
 * @Date 2020:08:11
 */

@Data
public class BalanceInquiryResponseWrapper {
    @JsonProperty("STATUS")
    private String STATUS;
    @JsonProperty("BOOKBALANCE")
    private String BOOKBALANCE;
    @JsonProperty("AVAILABLEBALANCE")
    private String AVAILABLEBALANCE;
    @JsonProperty("PERMITTEDBALANCE")
    private String PERMITTEDBALANCE;
}
