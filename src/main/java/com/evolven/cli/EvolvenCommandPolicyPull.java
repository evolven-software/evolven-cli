package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.common.PolicyFormat;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(name = "pull", header = "Download Evolven policy.")
public class EvolvenCommandPolicyPull extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-o", "--output-directory"}, defaultValue = "evolven-policies", description = "The output file/directory. The location will be created.")
    File output;

    @CommandLine.Option(names = {"-n", "--policy-name"}, description = "Policy name")
    String name;

    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the file if exists.")
    boolean force;

    @CommandLine.Option(names = {"-F", "--format"}, defaultValue = "YAML", description = "The output format." +
            " Options: \n\t1. JSON. \n\t2. Yaml.\n\t3) ROW")
    PolicyFormat format;

    @CommandLine.Option(names = {"-a", "--show-readonly-fields"}, description = "Reflect all (readonly and writable) fields.")
    boolean all;

    @CommandLine.Option(names = {"-c", "--show-full-as-comment"}, description = "Append the original policy as a comment.")
    boolean comment;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    public EvolvenCommandPolicyPull(Command command) {
        super(command);
    }

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            addOption(output, this);
            addOption(format, this);
            addOption(name, this);
            addFlag(all, this);
            addFlag(comment, this);
            addFlag(force, this);
            execute();
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }
}
