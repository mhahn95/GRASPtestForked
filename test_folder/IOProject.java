import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class IOProject {
	public static void exportProject(Project project, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		
		writer.write("Budget:;"+project.budget+"\n");
		
		writer.write("Partners:");
		for(Partner p : project.consortialPartner.partners){
			writer.write(";*"+p.name+";");
			for(double percentage : p.percentages)
				writer.write(percentage+";");
			writer.write("\n");
			//persons
			for(Person pers : p.persons){
				writer.write(";"+pers.name+";");
				writer.write(pers.hourly_rate+";");
				for(String role : pers.roles)
					writer.write(role+",");
				writer.write(";"+pers.partner+";");
				for(double hours : pers.hoursInPackages)
					writer.write(hours+",");
				writer.write(";");
				for(double maxHours : pers.maxHoursInPackages)
					writer.write(maxHours+",");
				writer.write("\n");
			}
		}
		writer.write("Workpackages:");
		for(Workpackage wp : project.workpackages){
			writer.write(";"+wp.name+";");
			writer.write(wp.costs+";");
			writer.write(wp.percentage+";");
			for(String role : wp.roles)
				writer.write(role+",");
			writer.write(";");
			for(double segmentation : wp.roleSegmentation)
				writer.write(segmentation+",");
			writer.write(";");
			for(Person pers : wp.personsWorkingHere)
				writer.write(pers.name+",");
			writer.write(";");
			writer.write(wp.duration+";");
			writer.write(wp.positionOfThisPackage+"\n");
		}
		writer.write("ConsortialPartnerCosts:");
		for(int i=0; i<project.consortialPartnerCosts.length; i++){
			writer.write(";"+project.consortialPartner.partners.get(i).name);
			for(int j=0; j<project.consortialPartnerCosts[i].length; j++){
				writer.write(";"+project.consortialPartnerCosts[i][j]);
			}
			writer.write("\n");
		}
		writer.write("ConsortialPartnerPercentages:");
		for(int i=0; i<project.consortialPartnerPercentages.length; i++){
			writer.write(";"+project.consortialPartner.partners.get(i).name);
			for(int j=0; j<project.consortialPartnerPercentages[i].length; j++){
				writer.write(";"+project.consortialPartnerPercentages[i][j]);
			}
			writer.write("\n");
		}
		writer.write("ConsortialPartnerCostsSums:");
		for(int i=0; i<project.consortialPartnerCostsSums.length; i++)
			writer.write(";"+project.consortialPartnerCostsSums[i]);
		writer.write("\n");
		writer.write("ConsortialPartnerPercentagesSums:");
		for(int i=0; i<project.consortialPartnerPercentagesSums.length; i++)
			writer.write(";"+project.consortialPartnerPercentagesSums[i]);
		writer.write("\n");
		writer.write("WorkpackagePercentagesSum:;"+project.workpackagePercentagesSum+"\n");
		writer.write("overallCosts:;"+project.overallCosts+"\n");
		writer.write("overallHours:;"+project.overallHours);
		
		writer.close();
	}
	
	public static Project importProject(String filename) throws NumberFormatException, IOException{
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(filename));
		}
		catch(FileNotFoundException e){
			System.out.println("**ERROR** \"" + filename + "\" could not be found. Execute \"01 - CalculatedProject.bat\" before this file, or use \"00 - ALL_IN_ONE.bat\"");
			System.exit(1);
		}
		Project project = new Project();
		
		project.budget = Double.parseDouble(reader.readLine().split(";")[1]);
		boolean done = false;
		Partner partner;
		Person person;
		ConsortialPartner consortialPartner = new ConsortialPartner();
		String[] lineData;
		
		partner = new Partner();
		lineData = reader.readLine().split(";");
		while(!done){
			partner.name = lineData[1].substring(1);
			partner.percentages = new double[lineData.length-2];
			for(int i=2; i<lineData.length; i++){
				partner.percentages[i-2] = Double.parseDouble(lineData[i]);
			}
			//read persons and add them to the partner
			while(true){
				lineData = reader.readLine().split(";");
				if(lineData[0].equals("Workpackages:")){ //all partners read
					done = true;
					break;
				}
				if(lineData[1].charAt(0) == '*') //start of next partner
					break;
				person = new Person();
				person.name = lineData[1];
				person.hourly_rate = Double.parseDouble(lineData[2]);
				person.roles = lineData[3].split(",");
				person.partner = lineData[4];
				person.hoursInPackages = MatrixCreator.parseDoubleArray(lineData[5].split(","));
				person.maxHoursInPackages = MatrixCreator.parseDoubleArray(lineData[6].split(","));
				
				partner.persons.add(person);
			}
			consortialPartner.partners.add(partner);
			partner = new Partner();
		}
		project.consortialPartner = consortialPartner;
		//read workpackages (first one is already in lineData)
		ArrayList<Workpackage> workpackages = new ArrayList<Workpackage>();
		Workpackage workpackage = new Workpackage();
		while(!lineData[0].equals("ConsortialPartnerCosts:")){ //all workpackages read
			workpackage.name = lineData[1];
			workpackage.costs = Double.parseDouble(lineData[2]);
			workpackage.percentage = Double.parseDouble(lineData[3]);
			workpackage.roles = lineData[4].split(",");
			workpackage.roleSegmentation = MatrixCreator.parseDoubleArray(lineData[5].split(","));
			for(String personName : lineData[6].split(",")){
				workpackage.personsWorkingHere.add(getPersonByName(personName, project.consortialPartner));
			}
			workpackage.duration = Double.parseDouble(lineData[7]);
			workpackage.positionOfThisPackage = Integer.parseInt(lineData[8]);
			
			workpackages.add(workpackage);
			workpackage = new Workpackage();
			lineData = reader.readLine().split(";");
		}
		project.workpackages = workpackages;
		//read consortialPartnerCosts
		project.consortialPartnerCosts=new double[project.consortialPartner.partners.size()][];
		for(int i=0; i<project.consortialPartnerCosts.length; i++){
			project.consortialPartnerCosts[i] = readDoubleArrayFromOffset(lineData, 2);
			lineData = reader.readLine().split(";");
		}
		//read consortialPartnerPercentages
		project.consortialPartnerPercentages=new double[project.consortialPartner.partners.size()][];
		for(int i=0; i<project.consortialPartnerPercentages.length; i++){
			project.consortialPartnerPercentages[i] = readDoubleArrayFromOffset(lineData, 2);
			lineData = reader.readLine().split(";");
		}
		//read consortialPartnerCostsSums
		project.consortialPartnerCostsSums = readDoubleArrayFromOffset(lineData, 1);
		lineData = reader.readLine().split(";");
		//read consortialPartnerPercentagesSums
		project.consortialPartnerPercentagesSums = readDoubleArrayFromOffset(lineData, 1);
		lineData = reader.readLine().split(";");
		//read workpackagePercentagesSum
		project.workpackagePercentagesSum = Double.parseDouble(lineData[1]);
		lineData = reader.readLine().split(";");
		//read overallCosts
		project.overallCosts = Double.parseDouble(lineData[1]);
		lineData = reader.readLine().split(";");
		//read overallHours
		project.overallHours = Double.parseDouble(lineData[1]);
		
		reader.close();
		return project;
	}
	
	private static Person getPersonByName(String name, ConsortialPartner consortialPartner){
		for(Partner partner : consortialPartner.partners){
			for(Person person : partner.persons){
				if(person.name.equals(name))
					return person;
			}
		}
		return null;
	}
	
	private static double[] readDoubleArrayFromOffset(String[] arr, int offset){
		double[] dArray = new double[arr.length-offset];
		for(int i=offset; i<arr.length; i++){
			dArray[i-offset] = Double.parseDouble(arr[i]);
		}
		return dArray;
	}
}
