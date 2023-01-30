package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = "test", header = "Upload an Evolven policy")
public class EvolvenCommandPolicyTest extends EvolvenCommand {

    @CommandLine.Option(names = {"-f", "--policy-file"}, required = true, description = "Path to a policy file.")
    protected String filename;

    @CommandLine.Option(names = {"-c", "--criterion"},
            description = "The policy will be tested on the environments that answer this search criterion.")
    protected String query;

    @CommandLine.Option(names = {"-s", "--use-policy-scope"}, description = "Use scope from the policy. " +
            "In such a case the \"--cretirion\" parameter will define the sub-group of the scope defined in the policy")
    protected boolean scope;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    public EvolvenCommandPolicyTest(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
            addOption(filename, this);
            addOption(query, this);
            addFlag(scope, this);
            invokeHandler();
    }
}
