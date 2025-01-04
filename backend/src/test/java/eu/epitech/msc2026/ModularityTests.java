package eu.epitech.msc2026;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * Tests to verify the modular structure and generate documentation for the modules.
 *
 * @author Oliver Drotbohm
 */
class ModularityTests {

	ApplicationModules modules = ApplicationModules.of(Application.class);

	@Test
	void verifiesModularStructure() {
		modules.verify();
	}
}
    
