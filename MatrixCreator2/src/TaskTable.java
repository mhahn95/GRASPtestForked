import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;


public class TaskTable {
	public static void main(String[] args) throws NumberFormatException, IOException, ParseException{
		String dataFile = "09 - Planning of tasks and work packages.properties";
		String importFile = "01 - CalculatedProject.csv";
		String meetingFilename = "13 - The meeting schedule with detailed costs is shown.properties";
		String workpackageFilename = "07 - Summary table of work packages.properties";
		String exportFile = "09 - Planning of tasks and work packages.csv";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<Task> taskData = readTaskData(dataFile, meetingFilename, workpackageFilename, project);
		printTaskData(taskData, exportFile, project);
	}
	
	public static void printTaskData(ArrayList<Task> data, String filename, Project project) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.GERMAN);
		
		writer.write("\"ID\";Task name;Duration [months];Start;Finish\n");
		for(Task task : data){
			writer.write(task.id+";"+task.name);
			if(!task.description.equals(""))
				writer.write(" - "+task.description);
			writer.write(";"+task.duration+";"+df.format(task.start)+";"+df.format(task.finish)+"\n");
		}
		writer.close();
	}
	
	public static ArrayList<Task> readTaskData(String filename, String meetingFilename, String workpackageFilename, Project project) throws IOException, ParseException{
		final Properties properties = new Properties();
		BufferedInputStream stream;
		try{
			stream = new BufferedInputStream(new FileInputStream(filename));
		}
		catch(FileNotFoundException e){
			stream = new BufferedInputStream(new FileInputStream("data.properties"));
		}
		properties.load(stream);
		stream.close();
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.GERMAN);
		ArrayList<Task> taskData = new ArrayList<Task>();
		
		int id=1;
		ArrayList<Meeting> meetings = MeetingTable.readMeetingData(meetingFilename, project);
		for(Meeting meeting : meetings){
			Date date = df.parse(properties.getProperty("meetings."+meeting.name+".date"));
			taskData.add(new Task(id, meeting.description, "", 0, date, date));
			id++;
		}
		ArrayList<WPSummaryData> workpackages = WPSummaryTable.readWPSummaryData(workpackageFilename, project);
		for(WPSummaryData workpackage : workpackages){
			Date startDate = df.parse(properties.getProperty("workPackage."+workpackage.workpackage.name+".date"));
			int duration = getWorkpackageDuration(workpackage.workpackage, Integer.parseInt(properties.getProperty("manhoursPerMonth")));
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.MONTH, duration);
			Date endDate = cal.getTime();
			taskData.add(new Task(id, workpackage.workpackage.name, workpackage.wpDescription, duration, startDate, endDate));
			id++;
		}
		return taskData;
	}
	
	public static int getWorkpackageDuration(Workpackage wp, int manhoursPerMonth){
		return (int)Math.round(getHighestManhours(wp)/manhoursPerMonth);
	}
	
	private static double getHighestManhours(Workpackage wp){
		double manhours = 0;
		for(Person p : wp.personsWorkingHere){
			if (p.hoursInPackages[wp.positionOfThisPackage] > manhours){
				manhours=p.hoursInPackages[wp.positionOfThisPackage];
			}
		}
		return manhours;
	}
}
