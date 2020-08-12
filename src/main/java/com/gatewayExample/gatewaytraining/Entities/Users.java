package com.gatewayExample.gatewaytraining.Entities;

import lombok.Data;

import javax.persistence.*;

@Data
//Entity
@SequenceGenerator(name="seq1" , initialValue = 1 , allocationSize = 50)
public class Users {
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq1")
    @Id
    private long id;
    private String fname;
    private String lname;
    private String email;
    private int contact;
    private String username;
    private String password;
}
