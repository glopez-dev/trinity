/**
 * User module is a supporting domain that manages customer identity and profile information.
 * It handles user authentication through PayPal and provides user data to other domains.
 * 
 * Key responsibilities:
 * - User identity management
 * - Customer profile management
 * - Authentication and authorization
 * - User data verification through PayPal
 * - Billing information management
 * 
 * This is a supporting domain because:
 * - It provides essential customer management functionality
 * - Supports core payment operations with customer data
 * - Contains specific business rules for user management
 * - Manages critical customer information
 * 
 * Domain interfaces:
 * - Validates user identity through PayPal
 * - Provides customer data to Payment domain for transactions
 * - Supplies user context to Cart domain for shopping sessions
 * - Shares customer data with Analytics domain for reporting
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "User",
    allowedDependencies = {"common"}
)
package eu.epitech.msc2026.user;
