package com.trinity;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Hexagonal-purity guard rails.
 *
 * <p>The "domain must not depend on frameworks" rule is enforced per bounded
 * context and widened as each context is hexagonalized. Contexts already
 * compliant (cart, payment) are enforced now; product and user are added when
 * their dedicated chantier lands. The full set being green is the completion
 * criterion of the hexagonal refactoring.
 */
class ArchitectureTest {

    private static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.trinity");

    /** Bounded contexts whose domain package is enforced as framework-free. */
    private static final String[] PURE_DOMAIN_CONTEXTS = {"cart", "payment", "product", "user"};

    @Test
    void domainPackagesAreFreeOfFrameworks() {
        for (String context : PURE_DOMAIN_CONTEXTS) {
            ArchRule rule = noClasses()
                    .that().resideInAPackage("..%s.domain..".formatted(context))
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "jakarta.persistence..",
                            "org.hibernate..",
                            "org.springframework..",
                            "com.stripe..",
                            "com.paypal..")
                    .because("the %s domain must stay free of infrastructure".formatted(context));
            rule.check(CLASSES);
        }
    }

    @Test
    void apiMappersDoNotDependOnPersistenceEntities() {
        // allowEmptyShould: these layered packages appear as contexts are migrated;
        // the rule still guards the boundary once they exist.
        ArchRule rule = noClasses()
                .that().resideInAPackage("..interfaces.rest.mapper..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.persistence.entity..")
                .because("API mappers map DTOs to/from the domain, never JPA entities")
                .allowEmptyShould(true);
        rule.check(CLASSES);
    }

    @Test
    void persistenceMappersDoNotDependOnApiDtos() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..infrastructure.persistence.mapper..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces.rest.dto..")
                .because("persistence mappers map the domain to/from JPA entities, never DTOs")
                .allowEmptyShould(true);
        rule.check(CLASSES);
    }
}
