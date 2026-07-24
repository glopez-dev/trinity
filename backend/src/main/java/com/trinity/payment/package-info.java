/**
 * Payments and invoicing against external providers. Prices checkout lines
 * server-side through the product module's exposed API.
 */
@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"product :: api", "product :: model", "common"})
package com.trinity.payment;
