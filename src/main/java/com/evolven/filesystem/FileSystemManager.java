package com.evolven.filesystem;

import com.evolven.logging.LoggerManager;
import com.evolven.policy.PolicyConfigFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

public class FileSystemManager {

    final static String CONFIG_DIRECTORY_NAME = ".evolven-cli";
    final static String CONFIG_NAME = "config.yaml";

    final static String POLICY_CONFIG_NAME = "policy-config.yaml";
    final static String LOGGER_DIRECTORY = "log";
    public final File configDirectory;
    public final File configFile;

    private Logger logger;

    public FileSystemManager() throws IOException {
        configDirectory = getCreateConfigDirectory();
        LoggerManager.createInstance(getCreateLoggerDirectory());
        logger = LoggerManager.getLogger(this);
        configFile = new File(configDirectory, CONFIG_NAME);
        dumpInitialPolicyConfig(false);
    }

    public File getCreateLoggerDirectory() throws IOException {
        File logDir = new File(configDirectory, LOGGER_DIRECTORY);

        if (logDir.exists() && !logDir.isDirectory()) {
            throw new IOException("Logging directory name occupied by a file.");
        }
        if (!logDir.exists()) {
           Files.createDirectories(logDir.toPath());
        }
        return logDir;
    }

    public EvolvenCliConfig getConfig() {
        return new EvolvenCliConfig(configFile);
    }

    File getCreateConfigDirectoryAtUserHone() {
        File userDirectory = FileUtils.getUserDirectory();
        if (!userDirectory.exists() || !userDirectory.isDirectory()) {
            logger.info("File with the name " + userDirectory + " exists (expected user home directory).");
            return null;
        }
        File configDirectory = new File(userDirectory, CONFIG_DIRECTORY_NAME);
        if (!configDirectory.exists()) {
            if (!configDirectory.mkdir()) return null;
        }
        return configDirectory;
    }

    File getCreateConfigDirectoryAtJarLocation() throws IOException {
        File jarDirectory = new File(new File(".").getCanonicalPath());
        File configDirectory = new File(jarDirectory, CONFIG_DIRECTORY_NAME);
        if (!configDirectory.exists()) {
            if (!configDirectory.mkdir()) return null;
        }
        return configDirectory;
    }

    public File getCreateConfigDirectory() throws IOException {
        File configDirectory = new File(new File("").getAbsolutePath(), CONFIG_DIRECTORY_NAME);
        if (configDirectory.exists() && configDirectory.isDirectory()) {
            return configDirectory;
        }
        configDirectory = getCreateConfigDirectoryAtUserHone();
        if (configDirectory == null) {
            configDirectory = getCreateConfigDirectoryAtJarLocation();
        }
        if (configDirectory != null && testConfigDirectory(configDirectory)) {
            return configDirectory;
        }
        return null ;
    }


    public void dumpInitialPolicyConfig(boolean override) throws IOException {
        File policyConfigFile = getPolicyConfigFile();
        if (policyConfigFile.exists() && !override) {
            logger.info("Policy config file exists and was not overridden.");
            return;
        }
        PolicyConfigFactory.dumpInitialConfig(policyConfigFile);
    }

    public void dumpInitialPolicyConfig() throws IOException {
        dumpInitialPolicyConfig(true);
    }

    public File getPolicyConfigFile() {
        return new File(configDirectory, POLICY_CONFIG_NAME);
    }

    private boolean testConfigDirectory(File configDirectory) {
        try {
            File tmpFile = File.createTempFile("evolven-cli", null,configDirectory);
            tmpFile.delete();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String getApiKey() {
        return null;
    }

    public File createConfigDirectory(File configDirectoryParent) {
        return createConfigDirectory(configDirectoryParent, false);
    }

    private File getCWD() {
        return new File(new File("").getAbsolutePath());
    }

    public File createConfigDirectory(boolean override) {
        return createConfigDirectory(getCWD(), override);
    }

    public File createConfigDirectory() {
        return createConfigDirectory(getCWD(), false);
    }

    public File createConfigDirectory(File configDirectoryParent, boolean override) {
        if (!configDirectoryParent.exists()) {
            if (!configDirectoryParent.mkdirs()) {
                System.err.println("failed to create config dir parent at: " + configDirectoryParent.getAbsolutePath());
                //TODO log
                return null;
            }
        } else if (!configDirectoryParent.isDirectory()) {
            //TODO log
            return null;
        }
        File configDirectory = new File(configDirectoryParent, CONFIG_DIRECTORY_NAME);
        if (configDirectory.exists() && override) {
            try {
                FileUtils.deleteDirectory(configDirectory);
            } catch (IOException e) {}
        }
        if (!configDirectory.exists()) {
            if (!configDirectory.mkdir()) return null;
        }
        if (!testConfigDirectory(configDirectory)) {
            //TODO log
            return null;
        }
        return configDirectory;
    }

    public File createNewFile(String data, String filename) throws IOException {
        return createNewFile(data, filename, true);
    }

    public File createNewFile(String data, String filename, boolean force) throws IOException {
        File file = new File(configDirectory, filename);
        if (file.exists()) {
            if (!force) {
                return null;
            }
            if (file.isDirectory()) {
                String msg = "the requested file ";
                try {
                    msg += "(" + file.getCanonicalPath() + ")";
                } catch (IOException e) {
                }
                msg += " is a directory.";
                throw new IOException(msg);
            }
        }
        Files.write(file.toPath(), data.getBytes());
        return file;
    }
}
