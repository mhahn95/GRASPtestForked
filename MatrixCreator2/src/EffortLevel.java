import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class EffortLevel {
	public static void main(String[] args) throws NumberFormatException, IOException{
		String importFile = "01 - CalculatedProject.csv";
		String exportFile = "08 - Level of effort per WP and per partner.csv";
		
		Project project = IOProject.importProject(importFile);
		writeProjectEffort(project, exportFile);
	}
	
	public static void writeProjectEffort(Project project, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		//title line (workpackage names)
		for(Workpackage wp : project.workpackages){
			writer.write(";"+wp.name);
		}
		writer.write(";Total\n");
		//partners with manhour values per workpackage
		for(Partner partner : project.consortialPartner.partners){
			writer.write(partner.name);
			for(int i=0; i<partner.percentages.length; i++){
				writer.write(";"+MatrixCreator.round(partner.getTotalHoursForPackage(i)));
			}
			writer.write(";"+MatrixCreator.round(partner.getTotalHours())+"\n");
		}
		//total manhours per workpackage
		writer.write("Total");
		for(Workpackage wp : project.workpackages){
			writer.write(";"+MatrixCreator.round(wp.duration));
		}
		writer.write(";"+MatrixCreator.round(project.overallHours));
		writer.close();
	}
}
