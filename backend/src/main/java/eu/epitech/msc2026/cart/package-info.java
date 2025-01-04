/**
 * Cart module is a supporting domain that manages shopping sessions
 * and prepares orders for payment processing.
 * 
 * Key responsibilities:
 * - Shopping cart management
 * - Cart item validation
 * - Session handling
 * - Order preparation
 * - Stock verification
 * 
 * This is a supporting domain because:
 * - It provides essential shopping functionality
 * - Coordinates between product selection and payment
 * - Contains specific business rules for cart management
 * - Supports the core payment process
 * 
 * Domain interfaces:
 * - Verifies stock availability with Product domain
 * - Gets product information from Catalog domain
 * - Validates user session with User domain
 * - Creates orders for Payment domain
 * - Shares cart analytics with Analytics domain
 * 
 * As a supporting domain, Cart orchestrates the shopping experience
 * by coordinating between product selection and order creation.
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Cart",
    allowedDependencies = {"common", "product", "catalog", "user"}
)
package eu.epitech.msc2026.cart;