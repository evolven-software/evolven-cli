package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandPolicyTest.COMMAND_NAME, header = "Upload an Evolven policy")
public class EvolvenCommandPolicyTest extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "test";

    @CommandLine.Option(names = {"-f", "--policy-file"}, required = true, description = "Path to a policy file.")
    protected String filename;

    @CommandLine.Option(names = {"-c", "--criterion"},
            description = "The policy will be tested on the environments that answer this search criterion.")
    protected String query;

    @CommandLine.Option(names = {"-s", "--use-policy-scope"}, description = "Use scope from the policy. " +
            "In such a case the \"--cretirion\" parameter will define the sub-group of the scope defined in the policy")
    protected Boolean scope;

    public EvolvenCommandPolicyTest(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(filename, this);
        addOption(query, this);
        addFlag(scope, this);
    }
}
