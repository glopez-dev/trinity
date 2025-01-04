/**
 * Catalog module is a supporting domain that manages product visibility,
 * availability, and promotions in the storefront.
 * 
 * Key responsibilities:
 * - Product display management
 * - Product categorization
 * - Promotion management
 * - Stock availability checking
 * - Product search and filtering
 * 
 * This is a supporting domain because:
 * - It provides essential storefront functionality
 * - Manages how products are presented to customers
 * - Contains specific business rules for promotions
 * - Supports the shopping experience
 * 
 * Domain interfaces:
 * - Gets product information from Product domain
 * - Checks stock availability with Product domain
 * - Provides catalog items to Cart domain
 * - Shares browsing data with Analytics domain
 * 
 * As a supporting domain, Catalog enriches the product information
 * with presentation and promotional aspects, while delegating core
 * product and inventory management to the Product domain.
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Catalog",
    allowedDependencies = {"common", "product"}
)
package eu.epitech.msc2026.catalog;