package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MiniStatementResponseWrapper {
    @JsonProperty("summary")
    private SummaryResponse summary;
    @JsonProperty("detail")
    private DetailResponse[] detail;
}
