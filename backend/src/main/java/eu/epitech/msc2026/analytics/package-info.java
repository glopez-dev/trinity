/**
 * Analytics module is a generic domain that handles reporting and KPI generation
 * by collecting data from all other domains.
 * 
 * Key responsibilities:
 * - KPI generation and tracking
 * - Business intelligence reporting
 * - Data aggregation and analysis
 * - Performance monitoring
 * 
 * This is a generic domain because:
 * - It provides standard reporting functionality
 * - Could be replaced by a third-party solution
 * - Doesn't contain core business rules
 * - Supports decision-making across all domains
 * 
 * Data collection from other domains:
 * - Payment: Orders and transaction data
 * - Product: Stock levels and inventory changes
 * - Catalog: Product views and interactions
 * - User: Customer behavior and profiles
 * - Cart: Shopping patterns and abandonment
 * 
 * As a generic domain, Analytics focuses on providing
 * standardized reporting and analysis capabilities
 * without implementing specific business rules.
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Analytics",
    allowedDependencies = {"common"}
)
package eu.epitech.msc2026.analytics;