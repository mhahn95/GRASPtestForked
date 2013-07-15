import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;


public class MilestoneTest {

	@Test
	public void test() throws NumberFormatException, IOException {
		String dataFile = "12 - The amounts of the proposed advance and progress payments are shown per partner.properties";
		String importFile = "01 - CalculatedProject.csv";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<Milestone> milestones = MilestoneTable.readMilestoneData(dataFile, project);
		
		Milestone ap = milestones.get(0);
		Milestone pp1 = milestones.get(1);
		Milestone fp = milestones.get(2);
		
		assertEquals("Advance payment", ap.name);
		assertEquals("Progress payment 1", pp1.name);
		assertEquals("Final payment", fp.name);
		
		assertEquals(31950, ap.getBudgetAmountsPerPartnerAndSum(project)[0], 0.01);
		assertEquals(28050, ap.getBudgetAmountsPerPartnerAndSum(project)[1], 0.01);
		assertEquals(60000, ap.getBudgetAmountsPerPartnerAndSum(project)[2], 0.01);
		
		assertEquals(42600, pp1.getBudgetAmountsPerPartnerAndSum(project)[0], 0.01);
		assertEquals(37400, pp1.getBudgetAmountsPerPartnerAndSum(project)[1], 0.01);
		assertEquals(80000, pp1.getBudgetAmountsPerPartnerAndSum(project)[2], 0.01);
		
		assertEquals(31950, fp.getBudgetAmountsPerPartnerAndSum(project)[0], 0.01);
		assertEquals(28050, fp.getBudgetAmountsPerPartnerAndSum(project)[1], 0.01);
		assertEquals(60000, fp.getBudgetAmountsPerPartnerAndSum(project)[2], 0.01);
	}

}
