package com.jake.sslchecker.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jake.sslchecker.model.CheckResult;

import java.util.List;

public class OutputFormatter {

    private final ObjectMapper objectMapper;

    public OutputFormatter() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void formatConsole(CheckResult result) {
        if (!result.success()) {
            System.err.printf("\u001B[31m[FAILED]\u001B[0m %s: %s%n", result.domain(),
                    result.errorMessage().orElse("Unknown error"));
            return;
        }

        result.certificateInfo().ifPresent(info -> {
            long days = info.daysUntilExpiry();
            String color = days < 7 ? "\u001B[31m" : (days < 30 ? "\u001B[33m" : "\u001B[32m");

            System.out.println("--------------------------------------------------");
            System.out.printf("Domain: %s%n", result.domain());
            System.out.printf("Subject: %s%n", info.subject());
            System.out.printf("Issuer: %s%n", info.issuer());
            System.out.printf("Status: %s%s days until expiry\u001B[0m%n", color, days);
            System.out.printf("Valid To: %s%n", info.validTo());
            if (!info.subjectAlternativeNames().isEmpty()) {
                System.out.println("SANs: " + String.join(", ", info.subjectAlternativeNames()));
            }
            System.out.println("--------------------------------------------------");
        });
    }

    public void formatJson(List<CheckResult> results) {
        try {
            System.out.println(objectMapper.writeValueAsString(results));
        } catch (Exception e) {
            System.err.println("Error formatting JSON: " + e.getMessage());
        }
    }
}
