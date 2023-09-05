package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandPluginUpload.COMMAND_NAME, header = "Upload an Evolven plugin")
public class EvolvenCommandPluginUpload extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "plugin-upload";

    @CommandLine.Option(names = {"-p", "--plugin-path"}, required = true, description = "Path to a plugin directory.")
    protected String path;

    public EvolvenCommandPluginUpload(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(path, this);
    }
}
