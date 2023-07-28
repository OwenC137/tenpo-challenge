package com.romero.tenpochallenge.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
@EnableConfigurationProperties({FlywayProperties.class})
public class DatabaseConfig {

    private final String username;
    private final String password;

    DatabaseConfig(
            @Value("${spring.r2dbc.username}") final String username,
            @Value("${spring.r2dbc.password}") final String password
    ) {
        this.username = username;
        this.password = password;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(final FlywayProperties flywayProperties) {
        return Flyway.configure()
                .dataSource(
                        flywayProperties.getUrl(),
                        this.username,
                        this.password
                )
                .locations(flywayProperties.getLocations().toArray(String[]::new))
                .baselineOnMigrate(true)
                .load();
    }
}
