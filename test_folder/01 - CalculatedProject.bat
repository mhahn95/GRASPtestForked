@ECHO OFF
CALL 00_setVariables.bat
ECHO formatted result is written into "output.txt" file
ECHO importable result is written into "01 - CalculatedProject.csv" file
IF %compile%=="YES" ECHO compiling...
IF %compile%=="YES" javac ConsortialPartner.java
IF %compile%=="YES" javac MatrixCreator.java
IF %compile%=="YES" javac Partner.java
IF %compile%=="YES" javac Person.java
IF %compile%=="YES" javac Project.java
IF %compile%=="YES" javac Workpackage.java
IF %compile%=="YES" javac ProjectConfigData.java
IF %compile%=="YES" javac IOProject.java
java MatrixCreator >> output.txt
@ECHO ON