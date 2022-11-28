package com.evolven.httpclient;

import com.evolven.command.Command;
import com.evolven.command.CommandException;

public class GetPoliciesCommand extends Command {
    @Override
    public void execute() throws CommandException {

    }

    @Override
    public String getName() {
        return "get-policies";
    }
}
