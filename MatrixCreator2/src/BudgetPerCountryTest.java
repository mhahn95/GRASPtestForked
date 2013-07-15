import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;


public class BudgetPerCountryTest {

	public ProjectConfigData pcd = new ProjectConfigData();
	public Project project;
	
	@Test
	public void testGetBudgetPercentPerCountry() throws IOException {
		pcd = MatrixCreator.readData("C:\\Users\\mhahn\\workspace\\MatrixCreator2\\src\\testdata.properties");
		project = MatrixCreator.configProject(pcd);
		project.calculateProject();
		
		double[] percents = BudgetPerCountry.getBudgetPercentPerCountry(project);
		
		assertEquals(26.5,percents[0],0.01);
		assertEquals(73.5,percents[1],0.01);
	}

}
