package com.trinity;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
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

    /** Every business module of the modular monolith (common is the shared kernel, free to import). */
    private static final String[] BUSINESS_MODULES = {"authentication", "cart", "payment", "product", "user"};

    /**
     * The only cross-module dependencies allowed, by consuming module. Each entry
     * is a deliberate, named contract — extend it in the same commit as the code
     * that needs it, never wholesale.
     */
    private static final Map<String, String[]> CROSS_MODULE_WHITELIST = Map.of(
            // authentication owns no users: it registers and loads them through
            // the user module's domain ports and models (never its infrastructure).
            "authentication", new String[]{
                    "com.trinity.user.domain.model..",
                    "com.trinity.user.domain.port.."
            },
            // product consumes the cart-validated domain event to deduct stock;
            // events belong to their producer, consumers import them, never the reverse.
            "product", new String[]{
                    "com.trinity.cart.domain.event.."
            },
            // cart resolves product names and prices server-side through the
            // product module's public surface (application service + domain model).
            "cart", new String[]{
                    "com.trinity.product.application..",
                    "com.trinity.product.domain.model.."
            }
    );

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
    void domainPackagesDoNotDependOnRestDtos() {
        // The domain must not know the presentation layer: REST DTOs are converted
        // to/from the domain by the API mappers at the application boundary. This is
        // the regression guard for the InvoicingGateway leak (it imported InvoiceDTO).
        for (String context : PURE_DOMAIN_CONTEXTS) {
            ArchRule rule = noClasses()
                    .that().resideInAPackage("..%s.domain..".formatted(context))
                    .should().dependOnClassesThat()
                    .resideInAPackage("..%s.interfaces.rest.dto..".formatted(context))
                    .because("the %s domain must not depend on REST DTOs".formatted(context))
                    .allowEmptyShould(true);
            rule.check(CLASSES);
        }
    }

    @Test
    void externalAclDtosAreConfinedToTheirAdapter() {
        // External-API DTOs (e.g. OpenFoodFacts response shapes) are an
        // anti-corruption concern: only the infrastructure.external adapter that
        // owns the integration may know them. They must not leak into the domain,
        // the application layer, the REST layer or persistence.
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("..infrastructure.external..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.external..dto..")
                .because("external ACL DTOs belong to their adapter only")
                .allowEmptyShould(true);
        rule.check(CLASSES);
    }

    @Test
    void apiMappersDoNotDependOnPersistenceEntities() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..interfaces.rest.mapper..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.persistence.entity..")
                .because("API mappers map DTOs to/from the domain, never JPA entities");
        rule.check(CLASSES);
    }

    @Test
    void persistenceMappersDoNotDependOnApiDtos() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..infrastructure.persistence.mapper..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces.rest.dto..")
                .because("persistence mappers map the domain to/from JPA entities, never DTOs");
        rule.check(CLASSES);
    }

    @Test
    void modulesDoNotReachIntoOtherModules() {
        // Inter-module boundary: a module may import common freely, but another
        // business module only through its whitelisted public surface above.
        for (String module : BUSINESS_MODULES) {
            String[] others = Arrays.stream(BUSINESS_MODULES)
                    .filter(m -> !m.equals(module))
                    .map("com.trinity.%s.."::formatted)
                    .toArray(String[]::new);
            String[] allowed = CROSS_MODULE_WHITELIST.getOrDefault(module, new String[0]);
            ArchRule rule = noClasses()
                    .that().resideInAPackage("com.trinity.%s..".formatted(module))
                    .should().dependOnClassesThat(
                            resideInAnyPackage(others).and(not(resideInAnyPackage(allowed))))
                    .because("modules communicate via domain events or a whitelisted public surface only")
                    .allowEmptyShould(true);
            rule.check(CLASSES);
        }
    }

    @Test
    void commonDependsOnNoBusinessModule() {
        String[] modulePackages = Arrays.stream(BUSINESS_MODULES)
                .map("com.trinity.%s.."::formatted)
                .toArray(String[]::new);
        ArchRule rule = noClasses()
                .that().resideInAPackage("com.trinity.common..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(modulePackages)
                .because("the shared kernel must not know any business module")
                .allowEmptyShould(true);
        rule.check(CLASSES);
    }

    @Test
    void applicationLayerDoesNotDependOnInterfacesLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..")
                .because("controllers map DTOs at the boundary; application services speak domain")
                .allowEmptyShould(true);
        rule.check(CLASSES);
    }
}
