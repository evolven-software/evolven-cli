# evolven-cli

## Installation
1. In the root project directory run: 
```bash
.\gradlew.bat build
```
2. (Optional) copy the ./build/output directory to a desired location
```bash
cp -r ./build/output ~/evolven-cli
```
2.1 Invoke the program by: 
```bash
~/evolven-cli/bin/evolven.bat --help
```
3. Invoke the program form the building location:
```bash
cd ./build/output
./evolven.bat --help
```

## Typical workflow
```bash
# login
evolven.bat login -H host13.evolven.com -p PASS -u evolven -e test
# download a policy from the Evolven server
evolven.bat policy pull -n "@Access"
# edit a policy in a text editor
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

##### Set the active environment (the environment the following commands with work with)
```bash
evolven.bat config set -a test
```

##### Get the current active environment (the environment the following commands with work with)
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



