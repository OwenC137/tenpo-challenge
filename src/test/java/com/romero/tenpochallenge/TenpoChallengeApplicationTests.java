package com.romero.tenpochallenge;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@OpenAPIDefinition(info = @Info(title = "Tenpo Challenge Api", version = "1.0", description = "Rest Api for Tenpo Challenge"))
class TenpoChallengeApplicationTests {
    @Test
    void contextLoads() {
    }

}
