package com.devops.backend.user.domain.valueobjects;

import lombok.Value;

@Value
public class PersonalInfo {
    String firstName;
    String lastName;
    String phoneNumber;
}
