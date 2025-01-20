package com.trinity.user.domain;

import com.trinity.user.domain.enums.EmployeeRole;
import com.trinity.user.domain.enums.UserStatus;
import com.trinity.user.domain.events.AccountDeleted;
import com.trinity.user.domain.events.EmployeePromoted;
import com.trinity.user.domain.events.UserConnected;
import com.trinity.user.domain.events.UserEvent;
import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.valueobjects.Address;
import com.trinity.user.domain.valueobjects.PersonalInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;
import java.util.ArrayList;

public class UserTest {
    private PersonalInfo personalInfo;
    private Address address;

    @BeforeEach
    void setUp() {
        personalInfo = new PersonalInfo("John", "Doe", "1234567890");
        address = new Address(
                "123 Street",
                "12345",
                "City",
                "Country",
                true
        );
    }

    @Test
    void customer_should_be_created_with_correct_initial_state() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        ArrayList<Address> addresses = new ArrayList<>(Arrays.asList(address));

        // When
        Customer customer = createTestCustomer();

        // Then
        assertNotNull(customer.getId());
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(personalInfo, customer.getPersonalInfo());
        assertEquals(addresses, customer.getAddresses());
        assertEquals(UserStatus.ACTIVE, customer.getStatus());
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
        assertNotNull(customer.getLastLoginAt());
    }

    @Test
    void employee_should_be_created_with_correct_initial_state() {
        // Given
        String email = "john.employee@example.com";
        String password = "password123";
        EmployeeRole role = EmployeeRole.STORE_MANAGER;
        LocalDateTime hireDate = LocalDateTime.now();

        // When
        Employee employee = Employee.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password(password)
                .personalInfo(personalInfo)
                .status(UserStatus.ACTIVE)
                .role(role)
                .hireDate(hireDate)
                .terminationDate(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        // Then
        assertNotNull(employee.getId());
        assertEquals(email, employee.getEmail());
        assertEquals(password, employee.getPassword());
        assertEquals(personalInfo, employee.getPersonalInfo());
        assertEquals(UserStatus.ACTIVE, employee.getStatus());
        assertEquals(role, employee.getRole());
        assertEquals(hireDate, employee.getHireDate());
        assertNull(employee.getTerminationDate());
    }

    @Test
    void connect_should_update_last_login_time() {
        // Given
        Customer customer = createTestCustomer();
        LocalDateTime previousLoginTime = customer.getLastLoginAt();
        String ipAddress = "192.168.1.1";
        String userAgent = "Mozilla/5.0";

        // When
        customer.connect(ipAddress, userAgent);

        // Then
        assertTrue(customer.getLastLoginAt().isAfter(previousLoginTime));
        List<UserEvent> events = customer.getDomainEvents();
        assertTrue(events.stream()
                .anyMatch(event -> event instanceof UserConnected &&
                        ((UserConnected) event).getIpAddress().equals(ipAddress) &&
                        ((UserConnected) event).getUserAgent().equals(userAgent)));
    }

    @Test
    void customer_should_update_profile() {
        // Given
        Customer customer = createTestCustomer();
        PersonalInfo newPersonalInfo = new PersonalInfo("Jane", "Doe", "0987654321");

        // When
        customer.updateProfile(newPersonalInfo);

        // Then
        assertEquals(newPersonalInfo, customer.getPersonalInfo());
        assertTrue(customer.getUpdatedAt().isAfter(customer.getCreatedAt()));
    }

    @Test
    void employee_should_be_promoted() {
        // Given
        Employee employee = createTestEmployee(EmployeeRole.CASHIER);

        // When
        employee.promote(EmployeeRole.STORE_MANAGER);

        // Then
        assertEquals(EmployeeRole.STORE_MANAGER, employee.getRole());
        List<UserEvent> events = employee.getDomainEvents();
        assertTrue(events.stream()
                .anyMatch(event -> event instanceof EmployeePromoted &&
                        ((EmployeePromoted) event).getNewRole().equals(EmployeeRole.STORE_MANAGER)));
    }

    @Test
    void delete_should_mark_user_as_deleted_and_create_event() {
        // Given
        Customer customer = createTestCustomer();
        String reason = "Account closure requested";

        // When
        customer.delete(reason);

        // Then
        assertEquals(UserStatus.DELETED, customer.getStatus());
        List<UserEvent> events = customer.getDomainEvents();
        assertTrue(events.stream()
                .anyMatch(event -> event instanceof AccountDeleted &&
                        ((AccountDeleted) event).getReason().equals(reason)));
    }

    private Customer createTestCustomer() {
        return Customer.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password123")
                .personalInfo(personalInfo)
                .addresses(new ArrayList<>(Arrays.asList(address)))
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();
    }

    private Employee createTestEmployee(EmployeeRole role) {
        return Employee.builder()
                .id(UUID.randomUUID())
                .email("employee@example.com")
                .password("password123")
                .personalInfo(personalInfo)
                .status(UserStatus.ACTIVE)
                .role(role)
                .hireDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();
    }
}
