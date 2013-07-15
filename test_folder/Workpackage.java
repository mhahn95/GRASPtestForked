import java.util.ArrayList;

public class Workpackage {
	public String name;
	public double costs;
	public double percentage;
	public String[] roles;
	public double[] roleSegmentation;
	public ArrayList<Person> personsWorkingHere = new ArrayList<Person>();
	public double duration;
	public int positionOfThisPackage;
	
	public Workpackage(final String name, final double budget, final double percentage, final String[] roles, final double[] roleSegmentation, final int positionOfThisPackage){
		this.name=name;
		this.costs=budget*(percentage/100);
		this.percentage=percentage;
		this.roles=roles;
		this.roleSegmentation=roleSegmentation;
		this.duration=0;
		this.positionOfThisPackage=positionOfThisPackage;
	}
	
	public Workpackage() {}
	

	@Override
	public boolean equals(Object workpackage2){
		Workpackage that = (Workpackage) workpackage2;
		
		if(!this.name.equals(that.name))
			return false;
		if(this.costs != that.costs)
			return false;
		if(this.percentage != that.percentage)
			return false;
		if(this.duration != that.duration)
			return false;
		if(this.positionOfThisPackage != that.positionOfThisPackage)
			return false;
		if(this.roles.length != that.roles.length)
			return false;
		for(int i=0; i<this.roles.length; i++){
			if(!this.roles[i].equals(that.roles[i]))
				return false;
		}
		if(this.roleSegmentation.length != that.roleSegmentation.length)
			return false;
		for(int i=0; i<this.roleSegmentation.length; i++){
			if(this.roleSegmentation[i] != that.roleSegmentation[i])
				return false;
		}
		if(this.personsWorkingHere.size() != that.personsWorkingHere.size())
			return false;
		for(int i=0; i<this.personsWorkingHere.size(); i++){
			if(!this.personsWorkingHere.get(i).equals(that.personsWorkingHere.get(i)))
				return false;
		}
		return true;
	}
	
	public void calculateHours(final ConsortialPartner consortialPartner, double[][] consortialPartnerPercentages){
		for(String role : this.roles){
			for(Partner p : consortialPartner.partners){
				final ArrayList<Person> personsWithThisRoleAndPartnerHere = this.getPersonsWithThisRoleAndPartnerHere(role, p.name);
				
				final double averageRate = getAverageHourlyRate(personsWithThisRoleAndPartnerHere);
				
				final double roleSegmentation = this.getSegmentationOfRole(role)/100;
				final double partnerSegmentation = consortialPartnerPercentages[consortialPartner.getPartnerPosition(p)][this.positionOfThisPackage]/100;
				
				final double hoursForThisRoleHere = (this.costs*roleSegmentation*partnerSegmentation)/averageRate;
				
				for(Person pers : personsWithThisRoleAndPartnerHere){
					final double hoursOfThisPerson = hoursForThisRoleHere / personsWithThisRoleAndPartnerHere.size();
					final double maxHoursOfThisPerson = pers.maxHoursInPackages[this.positionOfThisPackage];
					final double currentHoursOfThisPerson = pers.hoursInPackages[this.positionOfThisPackage];
					
					if(maxHoursOfThisPerson != -1 && (currentHoursOfThisPerson + hoursOfThisPerson) > maxHoursOfThisPerson){
						pers.hoursInPackages[this.positionOfThisPackage] = maxHoursOfThisPerson;
						//get remaining costs and split them on the other persons
						final double remainingHours = (currentHoursOfThisPerson + hoursOfThisPerson) - maxHoursOfThisPerson;
						final double remainingCosts = remainingHours * pers.hourly_rate;
						splitCostsOnPersons(remainingCosts, listWithoutPerson(this.getPersonsWithThisRolesAndPartnerHere(pers.roles, p.name), pers), this.positionOfThisPackage);
					}
					else{
						pers.hoursInPackages[this.positionOfThisPackage] += hoursOfThisPerson;
					}
					
					this.duration += hoursOfThisPerson;
				}
			}
		}
	}
	
