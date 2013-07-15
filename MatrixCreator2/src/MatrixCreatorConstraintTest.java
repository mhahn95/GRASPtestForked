import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class MatrixCreatorConstraintTest {

	public ProjectConfigData pcd = new ProjectConfigData();
	public Project project;
	
	@Before
	public void buildUp() throws IOException{
		pcd = MatrixCreator.readData("C:\\Users\\mhahn\\workspace\\MatrixCreator2\\src\\testdataConstraints.properties");
		project = MatrixCreator.configProject(pcd);
	}
	
	@Test
	public void splitCostsOnPersonsTest(){
		Person p1 = new Person("DummyPerson1", 10, new String[] { "DummyRole1", "DummyRole2" }, "DummyPartner1", 1);
		Person p2 = new Person("DummyPerson2", 20, new String[] { "DummyRole2" }, "DummyPartner1", 1);
		Person p3 = new Person("DummyPerson3", 30, new String[] { "DummyRole1" }, "DummyPartner1", 1);
		
		p1.hoursInPackages[0] = 50;
		p2.hoursInPackages[0] = 0;
		p3.hoursInPackages[0] = 10;
		p1.maxHoursInPackages[0] = 60;
		p2.maxHoursInPackages[0] = 100;
		p3.maxHoursInPackages[0] = 500;
		
		ArrayList<Person> testList = new ArrayList<Person>();
		testList.add(p1);
		testList.add(p2);
		testList.add(p3);
		
		Workpackage.splitCostsOnPersons(5000, testList, 0);
		
		assertEquals(5000, (p1.hoursInPackages[0]*p1.hourly_rate + p2.hoursInPackages[0]*p2.hourly_rate + p3.hoursInPackages[0]*p3.hourly_rate) - 50*p1.hourly_rate - 10*p3.hourly_rate, 0.01);
		
		assertEquals(60, p1.hoursInPackages[0], 0.01);
		assertEquals(98, p2.hoursInPackages[0], 0.01);
		assertEquals(108, p3.hoursInPackages[0], 0.01);
	}
	
	@Test
	public void calculateWorkpackagesTest(){
		project.calculateWorkpackages();
		//test assignPersonsWorkingAtThisPackage
		// workPackage.WP000.segmentation = (TA, 20%); (RE, 80%)
		Workpackage wp000 = project.workpackages.get(0);
		assertEquals(wp000.name, "WP000");
		assertTrue(consistsOfPersons(wp000.personsWorkingHere, new String[] { "CF", "WP", "O", "P", "T" }));
		// workPackage.WP100.segmentation = (E, 80%); (T, 20%)
		Workpackage wp100 = project.workpackages.get(1);
		assertEquals(wp100.name, "WP100");
		assertTrue(consistsOfPersons(wp100.personsWorkingHere, new String[] { "MA", "WE", "T", "PA"}));
		//workPackage.WP110.segmentation = (PE, 90%); (DEV, 10%)
		Workpackage wp110 = project.workpackages.get(2);
		assertEquals(wp110.name, "WP110");
		assertTrue(consistsOfPersons(wp110.personsWorkingHere, new String[] { "CF", "WP", "MA", "O", "T" }));
		//workPackage.WP200.segmentation = (RE, 10%); (DEV, 20%); (T, 70%)
		Workpackage wp200 = project.workpackages.get(3);
		assertEquals(wp200.name, "WP200");
		assertTrue(consistsOfPersons(wp200.personsWorkingHere, new String[] { "WP", "MA", "WE", "P", "T", "PA" }));
		//workPackage.WP210.segmentation = (E, 80%); (T, 20%)
		Workpackage wp210 = project.workpackages.get(4);
		assertEquals(wp210.name, "WP210");
		assertTrue(consistsOfPersons(wp210.personsWorkingHere, new String[] { "MA", "WE", "T", "PA" }));
		
		//test calculateHours
		Partner cat = project.consortialPartner.partners.get(0);
		Partner loa = project.consortialPartner.partners.get(1);
		assertEquals(cat.name,"CAT");
		assertEquals(loa.name,"LOA");
		Person cf = cat.persons.get(0);
		assertEquals(cf.name, "CF");
		assertEquals(cf.hoursInPackages[0], 58.75, 0.01); //changed
		assertEquals(cf.hoursInPackages[2], 693.75, 0.01); //changed
		Person wp = cat.persons.get(1);
		assertEquals(wp.name, "WP");
		assertEquals(wp.hoursInPackages[0], 30, 0.01); //changed
		Person ma = cat.persons.get(2);
		assertEquals(ma.name, "MA");
		assertEquals(ma.hoursInPackages[1], 10.66, 0.01);
		assertEquals(ma.hoursInPackages[2], 133.33, 0.01);
		assertEquals(ma.hoursInPackages[4], 13.33, 0.01);
		Person we = cat.persons.get(3);
		assertEquals(we.name, "WE");
		assertEquals(we.hoursInPackages[1], 81.77, 0.01);
		assertEquals(we.hoursInPackages[4], 102.22, 0.01);
		
		Person o = loa.persons.get(0);
		assertEquals(o.name, "O");
		assertEquals(o.hoursInPackages[0], 62.61, 0.01);
		assertEquals(o.hoursInPackages[2], 600, 0.01);
		Person p = loa.persons.get(1);
		assertEquals(p.name, "P");
		assertEquals(p.hoursInPackages[0], 1022.61, 0.01);
		assertEquals(p.hoursInPackages[3], 133.33, 0.01);
		Person t = loa.persons.get(2);
		assertEquals(t.name, "T");
		assertEquals(t.hoursInPackages[0], 62.61, 0.01);
		assertEquals(t.hoursInPackages[1], 49.231, 0.01);
		assertEquals(t.hoursInPackages[2], 160, 0.01);
		assertEquals(t.hoursInPackages[3], 750.77, 0.01);
		assertEquals(t.hoursInPackages[4], 46.15, 0.01);
		Person pa = loa.persons.get(3);
		assertEquals(pa.name, "PA");
		assertEquals(pa.hoursInPackages[1], 369.23, 0.01);
		assertEquals(pa.hoursInPackages[3], 430.77, 0.01);
		assertEquals(pa.hoursInPackages[4], 346.15, 0.01);
	}
	
	@Test
	public void calculateCostsPerPartnerTest(){
		project.calculateWorkpackages();
		project.calculateCostsPerPartner();
		assertEquals(project.consortialPartnerCostsSums[0], 53000, 0.01);
		assertEquals(project.consortialPartnerCostsSums[1], 147000, 0.01);
	}
	
	@Test
	public void calculateOverallCostsTest(){
		project.calculateWorkpackages();
		project.calculateCostsPerPartner();
		project.calculateOverallCosts();
		assertEquals(project.overallCosts, 200000, 0.01);
	}
	
	@Test
	public void getRealCostsTest(){
		project.calculateWorkpackages();
		project.calculateCostsPerPartner();
		project.calculateOverallCosts();
		
		Workpackage wp000 = project.workpackages.get(0);
		Workpackage wp100 = project.workpackages.get(1);
		Workpackage wp110 = project.workpackages.get(2);
		Workpackage wp200 = project.workpackages.get(3);
		Workpackage wp210 = project.workpackages.get(4);
		assertEquals(wp000.name, "WP000");
		assertEquals(wp100.name, "WP100");
		assertEquals(wp110.name, "WP110");
		assertEquals(wp200.name, "WP200");
		assertEquals(wp210.name, "WP210");
		assertEquals(wp000.costs, 40000, 0.01);
		assertEquals(wp100.costs, 20000, 0.01);
		assertEquals(wp110.costs, 80000, 0.01);
		assertEquals(wp200.costs, 40000, 0.01);
		assertEquals(wp210.costs, 20000, 0.01);
	}
	
	@Test
	public void calculateOverallHoursTest(){
		project.calculateWorkpackages();
		project.calculateCostsPerPartner();
		project.calculateOverallCosts();
		project.calculateOverallHours();
		assertEquals(project.overallHours, 5307.30, 0.01);
	}
	
	@Test
	public void calculateCostsPerPartnerPerWorkpackageTest(){
		project.calculateWorkpackages();
		project.calculateCostsPerPartner();
		project.calculateOverallCosts();
		project.calculateOverallHours();
		project.calculateCostsPerPartnerPerWorkpackage();
		assertEquals(project.consortialPartnerCosts[0][0], 4000, 0.01);
		assertEquals(project.consortialPartnerCosts[0][1], 4000, 0.01);
		assertEquals(project.consortialPartnerCosts[0][2], 40000, 0.01);
		assertEquals(project.consortialPartnerCosts[0][3], 0, 0.01);
		assertEquals(project.consortialPartnerCosts[0][4], 5000, 0.01);
		
		assertEquals(project.consortialPartnerCosts[1][0], 36000, 0.01);
		assertEquals(project.consortialPartnerCosts[1][1], 16000, 0.01);
		assertEquals(project.consortialPartnerCosts[1][2], 40000, 0.01);
		assertEquals(project.consortialPartnerCosts[1][3], 40000, 0.01);
		assertEquals(project.consortialPartnerCosts[1][4], 15000, 0.01);
	}
	
	public boolean containsPerson(ArrayList<Person> list, String person){
		for(Person p : list){
			if(p.name.equals(person)){
				return true;
			}
		}
		return false;
	}
	
	public boolean consistsOfPersons(ArrayList<Person> list, String[] persons){
		for(String person : persons){
			if(!containsPerson(list, person))
				return false;
			list.remove(0);
		}
		if(list.size() != 0)
			return false;
		return true;
	}

}
