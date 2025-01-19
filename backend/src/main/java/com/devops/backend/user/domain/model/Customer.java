package com.devops.backend.user.domain.model;

import com.devops.backend.user.domain.valueobjects.Address;
import com.devops.backend.user.domain.valueobjects.PersonalInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)

public class Customer extends User {
    @ElementCollection
    @Builder.Default
    @CollectionTable(name = "customer_addresses", joinColumns = @JoinColumn(name = "customer_id"))
    private List<Address> addresses = new ArrayList<>();

    public void updateProfile(PersonalInfo newPersonalInfo) {
        this.personalInfo = newPersonalInfo;
        this.updatedAt = LocalDateTime.now();
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }
}
