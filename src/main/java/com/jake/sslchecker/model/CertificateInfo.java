package com.jake.sslchecker.model;

import java.time.OffsetDateTime;
import java.util.List;

public record CertificateInfo(
        String subject,
        String issuer,
        OffsetDateTime validFrom,
        OffsetDateTime validTo,
        List<String> subjectAlternativeNames,
        String serialNumber,
        String signatureAlgorithm,
        int version) {
    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(validTo);
    }

    public long daysUntilExpiry() {
        return java.time.Duration.between(OffsetDateTime.now(), validTo).toDays();
    }
}
