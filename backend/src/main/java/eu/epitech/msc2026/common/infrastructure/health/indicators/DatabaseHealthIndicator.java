package eu.epitech.msc2026.common.infrastructure.health.indicators;

import javax.sql.DataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (var connection = dataSource.getConnection()) {
            var dbProductName = connection.getMetaData().getDatabaseProductName();
            var dbVersion = connection.getMetaData().getDatabaseProductVersion();
            
            return Health.up()
                    .withDetail("database", dbProductName)
                    .withDetail("version", dbVersion)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withException(e)
                    .build();
        }
    }
}