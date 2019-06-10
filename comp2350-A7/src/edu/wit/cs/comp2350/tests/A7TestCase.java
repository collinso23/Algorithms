package edu.wit.cs.comp2350.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Permission;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.A7;

import static org.junit.Assert.*;


public class A7TestCase{

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

	private void _testDynLen(String s0, String s1, int expectedLongest) {
		int actualLongest = 0;

		try {
			A7.findLCSdyn(s0, s1);
			actualLongest = A7.getLongest();
		} catch (ExitException e) {}

		assertEquals("Subsequence length is incorrect.", expectedLongest, actualLongest);
	}

	private void _testFileDynLen(String f1, String f2, int expectedBest) {
		String t1 = "";
		String t2 = "";
		try {
			t1 = new String(Files.readAllBytes(Paths.get(f1)));
			t2 = new String(Files.readAllBytes(Paths.get(f2)));
		} catch (IOException e) {
		}

		_testDynLen(t1, t2, expectedBest);
	}

	private void _testDynStr(String s0, String s1, int expectedLongest) {
		String[] actual = new String[0];
		String act0 = "";
		String act1 = "";

		try {
			actual = A7.findLCSdyn(s0, s1);
		} catch (ExitException e) {}

		assertEquals("Length of output should be the same,", actual[0].length(), actual[1].length());

		for (int i = 0; i < actual[0].length(); i++) {
			if ((actual[0].charAt(i) != '-') && (actual[1].charAt(i) != '-'))
				assertEquals("Characters at same positions should be the same.", actual[0].charAt(i), actual[1].charAt(i));
			if (actual[0].charAt(i) != '-')
				act0 += actual[0].charAt(i);
			if (actual[1].charAt(i) != '-')
				act1 += actual[1].charAt(i);
		}
		assertEquals("First string doesn't match result.", s0, act0);
		assertEquals("Second string doesn't match result.", s1, act1);
	}

	private void _testFileDynStr(String f1, String f2, int expectedBest) {
		String t1 = "";
		String t2 = "";
		try {
			t1 = new String(Files.readAllBytes(Paths.get(f1)));
			t2 = new String(Files.readAllBytes(Paths.get(f2)));
		} catch (IOException e) {
		}

		_testDynStr(t1, t2, expectedBest);
	}

	@Test
	public void testSmallLen() {
		_testDynLen("overlap", "overlop", 6);
		_testDynLen("sap", "sip", 2);
		_testDynLen("ACGGAT", "CCGCTT", 3);
		_testDynLen("ACCCGAG", "TCCTACGG", 5);
		_testDynLen("big", "even bigger", 3);
	}	

	@Test
	public void testSmallStr() {
		_testDynStr("overlap", "overlop", 6);
		_testDynStr("sap", "sip", 2);
		_testDynStr("ACGGAT", "CCGCTT", 3);
		_testDynStr("ACCCGAG", "TCCTACGG", 5);
		_testDynStr("big", "even bigger", 3);
	}

