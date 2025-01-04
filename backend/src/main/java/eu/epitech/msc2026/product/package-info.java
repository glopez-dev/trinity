/**
 * Product module is a supporting domain that manages product information and inventory.
 * It enriches product data through OpenFoodFacts API and provides product information
 * to other domains.
 * 
 * Key responsibilities:
 * - Product information management
 * - Product data enrichment via OpenFoodFacts
 * - Product lifecycle management
 * - Product categorization
 * 
 * This is a supporting domain because:
 * - It provides essential business functionality
 * - Contains specific business rules for product management
 * - Supports core payment operations with product data
 * - Manages critical product data
 * 
 * Domain interfaces:
 * - Provides product info to Payment domain for order processing
 * - Supplies product data to Catalog domain
 * - Shares product data with Analytics domain
 * - Enriches product data through OpenFoodFacts API
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Product",
    allowedDependencies = {"common"}
)
package eu.epitech.msc2026.product;
