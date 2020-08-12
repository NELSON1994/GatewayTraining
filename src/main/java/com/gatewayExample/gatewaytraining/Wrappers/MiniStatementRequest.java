package com.gatewayExample.gatewaytraining.Wrappers;

import lombok.Data;

@Data
public class MiniStatementRequest {
    private String apikey;
    private String cusNumber;
    private String startDate;
    private String endDate;
}
