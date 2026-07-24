package com.trinity.user.infrastructure.persistence.adapter;

import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.model.EmployeeRole;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.domain.port.EmployeeRepositoryPort;
import com.trinity.user.infrastructure.persistence.mapper.EmployeePersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Round-trips the employee aggregate through the port + hand-written mapper.
 * Pins the id-on-save contract and that the mapper copies the NOT-NULL ordinal
 * columns (status/role/type) from the domain defaults.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({EmployeeRepositoryAdapter.class, EmployeePersistenceMapper.class})
class EmployeeRepositoryAdapterTest {

    @Autowired
    private EmployeeRepositoryPort port;

    @Test
    void save_populatesGeneratedId_andDefaultsAreCopied() {
        // Build WITHOUT id/status/role/type -> relies on the domain defaults.
        Employee saved = port.save(Employee.builder()
                .email("erin@example.com")
                .hashedPassword("hash")
                .build());

        assertThat(saved.getId()).isNotNull();          // id-on-save contract (JWT)
        assertThat(saved.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(saved.getRole()).isEqualTo(EmployeeRole.EMPLOYEE);
        assertThat(saved.getType()).isEqualTo(UserType.EMPLOYEE);
        assertThat(saved.getVersion()).isNotNull();
    }

    @Test
    void save_thenFindByEmail_roundTripsDomain() {
        port.save(Employee.builder()
                .email("frank@example.com")
                .hashedPassword("hash")
                .role(EmployeeRole.MANAGER)
                .build());

        assertThat(port.findByEmail("frank@example.com"))
                .isPresent()
                .get()
                .satisfies(e -> {
                    assertThat(e.getRole()).isEqualTo(EmployeeRole.MANAGER);
                    assertThat(e.getStatus()).isEqualTo(UserStatus.ACTIVE);
                    assertThat(e.getType()).isEqualTo(UserType.EMPLOYEE);
                    assertThat(e.getHireDate()).isNotNull();
                });
    }

    @Test
    void save_existingEmployee_keepsSameId() {
        Employee created = port.save(Employee.builder()
                .email("grace@example.com")
                .hashedPassword("hash")
                .build());
        UUID id = created.getId();

        Employee reloaded = port.findById(id).orElseThrow();
        reloaded.changeRole(EmployeeRole.ADMIN);
        Employee updated = port.save(reloaded);

        assertThat(updated.getId()).isEqualTo(id);
        assertThat(port.findById(id).orElseThrow().getRole()).isEqualTo(EmployeeRole.ADMIN);
        assertThat(port.findAll()).hasSize(1);
    }

    @Test
    void existsByEmail_and_delete() {
        Employee created = port.save(Employee.builder()
                .email("heidi@example.com")
                .hashedPassword("hash")
                .build());

        assertThat(port.existsByEmail("heidi@example.com")).isTrue();

        port.delete(created);

        assertThat(port.findById(created.getId())).isEmpty();
    }
}
