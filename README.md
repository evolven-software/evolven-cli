# evolven-cli


## Build from the source
### prerequisites
* java (version >= 8)
* gradle (tested with version 7.4)

### Build
```bash
# In the root directory run the command
./gradlew build
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
destination_directory=~/ # or any other directory
cp -r evolven-cli ${destination_directory}
cmd="export PATH=\${PATH}:${destination_directory}/bin"
echo ${cmd} >> ~/.bashrc
eval ${cmd}
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
(On linux systems replace use evolven.sh command instead evolven.bat)
```bash
# login
evolven.bat login -H host13.evolven.com -p PASS -u evolven -e test
# download a policy from the Evolven server
evolven.bat policy pull -n "@Access"
# edit the policy in a text editor
# ...
# test the policy on hosts that match a search query
evolven.bat policy test -f .\evolven-policies\_Access.yaml -c "host:sergey"
# test the policy on hosts that match a the scope that is defined in the policy
evolven.bat policy test -f .\evolven-policies\_Access.yaml -s
# test the policy on hosts that match a the scope that is defined in the policy and the search criteria
evolven.bat policy test -f .\evolven-policies\_Access.yaml -s -c "host:sergey"
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

### Evolven CLI configuration
The configuration file for the cli is located at "~/.evolven-cli/policy-config.yaml" usually.

* Configure the editable fields (list) with the "editable-fields" property. 
* Configure groupings of the fields (fields that are separated by a comment) with the "groupings" property.
* Configure the printing of the original policy with "append-original-policy-as-comment" property.
* Configure the fields the output of readonly fields with "skip-readonly".

#### Evolven CLI logs
The logs are located at "~/.evolven-cli/log/" usually.

## For developers
### Create a zip archive with the program
```bash
# In the root directory run the command
gradlew archive
```
### Deploy
```bash
# In the root directory run the command
git tag -a vMAJOR.MINOR.PATH -m "RELEASE MASSAGE"
git push origin vMAJOR.MINOR.PATH
```
The binaries will be published under https://github.com/evolven-software/evolven-cli/releases.

