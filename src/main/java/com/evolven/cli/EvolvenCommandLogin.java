package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
@CommandLine.Command(name = "login", header = "Login and create session key")
public class EvolvenCommandLogin extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-U", "--url"}, description = "The full url of the server.")
    String url;

    @CommandLine.Option(names = {"-u", "--username"}, description = "Username.")
    String username;

    @CommandLine.Option(names = {"-p", "--password"}, /* required = true,*/ description = "Password.")
    String password;

    @CommandLine.Option(names = {"-H", "--host"}, description = "Password.")
    String host;

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
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            addOption(url, this);
            addOption(port, this);
            addOption(username, this);
            addOption(password, this);
            addOption(host, this);
            addOption(env, this);
            addFlag(skipCache, this);
            execute();

        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }
}
