package com.financial_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinancialTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialTrackerApplication.class, args);
    }
}