	@Test
	public void testLargeLen() {
		_testFileDynLen("text/melania.txt", "text/michelle.txt", 330);
		_testDynLen("GTCGTTATGCTAGTATACGCCTACCCGTCACCGGCCATCTGTGTGCAGATGGGGCGACGAGTTACTGGCCCTGATTTCTCCGCTTCTAATACCACACACTGGGCAATACGAGCTCAAGCCAGTCTCGCAGTAACGCTCATCAGCTAACGAAAGAGTTAGAGGCTCGCTAATTCGCACTGTCGGGGTCCCTTGGGTGTTTTGCACTAGCGTCAGGTAGGCTAGTATGTGTCTTTCCTTCCAGGGGTATGTGGCTGCGTGGTCAAATGTGCAGCATACGTATTTGCTCGGCGTGCTTGGTCTCTCGTACTTCTCCTGGAGATCAAGGAAATGTTTCTTGTCCAAGCGGACAGCGGTTCTACGGAATGGATCTACGTTACTGCCTGCATAAGGAGAACGGAGTTGCCAAGGACGAAAGCGACTCTAGGTTCTAACCGTCGACTTTGGCGGAAAGGTTTCACTCAGGAAGCAGACACTGATTGACACGGTTTAGCAGAACGTTTGAGGATTAGGTCAAATTGAGTGGTTTAATATCGGTATGTCTGGGATTAAAATATAGTATAGTGCGCTGATCGGAGACGAATTAAAGACACGAGTTCCCAAAACCAGGCGGGCTCGCCACGACGGCTAATCCTGGTAGTTTACGTGAACAATGTTCTGAAGAAAATTTATGAAAGAAGGACCCGTCATCGCCTACAATTACCTACAACGGTCGACCATACCTTCGATTGTCGTGGCCACCCTCGGATTACACGGCAGAGGTGGTTGTGTTCCGATAGGCCAGCATATTATCCTAAGGCGTTACCCCAATCGTTTTCCGTCGGATTTGCTATAGCCCCTGAACGCTACATGCACGAAACCAAGTTATGTATGCACTGGGTCATCAATAGGACATAGCCTTGTAGTTAACATGTAGCCCGGCCGTATTAGTACAGTAGAGCCTTCACCGGCATTCTGTTTATTAAGTTATTTCTACAGCAAAACGATCATATGCAGATCCGCA", "GTGCGCGGTAGAGACACGTCCACCCGGCTGCTCTGTAATAGGGACTAAAAAAGTGATGATTATCATGAGTGCCCCGTTATGGTCGTGTTCGATCAGAGCGCTCTTACGAGCAGTCGTATGCTTTCTCGAATTCCGTGCGGTTAAGCGTGACAGTCCCAGTGAACCCACAAAACGTGATGGCAGTCCATGCGATCATACGCAAGAAGGATGGTCTCCAGACACCGGCGCACCAGTTTTCACGCCGAAAGCATAAACGAGGAGCACAAATGAGAGTGTTTGAACTGGACCTGTAGTTTCTCTACGAAGAACACCTTGAGCTGTTGCGTTGTTGCGCTGCCTAGATGCAGTGTCGCACGTATCACTTTTGCCTCAACGACTGCTGCTTTCGCTGTAACCCTAGACAGACAACAGTAAGCGCCTTTTGTAGGCAAGAGCTCCGCCTGTGACTAACTGCGCCAAAACGTCTTCCAATCCCCTTATCCAATTTAACTCACCGAATTCTTACAATTTAGACCCTAATATCACATCATTAGACACTAATTGCCTCTGCCAAAATTCTGTCCACAAGCGTTTTAGTTCGCCCCAGTAAAGTTGTCAATAACGACCACCAAATCCGCATGTTACGGGACTTCTTATTAATTCTTTTTTCGTGGGGAGCAGCGGATCTTAATGGATGGCGCCAGGTGGTATGGAAGCTAATAGCGCGGGTGAGAGGGTAATCAGCCGTCTCCACCAACACAACGCTATCGGGTCATACTATAAGATTCCGCAATGCGACTACTTATAAGATGCCTTAACGGTATCCGCAACTTGCGATGTGCCTGCTATGCTTAAATGCATATCTCGCCCAGTAGCTTTCCAATATGAGAGCATCAATTGTAGATCGGGCCGGGATAATCATGTCGTCACGGAACTTACTGTAAGAGTAATAATTTAAAAGAGATGTCGGTTTGCTGGTTCACGTAAAGGTCCCTCGCGCTACCTCTAAGTAAGTGAGCGG", 646);
		_testFileDynLen("text/znc1.txt", "text/znc2.txt", 3827);
	}

