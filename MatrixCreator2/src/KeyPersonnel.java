import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class KeyPersonnel {
	public static void main(String[] args) throws NumberFormatException, IOException{
		String importFile = "01 - CalculatedProject.csv";
		String exportFile = "05 - The Key Personnel.csv";
		
		Project project = IOProject.importProject(importFile);
		
		ArrayList<PersonWithPercent> personnel = calculatePersonPercents(project.consortialPartner.partners, project.overallHours);
		
		exportKeyPersonnel(personnel, readMinimumKeyPersonnelWorkAmount("data.properties"), exportFile);
	}
	
	public static ArrayList<PersonWithPercent> calculatePersonPercents(ArrayList<Partner> partners, double overallProjectHours){
		ArrayList<PersonWithPercent> personnel = new ArrayList<PersonWithPercent>();
		for(Partner partner : partners){
			for(Person person : partner.persons){
				personnel.add(new PersonWithPercent(person, overallProjectHours));
			}
		}
		return personnel;
	}
	
	public static void exportKeyPersonnel(ArrayList<PersonWithPercent> personnel, double minPercentage, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		writer.write("Key Person Name;Position in Own Organisation;Organisation Abbreviation;Proportion of Time Allocated to this Project\n");
		for(PersonWithPercent person : personnel){
			if(person.percent>=minPercentage)
				writePersonLine(person, writer);
		}
		writer.close();
	}
	
	private static void writePersonLine(PersonWithPercent person, BufferedWriter writer) throws IOException{
		writer.write(person.data.name+";"+createRoleString(person.data.roles)+";"+person.data.partner+";"+MatrixCreator.round(person.percent)+"%\n");
	}
	
	private static String createRoleString(String[] roles){
		String rolestring="";
		if(roles.length == 1){
			return roles[0];
		}
		for(int i=0; i<roles.length-2; i++){
			rolestring += roles[i]+", ";
		}
		rolestring += roles[roles.length-2]+" and "+roles[roles.length-1];
		return rolestring;
	}
	
	private static double readMinimumKeyPersonnelWorkAmount(String filename) throws IOException{
		final Properties properties = new Properties();
		final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filename));
		properties.load(stream);
		stream.close();
		
		return Double.parseDouble(properties.getProperty("minimumKeyPersonnelWorkAmount").split("%")[0]);
	}
}
