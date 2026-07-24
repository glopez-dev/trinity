package com.trinity.payment.interfaces.rest.mapper;

import com.trinity.payment.domain.model.Cost;
import com.trinity.payment.domain.model.Invoice;
import com.trinity.payment.domain.model.InvoiceItem;
import com.trinity.payment.domain.model.Party;
import com.trinity.payment.interfaces.rest.dto.CostDTO;
import com.trinity.payment.interfaces.rest.dto.InvoiceDTO;
import com.trinity.payment.interfaces.rest.dto.InvoiceItemDTO;
import com.trinity.payment.interfaces.rest.dto.UserInfoDTO;
import org.mapstruct.Mapper;

/**
 * Maps between the {@link Invoice} domain aggregate and its REST API DTOs.
 * Follows the {@code ProductApiMapper} pattern: the domain model mirrors the
 * DTO structure field-for-field, so MapStruct resolves the nested value objects
 * by property name. The explicit methods below cover the value objects whose DTO
 * counterpart is named differently ({@link Party} &lt;-&gt; {@code UserInfoDTO}).
 */
@Mapper(componentModel = "spring")
public interface InvoiceApiMapper {

    Invoice toDomain(InvoiceDTO dto);

    InvoiceDTO toDTO(Invoice invoice);

    // --- nested value objects <-> DTOs ---

    Cost toCost(CostDTO dto);

    CostDTO toCostDTO(Cost cost);

    InvoiceItem toInvoiceItem(InvoiceItemDTO dto);

    InvoiceItemDTO toInvoiceItemDTO(InvoiceItem item);

    Party toParty(UserInfoDTO dto);

    UserInfoDTO toUserInfoDTO(Party party);
}
