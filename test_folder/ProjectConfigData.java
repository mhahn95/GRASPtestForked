import java.util.ArrayList;


public class ProjectConfigData {
	public double budget;
	public String[] wpNames;
	public double[] wpPercentages;
	public String[] pNames;
	public double[][] pPercentages;
	public String[][] wpRoles;
	public double[][] wpRoleSegmentation;
	public ArrayList<ArrayList<Person>> pPersons;
	
	public ProjectConfigData(double budget, String[] wpNames, double[] wpPercentages, String[] pNames, double[][] pPercentages, String[][] wpRoles, double[][] wpRoleSegmentation, ArrayList<ArrayList<Person>> pPersons){
		this.budget = budget;
		this.wpNames = wpNames;
		this.wpPercentages = wpPercentages;
		this.pNames = pNames;
		this.pPercentages = pPercentages;
		this.wpRoles = wpRoles;
		this.wpRoleSegmentation = wpRoleSegmentation;
		this.pPersons = pPersons;
	}

	public ProjectConfigData() {
		
	}
}
