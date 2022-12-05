package com.evolven.filesystem;

import com.evolven.policy.PolicyConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileSystemManager {

    //TODO remove it from here to CommandLine
    final static int DEFAULT_API_KEY_TIMEOUT = 150;
    final static String CONFIG_DIRECTORY_NAME = ".evolven-cli";
    final static String CONFIG_NAME = "config.yaml";

    final static String POLICY_CONFIG_NAME = "policy.yaml";
    public final File configDirectory;
    public final File configFile;

    public FileSystemManager() throws IOException {
        configDirectory = getCreateConfigDirectory();
        configFile = new File(configDirectory, CONFIG_NAME);
    }

    public EvolvenCliConfig getConfig() {
        return new EvolvenCliConfig(configFile);
    }

    File getCreateConfigDirectoryAtUserHone() {
        File userDirectory = FileUtils.getUserDirectory();
        if (!userDirectory.exists() || !userDirectory.isDirectory()) return null;
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
        if (configDirectory.exists() && configDirectory.isDirectory() && testConfigDirectory(configDirectory)) {
            return configDirectory;
        }
        configDirectory = getCreateConfigDirectoryAtUserHone();
        if (configDirectory == null) {
            configDirectory = getCreateConfigDirectoryAtJarLocation();
        }
        if (configDirectory != null && testConfigDirectory(configDirectory)) {
            getPolicyConfig().createInitialConfigs();
            return configDirectory;
        }
        return null ;
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
        System.out.println("creating config dir at: " + configDirectoryParent.getAbsolutePath());
        if (!configDirectoryParent.exists()) {
            if (!configDirectoryParent.mkdirs()) {
                System.out.println("failed to create config dir parent at: " + configDirectoryParent.getAbsolutePath());
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


    public File createInitialPolicyConfig(String data) throws IOException {
        return createNewFile(data, POLICY_CONFIG_NAME);
    }

    public PolicyConfig getPolicyConfig() {
        return new PolicyConfig(this);
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
