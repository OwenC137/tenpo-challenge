package com.romero.tenpochallenge;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "Tenpo Challenge Api", version = "1.0", description = "Rest Api for Tenpo Challenge"))
public class TenpoChallengeApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TenpoChallengeApplication.class, args);
    }

}
