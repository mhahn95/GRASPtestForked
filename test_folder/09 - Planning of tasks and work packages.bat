@ECHO OFF
CALL 00_setVariables.bat
ECHO result is written into "09 - Planning of tasks and work packages.csv" file
IF %compile%=="YES" ECHO compiling...
IF %compile%=="YES" javac ConsortialPartner.java
IF %compile%=="YES" javac MatrixCreator.java
IF %compile%=="YES" javac Partner.java
IF %compile%=="YES" javac Person.java
IF %compile%=="YES" javac Project.java
IF %compile%=="YES" javac Workpackage.java
IF %compile%=="YES" javac ProjectConfigData.java
IF %compile%=="YES" javac IOProject.java
IF %compile%=="YES" javac Meeting.java
IF %compile%=="YES" javac MeetingTable.java
IF %compile%=="YES" javac WPSummaryData.java
IF %compile%=="YES" javac WPSummaryTable.java
IF %compile%=="YES" javac Task.java
IF %compile%=="YES" javac TaskTable.java
java TaskTable
@ECHO ON