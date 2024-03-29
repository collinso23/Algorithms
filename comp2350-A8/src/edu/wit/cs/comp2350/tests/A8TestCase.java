package edu.wit.cs.comp2350.tests;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.A8;
import edu.wit.cs.comp2350.A8.Item;

import static org.junit.Assert.*;


public class A8TestCase{

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

	private void _testDynBest(A8.Item[] items, int weight, int expectedBest) {
		int actualBest = 0;

		try {
			A8.FindDynamic(items, weight);
			actualBest = A8.getBest();
		} catch (ExitException e) {}

		assertEquals("Best value is incorrect.", expectedBest, actualBest);
	}

	private void _testDyn(A8.Item[] items, int weight, int expectedBest) {
		A8.Item[] actual = new A8.Item[0];
		int actualBest = 0;

		try {
			actual = A8.FindDynamic(items, weight);
			actualBest = A8.getBest();
		} catch (ExitException e) {}
		assertNotNull("Knapsack is unexpectedly null.", actual);

		int actualValues = 0;
		int actualWeight = 0;

		boolean[] isUsed = new boolean[items.length];

		for (int i = 0; i < actual.length; i++) {
			assertFalse("Item was already used", isUsed[actual[i].index]);
			isUsed[actual[i].index] = true;
			actualValues += items[actual[i].index].price;
			actualWeight += items[actual[i].index].weight;
		}

		assertEquals("Best value is incorrect.", expectedBest, actualBest);
		assertEquals("Values of items don't add up to best value.", actualValues, actualBest);
		assertTrue("The knapsack is overstuffed.", weight >= actualWeight);
	}

	private void _testFileDyn(String file, int weight, int expectedBest, boolean checkItems) {
		ArrayList<Item> tableList = new ArrayList<Item>();

		try (Scanner f = new Scanner(new File(file))) {
			int i = 0;
			while(f.hasNextInt())
				tableList.add(new A8.Item(f.nextInt(), f.nextInt(), i++));
		} catch (IOException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}

		Item[] table = new Item[tableList.size()];
		for (int i = 0; i < tableList.size(); i++)
			table[i] = tableList.get(i);
		if (checkItems)
			_testDyn(table, weight, expectedBest);
		else
			_testDynBest(table, weight, expectedBest);
	}

	@Test
	public void testNotEnoughRoom() {
		_testFileDyn("objects/small1", 5, 0, true);
		_testFileDyn("objects/large1", 80, 0, true);
	}

	@Test
	public void testTiny() {
		_testFileDyn("objects/tiny", 2, 5, true);
		_testFileDyn("objects/tiny", 3, 8, true);
		_testFileDyn("objects/tiny", 4, 10, true);
	}

	@Test
	public void testSmall() {
		_testFileDyn("objects/small1", 26, 51, true);
		_testFileDyn("objects/small1", 39, 76, true);
		_testFileDyn("objects/small1", 40, 78, true);
		_testFileDyn("objects/small2", 5, 28, true);
		_testFileDyn("objects/small2", 6, 32, true);
		_testFileDyn("objects/small2", 7, 36, true);

	}

	@Test
	public void testLarge() {
		_testFileDyn("objects/large1", 5000, 7647, true);
		_testFileDyn("objects/large1", 6000, 8263, true);
		_testFileDyn("objects/large2", 35, 444, true);
		_testFileDyn("objects/large2", 60, 612, true);
	}


	@Test
	public void testTinyBest() {
		_testFileDyn("objects/tiny", 2, 5, false);
		_testFileDyn("objects/tiny", 3, 8, false);
		_testFileDyn("objects/tiny", 4, 10, false);
	}

	@Test
	public void testSmallBest() {
		_testFileDyn("objects/small1", 26, 51, false);
		_testFileDyn("objects/small1", 39, 76, false);
		_testFileDyn("objects/small1", 40, 78, false);
		_testFileDyn("objects/small2", 5, 28, false);
		_testFileDyn("objects/small2", 6, 32, false);
		_testFileDyn("objects/small2", 7, 36, false);
	}


	@Test
	public void testLargeBest() {
		_testFileDyn("objects/large1", 5000, 7647, false);
		_testFileDyn("objects/large1", 6000, 8263, false);
		_testFileDyn("objects/large2", 35, 444, false);
		_testFileDyn("objects/large2", 60, 612, false);
	}


}
