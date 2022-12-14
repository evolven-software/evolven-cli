package com.evolven.cli;

import picocli.CommandLine;
import java.io.PrintStream;

public class EvolvenCommandsGroup extends EvolvenCommand implements Runnable {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @Override
    public void run() {
        PrintStream ps = System.err;
        if (help) {
            ps = System.out;
        }
        spec.commandLine().usage(ps);
    }
}
