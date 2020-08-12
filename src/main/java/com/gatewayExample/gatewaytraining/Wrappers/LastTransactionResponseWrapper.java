package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Aurthor: Nelson Mose
 * @Date: 2020:08:11
 */

@Data
public class LastTransactionResponseWrapper {
    @JsonProperty("ACTION")
    private String action;
    @JsonProperty("DATE")
    private String date;
    @JsonProperty("ACCT_FRO")
    private String accFrom;
    @JsonProperty("ACCT_TO")
    private String accTo;
    @JsonProperty("DETAIL")
    private String details;
    @JsonProperty("AMOUNT")
    private String amount;
    @JsonProperty("TRANS_ID")
    private String transactionId;
}
