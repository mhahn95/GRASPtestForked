
public class Meeting {
	public String name;
	public String description;
	public double timeline;
	public String location;
	public int[] personCountPerPartner;
	public double[] costsPerPartner;
	
	public Meeting(String name, String description, double timeline, String location, int[] personCountPerPartner, double[] costsPerPartner){
		this.name = name;
		this.description = description;
		this.timeline = timeline;
		this.location = location;
		this.personCountPerPartner = personCountPerPartner;
		this.costsPerPartner = costsPerPartner;
	}
}
