package com.gatewayExample.gatewaytraining.Wrappers;

import lombok.Data;

/**
 * @Aurthor Nelson Mose
 * @Date 2020:08:08
 */

@Data
public class ChangePasswordWrapper {
    private String serialnumber;
    private String oldpassword;
    private String newpassword;


}
