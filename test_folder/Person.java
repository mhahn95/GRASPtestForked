
public class Person {
	String name;
	double hourly_rate;
	String[] roles;
	String partner;
	double[] hoursInPackages;
	double[] maxHoursInPackages;
	
	public Person(final String name, final double hourly_rate, final String[] roles, final String partner, final int packageCount){
		this.name = name;
		this.hourly_rate = hourly_rate;
		this.partner = partner;
		this.roles = roles;
		this.hoursInPackages = createFilledDoubleArray(packageCount, 0);
		this.maxHoursInPackages = createFilledDoubleArray(packageCount, -1);
	}
	
	public Person() {}
	

	@Override
	public boolean equals(Object person2){
		Person that = (Person) person2;
		
		if(!this.name.equals(that.name))
			return false;
		if(this.hourly_rate != that.hourly_rate)
			return false;
		if(!this.partner.equals(that.partner))
			return false;
		if(this.roles.length != that.roles.length)
			return false;
		for(int i=0; i<this.roles.length; i++){
			if(!this.roles[i].equals(that.roles[i]))
				return false;
		}
		if(this.hoursInPackages.length != that.hoursInPackages.length)
			return false;
		for(int i=0; i<this.hoursInPackages.length; i++){
			if(this.hoursInPackages[i] != that.hoursInPackages[i])
				return false;
		}
		if(this.maxHoursInPackages.length != that.maxHoursInPackages.length)
			return false;
		for(int i=0; i<this.maxHoursInPackages.length; i++){
			if(this.maxHoursInPackages[i] != that.maxHoursInPackages[i])
				return false;
		}
		return true;
	}
	
	public boolean hasRole(final String role){
		for(int i=0; i<this.roles.length; i++){
			if (roles[i].equals(role))
				return true;
		}
		return false;
	}
	
	private static double[] createFilledDoubleArray(final int size, final double value){
		double[] array = new double[size];
		for(int i=0; i<size; i++){
			array[i]=value;
		}
		return array;
	}
	
	public double getHourSum(){
		double totalHours=0;
		for(double hours : this.hoursInPackages){
			totalHours += hours;
		}
		return totalHours;
	}
}