package com.jake.sslchecker.service;

import com.jake.sslchecker.exception.SSLCheckException;
import com.jake.sslchecker.model.CertificateInfo;
import com.jake.sslchecker.model.CheckResult;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SSLAnalyzer {

    private static final int DEFAULT_PORT = 443;
    private static final int DEFAULT_TIMEOUT_MS = 10000;

    public CheckResult analyze(String domain) {
        return analyze(domain, DEFAULT_PORT, DEFAULT_TIMEOUT_MS);
    }

    public CheckResult analyze(String domain, int port, int timeoutMs) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            SSLSocketFactory factory = sslContext.getSocketFactory();

            try (SSLSocket socket = (SSLSocket) factory.createSocket()) {
                socket.connect(new InetSocketAddress(domain, port), timeoutMs);
                socket.startHandshake();

                SSLSession session = socket.getSession();
                Certificate[] certs = session.getPeerCertificates();

                if (certs.length > 0 && certs[0] instanceof X509Certificate x509) {
                    return CheckResult.success(domain, convert(x509));
                } else {
                    return CheckResult.failure(domain, "No X.509 certificates found");
                }
            }
        } catch (Exception e) {
            return CheckResult.failure(domain, e.getMessage());
        }
    }

    private CertificateInfo convert(X509Certificate cert) {
        List<String> sans = new ArrayList<>();
        try {
            Collection<List<?>> altNames = cert.getSubjectAlternativeNames();
            if (altNames != null) {
                for (List<?> name : altNames) {
                    if (name.size() >= 2 && name.get(1) instanceof String s) {
                        sans.add(s);
                    }
                }
            }
        } catch (CertificateParsingException ignored) {
        }

        return new CertificateInfo(
                cert.getSubjectX500Principal().getName(),
                cert.getIssuerX500Principal().getName(),
                cert.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                cert.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                sans,
                cert.getSerialNumber().toString(),
                cert.getSigAlgName(),
                cert.getVersion());
    }
}
