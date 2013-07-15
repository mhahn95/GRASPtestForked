import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class MilestoneTable {
	public static void main(String[] args) throws NumberFormatException, IOException{
		String dataFile = "12 - The amounts of the proposed advance and progress payments are shown per partner.properties";
		String importFile = "01 - CalculatedProject.csv";
		String exportFile = "12 - The amounts of the proposed advance and progress payments are shown per partner.csv";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<Milestone> milestoneData = readMilestoneData(dataFile, project);
		printMilestoneData(milestoneData, exportFile, project);
	}
	
	public static void printMilestoneData(ArrayList<Milestone> data, String filename, Project project) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		
		writer.write("Milestone");
		for(Partner partner : project.consortialPartner.partners){
			writer.write(";"+partner.name);
		}
		writer.write(";Total\n");
		for(Milestone milestone : data){
			writer.write(milestone.name);
			for(double amount : milestone.getBudgetAmountsPerPartnerAndSum(project)){
				writer.write(";"+amount);
			}
			writer.write("\n");
		}
		writer.write("Total");
		for(Partner partner : project.consortialPartner.partners){
			writer.write(";"+partner.getTotalCosts());
		}
		writer.write(";"+project.overallCosts);
		writer.close();
	}
	
	public static ArrayList<Milestone> readMilestoneData(String filename, Project project) throws IOException{
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
		
		ArrayList<Milestone> milestoneData = new ArrayList<Milestone>();
		
		String[] names = properties.getProperty("milestones").split("\\s*,\\s*");
		double[] separation = MatrixCreator.parsePercentDoubleArray(properties.getProperty("milestones.separation").split("\\s*,\\s*"));
		for(int i=0; i<names.length; i++){
			milestoneData.add(new Milestone(names[i], separation[i]));
		}
		
		return milestoneData;
	}
}
