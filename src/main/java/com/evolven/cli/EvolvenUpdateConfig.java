package com.evolven.cli;

import com.evolven.command.CommandException;
import com.evolven.command.InternalInvalidParameterException;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = "update-config", header = "Creates configuration files in the current working directory " +
        "(use it if other methods filed and the the program assumed to be executed from the current location always")
public class EvolvenUpdateConfig extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the config directory if exists.")
    Boolean force;

    @Override
    public void run() {
        try {
            addFlag(force, this);
            execute();
        } catch (InternalInvalidParameterException e) {
            System.err.println("Internal error: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }
}
