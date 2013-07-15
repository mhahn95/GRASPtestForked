
public class PersonWithPercent {
	final public Person data;
	final public double percent;
	
	public PersonWithPercent(Person person, double totalProjectHours){
		this.data = person;
		this.percent = (person.getHourSum()/totalProjectHours)*100;
	}
}
