package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetAccountDetailsResponseWrapper {
    @JsonProperty("STATUS")
    private String status;
    @JsonProperty("CUSTOMERNAME")
    private String customerName;
    @JsonProperty("EMAIL")
    private String email;
    @JsonProperty("MOBNUM")
    private String mobileNumber;
    @JsonProperty("TELEPHONE")
    private String telephone;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("BIRTHDATE")
    private String birthdate;
    @JsonProperty("MONTHNAME")
    private String monthName;
}
