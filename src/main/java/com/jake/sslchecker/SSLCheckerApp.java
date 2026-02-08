package com.jake.sslchecker;

import com.jake.sslchecker.command.BulkCommand;
import com.jake.sslchecker.command.CheckCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "ssl-checker", mixinStandardHelpOptions = true, version = "1.0.0", description = "Checks SSL certificate expiry and details.", subcommands = {
        CheckCommand.class, BulkCommand.class })
public class SSLCheckerApp implements Callable<Integer> {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SSLCheckerApp()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        return 0;
    }
}
