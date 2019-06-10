package edu.wit.cs.comp2350.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.Speller;
import edu.wit.cs.comp2350.Trie;

import static org.junit.Assert.*;


public class A6TestCase{


	@Rule
	public Timeout globalTimeout = Timeout.millis(1500);

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

	private void _testContains(Speller S, String word) {
		boolean result = false;
		try {
			result = S.contains(word);
		} catch (ExitException e) {}
		assertTrue("Contains did not find word \"" + word + "\".", result);
	}

	private void _testSuggestions(Speller S, String word, String[] expected) {
		String[] actual = new String[0];
		boolean result = false;
		try {
			actual = S.getSuggestions(word);
			result = S.contains(word);
		} catch (ExitException e) {}

		assertNotNull("Suggestions array is unexpectedly null", actual);
		assertTrue("contains found word \"" + word + "\" when it shouldn't have.", !result);
		assertEquals("Didn't find the correct number of suggestions.", expected.length, actual.length);
		for (int i = 0; i < expected.length; i++)
			assertEquals("Didn't find the correct suggestion.", expected[i], actual[i]);
	}

	private void populateSpeller(Speller l, String fileName) {

		try (Scanner s = new Scanner(new File(fileName))) {
			// loop over all input words
			while (s.hasNext()) {
				String w = s.next().toLowerCase().replaceAll("[^a-z ]","");
				l.insertWord(w);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file " + fileName + ". Exiting.");
			System.exit(0);
		}
	}

	@Test
	public void testSmallContains() {
		Speller T = new Trie();
		populateSpeller(T, "text/small.txt");

		_testContains(T, "they");
		_testContains(T, "people");
		_testContains(T, "a");
		_testContains(T, "we");
	}

	@Test
	public void testSmallSuggestions() {
		Speller T = new Trie();
		populateSpeller(T, "text/small.txt");

		String[] expected = {"than", "that", "them", "then", "they", "this", "what", "when"};
		_testSuggestions(T, "thet", expected);
		String[] expected2 = {"find", "like", "long"};
		_testSuggestions(T, "lint", expected2);
		String[] expected3 = {"can", "day", "for", "had", "has", "her", "may", "was", "way"};
		_testSuggestions(T, "par", expected3);

	}

	@Test
	public void test10000Contains() {
		Speller T = new Trie();
		populateSpeller(T, "text/10000.txt");

		_testContains(T, "level");
		_testContains(T, "accurately");
		_testContains(T, "likelihood");
		_testContains(T, "mon");
	}

	@Test
	public void test10000Suggestions() {
		Speller T = new Trie();
		populateSpeller(T, "text/10000.txt");

		String[] expected = {"cakes", "danny", "dates", "davis", "gains", "lakes", "makes", "takes"};
		_testSuggestions(T, "dakns", expected);
		String[] expected2 = {"adams", "beads", "beans", "bears", "beats", "deals", "exams", "fears", "grams", "heads", "heard", "heart", "heath", "heavy", "helps", "herbs", "jeans", "leads", "meals", "means", "reads", "seats", "seems", "teams", "tears", "terms", "years"};
		_testSuggestions(T, "heams", expected2);
		String[] expected3 = {};
		_testSuggestions(T, "bengan", expected3);
		String[] expected4 = {"alpine", "claire", "empire"};
		_testSuggestions(T, "alpire", expected4);
		String[] expected5 = {"international"};
		_testSuggestions(T, "intercational", expected5);
		String[] expected6 = {};
		_testSuggestions(T, "qqqadministrationde", expected6);
	}

	@Test
	public void testLongContains() {
		Speller T = new Trie();
		populateSpeller(T, "text/long.txt");

		_testContains(T, "intinctivity");
		_testContains(T, "prepaleolithic");
		_testContains(T, "sulphocinnamic");
		_testContains(T, "diphthongization");
	}

	@Test(timeout = 500)
	public void testLongSuggFast() {
		Speller T = new Trie();
		populateSpeller(T, "text/long.txt");

		String[] expected = {"disorientated", "disorientates"};
		_testSuggestions(T, "disarientated", expected);
		String[] expected2 = {"legitimatise", "legitimatist", "legitimatize"};
		_testSuggestions(T, "legitibatise", expected2);
		String[] expected3 = {"unexpeditable"};
		_testSuggestions(T, "unexpeditablu", expected3);
		String[] expected4 = {"parochialise", "parochialism", "parochialist"};
		_testSuggestions(T, "parochoalist", expected4);
		String[] expected5 = {"bioflavinoid", "bioflavonoid"};
		_testSuggestions(T, "bioglavonoid", expected5);
		String[] expected6 = {};
		_testSuggestions(T, "infantimatorialism", expected6);
	}

	@Test
	public void testLongSuggCorrect() {
		Speller T = new Trie();
		populateSpeller(T, "text/long.txt");

		String[] expected = {"disorientated", "disorientates"};
		_testSuggestions(T, "disarientated", expected);
		String[] expected2 = {"legitimatise", "legitimatist", "legitimatize"};
		_testSuggestions(T, "legitibatise", expected2);
		String[] expected3 = {"unexpeditable"};
		_testSuggestions(T, "unexpeditablu", expected3);
		String[] expected4 = {"parochialise", "parochialism", "parochialist"};
		_testSuggestions(T, "parochoalist", expected4);
		String[] expected5 = {"bioflavinoid", "bioflavonoid"};
		_testSuggestions(T, "bioglavonoid", expected5);
		String[] expected6 = {};
		_testSuggestions(T, "infantimatorialism", expected6);
	}


	@Test(timeout = 400)
	public void test10000Fast() {
		String file = "text/10000.txt";
		ArrayList<String> words = new ArrayList<String>();
		Random r = new Random();

		try (Scanner s = new Scanner(new File(file))) {
			Speller l = new Trie();

			while (s.hasNext()) {
				String w = s.next().toLowerCase().replaceAll("[^a-z ]","");
				l.insertWord(w);
				words.add(w);
			}

			for (int i = 0; i < 100; i++) {
				// merge two halves of two words in the list to make a (probably) made-up word
				String word = shmerge(words.get(r.nextInt(words.size()-1)), words.get(r.nextInt(words.size()-1)));

				if (!l.contains(word))
					l.getSuggestions(word);
			}
		}
		catch (FileNotFoundException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}
	}

	private String shmerge(String s1, String s2) {
		return s1.substring(0, s1.length()/2) + s2.substring(s2.length()/2, s2.length());
	}

	}
