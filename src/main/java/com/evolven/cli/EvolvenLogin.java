package com.evolven.cli;

import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
@CommandLine.Command(name = "login", header = "Login and create session key")
class EvolvenLogin extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-U", "--url"}, required = true, description = "The full url of the server.")
    String serverUrl;

    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Username.")
    String user;

    @CommandLine.Option(names = {"-p", "--password"}, required = true, description = "Password.")
    String password;

    @CommandLine.Option(names = {"-H", "--host"}, required = true, description = "Password.")
    String host;

    @CommandLine.Option(names = {"-P", "--port"}, required = true, description = "Password.")
    Short port;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @CommandLine.Option(names = {"-t", "--timeout"}, defaultValue = "150", description = "The session timeout in seconds (max = 150).")
    int timeout;

    @Spec
    CommandSpec spec;

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        System.out.println(user + ":" + password + "@" + serverUrl);
    }
}
