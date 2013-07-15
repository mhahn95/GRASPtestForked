import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class MeetingTable {
	public static void main(String[] args) throws NumberFormatException, IOException{
		String dataFile = "13 - The meeting schedule with detailed costs is shown.properties";
		String importFile = "01 - CalculatedProject.csv";
		String exportFile = "13 - The meeting schedule with detailed costs is shown.csv";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<Meeting> meetingData = readMeetingData(dataFile, project);
		printMeetingData(meetingData, exportFile, project);
	}
	
	public static void printMeetingData(ArrayList<Meeting> data, String filename, Project project) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		
		writer.write("Milestone;Description;Event Timeline;Location");
		for(Partner partner : project.consortialPartner.partners){
			writer.write(";"+partner.name);
		}
		writer.write("\n");
		for(Meeting meeting : data){
			writer.write(meeting.name+";"+meeting.description+";KO");
			if(meeting.timeline != 0)
				writer.write("+"+meeting.timeline);
			writer.write(";"+meeting.location);
			for(int i=0; i<meeting.personCountPerPartner.length; i++){
				writer.write(";"+meeting.personCountPerPartner[i]+" person");
				if(meeting.personCountPerPartner[i] != 1)
					writer.write("s");
				if(meeting.costsPerPartner[i] != 0)
					writer.write(" "+meeting.costsPerPartner[i]+"€");
			}
			writer.write("\n");
		}
		writer.close();
	}
	
	public static ArrayList<Meeting> readMeetingData(String filename, Project project) throws IOException{
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
		
		ArrayList<Meeting> meetingData = new ArrayList<Meeting>();
		String[] names = properties.getProperty("meetings").split("\\s*,\\s*");
		for(String meeting : names){
			String[] data = properties.getProperty("meetings."+meeting).split("\\s*,\\s*");
			String[] persons_and_costs = properties.getProperty("meetings."+meeting+".persons_and_costs").split("\\s*;\\s*");
			int[] personCountPerPartner = new int[project.consortialPartner.partners.size()];
			double[] costsPerPartner = new double[project.consortialPartner.partners.size()];
			for(String personCostData : persons_and_costs){
				String[] personCostDataSplit = personCostData.split("\\s*,\\s*");
				int partnerPosition = project.consortialPartner.getPartnerPosition(project.consortialPartner.getPartnerByName(personCostDataSplit[0]));
				personCountPerPartner[partnerPosition] = Integer.parseInt(personCostDataSplit[1]);
				costsPerPartner[partnerPosition] = Double.parseDouble(personCostDataSplit[2]);
			}
			meetingData.add(new Meeting(meeting, data[0], Double.parseDouble(data[1]), data[2], personCountPerPartner, costsPerPartner));
		}
		
		return meetingData;
	}
}
