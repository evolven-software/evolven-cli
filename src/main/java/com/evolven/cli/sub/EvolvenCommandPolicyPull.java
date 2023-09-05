package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = EvolvenCommandPolicyPull.COMMAND_NAME, header = "Download Evolven policy.")
public class EvolvenCommandPolicyPull extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "pull";

    @CommandLine.Option(names = {"-o", "--output-directory"}, defaultValue = "evolven-policies", description = "The output file/directory. The location will be created.")
    protected File output;

    @CommandLine.Option(names = {"-n", "--policy-name"}, required = false, description = "Policy name")
    protected String name;

    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the file if exists.")
    protected Boolean force;

    @CommandLine.Option(names = {"-a", "--show-readonly-fields"}, description = "Reflect all (readonly and writable) fields.")
    protected Boolean all;

    @CommandLine.Option(names = {"-c", "--show-full-as-comment"}, description = "Append the original policy as a comment.")
    protected Boolean comment;
    public EvolvenCommandPolicyPull(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(output, this);
        addOption(name, this);
        addFlag(all, this);
        addFlag(comment, this);
        addFlag(force, this);
    }
}
