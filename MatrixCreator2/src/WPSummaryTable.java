import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class WPSummaryTable {
	public static void main(String[] args) throws NumberFormatException, IOException{
		String dataFile = "07 - Summary table of work packages.properties";
		String importFile = "01 - CalculatedProject.csv";
		String exportFile = "07 - Summary table of work packages.csv";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<WPSummaryData> summaryData = readWPSummaryData(dataFile, project);
		printWPSummaryData(summaryData, exportFile);
	}
	
	public static void printWPSummaryData(ArrayList<WPSummaryData> data, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		writer.write("WP number;WP description;WP Lead;Partners involved;WP Manager;Duration (KO+#month)\n");
		for(WPSummaryData lineData : data){
			writer.write(lineData.wpNumber+";"+lineData.wpDescription+";"+lineData.wpLead.name+";"+lineData.partnersInvolved+";"+lineData.wpManager.name+";KO");
			if(lineData.startMonth != 0)
				writer.write("+"+lineData.startMonth);
			writer.write(" -> KO+"+lineData.endMonth+"\n");
		}
		writer.close();
	}
	
	public static ArrayList<WPSummaryData> readWPSummaryData(String filename, Project project) throws IOException{
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
		
		ArrayList<WPSummaryData> summaryData = new ArrayList<WPSummaryData>();
		
		for(Workpackage wp : project.workpackages){
			String[] data = properties.getProperty("workPackage."+wp.name+".data").split("\\s*,\\s*");
			Partner wpLead = project.consortialPartner.getPartnerByName(data[2]);
			Person wpManager = getPersonByName(data[3], wpLead.persons);
			double startMonth = Double.parseDouble(data[4].split("-")[0]);
			double endMonth = Double.parseDouble(data[4].split("-")[1]);
			summaryData.add(new WPSummaryData(wp, data[0], data[1], wpLead, wpManager, startMonth, endMonth, project));
		}
		
		return summaryData;
	}
	
	public static Person getPersonByName(String name, ArrayList<Person> persons){
		for(Person person : persons){
			if(person.name.equals(name)){
				return person;
			}
		}
		return null;
	}
}
