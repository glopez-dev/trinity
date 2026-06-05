package com.trinity.user.interfaces.rest.mapper;

import com.trinity.user.dto.employee.ReadEmployeeDTO;
import com.trinity.user.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Maps Employee domain entities to their API representation.
 */
@Mapper(componentModel = "spring")
public interface EmployeeApiMapper {

    ReadEmployeeDTO toResponse(Employee employee);

    List<ReadEmployeeDTO> toResponseList(List<Employee> employees);
}
