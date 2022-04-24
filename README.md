# JavaFx-Jdbc-Selenium-POI-AppForTerrazza


Preparação do Eclipse

ATENÇÃO: Eclipse 4.9 ou superior: http://www.eclipse.org/downloads/packages/

Checklist:

- Baixar o JavaFX SDK: https://gluonhq.com/products/javafx/
o Salvar em uma pasta "fácil", de preferência com nome sem espaços
- Exemplo: C:\java-libs

- Instalar o plug-in E(fx)clipse no Eclipse (ATENÇÃO: versão 3.4.1 ou superior)
o Help -> Install new Software
o Work with: escolha o correspondente à versão do seu Eclipse 
- Exemplo: 2018-09 - http://download.eclipse.org/releases/2018-09
- Exemplo de link direto: http://download.eclipse.org/efxclipse/updates-released/3.4.1/site/
o Localize e(fx)clipse
o Next Next etc.
o Reinicie o Eclipse

- Referenciar o SceneBuilder no Eclipse:
o Window -> Preferences -> JavaFX
o Entrar o caminho completo do arquivo executável do Scene Builder
- Exemplo: C:\Users\Nelio\AppData\Local\SceneBuilder\SceneBuilder.exe

- Criar uma User Library no Eclipse com o nome de JavaFX: 
o Window -> Preferences -> Java -> Build Path -> User Libraries -> New
o Dê o nome de User Library
o Add External Jars (aponte para a pasta bin do JavaFX)


Criando um novo projeto JavaFX no Eclipse

Checklist:

- Criação do projeto:
o File -> New -> Other -> JavaFX Project
o Dê um nome ao projeto e clique Next
o Na aba Libraries, selecione Modulepath, clique Add Library, e selecione JavaFX
o Clique Finish
o Module Info: Don't Create

- Configuração do build:
o Botão direito no projeto -> Run As -> Run Configurations -> Arguments -> VM Arguments
o Copiar o conteúdo abaixo, adaptando para sua pasta:
--module-path C:\java-libs\javafx-sdk\lib --add-modules=javafx.fxml,javafx.controls



DEPLOY

Building JAR file 

Checklist: 

- Build JAR file 
o Right click project name -> Export -> Java/Runnable JAR file -> Next 
o Select Main class 
o Select destination folder 
o Library handling: 3rd option

- Pack files 
o JAR file 
o db.properties 
o MySQL Connector 
o JavaFX SDK 
o Java SDK 


Instalation 

Checklist: 

- Install Java: https://www.oracle.com/technetwork/java/javase/downloads/index.html
o Setup JAVA_HOME (ex: C:\Program Files\Java\jdk-11.0.3) 
- Copy JavaFX 
o Setup PATH_TO_FX (ex: C:\java-libs\javafx-sdk\lib) 
o Place MySQL Connector in lib folder 
- Copy JA

Run app: 
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp myapp.jar 
application.Main 

Bat File (optional) 

java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp myapp.jar 
application.Main 

Windows Shortcut (optional) 

Target: 
"C:\Program Files\Java\jdk-11.0.3\bin\java.exe" -Dfile.encoding=utf-8 --module-path %PATH_TO_FX% --add-modules 
javafx.controls,javafx.fxml -cp myapp.jar application.Main 

Start in: 
C:\appfolde


CHANGING PROJECT TO OPEN AND RUN IN VSCODE

- Just delete some folders and files: ".settings", ".classpath" and ".project". These files are exclusively used by Eclipse and STS.
- After it, add the jar files to the referenced libraries in the project side bar. To do that, create a folder in the root of project called ".vscode", if it doesn't exists.
Inside this folder it is needed to be created a file named "settings.json". This file will set the path to the libraries needed. It'll must like this:

