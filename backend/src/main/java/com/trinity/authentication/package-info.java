/**
 * Registers and authenticates users. Owns no user data: it goes through the
 * user module's exposed domain model and ports.
 */
@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"user :: model", "user :: ports", "common"})
package com.trinity.authentication;
