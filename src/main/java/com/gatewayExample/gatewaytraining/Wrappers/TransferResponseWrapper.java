package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransferResponseWrapper {
    @JsonProperty("STATUS")
    private String status;
    @JsonProperty("BANK_REFERENCE_ID")
    private String refNumber;
    @JsonProperty("SOURCE_ACCT_NO")
    private String source;
    @JsonProperty("DESTINATION_ACCT_NO")
    private String destination;
    @JsonProperty("AMOUNT")
    private String amount;
}
