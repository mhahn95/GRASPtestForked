import java.util.ArrayList;

public class ConsortialPartner {
	public ArrayList<Partner> partners;
	
	public ConsortialPartner(){
		this.partners = new ArrayList<Partner>();
	}
	
	public void addPartner(final Partner p){
		partners.add(p);
	}
	
	public void addNewPartner(final String name, final double[] percentages, final ArrayList<Person> persons){
		partners.add(new Partner(name, percentages, persons));
	}
	
	public Partner getPartnerByName(final String name){
		for(Partner p : this.partners){
			if(p.name.equals(name))
				return p;
		}
		return null;
	}
	
	public int getPartnerPosition(final Partner p){
		int i=0;
		for(Partner part : this.partners){
			if(p.name.equals(part.name))
				return i;
			i++;
		}
		return -1;
	}
}