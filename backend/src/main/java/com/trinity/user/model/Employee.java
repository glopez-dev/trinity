package com.trinity.user.model;

import java.time.Instant;

import com.trinity.user.constant.EmployeeRole;
import com.trinity.user.constant.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* LOMBOK */
@Data
@EqualsAndHashCode(callSuper = false) // Prevents stack overflow error when using Lombok annotations with inheritance.
@SuperBuilder// Allows this class to use the builder pattern with it's parent class attributes. 
@NoArgsConstructor // Hibernate needs a no-args constructor. 
@AllArgsConstructor // Builder pattern requires all args constructor.
/* JPA */
@Entity
@Table(name = "employee")
public class Employee extends AbstractUser {

    @Builder.Default
    private Instant hireDate = Instant.now();

    private Instant terminationDate;

    @Builder.Default
    @Column(nullable = false)
    private EmployeeRole role = EmployeeRole.EMPLOYEE;

    @Builder.Default
    @Column(nullable = false)
    private UserType type = UserType.EMPLOYEE;

}
