import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class MatrixCreatorTest {
	
	public ProjectConfigData pcd = new ProjectConfigData();
	public Project project;
	
	@Before
	public void buildUp() throws IOException{
		pcd = MatrixCreator.readData("C:\\Users\\mhahn\\workspace\\MatrixCreator2\\src\\testdata.properties");
		project = MatrixCreator.configProject(pcd);
	}

	@Test
	public void readDataTest(){
		// budget = 200000.00
		assertTrue(pcd.budget == 200000);
		
		// workPackages = WP000, WP100, WP110, WP200, WP210
		assertEquals(pcd.wpNames[0], "WP000");
		assertEquals(pcd.wpNames[1], "WP100");
		assertEquals(pcd.wpNames[2], "WP110");
		assertEquals(pcd.wpNames[3], "WP200");
		assertEquals(pcd.wpNames[4], "WP210");
		
		// workPackageSegmentation = 20%, 10%, 40%, 20%, 10%
		assertTrue(pcd.wpPercentages[0] == 20);
		assertTrue(pcd.wpPercentages[1] == 10);
		assertTrue(pcd.wpPercentages[2] == 40);
		assertTrue(pcd.wpPercentages[3] == 20);
		assertTrue(pcd.wpPercentages[4] == 10);
		
		// consortialPartner = CAT, LOA
		assertEquals(pcd.pNames[0], "CAT");
		assertEquals(pcd.pNames[1], "LOA");
		
		// consortialPartner.CAT.segmentation = 10%, 20%, 50%, 0%, 25%
		assertTrue(pcd.pPercentages[0][0] == 10);
		assertTrue(pcd.pPercentages[0][1] == 20);
		assertTrue(pcd.pPercentages[0][2] == 50);
		assertTrue(pcd.pPercentages[0][3] == 0);
		assertTrue(pcd.pPercentages[0][4] == 25);
		// consortialPartner.LOA.segmentation = 90%, 80%, 50%, 100%, 75%
		assertTrue(pcd.pPercentages[1][0] == 90);
		assertTrue(pcd.pPercentages[1][1] == 80);
		assertTrue(pcd.pPercentages[1][2] == 50);
		assertTrue(pcd.pPercentages[1][3] == 100);
		assertTrue(pcd.pPercentages[1][4] == 75);
		
		// workPackage.WP000.segmentation = (TA, 20%); (RE, 80%)
		assertEquals(pcd.wpRoles[0][0], "TA");
		assertEquals(pcd.wpRoles[0][1], "RE");
		assertTrue(pcd.wpRoleSegmentation[0][0] == 20);
		assertTrue(pcd.wpRoleSegmentation[0][1] == 80);
		// workPackage.WP100.segmentation = (E, 80%); (T, 20%)
		assertEquals(pcd.wpRoles[1][0], "E");
		assertEquals(pcd.wpRoles[1][1], "T");
		assertTrue(pcd.wpRoleSegmentation[1][0] == 80);
		assertTrue(pcd.wpRoleSegmentation[1][1] == 20);
		// workPackage.WP110.segmentation = (PE, 90%); (DEV, 10%)
		assertEquals(pcd.wpRoles[2][0], "PE");
		assertEquals(pcd.wpRoles[2][1], "DEV");
		assertTrue(pcd.wpRoleSegmentation[2][0] == 90);
		assertTrue(pcd.wpRoleSegmentation[2][1] == 10);
		// workPackage.WP200.segmentation = (RE, 10%); (DEV, 20%); (T, 70%)
		assertEquals(pcd.wpRoles[3][0], "RE");
		assertEquals(pcd.wpRoles[3][1], "DEV");
		assertEquals(pcd.wpRoles[3][2], "T");
		assertTrue(pcd.wpRoleSegmentation[3][0] == 10);
		assertTrue(pcd.wpRoleSegmentation[3][1] == 20);
		assertTrue(pcd.wpRoleSegmentation[3][2] == 70);
		// workPackage.WP210.segmentation = (E, 80%); (T, 20%)
		assertEquals(pcd.wpRoles[4][0], "E");
		assertEquals(pcd.wpRoles[4][1], "T");
		assertTrue(pcd.wpRoleSegmentation[4][0] == 80);
		assertTrue(pcd.wpRoleSegmentation[4][1] == 20);
		
		// consortialPartner.CAT.people = (CF, 40); (WP, 55); (MA, 30); (WE, 45)
		// consortialPartner.CAT.CF = TA, PE
		Person cf = pcd.pPersons.get(0).get(0);
		assertEquals(cf.name, "CF");
		assertTrue(cf.hourly_rate == 40);
		assertEquals(cf.roles[0], "TA");
		assertEquals(cf.roles[1], "PE");
		// consortialPartner.CAT.WP = TA, RE, PE
		Person wp = pcd.pPersons.get(0).get(1);
		assertEquals(wp.name, "WP");
		assertTrue(wp.hourly_rate == 55);
		assertEquals(wp.roles[0], "TA");
		assertEquals(wp.roles[1], "RE");
		assertEquals(wp.roles[2], "PE");
		// consortialPartner.CAT.MA = DEV, T
		Person ma = pcd.pPersons.get(0).get(2);
		assertEquals(ma.name, "MA");
		assertTrue(ma.hourly_rate == 30);
		assertEquals(ma.roles[0], "DEV");
		assertEquals(ma.roles[1], "T");
		// consortialPartner.CAT.WE = T, E
		Person we = pcd.pPersons.get(0).get(3);
		assertEquals(we.name, "WE");
		assertTrue(we.hourly_rate == 45);
		assertEquals(we.roles[0], "T");
		assertEquals(we.roles[1], "E");
		// consortialPartner.LOA.people = (O, 60); (P, 30); (T, 25); (PA, 40)
		// consortialPartner.LOA.O = TA, PE
		Person o = pcd.pPersons.get(1).get(0);
		assertEquals(o.name, "O");
		assertTrue(o.hourly_rate == 60);
		assertEquals(o.roles[0], "TA");
		assertEquals(o.roles[1], "PE");
		// consortialPartner.LOA.P = TA, RE
		Person p = pcd.pPersons.get(1).get(1);
		assertEquals(p.name, "P");
		assertTrue(p.hourly_rate == 30);
		assertEquals(p.roles[0], "TA");
		assertEquals(p.roles[1], "RE");
		// consortialPartner.LOA.T = DEV, T, TA
		Person t = pcd.pPersons.get(1).get(2);
		assertEquals(t.name, "T");
		assertTrue(t.hourly_rate == 25);
		assertEquals(t.roles[0], "DEV");
		assertEquals(t.roles[1], "T");
		assertEquals(t.roles[2], "TA");
		// consortialPartner.LOA.PA = T, E
		Person pa = pcd.pPersons.get(1).get(3);
		assertEquals(pa.name, "PA");
		assertTrue(pa.hourly_rate == 40);
		assertEquals(pa.roles[0], "T");
		assertEquals(pa.roles[1], "E");
	}
	
	@Test
	public void configProjectTest(){
		assertTrue(project.budget == 200000);
		
		// consortialPartner.CAT.segmentation = 10%, 20%, 50%, 0%, 25%
		assertTrue(project.consortialPartnerPercentages[0][0] == 10);
		assertTrue(project.consortialPartnerPercentages[0][1] == 20);
		assertTrue(project.consortialPartnerPercentages[0][2] == 50);
		assertTrue(project.consortialPartnerPercentages[0][3] == 0);
		assertTrue(project.consortialPartnerPercentages[0][4] == 25);
		// consortialPartner.LOA.segmentation = 90%, 80%, 50%, 100%, 75%
		assertTrue(project.consortialPartnerPercentages[1][0] == 90);
		assertTrue(project.consortialPartnerPercentages[1][1] == 80);
		assertTrue(project.consortialPartnerPercentages[1][2] == 50);
		assertTrue(project.consortialPartnerPercentages[1][3] == 100);
		assertTrue(project.consortialPartnerPercentages[1][4] == 75);
		
		// workPackageSegmentation = 20%, 10%, 40%, 20%, 10%
		assertTrue(project.workpackagePercentagesSum == 100);
		// workPackages = WP000, WP100, WP110, WP200, WP210
		// workPackageSegmentation = 20%, 10%, 40%, 20%, 10%
		// workPackage.WP000.segmentation = (TA, 20%); (RE, 80%)
		Workpackage wp000 = project.workpackages.get(0);
		assertEquals(wp000.name, "WP000");
		assertTrue(wp000.costs == 40000);
		assertTrue(wp000.percentage == 20);
		assertEquals(wp000.roles[0], "TA");
		assertEquals(wp000.roles[1], "RE");
		assertTrue(wp000.roleSegmentation[0] == 20);
		assertTrue(wp000.roleSegmentation[1] == 80);
		assertTrue(wp000.positionOfThisPackage == 0);
		// workPackage.WP100.segmentation = (E, 80%); (T, 20%)
		Workpackage wp100 = project.workpackages.get(1);
		assertEquals(wp100.name, "WP100");
		assertTrue(wp100.costs == 20000);
		assertTrue(wp100.percentage == 10);
		assertEquals(wp100.roles[0], "E");
		assertEquals(wp100.roles[1], "T");
		assertTrue(wp100.roleSegmentation[0] == 80);
		assertTrue(wp100.roleSegmentation[1] == 20);
		assertTrue(wp100.positionOfThisPackage == 1);
		// workPackage.WP110.segmentation = (PE, 90%); (DEV, 10%)
		Workpackage wp110 = project.workpackages.get(2);
		assertEquals(wp110.name, "WP110");
		assertTrue(wp110.costs == 80000);
		assertTrue(wp110.percentage == 40);
		assertEquals(wp110.roles[0], "PE");
		assertEquals(wp110.roles[1], "DEV");
		assertTrue(wp110.roleSegmentation[0] == 90);
		assertTrue(wp110.roleSegmentation[1] == 10);
		assertTrue(wp110.positionOfThisPackage == 2);
		// workPackage.WP200.segmentation = (RE, 10%); (DEV, 20%); (T, 70%)
		Workpackage wp200 = project.workpackages.get(3);
		assertEquals(wp200.name, "WP200");
		assertTrue(wp200.costs == 40000);
		assertTrue(wp200.percentage == 20);
		assertEquals(wp200.roles[0], "RE");
		assertEquals(wp200.roles[1], "DEV");
		assertEquals(wp200.roles[2], "T");
		assertTrue(wp200.roleSegmentation[0] == 10);
		assertTrue(wp200.roleSegmentation[1] == 20);
		assertTrue(wp200.roleSegmentation[2] == 70);
		assertTrue(wp200.positionOfThisPackage == 3);
		// workPackage.WP210.segmentation = (E, 80%); (T, 20%)
		Workpackage wp210 = project.workpackages.get(4);
		assertEquals(wp210.name, "WP210");
		assertTrue(wp210.costs == 20000);
		assertTrue(wp210.percentage == 10);
		assertEquals(wp210.roles[0], "E");
		assertEquals(wp210.roles[1], "T");
		assertTrue(wp210.roleSegmentation[0] == 80);
		assertTrue(wp210.roleSegmentation[1] == 20);
		assertTrue(wp210.positionOfThisPackage == 4);
		
		// consortialPartner = CAT, LOA
		// consortialPartner.CAT.people = (CF, 40); (WP, 55); (MA, 30); (WE, 45)
		// consortialPartner.CAT.segmentation = 10%, 20%, 50%, 0%, 25%
		Partner cat = project.consortialPartner.partners.get(0);
		assertEquals(cat.name, "CAT");
		assertTrue(cat.percentages[0] == 10);
		assertTrue(cat.percentages[1] == 20);
		assertTrue(cat.percentages[2] == 50);
		assertTrue(cat.percentages[3] == 0);
		assertTrue(cat.percentages[4] == 25);
		// consortialPartner.CAT.CF = TA, PE
		Person cf = cat.persons.get(0);
		assertEquals(cf.name, "CF");
		assertTrue(cf.hourly_rate == 40);
		assertEquals(cf.roles[0], "TA");
		assertEquals(cf.roles[1], "PE");
		// consortialPartner.CAT.WP = TA, RE, PE
		Person wp = cat.persons.get(1);
		assertEquals(wp.name, "WP");
		assertTrue(wp.hourly_rate == 55);
		assertEquals(wp.roles[0], "TA");
		assertEquals(wp.roles[1], "RE");
		assertEquals(wp.roles[2], "PE");
		// consortialPartner.CAT.MA = DEV, T
		Person ma = cat.persons.get(2);
		assertEquals(ma.name, "MA");
		assertTrue(ma.hourly_rate == 30);
		assertEquals(ma.roles[0], "DEV");
		assertEquals(ma.roles[1], "T");
		// consortialPartner.CAT.WE = T, E
		Person we = cat.persons.get(3);
		assertEquals(we.name, "WE");
		assertTrue(we.hourly_rate == 45);
		assertEquals(we.roles[0], "T");
		assertEquals(we.roles[1], "E");
		// consortialPartner.LOA.people = (O, 60); (P, 30); (T, 25); (PA, 40)
		// consortialPartner.LOA.segmentation = 90%, 80%, 50%, 100%, 75%
		Partner loa = project.consortialPartner.partners.get(1);
		assertEquals(loa.name, "LOA");
		assertTrue(loa.percentages[0] == 90);
		assertTrue(loa.percentages[1] == 80);
		assertTrue(loa.percentages[2] == 50);
		assertTrue(loa.percentages[3] == 100);
		assertTrue(loa.percentages[4] == 75);
		// consortialPartner.LOA.O = TA, PE
		Person o = loa.persons.get(0);
		assertEquals(o.name, "O");
		assertTrue(o.hourly_rate == 60);
		assertEquals(o.roles[0], "TA");
		assertEquals(o.roles[1], "PE");
		// consortialPartner.LOA.P = TA, RE
		Person p_ = loa.persons.get(1);
		assertEquals(p_.name, "P");
		assertTrue(p_.hourly_rate == 30);
		assertEquals(p_.roles[0], "TA");
		assertEquals(p_.roles[1], "RE");
		// consortialPartner.LOA.T = DEV, T, TA
		Person t = loa.persons.get(2);
		assertEquals(t.name, "T");
		assertTrue(t.hourly_rate == 25);
		assertEquals(t.roles[0], "DEV");
		assertEquals(t.roles[1], "T");
		assertEquals(t.roles[2], "TA");
		// consortialPartner.LOA.PA = T, E
		Person pa = loa.persons.get(3);
		assertEquals(pa.name, "PA");
		assertTrue(pa.hourly_rate == 40);
		assertEquals(pa.roles[0], "T");
		assertEquals(pa.roles[1], "E");
	}
	
	@Test
	public void calculateConsortialPartnerPercentagesSumsTest(){
		project.calculateConsortialPartnerPercentagesSums();
		assertTrue(project.consortialPartnerPercentagesSums[0] == 100);
		assertTrue(project.consortialPartnerPercentagesSums[1] == 100);
		assertTrue(project.consortialPartnerPercentagesSums[2] == 100);
		assertTrue(project.consortialPartnerPercentagesSums[3] == 100);
		assertTrue(project.consortialPartnerPercentagesSums[4] == 100);
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
		assertEquals(cf.hoursInPackages[0], 8.42, 0.01);
		assertEquals(cf.hoursInPackages[2], 378.95, 0.01);
		Person wp = cat.persons.get(1);
		assertEquals(wp.name, "WP");
		assertEquals(wp.hoursInPackages[0], 66.60, 0.01);
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
		assertEquals(project.overallHours, 5207.71, 0.01);
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