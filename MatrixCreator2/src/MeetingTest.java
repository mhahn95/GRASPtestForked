import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;


public class MeetingTest {

	@Test
	public void test() throws NumberFormatException, IOException {
		String dataFile = "13 - The meeting schedule with detailed costs is shown.properties";
		String importFile = "01 - CalculatedProject.csv";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<Meeting> meetings = MeetingTable.readMeetingData(dataFile, project);
		
		Meeting ko = meetings.get(0);
		Meeting pm1 = meetings.get(1);
		Meeting pm2 = meetings.get(2);
		Meeting fp = meetings.get(3);
		
		assertEquals("KO", ko.name);
		assertEquals("PM_1", pm1.name);
		assertEquals("PM_2", pm2.name);
		assertEquals("FP", fp.name);
		
		assertEquals("Kick-Off", ko.description);
		assertEquals("Progress Meeting 1", pm1.description);
		assertEquals("Progress Meeting 2", pm2.description);
		assertEquals("Final Presentation", fp.description);
		
		assertEquals(0, ko.timeline, 0.01);
		assertEquals(3, pm1.timeline, 0.01);
		assertEquals(6, pm2.timeline, 0.01);
		assertEquals(9, fp.timeline, 0.01);
		
		assertEquals("Telco", ko.location);
		assertEquals("Telco", pm1.location);
		assertEquals("ESRIN", pm2.location);
		assertEquals("ESRIN", fp.location);
		
		assertEquals(3, ko.personCountPerPartner[0]);
		assertEquals(1, ko.personCountPerPartner[1]);
		assertEquals(4, pm1.personCountPerPartner[0]);
		assertEquals(3, pm1.personCountPerPartner[1]);
		assertEquals(3, pm2.personCountPerPartner[0]);
		assertEquals(1, pm2.personCountPerPartner[1]);
		assertEquals(3, fp.personCountPerPartner[0]);
		assertEquals(1, fp.personCountPerPartner[1]);
		
		assertEquals(0, ko.costsPerPartner[0], 0.01);
		assertEquals(0, ko.costsPerPartner[1], 0.01);
		assertEquals(0, pm1.costsPerPartner[0], 0.01);
		assertEquals(0, pm1.costsPerPartner[1], 0.01);
		assertEquals(1300, pm2.costsPerPartner[0], 0.01);
		assertEquals(600, pm2.costsPerPartner[1], 0.01);
		assertEquals(1300, fp.costsPerPartner[0], 0.01);
		assertEquals(600, fp.costsPerPartner[1], 0.01);
	}
}
