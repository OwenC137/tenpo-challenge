package com.romero.tenpochallenge.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
@EnableConfigurationProperties({FlywayProperties.class})
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    private final String host;
    private final Integer port;
    private final String username;
    private final String password;
    private final String database;

    DatabaseConfig(
            @Value("${spring.data.postgres.host}") final String host,
            @Value("${spring.data.postgres.port}") final Integer port,
            @Value("${spring.data.postgres.username}") final String username,
            @Value("${spring.data.postgres.password}") final String password,
            @Value("${spring.data.postgres.database}") final String database
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    @Bean
    public @NotNull ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(this.host)
                        .port(this.port)
                        .username(this.username)
                        .password(this.password)
                        .database(this.database)
                        .build());
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
