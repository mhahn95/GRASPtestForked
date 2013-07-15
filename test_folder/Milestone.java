
public class Milestone {
	public String name;
	public double percentageOfBudget;
	
	public Milestone(String name, double percentageOfBudget){
		this.name = name;
		this.percentageOfBudget = percentageOfBudget;
	}
	
	public double[] getBudgetAmountsPerPartnerAndSum(Project project){
		double[] amounts = new double[project.consortialPartner.partners.size()+1];
		int i=0;
		double sum=0;
		for(Partner partner : project.consortialPartner.partners){
			amounts[i] = partner.getTotalCosts()*(percentageOfBudget/100);
			sum += amounts[i];
			i++;
		}
		amounts[i] = sum;
		return amounts;
	}
}
