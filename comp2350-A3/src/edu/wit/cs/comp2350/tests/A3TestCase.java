package edu.wit.cs.comp2350.tests;

import java.security.Permission;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import edu.wit.cs.comp2350.Coord;
import edu.wit.cs.comp2350.A3;

public class A3TestCase{


	@Rule
	public Timeout globalTimeout = Timeout.seconds(15);

	@SuppressWarnings("serial")
	private static class ExitException extends SecurityException {}

	private static class NoExitSecurityManager extends SecurityManager 
	{
		@Override
		public void checkPermission(Permission perm) {}

		@Override
		public void checkPermission(Permission perm, Object context) {}

		@Override
		public void checkExit(int status) { super.checkExit(status); throw new ExitException(); }
	}

	@Before
	public void setUp() throws Exception 
	{
		System.setSecurityManager(new NoExitSecurityManager());
	}

	@After
	public void tearDown() throws Exception 
	{
		System.setSecurityManager(null);
	}	


	private void _testQSorts(Coord[] actual, Coord[] expected) {

		A3.systemSort(expected);
		try {
			A3.quickSort(actual);
		} catch (Exception e) {}

		assertEquals("Array is the wrong length!", expected.length, actual.length);
		for (int i = 1; i < actual.length; i++)
			assertEquals("Value in position " + i + " does not match solution", expected[i].getDist(), actual[i].getDist(), 1E-8);
	}

	private void _testRSorts(Coord[] actual, Coord[] expected) {

		A3.systemSort(expected);
		try {
			A3.randQuickSort(actual);
		} catch (Exception e) {}

		assertEquals("Array is the wrong length!", expected.length, actual.length);
		for (int i = 1; i < actual.length; i++)
			assertEquals("Value in position " + i + " does not match solution", expected[i].getDist(), actual[i].getDist(), 1E-8);
	}


