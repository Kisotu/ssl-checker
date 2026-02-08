package com.jake.sslchecker.command;

import com.jake.sslchecker.model.CheckResult;
import com.jake.sslchecker.service.OutputFormatter;
import com.jake.sslchecker.service.SSLAnalyzer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Collections;
import java.util.concurrent.Callable;

@Command(name = "check", description = "Check SSL certificate for a single domain")
public class CheckCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "Domain to check")
    private String domain;

    @Option(names = { "-p", "--port" }, description = "Port number (default: 443)", defaultValue = "443")
    private int port;

    @Option(names = { "--json" }, description = "Output results in JSON format")
    private boolean jsonOutput;

    @Override
    public Integer call() {
        SSLAnalyzer analyzer = new SSLAnalyzer();
        OutputFormatter formatter = new OutputFormatter();

        CheckResult result = analyzer.analyze(domain, port, 10000);

        if (jsonOutput) {
            formatter.formatJson(Collections.singletonList(result));
        } else {
            formatter.formatConsole(result);
        }

        return result.success() ? 0 : 1;
    }
}
