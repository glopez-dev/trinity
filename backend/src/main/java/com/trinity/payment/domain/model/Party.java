package com.trinity.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Party to an invoice (merchant or billing recipient). Mirrors
 * {@code UserInfoDTO}; named {@code Party} to convey its invoicing role rather
 * than the generic "user info" framing of the REST contract.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Party {
    private String email;
    private String firstName;
    private String lastName;
}
