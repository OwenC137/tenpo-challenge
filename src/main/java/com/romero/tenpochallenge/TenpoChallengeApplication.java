package com.romero.tenpochallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TenpoChallengeApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TenpoChallengeApplication.class, args);
    }

}
