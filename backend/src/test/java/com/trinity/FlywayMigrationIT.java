package com.trinity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guard rail: proves Flyway runs the V1 migration and that ddl-auto=validate
 * accepts the resulting schema (the context simply would not start otherwise).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class FlywayMigrationIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void flyway_appliedV1_andCreatedAllTables() {
        Integer applied = jdbcTemplate.queryForObject(
                "select count(*) from flyway_schema_history where success = true", Integer.class);
        assertThat(applied).isGreaterThanOrEqualTo(1);

        for (String table : new String[]{"employee", "customer", "products", "carts", "cart_items", "payments"}) {
            Integer count = jdbcTemplate.queryForObject(
                    "select count(*) from " + table, Integer.class);
            assertThat(count).isNotNull();
        }
    }
}
