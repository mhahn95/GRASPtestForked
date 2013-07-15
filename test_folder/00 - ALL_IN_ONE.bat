@ECHO OFF
CALL 00_setVariables.bat
IF %compile%=="YES" ECHO compiling...
IF %compile%=="YES" javac ConsortialPartner.java
IF %compile%=="YES" javac MatrixCreator.java
IF %compile%=="YES" javac Partner.java
IF %compile%=="YES" javac Person.java
IF %compile%=="YES" javac Project.java
IF %compile%=="YES" javac Workpackage.java
IF %compile%=="YES" javac ProjectConfigData.java
IF %compile%=="YES" javac IOProject.java
IF %compile%=="YES" javac KeyPersonnel.java
IF %compile%=="YES" javac EffortLevel.java
IF %compile%=="YES" javac WPSummaryData.java
IF %compile%=="YES" javac WPSummaryTable.java
IF %compile%=="YES" javac PartnerSummaryData.java
IF %compile%=="YES" javac PartnerSummaryTable.java
IF %compile%=="YES" javac BudgetPerCountry.java
IF %compile%=="YES" javac Milestone.java
IF %compile%=="YES" javac MilestoneTable.java
IF %compile%=="YES" javac Meeting.java
IF %compile%=="YES" javac MeetingTable.java
IF %compile%=="YES" javac Task.java
IF %compile%=="YES" javac TaskTable.java
java MatrixCreator >> output.txt
java KeyPersonnel
java EffortLevel
java WPSummaryTable
java PartnerSummaryTable
java BudgetPerCountry
java MilestoneTable
java MeetingTable
java TaskTable
@ECHO ON