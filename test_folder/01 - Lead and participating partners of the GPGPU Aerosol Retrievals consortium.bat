@ECHO OFF
CALL 00_setVariables.bat
ECHO result is written into "01 - Lead and participating partners of the GPGPU Aerosol Retrievals consortium.csv" file
IF %compile%=="YES" ECHO compiling...
IF %compile%=="YES" javac ConsortialPartner.java
IF %compile%=="YES" javac MatrixCreator.java
IF %compile%=="YES" javac Partner.java
IF %compile%=="YES" javac Person.java
IF %compile%=="YES" javac Project.java
IF %compile%=="YES" javac Workpackage.java
IF %compile%=="YES" javac ProjectConfigData.java
IF %compile%=="YES" javac IOProject.java
IF %compile%=="YES" javac PartnerSummaryData.java
IF %compile%=="YES" javac PartnerSummaryTable.java
java PartnerSummaryTable
@ECHO ON