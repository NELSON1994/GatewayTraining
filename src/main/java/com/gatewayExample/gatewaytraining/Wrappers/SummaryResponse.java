package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SummaryResponse {
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("Account")
    private String account;
    @JsonProperty("PreviousBalance")
    private String PreviousBalance;
    @JsonProperty("CrCount")
    private String CrCount;
    @JsonProperty("TotalCredits")
    private String TotalCredits;
    @JsonProperty("DrCount")
    private String DrCount;
    @JsonProperty("TotalDebits")
    private String TotalDebits;
    @JsonProperty("PresentBalance")
    private String PresentBalance;
    @JsonProperty("st.dte")
    private String stdte;

}
