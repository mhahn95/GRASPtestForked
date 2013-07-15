import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;


public class KeyPersonnelTest {

	@Test
	public void testKeyPersonnel() throws NumberFormatException, IOException {
		String importFile = "01 - CalculatedProject.csv";
		
		Project project = IOProject.importProject(importFile);
		
		ArrayList<PersonWithPercent> personnel = KeyPersonnel.calculatePersonPercents(project.consortialPartner.partners, project.overallHours);
		assertEquals(5.10, personnel.get(0).percent, 0.01);
		assertEquals(4.30, personnel.get(1).percent, 0.01);
		assertEquals(10.75, personnel.get(2).percent, 0.01);
		assertEquals(30.88, personnel.get(3).percent, 0.01);
		
		assertEquals(6.67, personnel.get(4).percent, 0.01);
		assertEquals(10.50, personnel.get(5).percent, 0.01);
		assertEquals(6.36, personnel.get(6).percent, 0.01);
		assertEquals(25.40, personnel.get(7).percent, 0.01);
	}

}
