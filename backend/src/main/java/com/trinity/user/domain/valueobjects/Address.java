package com.trinity.user.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {
    String street;
    String zipCode;
    String city;
    String country;
    boolean isDefault;
}
