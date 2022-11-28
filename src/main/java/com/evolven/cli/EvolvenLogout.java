package com.evolven.cli;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@CommandLine.Command(name = "logout", header = "Invalidate Evolven credentials")
class EvolvenLogout extends EvolvenCommand implements Runnable {

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
