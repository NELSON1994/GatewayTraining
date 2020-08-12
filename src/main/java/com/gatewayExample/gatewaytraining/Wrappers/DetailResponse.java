package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DetailResponse {
    @JsonProperty("Date")
    private String Date;
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("Debit")
    private String Debit;
    @JsonProperty("Credit")
    private String Credit;
    @JsonProperty("Balance")
    private String Balance;
}
