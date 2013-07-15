import java.util.ArrayList;

public class Project {
	public double budget;
	public ConsortialPartner consortialPartner;
	public ArrayList<Workpackage> workpackages;
	
	public double[][] consortialPartnerCosts;
	public double[][] consortialPartnerPercentages;
	
	public double[] consortialPartnerCostsSums;
	public double[] consortialPartnerPercentagesSums;
	public double workpackagePercentagesSum;
	
	public double overallCosts;
	public double overallHours;
	
	public Project(){}
	
	public Project(double budget, ConsortialPartner consortialPartner, ArrayList<Workpackage> workpackages){
		this.budget=budget;
		this.consortialPartner=consortialPartner;
		this.workpackages=workpackages;
		this.workpackagePercentagesSum=this.getWorkpackagePercentagesSum();
		this.consortialPartnerPercentages=this.getConsortialPartnerPercentages();
		
		this.consortialPartnerCosts=new double[this.consortialPartner.partners.size()][];
		this.consortialPartnerCostsSums = new double[this.consortialPartner.partners.size()];
		this.consortialPartnerPercentagesSums = new double[this.workpackages.size()];
	}
	
	public void calculateProject(){
		this.calculateConsortialPartnerPercentagesSums();
		
		this.calculateWorkpackages();
		
		this.calculateCostsPerPartner();
		
		this.calculateOverallCosts(); //also save costs in Workpackage objects for later use
		
		this.calculateOverallHours();
		
		this.calculateCostsPerPartnerPerWorkpackage();
	}
	
	public void calculateConsortialPartnerPercentagesSums(){
		int i=0, j=0;
		for(@SuppressWarnings("unused") Workpackage wp : this.workpackages){
			j=0;
			this.consortialPartnerPercentagesSums[i]=0;
			for(@SuppressWarnings("unused") Partner p : consortialPartner.partners){
				this.consortialPartnerPercentagesSums[i] += this.consortialPartnerPercentages[j][i];
				j++;
			}
			i++;
		}
	}
	
	public void calculateWorkpackages(){
		for(Workpackage wp : this.workpackages){
			wp.assignPersonsWorkingAtThisPackage(this.consortialPartner);
			wp.calculateHours(this.consortialPartner, this.consortialPartnerPercentages);
		}
	}
	
	public void calculateCostsPerPartner(){
		int i=0;
		for(Partner p : this.consortialPartner.partners){
			this.consortialPartnerCostsSums[i] = p.getTotalCosts();
			i++;
		}
	}
	
	public void calculateOverallCosts(){
		this.overallCosts=0;
		for(Workpackage wp : this.workpackages){
			this.overallCosts += wp.getRealCosts(getPositionOfPackage(wp, this.workpackages));
		}
	}
	
	public void calculateOverallHours(){
		double hours=0;
		for(Partner partner : consortialPartner.partners){
			hours += partner.getTotalHours();
		}
		this.overallHours = hours;
	}
	
	public void calculateCostsPerPartnerPerWorkpackage(){
		int i=0;
		for(Partner p : this.consortialPartner.partners){
			int j=0;
			this.consortialPartnerCosts[i] = new double[this.workpackages.size()];
			for(Workpackage wp : this.workpackages){
				this.consortialPartnerCosts[i][j] = p.getTotalCostsForPackage(getPositionOfPackage(wp, this.workpackages));
				j++;
			}
			i++;
		}
	}
	
	private static int getPositionOfPackage(final Workpackage wp, final ArrayList<Workpackage> workpackages){
		for(int i=0; i<workpackages.size(); i++){
			if(wp.name.equals(workpackages.get(i).name))
				return i;
		}
		return -1;
	}
	
	public double getWorkpackagePercentagesSum(){
		double percent = 0;
		for(Workpackage workpackage : this.workpackages){
			percent += workpackage.percentage;
		}
		return percent;
	}
	
	public double[][] getConsortialPartnerPercentages(){
		final double[][] percentages = new double[this.consortialPartner.partners.size()][this.workpackages.size()];
		int i=0;
		for(Partner partner : this.consortialPartner.partners){
			percentages[i] = partner.percentages;
			i++;
		}
		return percentages;
	}


	@Override
	public boolean equals(Object project2) {
		Project that = (Project) project2;
		
		if(this.budget != that.budget)
			return false;
		if(this.overallCosts != that.overallCosts)
			return false;
		if(this.overallHours != that.overallHours)
			return false;
		if(this.workpackagePercentagesSum != that.workpackagePercentagesSum)
			return false;
		if(this.consortialPartnerCostsSums.length != that.consortialPartnerCostsSums.length)
			return false;
		for(int i=0; i<this.consortialPartnerCostsSums.length; i++){
			if(this.consortialPartnerCostsSums[i] != that.consortialPartnerCostsSums[i])
				return false;
		}
		if(this.consortialPartnerPercentagesSums.length != that.consortialPartnerPercentagesSums.length)
			return false;
		for(int i=0; i<this.consortialPartnerPercentagesSums.length; i++){
			if(this.consortialPartnerPercentagesSums[i] != that.consortialPartnerPercentagesSums[i])
				return false;
		}
		if(this.consortialPartnerCosts.length != that.consortialPartnerCosts.length)
			return false;
		for(int i=0; i<this.consortialPartnerCosts.length; i++){
			for(int j=0; j<this.consortialPartnerCosts[i].length; j++){
				if(this.consortialPartnerCosts[i][j] != that.consortialPartnerCosts[i][j])
					return false;
			}
		}
		if(this.consortialPartnerPercentages.length != that.consortialPartnerPercentages.length)
			return false;
		for(int i=0; i<this.consortialPartnerPercentages.length; i++){
			for(int j=0; j<this.consortialPartnerPercentages[i].length; j++){
				if(this.consortialPartnerPercentages[i][j] != that.consortialPartnerPercentages[i][j])
					return false;
			}
		}
		if(this.workpackages.size() != that.workpackages.size())
			return false;
		for(int i=0; i<this.workpackages.size(); i++){
			if(!this.workpackages.get(i).equals(that.workpackages.get(i)))
				return false;
		}
		if(this.consortialPartner.partners.size() != that.consortialPartner.partners.size())
			return false;
		for(int i=0; i<this.consortialPartner.partners.size(); i++){
			if(!this.consortialPartner.partners.get(i).equals(that.consortialPartner.partners.get(i)))
				return false;
		}
		return true;
	}

}