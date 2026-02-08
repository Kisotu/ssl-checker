package com.jake.sslchecker;

import com.jake.sslchecker.model.CertificateInfo;
import com.jake.sslchecker.model.CheckResult;
import com.jake.sslchecker.service.SSLAnalyzer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SSLAnalyzerTest {

    @Test
    void testAnalyzeExampleDotCom() {
        SSLAnalyzer analyzer = new SSLAnalyzer();
        CheckResult result = analyzer.analyze("example.com");

        assertTrue(result.success(), "Should successfully connect to example.com");
        assertTrue(result.certificateInfo().isPresent());

        CertificateInfo info = result.certificateInfo().get();
        assertTrue(info.subject().contains("CN=www.example.org") || info.subject().contains("CN=*.example.org"),
                "Subject should contain example.org");
    }

    @Test
    void testAnalyzeInvalidDomain() {
        SSLAnalyzer analyzer = new SSLAnalyzer();
        CheckResult result = analyzer.analyze("nonexistent.domain.jake");

        assertFalse(result.success());
        assertTrue(result.errorMessage().isPresent());
    }
}
