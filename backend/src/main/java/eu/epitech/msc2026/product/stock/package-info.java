/**
 * Stock management submodule handling inventory tracking and stock operations.
 * 
 * Key responsibilities:
 * - Stock level tracking
 * - Stock availability checking
 * - Stock reservation management
 * - Low stock alerting
 * - Stock movement history
 * 
 * This module handles:
 * - Real-time stock verification for Cart domain
 * - Stock level queries for Catalog domain
 * - Stock updates after order processing
 * - Stock analytics for reporting
 * 
 * Business rules:
 * - Stock reservation timeout handling
 * - Low stock threshold management
 * - Stock synchronization
 * - Concurrent stock access management
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Product :: Stock",
    allowedDependencies = {"common"}
)
package eu.epitech.msc2026.product.stock;
