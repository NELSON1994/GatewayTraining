package com.gatewayExample.gatewaytraining.Entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="posirisdata")
@Data
@NoArgsConstructor
@Getter
@Setter
public class PosIrisData {
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq1")
    @Id
    private long id;
    private String serialNumber;
    private String sdkUsed;
    private String batterylevel;
    private String network;
    private String osVersion;
    private String temperature;
    private String appVersion;
    private String isCharging;
    private String terminalType;

    public PosIrisData(String serialNumber, String sdkUsed, String batterylevel, String network, String osVersion, String temperature, String appVersion, String isCharging, String terminalType) {
        this.serialNumber = serialNumber;
        this.sdkUsed = sdkUsed;
        this.batterylevel = batterylevel;
        this.network = network;
        this.osVersion = osVersion;
        this.temperature = temperature;
        this.appVersion = appVersion;
        this.isCharging = isCharging;
        this.terminalType = terminalType;
    }
}
