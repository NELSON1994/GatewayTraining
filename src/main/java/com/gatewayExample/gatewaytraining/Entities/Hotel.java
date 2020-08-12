package com.gatewayExample.gatewaytraining.Entities;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Hotel {
    private String hotelName;
    private String hotelDescriptions;
    private String Services;
}
