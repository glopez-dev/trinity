package com.trinity;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Spring Modulith verification: the packages directly under com.trinity are
 * the application modules. verify() fails on any cycle and on any dependency
 * outside the allowedDependencies declared in each module's package-info —
 * the executable counterpart of the modular-monolith decision (ADR-0001).
 * Complements ArchitectureTest, which keeps enforcing the intra-module layers.
 */
class ModularityTest {

    static final ApplicationModules MODULES = ApplicationModules.of(Application.class);

    @Test
    void verifiesModularStructure() {
        MODULES.verify();
    }

    /** Generates PlantUML C4 diagrams of the real module graph under target/spring-modulith-docs. */
    @Test
    void writeDocumentation() {
        new Documenter(MODULES).writeDocumentation();
    }
}