	@Test
	public void testSimpleQuick() {
		Coord start = new Coord(0, 0);

		Coord[] d = new Coord[3];
		d[0] = new Coord(1, 1, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(3, 3, start);

		Coord[] e = d.clone();

		_testQSorts(e, d);

		d[0] = new Coord(3, 3, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(1, 1, start);

		e = d.clone();

		_testQSorts(e, d);
	}


	@Test
	public void testSmallQuick() {
		Coord start = new Coord(0, 0);

		Coord[] d = new Coord[7];
		d[0] = new Coord(1, 1, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(3, 3, start);
		d[3] = new Coord(2, 2, start);
		d[4] = new Coord(1, 3, start);
		d[5] = new Coord(3, 1, start);
		d[6] = new Coord(3, 0, start);

		Coord[] e = d.clone();

		_testQSorts(e, d);

		d[0] = new Coord(3, 3, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(1, 1, start);
		d[3] = new Coord(.5, -.5, start);
		d[4] = new Coord(1, -10, start);
		d[5] = new Coord(3, 1, start);
		d[6] = new Coord(0, 1, start);

		e = d.clone();

		_testQSorts(e, d);
	}

	@Test
	public void testBigQuick() {
		Coord[] d = _buildBigCoords();
		Coord[] e = d.clone();

		_testQSorts(e, d);
	}

	@Test
	public void testStepsQuick() {
		Coord office = new Coord(42.3366322, -71.0942150);
		Coord[] d = new Coord[5000];

		double step = .001953125;
		double val = step;
		for (int i = 0; i < 4999; i++) {
			d[i] = new Coord(val, val, office);
			val += step;
		}
		d[4999] = office;

		Coord[] e = d.clone();

		_testQSorts(d, e);

		val = step;
		for (int i = 4999; i >= 0; i--) {
			d[i] = new Coord(val, val, office);
			val += step;
		}

		e = d.clone();

		_testQSorts(d, e);

	}


	@Test
	public void testRandQuick() {

		for (int i = 0; i < 10; i++) {
			Coord[] d = _generateRandArray(5000);
			Coord[] e = d.clone();

			_testQSorts(d, e);
		}
	}



	@Test
	public void testSimpleRandQ() {
		Coord start = new Coord(0, 0);

		Coord[] d = new Coord[3];
		d[0] = new Coord(1, 1, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(3, 3, start);

		Coord[] e = d.clone();

		_testRSorts(e, d);

		d[0] = new Coord(3, 3, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(1, 1, start);

		e = d.clone();

		_testRSorts(e, d);
	}


	@Test
	public void testSmallRandQ() {
		Coord start = new Coord(0, 0);

		Coord[] d = new Coord[7];
		d[0] = new Coord(1, 1, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(3, 3, start);
		d[3] = new Coord(2, 2, start);
		d[4] = new Coord(1, 3, start);
		d[5] = new Coord(3, 1, start);
		d[6] = new Coord(3, 0, start);

		Coord[] e = d.clone();

		_testRSorts(e, d);

		d[0] = new Coord(3, 3, start);
		d[1] = new Coord(2, 2, start);
		d[2] = new Coord(1, 1, start);
		d[3] = new Coord(.5, -.5, start);
		d[4] = new Coord(1, -10, start);
		d[5] = new Coord(3, 1, start);
		d[6] = new Coord(0, 1, start);

		e = d.clone();

		_testRSorts(e, d);
	}

	@Test
	public void testBigRandQ() {
		Coord[] d = _buildBigCoords();
		Coord[] e = d.clone();

		_testRSorts(e, d);
	}

	@Test
	public void testStepsRandQ() {
		Coord office = new Coord(42.3366322, -71.0942150);
		Coord[] d = new Coord[5000];

		double step = .001953125;
		double val = step;
		for (int i = 0; i < 4999; i++) {
			d[i] = new Coord(val, val, office);
			val += step;
		}
		d[4999] = office;

		Coord[] e = d.clone();

		_testRSorts(d, e);


		val = step;
		for (int i = 4999; i > 0; i--) {
			d[i] = new Coord(val, val, office);
			val += step;
		}
		d[0] = office;
		e = d.clone();

		_testRSorts(d, e);
	}

	@Test
	public void testRandRandQ() {

		for (int i = 0; i < 10; i++) {
			Coord[] d = _generateRandArray(5000);
			Coord[] e = d.clone();

			_testRSorts(d, e);
		}
	}


	private Coord[] _generateRandArray(int size) {
		Coord[] ret = new Coord[size];

		Random r = new Random();

		Coord start = new Coord(r.nextDouble() * 180 - 90, r.nextDouble() * 360 - 180);
		for (int i = 0; i < size; i++) {
			ret[i] = new Coord(r.nextDouble() * 180 - 90, r.nextDouble() * 360 - 180, start);
		}
		return ret;
	}

	private Coord[] _buildBigCoords() {
		Coord start = new Coord(41.8781, -87.6298);	// Chicago

		Coord[] ret = new Coord[50];

		// Canadian cities
		ret[0] = new Coord(49.104431, -122.801094, start, "Surrey");
		ret[1] = new Coord(43.837208, -79.508278, start, "Vaughan");
		ret[2] = new Coord(42.778828, -81.175369, start, "St. Thomas");
		ret[3] = new Coord(49.283764, -122.793205, start, "Coquitlam");
		ret[4] = new Coord(43.897545, -78.942932, start, "Whitby");
		ret[5] = new Coord(49.166592, -123.133568, start, "Richmond");
		ret[6] = new Coord(47.560539, -52.71283, start, "St. John's");
		ret[7] = new Coord(45.826653, -77.110886, start, "Pembroke");
		ret[8] = new Coord(49.895077, -97.138451, start, "Winnipeg");
		ret[9] = new Coord(50.584435, -113.873505, start, "High River");
		ret[10] = new Coord(46.738228, -71.24646, start, "Levis");
		ret[11] = new Coord(45.964993, -66.646332, start, "Fredericton");
		ret[12] = new Coord(45.536945, -73.510712, start, "Longueuil");
		ret[13] = new Coord(33.039139, -117.295425, start, "Encinitas");
		ret[14] = new Coord(48.098709, -77.796768, start, "Val-d'Or");
		ret[15] = new Coord(43.856098, -79.337021, start, "Markham");
		ret[16] = new Coord(49.267132, -122.968941, start, "Burnaby");
		ret[17] = new Coord(44.389355, -79.690331, start, "Barrie");
		ret[18] = new Coord(45.273918, -66.067657, start, "Saint John");
		ret[19] = new Coord(43.328674, -79.817734, start, "Burlington");
		ret[20] = new Coord(49.165882, -123.940063, start, "Nanaimo");
		ret[21] = new Coord(43.159374, -79.246864, start, "St. Catharines");
		ret[22] = new Coord(39.813019, -104.93351, start, "Commerce City");
		ret[23] = new Coord(56.732014, -111.375961, start, "Fort McMurray");
		ret[24] = new Coord(43.6912, -79.341667, start, "East York");
		ret[25] = new Coord(42.404804, -82.19104, start, "Chatham-Kent");
		ret[26] = new Coord(50.27179, -119.276505, start, "Vernon");
		ret[27] = new Coord(43.59531, -79.640579, start, "Mississauga");
		ret[28] = new Coord(48.407326, -123.329773, start, "Victoria");
		ret[29] = new Coord(47.560539, -52.71283, start, "St. John's");
		ret[30] = new Coord(52.146973, -106.647034, start, "Saskatoon");
		ret[31] = new Coord(49.489536, -119.589615, start, "Penticton");
		ret[32] = new Coord(44.65107, -63.582687, start, "Halifax");
		ret[33] = new Coord(43.683334, -79.76667, start, "Brampton");
		ret[34] = new Coord(53.631611, -113.323975, start, "Edmonton");
		ret[35] = new Coord(43.526646, -79.891205, start, "Milton");
		ret[36] = new Coord(45.427944, -75.69883, start, "Major's Hill Park");
		ret[37] = new Coord(43.255203, -79.843826, start, "Hamilton");
		ret[38] = new Coord(42.317432, -83.026772, start, "Windsor");
		ret[39] = new Coord(46.803284, -71.242798, start, "Quebec");
		ret[40] = new Coord(42.992157, -79.248253, start, "Welland");
		ret[41] = new Coord(49.15794, -121.951469, start, "Chilliwack");
		ret[42] = new Coord(52.268112, -113.811241, start, "Red Deer");
		ret[43] = new Coord(46.490002, -81.010002, start, "Greater Sudbury");
		ret[44] = new Coord(46.090946, -64.790497, start, "Moncton");
		ret[45] = new Coord(44.608246, -79.419678, start, "Orillia");
		ret[46] = new Coord(43.887501, -79.428406, start, "Richmond Hill");
		ret[47] = new Coord(49.882114, -119.477829, start, "Kelowna");
		ret[48] = new Coord(45.404476, -71.888351, start, "Sherbrooke");
		ret[49] = new Coord(42.891411, -79.252075, start, "Port Colborne");

		return ret;
	}

}
