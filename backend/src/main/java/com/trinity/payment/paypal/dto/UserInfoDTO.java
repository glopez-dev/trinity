package com.trinity.payment.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private String email;
    private String firstName;
    private String lastName;
}
