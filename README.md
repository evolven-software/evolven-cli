# evolven-cli

## Build from the source
```bash
# In the root directory run the command
gradle build
```


## Installation
#### Windows
There are two ways to install evolven-cli on a Windows machine: 
1. manually 
2. InstallWindows.ps1
##### Manual installation
1. Copy the evolven-cli directory to a desired location (ex.: C:\Program Files\evolven-cli).
2. Append the bin directory to the path (ex.: append to the Path environment variable the value C:\Program Files\evolven-cli\bin).
##### Installation with the script:
1. Open PowerShell and cd to evolven-cli directory.
2. Execute the InstallWindows.ps1. The script will update the Path environment variable.
```PowerShell
./InstallWindows.ps1
```
3. Use the -AllUsers flag to install for all the users.
```PowerShell
./InstallWindows.ps1 -AllUsers
```

#### Unix like systems
1. Copy evolven-cli directory to a desired location.
2. Update the .bashrc (or similar configuration file for your shell) with the new PATH variable that contains evolven-cli/bin directory. Example:
```bash
cp -r evolven-cli ~/
echo "export PATH=${PATH}:~/evolven-cli/bin" >> ~/.bashrc
```
3. Restart the shell.

## Invoke evolven-cli
1. On Windows machines 
```PowerShell
evolven.bat --help
```
2. On Unix-like machines
```bash
evolven.sh --help
```


## Typical workflow
```bash
# login
evolven.bat login -H host13.evolven.com -p PASS -u evolven -e test
# download a policy from the Evolven server
evolven.bat policy pull -n "@Access"
# edit the policy in a text editor
# ...
# test the policy on a hosts that answers a search query
evolven.bat policy test -f .\evolven-policies\_Access.yaml -c "host:sergey"
# push the policy to the Evolven server
evolven.bat policy push -f .\evolven-policies\_Access.yaml
# Logout from the Evolven server
evolven.bat logout
```

## Evolven CLI commands

### Login to the Evolven server

```bash
evolven.bat login -H host13.evolven.com -p PASS -u evolven -e test
evolven.bat login -H host13.evolven.com -p PASS -u evolven -e test1
```

### logout from the Evolven server (from current active environment)

```bash
evolven.bat logout
```

### Config commands group

##### Set the active environment (the environment the following commands will work with)
```bash
evolven.bat config set -a test
```

##### Get the current active environment (the environment the following commands will work with)
```bash
evolven.bat config get -a
```

##### Set a configuration for an environment
```bash
evolven.bat config set -e test -n username -v admin
```

##### Get a configuration for an environment
```bash
evolven.bat config get -e test -n username
```

### Policy commands group
##### Pull all the Evolven policies
```bash
evolven.bat policy pull -o evolven-policies
```
##### Pull specific Evolven policy by name
```bash
evolven.bat policy pull -n "@Access"
```

##### Test Evolven policy on the Evolven environments that match the criterion
```bash
evolven.bat policy test -f .\evolven-policies\_Access.yaml -c "host:sergey"
```

#### Push a specific Evolven policy
```bash
evolven.bat policy push -f .\evolven-policies\Taras-test-AAAS__Connectivity_Check___Windows__WinRM-Enabled.yaml
```
