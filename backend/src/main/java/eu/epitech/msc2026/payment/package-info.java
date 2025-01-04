/**
 * Payment module is the core domain of the application, managing all payment-related 
 * operations and order processing.
 * 
 * Core responsibilities:
 * - Order management and validation
 * - Payment processing via PayPal integration
 * - Payment status tracking and updates
 * - Refund handling
 * - Transaction history
 * 
 * This is a core domain because:
 * - It represents our main business value
 * - It contains complex business rules around payment processing
 * - It requires careful handling of financial transactions
 * - It integrates critical external services (PayPal)
 * 
 * Domain interfaces:
 * - Receives order data from Cart domain
 * - Validates products with Product domain
 * - Verifies customers with User domain
 * - Processes payments through PayPal
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Payment",
    allowedDependencies = {"common", "cart", "user", "product"}
)
package eu.epitech.msc2026.payment;