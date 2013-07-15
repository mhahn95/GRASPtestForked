
public class PartnerSummaryData {
	public String cpFullName;
	public String cpRole;
	public Partner partner;
	public String cpCountry;
	public Person cpLeader;
	
	public PartnerSummaryData(String cpFullName, String cpRole, Partner partner, String cpCountry, Person cpLeader){
		this.cpFullName=cpFullName;
		this.cpRole=cpRole;
		this.partner=partner;
		this.cpCountry=cpCountry;
		this.cpLeader=cpLeader;
	}
}
