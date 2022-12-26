package com.evolven.cli.exception;

import com.evolven.cli.EvolvenCommandLogin;

public class EvolvenCommandExceptionLogin extends EvolvenCommandException {
    public static final String MSG = "You are not authenticated with any Evolven server. " +
            "Use the following command to login: \n" + EvolvenCommandLogin.COMMAND_EXAMPLE;

    public EvolvenCommandExceptionLogin() {
        super(MSG);
    }
}
