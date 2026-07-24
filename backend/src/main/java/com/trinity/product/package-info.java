/**
 * Product catalogue and stock. Consumes the cart's domain event (stock
 * deduction) and implements the cart's ProductInfoPort — the single allowed
 * direction between the two modules.
 */
@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"cart :: events", "cart :: ports", "common"})
package com.trinity.product;
