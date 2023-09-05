package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandPolicyPush.COMMAND_NAME, header = "Upload an Evolven policy")
public class EvolvenCommandPolicyPush extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "push-policy";

    @CommandLine.Option(names = {"-f", "--policy-file"}, required = true, description = "Path to a policy file.")
    protected String filename;

    public EvolvenCommandPolicyPush(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(filename, this);
    }
}
