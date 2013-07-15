import java.util.ArrayList;

public class Partner {
	public String name;
	public double[] percentages;
	public ArrayList<Person> persons = new ArrayList<Person>();
	
	public Partner() {}
	
	public Partner(final String name, final double[] percentages, final ArrayList<Person> persons) {
		this.name=name;
		this.percentages=percentages;
		this.persons=persons;
	}
	
	@Override
	public boolean equals(Object partner2){
		Partner that = (Partner) partner2;
		
		if(!this.name.equals(that.name))
			return false;
		if(this.percentages.length != that.percentages.length)
			return false;
		for(int i=0; i<this.percentages.length; i++){
			if(this.percentages[i] != that.percentages[i])
				return false;
		}
		if(this.persons.size() != that.persons.size())
			return false;
		for(int i=0; i<this.persons.size(); i++){
			if(!this.persons.get(i).equals(that.persons.get(i)))
				return false;
		}
		
		return true;
	}
	
	public double getTotalHoursForPackage(final int wpPosition){
		double hours = 0;
		for(Person p : this.persons){
			hours += p.hoursInPackages[wpPosition];
		}
		return hours;
	}
	
	public double getTotalHours(){
		double hours = 0;
		for(int i=0; i<percentages.length; i++){
			hours += this.getTotalHoursForPackage(i);
		}
		return hours;
	}
	
	public double getTotalCosts(){
		double costs = 0;
		for(Person p : this.persons){
			costs += p.getHourSum()*p.hourly_rate;
		}
		return costs;
	}
	
	public double getTotalCostsForPackage(final int position){
		double costs = 0;
		for(Person p : this.persons){
			costs += p.hoursInPackages[position]*p.hourly_rate;
		}
		return costs;
	}
}