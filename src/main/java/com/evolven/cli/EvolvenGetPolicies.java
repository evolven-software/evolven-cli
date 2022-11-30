package com.evolven.cli;

import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

import java.io.File;

// defines some commands to show in the list (option/parameters fields omitted for this demo)
@CommandLine.Command(name = "get-policies", header = "Download all th Evolven policies.")
class EvolvenGetPolicies extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-o", "--output"}, description = "The output file/directory. The location will be created.")
    File files;

    @CommandLine.Option(names = {"-s", "--single-file"}, description = "Output to a single file.")
    boolean singleFile;

    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the file if exists.")
    boolean force;

    @CommandLine.Option(names = {"-F", "--format"}, description = "The output format." +
            " Options: \n\t1. Row (JSON). \n\t2. Yaml.\n\t3) Key-value pairs")
    OutputFormat outputFormat = OutputFormat.JSON;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            //addOption(url, this);
            execute();

        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    enum OutputFormat {
        JSON,
        YAML,
        KV //TODO is required?
    }
}
