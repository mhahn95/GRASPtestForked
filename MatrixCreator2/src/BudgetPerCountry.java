import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


public class BudgetPerCountry {
	public static void main(String[] args) throws NumberFormatException, IOException{
		String dataFile = "data.properties";
		String importFile = "01 - CalculatedProject.csv";
		String exportFile = "10 - Geographical distribution per country and per partner of total budget.csv";
		
		Project project = IOProject.importProject(importFile);
		writeBudgetPerCountry(project, exportFile, dataFile);
	}
	
	public static void writeBudgetPerCountry(Project project, String filename, String dataFile) throws IOException{
		final Properties properties = new Properties();
		final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(dataFile));
		properties.load(stream);
		stream.close();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		String[] countries = properties.getProperty("consortialPartnerCountries").split("\\s*,\\s*");
		writer.write("Country;Company;Amount (Euro);Amount (%)\n");
		double[] budgetPercentPerCountry = getBudgetPercentPerCountry(project);
		for(int i=0; i<countries.length; i++){
			writer.write(countries[i]+";"+project.consortialPartner.partners.get(i).name+";"+project.consortialPartnerCostsSums[i]+";"+MatrixCreator.round(budgetPercentPerCountry[i])+"%\n");
		}
		writer.write("Total;;"+project.overallCosts+";100%");
		writer.close();
	}
	
	public static double[] getBudgetPercentPerCountry(Project project){
		double[] percentPerCountry = new double[project.consortialPartnerCostsSums.length];
		for(int i=0; i<project.consortialPartnerCostsSums.length; i++){
			percentPerCountry[i] = (project.consortialPartnerCostsSums[i]/project.overallCosts)*100;
		}
		return percentPerCountry;
	}
}
