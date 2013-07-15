import java.util.Date;


public class Task {
	public int id;
	public String name;
	public String description;
	public int duration;
	public Date start;
	public Date finish;
	
	public Task(int id, String name, String description, int duration, Date start, Date finish){
		this.id=id;
		this.name=name;
		this.description=description;
		this.duration=duration;
		this.start=start;
		this.finish=finish;
	}
}
