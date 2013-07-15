import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class PartnerSummaryTable {
	public static void main(String[] args) throws NumberFormatException, IOException{
		String dataFile = "01 - Lead and participating partners of the GPGPU Aerosol Retrievals consortium.properties";
		String importFile = "01 - CalculatedProject.csv";
		String exportFile = "01 - Lead and participating partners of the GPGPU Aerosol Retrievals consortium.csv";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<PartnerSummaryData> summaryData = readPartnerSummaryData(dataFile, project);
		printPartnerSummaryData(summaryData, exportFile);
	}
	
	public static void printPartnerSummaryData(ArrayList<PartnerSummaryData> data, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		writer.write("Partner;Role;Abbreviation;Country;Leader\n");
		
		for(PartnerSummaryData lineData : data){
			writer.write(lineData.cpFullName+";"+lineData.cpRole+";"+lineData.partner.name+";"+lineData.cpCountry+";"+lineData.cpLeader.name+"\n");
		}
		
		writer.close();
	}
	
	public static ArrayList<PartnerSummaryData> readPartnerSummaryData(String filename, Project project) throws IOException{
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
		
		ArrayList<PartnerSummaryData> summaryData = new ArrayList<PartnerSummaryData>();
		
		for(Partner partner : project.consortialPartner.partners){
			String[] data = properties.getProperty("consortialPartner."+partner.name+".data").split("\\s*,\\s*");
			Person cpLeader = getPersonByName(data[4], partner.persons);
			summaryData.add(new PartnerSummaryData(data[0], data[1], partner, data[3], cpLeader));
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