	public void assignPersonsWorkingAtThisPackage(final ConsortialPartner consortialPartner){
		for(Partner p : consortialPartner.partners){
			for(Person pers : p.persons){
				if (this.personHasRoleHere(pers))
					this.personsWorkingHere.add(pers);
			}
		}
	}
	
	public double getSegmentationOfRole(final String role){
		for(int i=0; i<this.roles.length; i++){
			if(this.roles[i].equals(role))
				return this.roleSegmentation[i];
		}
		return -1;
	}
	
	public boolean personHasRoleHere(final Person p){
		for (int i=0; i<this.roles.length; i++){
			if (p.hasRole(this.roles[i]))
				return true;
		}
		return false;
	}
	
	public boolean personHasThisRoleHere(final Person p, final String role){
		for (int i=0; i<this.roles.length; i++){
			if (this.roles[i].equals(role) && p.hasRole(this.roles[i]))
				return true;
		}
		return false;
	}
	
	public ArrayList<Person> getPersonsWithThisRoleHere(final String role){
		final ArrayList<Person> personsWithThisRoleHere = new ArrayList<Person>();
		for(Person p : this.personsWorkingHere){
			if(this.personHasThisRoleHere(p, role))
				personsWithThisRoleHere.add(p);
		}
		return personsWithThisRoleHere;
	}
	
	public ArrayList<Person> getPersonsWithThisRoleAndPartnerHere(final String role, final String partner){
		final ArrayList<Person> personsWithThisRoleHere = new ArrayList<Person>();
		for(Person p : this.personsWorkingHere){
			if(this.personHasThisRoleHere(p, role) && p.partner.equals(partner))
				personsWithThisRoleHere.add(p);
		}
		return personsWithThisRoleHere;
	}

	public ArrayList<Person> getPersonsWithThisRolesAndPartnerHere(final String[] roles, final String partner) {
		final ArrayList<Person> personsWithThisRolesHere = new ArrayList<Person>();
		for(String role : roles){
			for(Person p : this.personsWorkingHere){
				if(this.personHasThisRoleHere(p, role) && p.partner.equals(partner) && !personsWithThisRolesHere.contains(p))
					personsWithThisRolesHere.add(p);
			}
		}
		return personsWithThisRolesHere;
	}
	
	public double getRealCosts(final int position){
		double costs = 0;
		for(Person p : this.personsWorkingHere){
			costs += p.hoursInPackages[position]*p.hourly_rate;
		}
		this.costs=costs;
		return costs;
	}
	
	private static double getAverageHourlyRate(final ArrayList<Person> persons){
		double sum=0;
		int count=0;
		for(Person p : persons){
			sum+=p.hourly_rate;
			count++;
		}
		return sum/count;
	}
	
	private static ArrayList<Person> listWithoutPerson(final ArrayList<Person> persons, final Person person){
		final ArrayList<Person> newList = new ArrayList<Person>();
		for(Person p : persons){
			if (!p.name.equals(person.name)) {
				newList.add(p);
			}
		}
		return newList;
	}
	
	public static void splitCostsOnPersons(final double costs, final ArrayList<Person> persons, final int packagePosition){
		final double costsSplit = costs / persons.size();
		final double avgHourlyRate = getAverageHourlyRate(persons);
		final double hoursPerPerson = costsSplit / avgHourlyRate;
		
		for(Person p : persons){
			if(p.maxHoursInPackages[packagePosition] != -1 && p.maxHoursInPackages[packagePosition] < p.hoursInPackages[packagePosition] + hoursPerPerson){
				final double remainingHours = hoursPerPerson - (p.maxHoursInPackages[packagePosition] - p.hoursInPackages[packagePosition]);
				final double remainingCosts = remainingHours*p.hourly_rate;
				p.hoursInPackages[packagePosition] = p.maxHoursInPackages[packagePosition];
				splitCostsOnPersons(remainingCosts, listWithoutPerson(persons, p), packagePosition);
			}
			else{
				p.hoursInPackages[packagePosition] += hoursPerPerson;
			}
		}
	}
}