import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;


public class IOProjectTest {
	@Test
	public void testExportImportProject() throws IOException {
		String dataFile = "data.properties";
		String outputFile = "01 - CalculatedProject.csv";
		final Project p = MatrixCreator.configProject(MatrixCreator.readData(dataFile));
		p.calculateProject();
		IOProject.exportProject(p, outputFile);
		final Project result = IOProject.importProject(outputFile);
		
		int[] outputData = new int[]{20,48,17,17,14,10,12};
		MatrixCreator.print(p, outputData);
		System.out.println();
		MatrixCreator.print(result, outputData);
		
		assertEquals(result, p);
	}

}
