package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = "push-policy", header = "Upload an Evolven policy")
public class EvolvenCommandPolicyPush extends EvolvenCommand {

    @CommandLine.Option(names = {"-f", "--policy-file"}, required = true, description = "Path to a policy file.")
    String filename;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    public EvolvenCommandPolicyPush(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(filename, this);
        invokeHandler();
    }
}
