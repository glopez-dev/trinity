package com.trinity.user.interfaces.rest.mapper;

import com.trinity.user.interfaces.rest.dto.EmployeeResponse;
import com.trinity.user.domain.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Maps Employee domain entities to their API representation.
 */
@Mapper(componentModel = "spring")
public interface EmployeeApiMapper {

    EmployeeResponse toResponse(Employee employee);

    List<EmployeeResponse> toResponseList(List<Employee> employees);
}
