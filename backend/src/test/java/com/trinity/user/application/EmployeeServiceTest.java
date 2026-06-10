package com.trinity.user.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.user.application.command.CreateEmployeeCommand;
import com.trinity.user.application.command.UpdateEmployeeCommand;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.model.EmployeeRole;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.port.EmployeeRepositoryPort;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepositoryPort employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void findById_ShouldReturnEmployee_WhenEmployeeExists() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setId(employeeId);
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        Employee foundEmployee = ReflectionTestUtils.invokeMethod(employeeService, "findById", employeeId);

        // Then
        Assertions.assertNotNull(foundEmployee);
        Assertions.assertEquals(employeeId, foundEmployee.getId());
        Mockito.verify(employeeRepository).findById(employeeId);
    }

    @Test
    void findById_ShouldThrowException_WhenEmployeeDoesNotExist() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
            () -> ReflectionTestUtils.invokeMethod(employeeService, "findById", employeeId)
        );
        Assertions.assertEquals("Employee not found", ex.getMessage());
        Mockito.verify(employeeRepository).findById(employeeId);
    }

    @Test
    void createEmployee_ShouldHashPasswordAndSave() {
        // Given
        CreateEmployeeCommand command = new CreateEmployeeCommand(
                "jane@company.com", "rawPwd", "Jane", "Doe", EmployeeRole.EMPLOYEE);
        Mockito.when(passwordEncoder.encode("rawPwd")).thenReturn("hashedPwd");
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        Employee result = employeeService.createEmployee(command);

        // Then
        Assertions.assertNotNull(result);
        Mockito.verify(passwordEncoder).encode("rawPwd");
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        Mockito.verify(employeeRepository).save(captor.capture());
        Employee saved = captor.getValue();
        Assertions.assertEquals("jane@company.com", saved.getEmail());
        Assertions.assertEquals("hashedPwd", saved.getHashedPassword());
        Assertions.assertEquals(EmployeeRole.EMPLOYEE, saved.getRole());
        Assertions.assertNotNull(saved.getHireDate());
    }

    @Test
    void getAllEmployees_ShouldReturnAll() {
        // Given
        Mockito.when(employeeRepository.findAll())
                .thenReturn(List.of(new Employee(), new Employee()));

        // When
        List<Employee> result = employeeService.getAllEmployees();

        // Then
        Assertions.assertEquals(2, result.size());
        Mockito.verify(employeeRepository).findAll();
    }

    @Test
    void getEmployee_ShouldReturnEmployee_WhenExists() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = Employee.builder().id(employeeId).build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        Employee result = employeeService.getEmployee(employeeId);

        // Then
        Assertions.assertEquals(employeeId, result.getId());
        Mockito.verify(employeeRepository).findById(employeeId);
    }

    @Test
    void getEmployee_ShouldThrowNotFound_WhenMissing() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When / Then
        Assertions.assertThrows(NotFoundException.class,
                () -> employeeService.getEmployee(employeeId));
    }

    @Test
    void updateEmployee_ShouldApplyAllFields() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = Employee.builder()
                .id(employeeId)
                .email("old@company.com")
                .firstName("Old")
                .lastName("Name")
                .role(EmployeeRole.EMPLOYEE)
                .status(UserStatus.ACTIVE)
                .build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UpdateEmployeeCommand command = new UpdateEmployeeCommand(
                Optional.of("new@company.com"),
                Optional.of("NewFirst"),
                Optional.of("NewLast"),
                Optional.of(EmployeeRole.MANAGER),
                Optional.of(UserStatus.ACTIVE));

        // When
        Employee result = employeeService.updateEmployee(employeeId, command);

        // Then
        Assertions.assertEquals("new@company.com", result.getEmail());
        Assertions.assertEquals("NewFirst", result.getFirstName());
        Assertions.assertEquals("NewLast", result.getLastName());
        Assertions.assertEquals(EmployeeRole.MANAGER, result.getRole());
        Mockito.verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_ShouldLeaveFieldsUnchanged_WhenCommandEmpty() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = Employee.builder()
                .id(employeeId)
                .email("keep@company.com")
                .firstName("Keep")
                .lastName("Name")
                .role(EmployeeRole.EMPLOYEE)
                .status(UserStatus.ACTIVE)
                .build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UpdateEmployeeCommand command =
                new UpdateEmployeeCommand(null, null, null, null, null);

        // When
        Employee result = employeeService.updateEmployee(employeeId, command);

        // Then
        Assertions.assertEquals("keep@company.com", result.getEmail());
        Assertions.assertEquals("Keep", result.getFirstName());
        Assertions.assertEquals(EmployeeRole.EMPLOYEE, result.getRole());
        Mockito.verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_ShouldThrowNotFound_WhenMissing() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        UpdateEmployeeCommand command =
                new UpdateEmployeeCommand(null, null, null, null, null);

        // When / Then
        Assertions.assertThrows(NotFoundException.class,
                () -> employeeService.updateEmployee(employeeId, command));
    }

    @Test
    void deleteEmployee_ShouldDelete_WhenExists() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = Employee.builder().id(employeeId).build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        employeeService.deleteEmployee(employeeId);

        // Then
        Mockito.verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteEmployee_ShouldThrowNotFound_WhenMissing() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When / Then
        Assertions.assertThrows(NotFoundException.class,
                () -> employeeService.deleteEmployee(employeeId));
    }

    @ParameterizedTest
    @EnumSource(UserStatus.class)
    void updateEmployee_ShouldApplyEachStatusBranch(UserStatus targetStatus) {
        // Given — start from ACTIVE so non-DELETED transitions are allowed
        UUID employeeId = UUID.randomUUID();
        Employee employee = Employee.builder()
                .id(employeeId)
                .status(UserStatus.ACTIVE)
                .build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UpdateEmployeeCommand command = new UpdateEmployeeCommand(
                null, null, null, null, Optional.of(targetStatus));

        // When
        Employee result = employeeService.updateEmployee(employeeId, command);

        // Then
        Assertions.assertEquals(targetStatus, result.getStatus());
    }
}