package com.gatewayExample.gatewaytraining.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Aurthor Nelson Mose
 * @Date 2020:08:11
 */

@Data
public class LastTransactionResponse {
    @JsonProperty("lastTransactiondetail")
    LastTransactionResponseWrapper[] lastTransactiondetail;
}
