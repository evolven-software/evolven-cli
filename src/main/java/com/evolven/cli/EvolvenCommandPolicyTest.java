package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = "test", header = "Upload an Evolven policy")
public class EvolvenCommandPolicyTest extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-f", "--policy-file"}, required = true, description = "Path to a policy file.")
    String filename;

    @CommandLine.Option(names = {"-c", "--criterion"}, required = true, description = "The policy will be tested on the environments that answer this search criterion.")
    String query;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    public EvolvenCommandPolicyTest(Command command) {
        super(command);
    }

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            addOption(filename, this);
            addOption(query, this);
            execute();
        } catch (CommandException e) {
            throw new EvolvenCommandException(e);
        }
    }
}
