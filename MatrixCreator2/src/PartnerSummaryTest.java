import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;


public class PartnerSummaryTest {

	@Test
	public void test() throws IOException {
		String dataFile = "data.properties";
		
		final Project p = MatrixCreator.configProject(MatrixCreator.readData(dataFile));
		p.calculateProject();
		
		ArrayList<PartnerSummaryData> readData = PartnerSummaryTable.readPartnerSummaryData(dataFile, p);
		PartnerSummaryData cat = readData.get(0);
		PartnerSummaryData loa = readData.get(1);
		
		assertEquals("Catalysts GmbH", cat.cpFullName);
		assertEquals("Laboratoire d'Optique Atmospherique", loa.cpFullName);
		
		assertEquals("Consortium Leader", cat.cpRole);
		assertEquals("Subcontractor", loa.cpRole);
		
		assertEquals(p.consortialPartner.getPartnerByName("CAT"), cat.partner);
		assertEquals(p.consortialPartner.getPartnerByName("LOA"), loa.partner);
		
		assertEquals("Austria", cat.cpCountry);
		assertEquals("France", loa.cpCountry);
		
		assertEquals("MA", cat.cpLeader.name);
		assertEquals("O", loa.cpLeader.name);
	}

}
