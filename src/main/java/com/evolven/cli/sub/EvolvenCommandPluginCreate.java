package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandBase;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandPluginCreate.COMMAND_NAME, header = "Create an Evolven plugin")
public class EvolvenCommandPluginCreate extends EvolvenCommandBase {

    public static final String COMMAND_NAME = "plugin-create";
    
    @CommandLine.Option(names = {"-n", "--name"}, required = true, description = "Name of the plugin.")
    protected String name;
    
    @CommandLine.Option(names = {"-o", "--output"}, required = true, description = "Path to the output directory (will be create if doesn't exist, otherwise must be empty).")
    protected String output;
    
    @CommandLine.Option(names = {"-v", "--version"}, description = "The plugin's version (default: 2.1.1).")
    protected String version;
    
    @CommandLine.Option(names = {"-c", "--collection"}, description = "Path to the collection file.")
    protected String collection;
    
    @CommandLine.Option(names = {"-d", "--dashboard"}, description = "Path to the dashboard file.")
    protected String dashboard;
    
    @CommandLine.Option(names = {"-p", "--policy"}, description = "Path to the policy file.")
    protected String policy;

    public EvolvenCommandPluginCreate(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(name, this);
        addOption(output, this);
        addOption(version, this);
        addOption(collection, this);
        addOption(dashboard, this);
        addOption(policy, this);
    }
}