	@Test
	public void testLargeStr() {
		_testFileDynStr("text/melania.txt", "text/michelle.txt", 330);
		_testDynStr("GTCGTTATGCTAGTATACGCCTACCCGTCACCGGCCATCTGTGTGCAGATGGGGCGACGAGTTACTGGCCCTGATTTCTCCGCTTCTAATACCACACACTGGGCAATACGAGCTCAAGCCAGTCTCGCAGTAACGCTCATCAGCTAACGAAAGAGTTAGAGGCTCGCTAATTCGCACTGTCGGGGTCCCTTGGGTGTTTTGCACTAGCGTCAGGTAGGCTAGTATGTGTCTTTCCTTCCAGGGGTATGTGGCTGCGTGGTCAAATGTGCAGCATACGTATTTGCTCGGCGTGCTTGGTCTCTCGTACTTCTCCTGGAGATCAAGGAAATGTTTCTTGTCCAAGCGGACAGCGGTTCTACGGAATGGATCTACGTTACTGCCTGCATAAGGAGAACGGAGTTGCCAAGGACGAAAGCGACTCTAGGTTCTAACCGTCGACTTTGGCGGAAAGGTTTCACTCAGGAAGCAGACACTGATTGACACGGTTTAGCAGAACGTTTGAGGATTAGGTCAAATTGAGTGGTTTAATATCGGTATGTCTGGGATTAAAATATAGTATAGTGCGCTGATCGGAGACGAATTAAAGACACGAGTTCCCAAAACCAGGCGGGCTCGCCACGACGGCTAATCCTGGTAGTTTACGTGAACAATGTTCTGAAGAAAATTTATGAAAGAAGGACCCGTCATCGCCTACAATTACCTACAACGGTCGACCATACCTTCGATTGTCGTGGCCACCCTCGGATTACACGGCAGAGGTGGTTGTGTTCCGATAGGCCAGCATATTATCCTAAGGCGTTACCCCAATCGTTTTCCGTCGGATTTGCTATAGCCCCTGAACGCTACATGCACGAAACCAAGTTATGTATGCACTGGGTCATCAATAGGACATAGCCTTGTAGTTAACATGTAGCCCGGCCGTATTAGTACAGTAGAGCCTTCACCGGCATTCTGTTTATTAAGTTATTTCTACAGCAAAACGATCATATGCAGATCCGCA", "GTGCGCGGTAGAGACACGTCCACCCGGCTGCTCTGTAATAGGGACTAAAAAAGTGATGATTATCATGAGTGCCCCGTTATGGTCGTGTTCGATCAGAGCGCTCTTACGAGCAGTCGTATGCTTTCTCGAATTCCGTGCGGTTAAGCGTGACAGTCCCAGTGAACCCACAAAACGTGATGGCAGTCCATGCGATCATACGCAAGAAGGATGGTCTCCAGACACCGGCGCACCAGTTTTCACGCCGAAAGCATAAACGAGGAGCACAAATGAGAGTGTTTGAACTGGACCTGTAGTTTCTCTACGAAGAACACCTTGAGCTGTTGCGTTGTTGCGCTGCCTAGATGCAGTGTCGCACGTATCACTTTTGCCTCAACGACTGCTGCTTTCGCTGTAACCCTAGACAGACAACAGTAAGCGCCTTTTGTAGGCAAGAGCTCCGCCTGTGACTAACTGCGCCAAAACGTCTTCCAATCCCCTTATCCAATTTAACTCACCGAATTCTTACAATTTAGACCCTAATATCACATCATTAGACACTAATTGCCTCTGCCAAAATTCTGTCCACAAGCGTTTTAGTTCGCCCCAGTAAAGTTGTCAATAACGACCACCAAATCCGCATGTTACGGGACTTCTTATTAATTCTTTTTTCGTGGGGAGCAGCGGATCTTAATGGATGGCGCCAGGTGGTATGGAAGCTAATAGCGCGGGTGAGAGGGTAATCAGCCGTCTCCACCAACACAACGCTATCGGGTCATACTATAAGATTCCGCAATGCGACTACTTATAAGATGCCTTAACGGTATCCGCAACTTGCGATGTGCCTGCTATGCTTAAATGCATATCTCGCCCAGTAGCTTTCCAATATGAGAGCATCAATTGTAGATCGGGCCGGGATAATCATGTCGTCACGGAACTTACTGTAAGAGTAATAATTTAAAAGAGATGTCGGTTTGCTGGTTCACGTAAAGGTCCCTCGCGCTACCTCTAAGTAAGTGAGCGG", 646);
		_testFileDynStr("text/znc1.txt", "text/znc2.txt", 3827);
	}

}