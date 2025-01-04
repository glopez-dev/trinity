/**
 * Common module provides shared infrastructure components and cross-cutting concerns
 * for all other modules in the application.
 * 
 * Key responsibilities:
 * - Infrastructure configuration (RabbitMQ, Health checks)
 * - Cross-cutting concerns (Logging, Monitoring)
 * - Shared value objects (Money, DateRange)
 * - Technical services (Event publishing)
 * 
 * Infrastructure components:
 * - Message broker configuration
 * - Health check endpoints
 * - Monitoring setup
 * 
 * Shared services:
 * - Event publishing
 * - Error handling
 * - Monitoring and metrics collection
 * 
 * This module contains no business logic but provides
 * technical capabilities to all business domains.
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Common",
    allowedDependencies = {}
)
package eu.epitech.msc2026.common;

