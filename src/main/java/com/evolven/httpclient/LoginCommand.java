package com.evolven.httpclient;

import com.evolven.command.Command;
import com.evolven.command.CommandException;

public class LoginCommand extends Command {
    @Override
    public void execute() throws CommandException {

    }

    @Override
    public String getName() {
        return "login";
    }
}
