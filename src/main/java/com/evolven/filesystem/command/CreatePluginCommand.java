package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.filesystem.FileSystemManager;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class CreatePluginCommand extends Command {
    public static final String DEFAULT_VERSION = "2.1.1";
//    public static final String FLAG_ALLOW_NON_EMPTY_FOLDER = "allowNonEmptyFolder";

    public CreatePluginCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);
    
        registerOptions(
                OPTION_NAME,
                OPTION_VERSION,
		        OPTION_COLLECTION,
		        OPTION_POLICY,
		        OPTION_DASHBOARD,
		        OPTION_OUTPUT
        );
    
//        registerFlag(FLAG_ALLOW_NON_EMPTY_FOLDER);
    }

    @Override
    public void execute() throws CommandException {
        String name = options.get(OPTION_NAME);
        if (StringUtils.isNullOrBlank(name)) {
            throw new CommandException("No plugin name value (use \"name\" option).");
        }
        String output = options.get(OPTION_OUTPUT);
        if (StringUtils.isNullOrBlank(output)) {
            throw new CommandException("No plugin output value (use \"output\" option).");
        }
        File outputDirectory = new File(output);
        if (outputDirectory.exists()) {
            if (!outputDirectory.isDirectory()) {
                throw new CommandException("Output destination must be a directory.");
            }
            File[] files = outputDirectory.listFiles();
            if (files != null && files.length > 0) {
                throw new CommandException("Output directory must be empty.");
            }
        }
        String version = options.get(OPTION_VERSION);
        if (StringUtils.isNullOrBlank(version)) {
            version = DEFAULT_VERSION;
        }
        String collection = options.get(OPTION_COLLECTION);
        File collectionFile;
        if (StringUtils.isNullOrBlank(collection)) {
            collectionFile = null;
        } else {
            collectionFile = new File(collection);
            if (!collectionFile.exists()) {
                throw new CommandException("Collection destination doesn't exist");
            } else if (!collectionFile.isFile()) {
                throw new CommandException("Collection destination must be a file");
            }
        }
        String dashboard = options.get(OPTION_DASHBOARD);
        File dashboardFile;
        if (StringUtils.isNullOrBlank(dashboard)) {
            dashboardFile = null;
        } else {
            dashboardFile = new File(dashboard);
            if (!dashboardFile.exists()) {
                throw new CommandException("Dashboard destination doesn't exist");
            } else if (!dashboardFile.isFile()) {
                throw new CommandException("Dashboard destination must be a file");
            }
        }
        String policy = options.get(OPTION_POLICY);
        File policyFile;
        if (StringUtils.isNullOrBlank(policy)) {
            policyFile = null;
        } else {
            policyFile = new File(policy);
            if (!policyFile.exists()) {
                throw new CommandException("Policy destination doesn't exist");
            } else if (!policyFile.isFile()) {
                throw new CommandException("Policy destination must be a file");
            }
        }
        if (!outputDirectory.exists() && !outputDirectory.mkdir()) {
            throw new CommandException("Couldn't create output directory");
        }
        File pluginFile = new File(outputDirectory, "plugin.yml");
        List<String> lines = Arrays.asList("name: \"" + name + "\"", "version: \"" + version + "\"");
        try {
            if (!pluginFile.createNewFile()) {
                throw new Exception();
            }
            Files.write(pluginFile.toPath(), lines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CommandException("Couldn't create plugin.yml file in output directory");
        }
        File collectionDirectory = new File(outputDirectory, "collections");
        if (!collectionDirectory.mkdir()) {
            throw new CommandException("Couldn't create collection sub-directory in output directory");
        }
        File dashboardDirectory = new File(outputDirectory, "dashboards");
        if (!dashboardDirectory.mkdir()) {
            throw new CommandException("Couldn't create dashboard sub-directory in output directory");
        }
        File policyDirectory = new File(outputDirectory, "policies");
        if (!policyDirectory.mkdir()) {
            throw new CommandException("Couldn't create policy sub-directory in output directory");
        }
        if (collectionFile != null) {
            try {
                Files.copy(collectionFile.toPath(), new File(collectionDirectory, collectionFile.getName()).toPath());
            } catch (Exception e) {
                throw new CommandException("Couldn't copy collection file to output directory");
            }
        }
        if (dashboardFile != null) {
            try {
                Files.copy(dashboardFile.toPath(), new File(dashboardDirectory, dashboardFile.getName()).toPath());
            } catch (Exception e) {
                throw new CommandException("Couldn't copy dashboard file to output directory");
            }
        }
        if (policyFile != null) {
            try {
                Files.copy(policyFile.toPath(), new File(policyDirectory, policyFile.getName()).toPath());
            } catch (Exception e) {
                throw new CommandException("Couldn't copy policy file to output directory");
            }
        }
        System.out.print("Plugin \"" + name + "\" was created successfully!");
    }

    @Override
    public String getName() {
        return "create";
    }
}
