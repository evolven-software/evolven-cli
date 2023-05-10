package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = "plugin-create", header = "Create an Evolven plugin")
public class EvolvenCommandPluginCreate extends EvolvenCommand {
    
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

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    public EvolvenCommandPluginCreate(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(name, this);
        addOption(output, this);
        addOption(version, this);
        addOption(collection, this);
        addOption(dashboard, this);
        addOption(policy, this);
        invokeHandler();
    }
}
