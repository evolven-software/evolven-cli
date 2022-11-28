package com.evolven.cli;

import picocli.CommandLine;

import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.command.InternalInvalidParameterException;

@CommandLine.Command(name = "create-config", header = "Creates configuration files in the current working directory " +
        "(use it if other methods filed and the the program assumed to be executed from the current location always")
public class EvolvenCreateConfig extends EvolvenCommand implements Runnable {


    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the config directory if exists.")
    Boolean force = false;

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
        } catch(CommandException e) {
            throw new RuntimeException(e);
        }
    }
}
