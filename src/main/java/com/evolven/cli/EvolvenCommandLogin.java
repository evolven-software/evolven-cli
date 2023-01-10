package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@CommandLine.Command(
        name = EvolvenCommandLogin.COMMAND_NAME,
        header = "Login and create session key",
        footer = "%nExample:%n" + EvolvenCommandLogin.COMMAND_EXAMPLE
)
public class EvolvenCommandLogin extends EvolvenCommand {

    public static final String COMMAND_NAME = "login";
    public static final String COMMAND_EXAMPLE = "evolven " + COMMAND_NAME + " -H <host> -p <password> -u <username> -e <label>";

    @CommandLine.Option(names = {"-U", "--url"}, description = "The full url of the server.")
    String url;

    @CommandLine.Option(names = {"-u", "--username"}, description = "Username.")
    String username;

    @CommandLine.Option(names = {"-H", "--host"}, description = "Host.")
    String host;

    @CommandLine.Option(names = {"-p", "--password"}, required = true, description = "Password.")
    String password;
    @CommandLine.Option(names = {"-s", "--schema"}, defaultValue = "https", description = "The url schema (ex.: https).")
    String schema;

    @CommandLine.Option(names = {"-e", "--env"}, defaultValue = "test", description = "Environment label.")
    String env;

    @CommandLine.Option(names = {"-P", "--port"}, description = "Password.")
    Short port;

    @CommandLine.Option(names = {"-C", "--skip-caching"}, description = "Skip caching data.")
    Boolean skipCache;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @Spec
    CommandSpec spec;

    public EvolvenCommandLogin(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(password, this);
        addOption(url, this);
        addOption(port, this);
        addOption(username, this);
        addOption(host, this);
        addOption(schema, this);
        addOption(env, this);
        addFlag(skipCache, this);
        invokeHandler();
    }
}
