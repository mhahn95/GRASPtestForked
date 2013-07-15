import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.Test;


public class TaskTest {

	@Test
	public void test() throws NumberFormatException, IOException, ParseException {
		String dataFile = "09 - Planning of tasks and work packages.properties";
		String importFile = "01 - CalculatedProject.csv";
		String meetingFilename = "13 - The meeting schedule with detailed costs is shown.properties";
		String workpackageFilename = "07 - Summary table of work packages.properties";
		
		Project project = IOProject.importProject(importFile);
		ArrayList<Task> tasks = TaskTable.readTaskData(dataFile, meetingFilename, workpackageFilename, project);
		
		Task ko = tasks.get(0);
		Task pm1 = tasks.get(1);
		Task pm2 = tasks.get(2);
		Task fp = tasks.get(3);
		Task wp000 = tasks.get(4);
		Task wp100 = tasks.get(5);
		Task wp110 = tasks.get(6);
		Task wp200 = tasks.get(7);
		Task wp210 = tasks.get(8);
		Task wp300 = tasks.get(9);
		Task wp400 = tasks.get(10);
		Task wp410 = tasks.get(11);
		Task wp500 = tasks.get(12);
		Task wp600 = tasks.get(13);
		
		assertEquals(1, ko.id);
		assertEquals(2, pm1.id);
		assertEquals(3, pm2.id);
		assertEquals(4, fp.id);
		assertEquals(5, wp000.id);
		assertEquals(6, wp100.id);
		assertEquals(7, wp110.id);
		assertEquals(8, wp200.id);
		assertEquals(9, wp210.id);
		assertEquals(10, wp300.id);
		assertEquals(11, wp400.id);
		assertEquals(12, wp410.id);
		assertEquals(13, wp500.id);
		assertEquals(14, wp600.id);
		
		assertEquals("Kick-Off", ko.name);
		assertEquals("Progress Meeting 1", pm1.name);
		assertEquals("Progress Meeting 2", pm2.name);
		assertEquals("Final Presentation", fp.name);
		assertEquals("WP000", wp000.name);
		assertEquals("WP100", wp100.name);
		assertEquals("WP110", wp110.name);
		assertEquals("WP200", wp200.name);
		assertEquals("WP210", wp210.name);
		assertEquals("WP300", wp300.name);
		assertEquals("WP400", wp400.name);
		assertEquals("WP410", wp410.name);
		assertEquals("WP500", wp500.name);
		assertEquals("WP600", wp600.name);
		
		assertEquals("", ko.description);
		assertEquals("", pm1.description);
		assertEquals("", pm2.description);
		assertEquals("", fp.description);
		assertEquals("Project Management", wp000.description);
		assertEquals("Hot spots scientific accelerating", wp100.description);
		assertEquals("Porting and accelerating the hot spot", wp110.description);
		assertEquals("Scientific accelerating the forward model", wp200.description);
		assertEquals("Porting and accelerating the forward model", wp210.description);
		assertEquals("Porting and accelerating the total aerosol retrievals algorithm code", wp300.description);
		assertEquals("Modifications to process MERIS/AATSR data", wp400.description);
		assertEquals("MERIS/AATSR preprocessing for applying the retrieval algorithm", wp410.description);
		assertEquals("Multi-Orbit processing", wp500.description);
		assertEquals("Scientific result verification", wp600.description);
		
		assertEquals(0, ko.duration);
		assertEquals(0, pm1.duration);
		assertEquals(0, pm2.duration);
		assertEquals(0, fp.duration);
		assertEquals(3, wp000.duration); //TODO calculate right durations
		assertEquals(1, wp100.duration);
		assertEquals(2, wp110.duration);
		assertEquals(2, wp200.duration);
		assertEquals(1, wp210.duration);
		assertEquals(4, wp300.duration);
		assertEquals(2, wp400.duration);
		assertEquals(0, wp410.duration);
		assertEquals(5, wp500.duration);
		assertEquals(0, wp600.duration);
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.GERMAN);
		
		assertEquals(df.parse("10.12.2012"), ko.start);
		assertEquals(df.parse("10.03.2013"), pm1.start);
		assertEquals(df.parse("08.06.2013"), pm2.start);
		assertEquals(df.parse("06.09.2013"), fp.start);
		assertEquals(df.parse("10.12.2012"), wp000.start);
		assertEquals(df.parse("10.12.2012"), wp100.start);
		assertEquals(df.parse("10.12.2012"), wp110.start);
		assertEquals(df.parse("10.12.2012"), wp200.start);
		assertEquals(df.parse("08.02.2013"), wp210.start);
		assertEquals(df.parse("10.03.2013"), wp300.start);
		assertEquals(df.parse("10.03.2013"), wp400.start);
		assertEquals(df.parse("10.12.2012"), wp410.start);
		assertEquals(df.parse("08.06.2013"), wp500.start);
		assertEquals(df.parse("08.06.2013"), wp600.start);
		
		assertEquals(df.parse("10.12.2012"), ko.finish);
		assertEquals(df.parse("10.03.2013"), pm1.finish);
		assertEquals(df.parse("08.06.2013"), pm2.finish);
		assertEquals(df.parse("06.09.2013"), fp.finish);
		
		assertEquals(df.parse("10.03.2013"), wp000.finish);
		assertEquals(df.parse("10.01.2013"), wp100.finish);
		assertEquals(df.parse("10.02.2013"), wp110.finish);
		assertEquals(df.parse("10.02.2013"), wp200.finish);
		assertEquals(df.parse("08.03.2013"), wp210.finish);
		assertEquals(df.parse("10.07.2013"), wp300.finish);
		assertEquals(df.parse("10.05.2013"), wp400.finish);
		assertEquals(df.parse("10.12.2012"), wp410.finish);
		assertEquals(df.parse("08.11.2013"), wp500.finish);
		assertEquals(df.parse("08.06.2013"), wp600.finish);
	}
}
