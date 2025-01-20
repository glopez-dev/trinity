package com.trinity.user.domain.model;

import com.trinity.user.domain.enums.EmployeeRole;
import com.trinity.user.domain.events.EmployeePromoted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)

public class Employee extends User {
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    @Column(name = "termination_date")
    private LocalDateTime terminationDate;

    public void promote(EmployeeRole newRole) {
        EmployeeRole oldRole = this.role;
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
        addDomainEvent(new EmployeePromoted(getId(), oldRole, newRole, LocalDateTime.now()));
    }
}
