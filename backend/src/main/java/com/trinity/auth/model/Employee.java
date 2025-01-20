package com.trinity.auth.model;

import java.time.Instant;

import com.trinity.auth.constant.UserRole;

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
@SuperBuilder // Allows this class to use the builder pattern with it's parent class attributes. 
@NoArgsConstructor // Hibernate needs a no-args constructor. 
@AllArgsConstructor // Builder pattern requires all args constructor.
/* JPA */
@Entity
@Table(name = "employee")
public class Employee extends AbstractUser {

    private Instant hireDate;
    private Instant terminationDate;

    @Builder.Default
    private UserRole role = UserRole.EMPLOYEE;

}
