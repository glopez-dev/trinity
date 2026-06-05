package com.trinity.user.interfaces.rest.mapper;

import com.trinity.user.dto.customer.ReadCustomerDTO;
import com.trinity.user.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Maps Customer domain entities to their API representation.
 * {@code tokenExpired} is derived from {@link Customer#isTokenExpired()} by name.
 */
@Mapper(componentModel = "spring")
public interface CustomerApiMapper {

    ReadCustomerDTO toResponse(Customer customer);

    List<ReadCustomerDTO> toResponseList(List<Customer> customers);
}
