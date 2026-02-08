package com.jake.sslchecker.command;

import com.jake.sslchecker.model.CheckResult;
import com.jake.sslchecker.service.OutputFormatter;
import com.jake.sslchecker.service.SSLAnalyzer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@Command(name = "bulk", description = "Check SSL certificates for multiple domains")
public class BulkCommand implements Callable<Integer> {

    @Parameters(arity = "0..*", description = "Domains to check")
    private List<String> domains;

    @Option(names = { "-f", "--file" }, description = "File containing list of domains (one per line)")
    private Path domainFile;

    @Option(names = { "--json" }, description = "Output results in JSON format")
    private boolean jsonOutput;

    @Override
    public Integer call() {
        SSLAnalyzer analyzer = new SSLAnalyzer();
        OutputFormatter formatter = new OutputFormatter();
        List<String> allDomains = new ArrayList<>();

        if (domains != null) {
            allDomains.addAll(domains);
        }

        if (domainFile != null) {
            try (Stream<String> lines = Files.lines(domainFile)) {
                lines.map(String::trim).filter(s -> !s.isEmpty()).forEach(allDomains::add);
            } catch (IOException e) {
                System.err.println("Error reading domain file: " + e.getMessage());
                return 1;
            }
        }

        if (allDomains.isEmpty()) {
            System.err.println("No domains specified. Use parameters or --file.");
            return 1;
        }

        List<CheckResult> results = allDomains.parallelStream()
                .map(analyzer::analyze)
                .toList();

        if (jsonOutput) {
            formatter.formatJson(results);
        } else {
            results.forEach(formatter::formatConsole);
        }

        boolean allSuccess = results.stream().allMatch(CheckResult::success);
        return allSuccess ? 0 : 1;
    }
}
