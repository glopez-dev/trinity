package com.trinity.user.interfaces.rest.mapper;

import com.trinity.user.application.command.CreateEmployeeCommand;
import com.trinity.user.application.command.UpdateEmployeeCommand;
import com.trinity.user.interfaces.rest.dto.CreateEmployeeRequest;
import com.trinity.user.interfaces.rest.dto.EmployeeResponse;
import com.trinity.user.interfaces.rest.dto.UpdateEmployeeRequest;
import com.trinity.user.domain.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Maps Employee domain entities to their API representation, and REST requests
 * to application commands.
 */
@Mapper(componentModel = "spring")
public interface EmployeeApiMapper {

    EmployeeResponse toResponse(Employee employee);

    List<EmployeeResponse> toResponseList(List<Employee> employees);

    @Mapping(target = "rawPassword", source = "password")
    CreateEmployeeCommand toCommand(CreateEmployeeRequest request);

    UpdateEmployeeCommand toCommand(UpdateEmployeeRequest request);
}