```
{
    "java.project.referencedLibraries": [
        "lib/**/*.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx.base.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx.controls.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx.fxml.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx.graphics.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx.media.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx.swing.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx.web.jar",
        "c:\\java-libs\\javafx-sdk\\lib\\javafx-swt.jar",
        "res/controlsfx-11.1.1.jar",
        "res/gson-2.8.5.jar",
        "res/POI/commons-codec-1.15.jar",
        "res/POI/commons-collections4-4.4.jar",
        "res/POI/commons-compress-1.21.jar",
        "res/POI/commons-io-2.11.0.jar",
        "res/POI/commons-logging-1.2.jar",
        "res/POI/commons-math3-3.6.1.jar",
        "res/POI/curvesapi-1.06.jar",
        "res/POI/log4j-api-2.14.1.jar",
        "res/POI/log4j-core-2.14.1.jar",
        "res/POI/poi-5.1.0.jar",
        "res/POI/poi-examples-5.1.0.jar",
        "res/POI/poi-excelant-5.1.0.jar",
        "res/POI/poi-javadoc-5.1.0.jar",
        "res/POI/poi-ooxml-5.1.0.jar",
        "res/POI/poi-ooxml-full-5.1.0.jar",
        "res/POI/poi-ooxml-lite-5.1.0.jar",
        "res/POI/poi-scratchpad-5.1.0.jar",
        "res/POI/slf4j-api-1.7.32.jar",
        "res/POI/SparseBitSet-1.2.jar",
        "res/POI/xmlbeans-5.0.2.jar",
        "res/HttpClient4.5/commons-codec-1.11.jar",
        "res/HttpClient4.5/commons-logging-1.2.jar",
        "res/HttpClient4.5/fluent-hc-4.5.13.jar",
        "res/HttpClient4.5/httpclient-4.5.13.jar",
        "res/HttpClient4.5/httpclient-cache-4.5.13.jar",
        "res/HttpClient4.5/httpclient-osgi-4.5.13.jar",
        "res/HttpClient4.5/httpclient-win-4.5.13.jar",
        "res/HttpClient4.5/httpcore-4.4.13.jar",
        "res/HttpClient4.5/httpmime-4.5.13.jar",
        "res/HttpClient4.5/jna-4.5.2.jar",
        "res/HttpClient4.5/jna-platform-4.5.2.jar",
        "c:\\java-libs\\jdbc-connectors\\mysql-connector-java-8.0.26.jar"
    ]
}
```

IMPORTANT! There is a folder "lib" in the root, all of those libs will be automatically referenced to project. Due to line "lib/**/*.jar", in "settings.json".
If you need to add more libs just add it like example above. Or, do it pressing the "plus" button on IDE VSCode inthe Project Sidebar.
OBS.: The "res" is a folder in the root of the project. VsCode build it automatically with "/". Other folders are automatically builded with "\\".

- When you try to run the project VsCode will ask to create the "launch.json" file inside ".vscode" folder.
- If it won't create the "launch.json" file automatically you can create it like this:

```
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch Current File",
            "request": "launch",
            "mainClass": "${file}"
        },
        {
            "type": "java",
            "name": "Launch Main",
            "request": "launch",
            "mainClass": "application.Main",
            "projectName": "ClientSide_4f407b8",
            "vmArgs": "--module-path PATH/TO/FX --add-modules javafx.controls,javafx.fxml"

        }
    ]
}
```

- IMPORTANT! When vscode creates the "launch.json" automatically the "vmArgs" line won't be created and you'll get an error. Just add it to the file and run it again.

- IMPORTANT 2! VSCODE accept the "/" to build the path between directories even in Windows. 

- IMPORTANT 3! It seems to have some difference between write the code to add modules with and without the "=":
--module-path C:\java-libs\javafx-sdk\lib --add-modules=javafx.fxml,javafx.controls
--module-path C:\java-libs\javafx-sdk\lib --add-modules javafx.fxml,javafx.controls
Eclipse and Spring Boot Suite 4 accepts only with the "=" and VSCode accept both.
