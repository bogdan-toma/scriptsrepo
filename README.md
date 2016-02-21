# ScriptsRepo: Automatic deploy tool for SAP Sourcing scripts

ScriptsRepo is a tool that allows developers to automatically deploy BeanShell
scripts from a locally managed repository to a SAP Sourcing application server.

## Latest release
The most recent release is ScriptsRepo [0.4-beta].

[0.4-beta]:https://www.google.com

## Installing ScriptsRepo
Download latest release jar or build from [source].
## Running ScriptsRepo
### Import
```sh
java -jar scriptsrepo-0.4-beta.jar --import
```
- Initialise resources 
- Generate [config.properties](#configuration) file *(only if not existent)*.
- Import/Update relevant list of script definitions from **`allscripts.oma`** file.

###Deploy
```sh
java -jar scriptsrepo-0.4-beta.jar --deploy
```
- Generate import metadata
- Push data files and metadata to configured directory via deploy channel

### Configuration
The [import](#import) action will generate a `config.properties` file in the same directory as the jar. The parameters and possible options are:
- **REPOSITORY_TYPE** - *type of source control management repository*
    - `GIT` - 
    - `LOCAL` - *if files are just stored locally without any SCM*
- **REPOSITORY_DIR** - *absolute path to location of script files*
- **REPOSITORY_FILE_ID** - *must be a metadata of the script definition; this is used to link the script with the local file. eg: `EXTERNAL_ID`, where local file is named `EXTERNAL_ID`**.**`DATA_FILE_EXTENSION`*
- **DATA_FILE_EXTENSION** - *extension used for script files. eg `.java`*
- **ESO_DATA_DIR** - *remote directory where to publish script data files*
- **ESO_UPLOAD_DIR** - *remote directory where to publish script import medatada xml*
- **TRANSPORT_PROTOCOL** - *transport protocol for scripts deployment to app server*
    - `SSH`
    - `DUMMY` - *test protocol, displays resulting metadata xml to console*
- **HOST** - *SSH host - irrelevant when `TRANSPORT_PROTOCOL` is `DUMMY`*
- **PORT** - *SSH port*
- **USER** - *SSH username*
- **PASS** - *SSH password*


[0.4-beta]:https://sourceforge.net/projects/scriptsrepo/
[source]:https://github.com/bogdan-toma/scriptsrepo
