package com.gatewayExample.gatewaytraining.Entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "all_transactions")
public class AllTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String field0;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field7;
    private String field11;
    private String field12;
    private String field37;
    private String field39;
    private String field41;
    private String field42;
    private String field47;
    private String field48;
    private String field70;
    private String field72;
    private String field102;
}
