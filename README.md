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
### login to the Evolven server
evolven.bat login -H host13.evolven.com -p PASS -u evolven -e test
evolven.bat login -H host13.evolven.com -p PASS -u evolven -e test1

### Set the active environment (the environment the following commands with work with)
evolven.bat config set -a test

### Pull all the Evolven policies
evolven.bat policy pull -o evolven-policies
#### Pull specific Evolven policy by name
evolven.bat policy pull -n "@Access"

### Test Evolven policy on the Evolven environments that match the criterion
evolven.bat policy test -f .\evolven-policies\_Access.yaml -c "host:sergey"

#### Test Evolven policy on all the Evolven environments (takes time)
evolven.bat policy test -f .\evolven-policies\_Access.yaml

#### Push a specific Evolven policy
evolven.bat policy push -f .\evolven-policies\Taras-test-AAAS__Connectivity_Check___Windows__WinRM-Enabled.yaml

