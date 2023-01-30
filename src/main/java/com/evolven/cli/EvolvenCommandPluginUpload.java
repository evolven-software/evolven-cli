package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = "plugin-upload", header = "Upload an Evolven plugin")
public class EvolvenCommandPluginUpload extends EvolvenCommand {

    @CommandLine.Option(names = {"-p", "--plugin-path"}, required = true, description = "Path to a plugin directory.")
    protected String path;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    public EvolvenCommandPluginUpload(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(path, this);
        invokeHandler();
    }
}
