package com.evolven.cli.single;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(
        name = EvolvenCommandLogin.COMMAND_NAME,
        header = "Login and create session key",
        footer = "%nExample:%n" + EvolvenCommandLogin.COMMAND_EXAMPLE
)
public class EvolvenCommandLogin extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "login";
    public static final String COMMAND_EXAMPLE = "evolven " + COMMAND_NAME + " -H <host> -p <password> -u <username> -e <label>";

    @CommandLine.Option(names = {"-U", "--url"}, description = "The full url of the server.")
    protected String url;

    @CommandLine.Option(names = {"-u", "--username"}, description = "Username.")
    protected String username;

    @CommandLine.Option(names = {"-H", "--host"}, description = "Host.")
    protected String host;

    @CommandLine.Option(names = {"-p", "--password"}, required = true, description = "Password.")
    protected String password;

    @CommandLine.Option(names = {"-s", "--schema"}, defaultValue = "https", description = "The url schema (ex.: https).")
    protected String schema;

    @CommandLine.Option(names = {"-P", "--port"}, description = "Port.")
    protected Short port;

    @CommandLine.Option(names = {"-C", "--skip-caching"}, description = "Skip caching data.")
    protected Boolean skipCaching;

    public EvolvenCommandLogin(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(password, this);
        addOption(url, this);
        addOption(port, this);
        addOption(username, this);
        addOption(host, this);
        addOption(schema, this);
        addFlag(skipCaching, this);
    }
}
