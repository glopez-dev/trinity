/**
 * Customer carts. Depends on no business module: product information arrives
 * through its own ProductInfoPort (implemented by the product module) and the
 * purchase flow leaves via the CartValidatedEvent domain event.
 */
@org.springframework.modulith.ApplicationModule(allowedDependencies = {"common"})
package com.trinity.cart;
