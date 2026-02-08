package com.jake.sslchecker.model;

import java.util.Optional;

public record CheckResult(
        String domain,
        boolean success,
        Optional<CertificateInfo> certificateInfo,
        Optional<String> errorMessage) {
    public static CheckResult success(String domain, CertificateInfo info) {
        return new CheckResult(domain, true, Optional.of(info), Optional.empty());
    }

    public static CheckResult failure(String domain, String error) {
        return new CheckResult(domain, false, Optional.empty(), Optional.of(error));
    }
}
