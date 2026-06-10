package com.trinity.user.interfaces.rest.mapper;

import com.trinity.user.application.command.CreateCustomerCommand;
import com.trinity.user.application.command.UpdateCustomerCommand;
import com.trinity.user.interfaces.rest.dto.CreateCustomerRequest;
import com.trinity.user.interfaces.rest.dto.CustomerResponse;
import com.trinity.user.interfaces.rest.dto.UpdateCustomerRequest;
import com.trinity.user.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Maps Customer domain entities to their API representation, and REST requests
 * to application commands.
 * {@code tokenExpired} is derived from {@link Customer#isTokenExpired()} by name.
 */
@Mapper(componentModel = "spring")
public interface CustomerApiMapper {

    CustomerResponse toResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);

    @Mapping(target = "rawPassword", source = "password")
    CreateCustomerCommand toCommand(CreateCustomerRequest request);

    @Mapping(target = "rawPassword", source = "password")
    UpdateCustomerCommand toCommand(UpdateCustomerRequest request);
}
