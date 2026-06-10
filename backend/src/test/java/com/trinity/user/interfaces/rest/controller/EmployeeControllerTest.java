package com.trinity.user.interfaces.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.user.application.EmployeeService;
import com.trinity.user.application.command.CreateEmployeeCommand;
import com.trinity.user.application.command.UpdateEmployeeCommand;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.model.EmployeeRole;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.interfaces.rest.dto.CreateEmployeeRequest;
import com.trinity.user.interfaces.rest.dto.EmployeeResponse;
import com.trinity.user.interfaces.rest.dto.UpdateEmployeeRequest;
import com.trinity.user.interfaces.rest.mapper.EmployeeApiMapper;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Spy
    private EmployeeApiMapper employeeApiMapper = Mappers.getMapper(EmployeeApiMapper.class);

    @InjectMocks
    private EmployeeController employeeController;

    private UUID employeeId;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employeeId = UUID.randomUUID();
        employee = Employee.builder()
                .id(employeeId)
                .email("jane.doe@company.com")
                .firstName("Jane")
                .lastName("Doe")
                .role(EmployeeRole.EMPLOYEE)
                .status(UserStatus.ACTIVE)
                .hireDate(Instant.now())
                .build();
    }

    @Test
    void createEmployee_ReturnsOkWithBody() {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "jane.doe@company.com", "password123", "Jane", "Doe", EmployeeRole.EMPLOYEE, Instant.now());
        when(employeeService.createEmployee(any(CreateEmployeeCommand.class))).thenReturn(employee);

        ResponseEntity<EmployeeResponse> response = employeeController.createEmployee(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeId, response.getBody().getId());
        assertEquals("jane.doe@company.com", response.getBody().getEmail());
        verify(employeeService).createEmployee(any(CreateEmployeeCommand.class));
    }

    @Test
    void getAllEmployees_ReturnsOkWithList() {
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        ResponseEntity<List<EmployeeResponse>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void getEmployee_ReturnsOkWhenFound() {
        when(employeeService.getEmployee(employeeId)).thenReturn(employee);

        ResponseEntity<EmployeeResponse> response = employeeController.getEmployee(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeId, response.getBody().getId());
        verify(employeeService).getEmployee(employeeId);
    }

    @Test
    void getEmployee_PropagatesNotFound() {
        when(employeeService.getEmployee(employeeId))
                .thenThrow(new NotFoundException("Employee not found"));

        assertThrows(NotFoundException.class, () -> employeeController.getEmployee(employeeId));

        verify(employeeService).getEmployee(employeeId);
    }

    @Test
    void updateEmployee_ReturnsOkWithBody() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest(
                Optional.of("new@company.com"),
                Optional.of("NewFirst"),
                Optional.of("NewLast"),
                Optional.of(EmployeeRole.MANAGER),
                Optional.of(UserStatus.ACTIVE));
        when(employeeService.updateEmployee(eq(employeeId), any(UpdateEmployeeCommand.class)))
                .thenReturn(employee);

        ResponseEntity<EmployeeResponse> response = employeeController.updateEmployee(employeeId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(employeeService).updateEmployee(eq(employeeId), any(UpdateEmployeeCommand.class));
    }

    @Test
    void deleteEmployee_ReturnsNoContent() {
        doNothing().when(employeeService).deleteEmployee(employeeId);

        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService).deleteEmployee(employeeId);
    }
}
