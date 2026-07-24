package com.trinity.payment.interfaces.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class InvoiceItemDTOTest {

    @Test
    void givenAllArgs_whenBuilt_thenGettersMatch() {
        InvoiceItemDTO dto = InvoiceItemDTO.builder()
                .name("Product XYZ")
                .quantity(2)
                .unitPrice(new BigDecimal("99.99"))
                .build();

        assertThat(dto.getName()).isEqualTo("Product XYZ");
        assertThat(dto.getQuantity()).isEqualTo(2);
        assertThat(dto.getUnitPrice()).isEqualTo(new BigDecimal("99.99"));
    }

    @Test
    void givenNoArgs_whenSettersUsed_thenGettersReflectChanges() {
        InvoiceItemDTO dto = new InvoiceItemDTO();
        dto.setName("Coffee");
        dto.setQuantity(1);
        dto.setUnitPrice(new BigDecimal("4.50"));

        assertThat(dto.getName()).isEqualTo("Coffee");
        assertThat(dto.getQuantity()).isEqualTo(1);
        assertThat(dto.getUnitPrice()).isEqualTo(new BigDecimal("4.50"));
    }

    @Test
    void givenTwoEqualDtos_thenEqualsAndHashCodeConsistent() {
        InvoiceItemDTO a = new InvoiceItemDTO("Coffee", 1, new BigDecimal("4.50"));
        InvoiceItemDTO b = new InvoiceItemDTO("Coffee", 1, new BigDecimal("4.50"));

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("Coffee");
    }
}
