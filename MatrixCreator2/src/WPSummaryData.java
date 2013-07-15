
public class WPSummaryData {
	final public Workpackage workpackage;
	final public String wpNumber;
	final public String wpDescription;
	final public Partner wpLead;
	final public Person wpManager;
	final public double startMonth;
	final public double endMonth;
	final public String partnersInvolved;
	
	public WPSummaryData(Workpackage workpackage, String wpNumber, String wpDescription, Partner wpLead, Person wpManager, double startMonth, double endMonth, Project project){
		this.workpackage=workpackage;
		this.wpNumber=wpNumber;
		this.wpDescription=wpDescription;
		this.wpLead=wpLead;
		this.wpManager=wpManager;
		this.startMonth=startMonth;
		this.endMonth=endMonth;
		this.partnersInvolved=getPartnersInvolved(project);
	}
	
	private String getPartnersInvolved(Project project){
		String involved="";
		for(int i=0; i<project.consortialPartner.partners.size(); i++){
			if(project.consortialPartnerPercentages[i][this.workpackage.positionOfThisPackage] > 0){
				if(involved.length() == 0)
					involved += project.consortialPartner.partners.get(i).name;
				else
					involved += ", "+project.consortialPartner.partners.get(i).name;
			}
		}
		return involved;
	}
}
