# JavaFx-Jdbc-Selenium-POI-AppForTerrazza


Preparação do Eclipse

ATENÇÃO: Eclipse 4.9 ou superior: http://www.eclipse.org/downloads/packages/

Checklist:

 Baixar o JavaFX SDK: https://gluonhq.com/products/javafx/
o Salvar em uma pasta "fácil", de preferência com nome sem espaços
 Exemplo: C:\java-libs

 Instalar o plug-in E(fx)clipse no Eclipse (ATENÇÃO: versão 3.4.1 ou superior)
o Help -> Install new Software
o Work with: escolha o correspondente à versão do seu Eclipse 
 Exemplo: 2018-09 - http://download.eclipse.org/releases/2018-09
 Exemplo de link direto: http://download.eclipse.org/efxclipse/updates-released/3.4.1/site/
o Localize e(fx)clipse
o Next Next etc.
o Reinicie o Eclipse

 Referenciar o SceneBuilder no Eclipse:
o Window -> Preferences -> JavaFX
o Entrar o caminho completo do arquivo executável do Scene Builder
 Exemplo: C:\Users\Nelio\AppData\Local\SceneBuilder\SceneBuilder.exe

 Criar uma User Library no Eclipse com o nome de JavaFX: 
o Window -> Preferences -> Java -> Build Path -> User Libraries -> New
o Dê o nome de User Library
o Add External Jars (aponte para a pasta bin do JavaFX)


Criando um novo projeto JavaFX no Eclipse

Checklist:

 Criação do projeto:
o File -> New -> Other -> JavaFX Project
o Dê um nome ao projeto e clique Next
o Na aba Libraries, selecione Modulepath, clique Add Library, e selecione JavaFX
o Clique Finish
o Module Info: Don't Create

 Configuração do build:
o Botão direito no projeto -> Run As -> Run Configurations -> Arguments -> VM Arguments
o Copiar o conteúdo abaixo, adaptando para sua pasta:
--module-path C:\java-libs\javafx-sdk\lib --add-modules=javafx.fxml,javafx.controls



DEPLOY

Building JAR file 

Checklist: 

 Build JAR file 
o Right click project name -> Export -> Java/Runnable JAR file -> Next 
o Select Main class 
o Select destination folder 
o Library handling: 3rd option

 Pack files 
o JAR file 
o db.properties 
o MySQL Connector 
o JavaFX SDK 
o Java SDK 


Instalation 

Checklist: 

 Install Java: https://www.oracle.com/technetwork/java/javase/downloads/index.html
o Setup JAVA_HOME (ex: C:\Program Files\Java\jdk-11.0.3) 
 Copy JavaFX 
o Setup PATH_TO_FX (ex: C:\java-libs\javafx-sdk\lib) 
o Place MySQL Connector in lib folder 
 Copy JA

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

- Just delete some folders and files: ".settings", ".classpath" and ".project".
- After it, add the jar files to the referenced libraries in the project side bar.
- When you try to run the project VsCode will ask to create the "launch.json" file.
- If it won't create the "launch.json" file automatically you can create it like this:


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

- IMPORTANT! When vscode creates the "launch.json" automatically the "vmArgs" line won't be created and you'll get an error. Just add it to the file and run it again.
