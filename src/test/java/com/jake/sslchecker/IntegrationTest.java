package com.jake.sslchecker;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @Test
    void testCheckCommandExecution() {
        SSLCheckerApp app = new SSLCheckerApp();
        CommandLine cmd = new CommandLine(app);

        int exitCode = cmd.execute("check", "google.com");
        assertEquals(0, exitCode, "CLI execution for google.com should succeed");
    }

    @Test
    void testBulkCommandExecution() {
        SSLCheckerApp app = new SSLCheckerApp();
        CommandLine cmd = new CommandLine(app);

        int exitCode = cmd.execute("bulk", "google.com", "example.com");
        assertEquals(0, exitCode, "CLI execution for bulk domains should succeed");
    }
}
