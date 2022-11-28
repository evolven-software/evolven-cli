package com.evolven.cli;

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
    OutputFormat outputFormat = OutputFormat.ROW;

    @Override
    public void run() {
    }

    enum OutputFormat {
        ROW,
        YAML,
        KV //TODO is required?
    }
}
