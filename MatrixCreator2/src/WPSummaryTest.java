import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;


public class WPSummaryTest {

	@Test
	public void test() throws IOException {
		String dataFile = "data.properties";
		
		final Project p = MatrixCreator.configProject(MatrixCreator.readData(dataFile));
		p.calculateProject();
		
		ArrayList<WPSummaryData> readData = WPSummaryTable.readWPSummaryData(dataFile, p);
		WPSummaryData wp000 = readData.get(0);
		WPSummaryData wp100 = readData.get(1);
		WPSummaryData wp110 = readData.get(2);
		WPSummaryData wp200 = readData.get(3);
		WPSummaryData wp210 = readData.get(4);
		WPSummaryData wp300 = readData.get(5);
		WPSummaryData wp400 = readData.get(6);
		WPSummaryData wp410 = readData.get(7);
		WPSummaryData wp500 = readData.get(8);
		WPSummaryData wp600 = readData.get(9);
		
		assertEquals("000", wp000.wpNumber);
		assertEquals("100", wp100.wpNumber);
		assertEquals("110", wp110.wpNumber);
		assertEquals("200", wp200.wpNumber);
		assertEquals("210", wp210.wpNumber);
		assertEquals("300", wp300.wpNumber);
		assertEquals("400", wp400.wpNumber);
		assertEquals("410", wp410.wpNumber);
		assertEquals("500", wp500.wpNumber);
		assertEquals("600", wp600.wpNumber);
		
		assertEquals("Project Management", wp000.wpDescription);
		assertEquals("Hot spots scientific accelerating", wp100.wpDescription);
		assertEquals("Porting and accelerating the hot spot", wp110.wpDescription);
		assertEquals("Scientific accelerating the forward model", wp200.wpDescription);
		assertEquals("Porting and accelerating the forward model", wp210.wpDescription);
		assertEquals("Porting and accelerating the total aerosol retrievals algorithm code", wp300.wpDescription);
		assertEquals("Modifications to process MERIS/AATSR data", wp400.wpDescription);
		assertEquals("MERIS/AATSR preprocessing for applying the retrieval algorithm", wp410.wpDescription);
		assertEquals("Multi-Orbit processing", wp500.wpDescription);
		assertEquals("Scientific result verification", wp600.wpDescription);
		
		assertEquals(p.consortialPartner.getPartnerByName("CAT"), wp000.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("LOA"), wp100.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("CAT"), wp110.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("LOA"), wp200.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("CAT"), wp210.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("CAT"), wp300.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("LOA"), wp400.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("LOA"), wp410.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("CAT"), wp500.wpLead);
		assertEquals(p.consortialPartner.getPartnerByName("LOA"), wp600.wpLead);
		
		assertEquals("MA", wp000.wpManager.name);
		assertEquals("O", wp100.wpManager.name);
		assertEquals("WP", wp110.wpManager.name);
		assertEquals("O", wp200.wpManager.name);
		assertEquals("WP", wp210.wpManager.name);
		assertEquals("MA", wp300.wpManager.name);
		assertEquals("O", wp400.wpManager.name);
		assertEquals("O", wp410.wpManager.name);
		assertEquals("MA", wp500.wpManager.name);
		assertEquals("O", wp600.wpManager.name);
		
		assertEquals(0, wp000.startMonth, 0.01);
		assertEquals(9, wp000.endMonth, 0.01);
		assertEquals(0, wp100.startMonth, 0.01);
		assertEquals(1.5, wp100.endMonth, 0.01);
		assertEquals(0, wp110.startMonth, 0.01);
		assertEquals(3, wp110.endMonth, 0.01);
		assertEquals(0, wp200.startMonth, 0.01);
		assertEquals(3, wp200.endMonth, 0.01);
		assertEquals(2, wp210.startMonth, 0.01);
		assertEquals(6, wp210.endMonth, 0.01);
		assertEquals(3, wp300.startMonth, 0.01);
		assertEquals(9, wp300.endMonth, 0.01);
		assertEquals(3, wp400.startMonth, 0.01);
		assertEquals(6, wp400.endMonth, 0.01);
		assertEquals(0, wp410.startMonth, 0.01);
		assertEquals(3, wp410.endMonth, 0.01);
		assertEquals(6, wp500.startMonth, 0.01);
		assertEquals(9, wp500.endMonth, 0.01);
		assertEquals(6, wp600.startMonth, 0.01);
		assertEquals(9, wp600.endMonth, 0.01);
		
		assertEquals("CAT, LOA", wp000.partnersInvolved);
		assertEquals("CAT, LOA", wp100.partnersInvolved);
		assertEquals("CAT, LOA", wp110.partnersInvolved);
		assertEquals("CAT, LOA", wp200.partnersInvolved);
		assertEquals("CAT, LOA", wp210.partnersInvolved);
		assertEquals("CAT, LOA", wp300.partnersInvolved);
		assertEquals("CAT, LOA", wp400.partnersInvolved);
		assertEquals("LOA", wp410.partnersInvolved);
		assertEquals("CAT", wp500.partnersInvolved);
		assertEquals("CAT, LOA", wp600.partnersInvolved);
	}

}
