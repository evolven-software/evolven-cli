package com.evolven.cli;

import com.evolven.command.Command;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@CommandLine.Command(name = "logout", header = "Invalidate Evolven credentials")
public class EvolvenCommandLogout extends EvolvenCommand implements Runnable {

    public EvolvenCommandLogout(Command command) {
        super(command);
    }

    @CommandLine.Option(names = {"-h", "--help"}, description = "Show help")
    boolean help;

    @Spec
    CommandSpec spec;

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        System.out.println(help);
    }
}
