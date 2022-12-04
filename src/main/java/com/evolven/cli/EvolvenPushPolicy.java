package com.evolven.cli;

import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = "push-policy", header = "Upload an Evolven policy")
public class EvolvenPushPolicy extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-f", "--policy-file"}, required = true, description = "Path to a policy file.")
    String filename;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            System.out.printf("filename: " + filename);
            addOption(filename, this);
            execute();
        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }
}
