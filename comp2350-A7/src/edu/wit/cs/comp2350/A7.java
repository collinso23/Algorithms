package edu.wit.cs.comp2350;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/* Aligns strings in two text files by matching their longest common substring 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Assignment 7
 * 
 */

public class A7 {

	//TODO Document this method
	public static String[] findLCSdyn(String text1, String text2) {
		int m = text1.length();
		int n = text2.length();
		int b[][] = new int[m+1][n+1];
		int c[][] = new int[m+1][n+1];
		
		//build the graph for values and direction
		for(int i=1; i<=m; i++) {
			for(int j=1; j<=n; j++) {
				if(text1.charAt(i-1)==text2.charAt(j-1)) {
					c[i][j] = c[i-1][j-1]+1;
					b[i][j] = 1; //diagonal
				}
				
				else if(c[i-1][j] >= c[i][j-1]) {
					c[i][j] = c[i-1][j];
					b[i][j] = 2; //up
				}
				
				else {
					c[i][j] = c[i][j-1];
					b[i][j] = 0; //left
				}
			}
		}
		
		// TODO set static var "longest" to longest common subsequence length
		longest = c[m][n];
		
		StringBuffer str1 = new StringBuffer();
		StringBuffer str2 = new StringBuffer();
	
		int i=m, j=n;
		
		//follow arrows backwards to build string
		while(i!=0 && j!=0) {
			switch(b[i][j]) {
			case 0: //left
				str1.append("-");
				str2.append(text2.charAt(j-1));
				j--;
				break;
			case 1: //diagonal
				str1.append(text1.charAt(i-1));
				str2.append(text2.charAt(j-1));
				i--;
				j--;
				break;
			case 2: //up
				str1.append(text1.charAt(i-1));
				str2.append("-");
				i--;
				break;		
			}
			
		}
		
		//j is 0 but i has not reached 0
		while(i!=0) {
			switch(b[i][j]) {
			case 2: //up
				str1.append(text1.charAt(i-1));
				str2.append("-");
				i--;
				break;		
			}
		}
		
		//i is 0 but j has not reached 0
		while(j!=0) {
			switch(b[i][j]) {
			case 0: //left
				str1.append("-");
				str2.append(text2.charAt(j-1));
				j--;
				break;	
			}
			
		}
		
		return new String[]{str1.reverse().toString(), str2.reverse().toString()};	// return an array with aligned text1 and text2 strings
	}
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	private static int longest = -1;

	// recursive helper for DFS
	private static void dfs_solve(int i1, int i2, String s1, String s2, char[] out1, char[] out2, int score, int index) {

		if ((i1 >= s1.length()) && (i2 >= s2.length())) {
			if (score > longest) {
				out1[index] = '\0';
				out2[index] = '\0';
				longest = score;
				sol1 = String.valueOf(out1).substring(0, String.valueOf(out1).indexOf('\0'));
				sol2 = String.valueOf(out2).substring(0, String.valueOf(out2).indexOf('\0'));
			}
		}
		else if ((i1 >= s1.length()) && (i2 < s2.length())) {	// at the end of first string
			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index+1);
		}
		else if ((i1 < s1.length()) && (i2 >= s2.length())) {	// at the end of second string
			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index+1);
		}
		else {
			if (s1.charAt(i1) == s2.charAt(i2)) {	// matching next character
				out1[index] = s1.charAt(i1);
				out2[index] = s2.charAt(i2);
				dfs_solve(i1 + 1, i2 + 1, s1, s2, out1, out2, score + 1, index + 1);
			}

			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);

			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
		}

	}

	// Used for DFS solution
	private static String sol1, sol2;

	// recursively searches for longest substring, checking all possible alignments
	public static String[] findLCSdfs(String text1, String text2) {
		int max_len = text1.length() + text2.length() + 1;
		char[] out1 = new char[max_len];
		char[] out2 = new char[max_len];

		dfs_solve(0, 0, text1, text2, out1, out2, 0, 0);

		String[] ret = new String[2];
		ret[0] = sol1; ret[1] = sol2;
		return ret; 
	}	

	// returns the length of the longest string
	public static int getLongest() {
		return longest;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1, file2, text1 = "", text2 = "";
		System.out.printf("Enter <text1> <text2> <algorithm>, ([dfs] - depth first search, [dyn] - dynamic programming): ");
		System.out.printf("(e.g: text/a.txt text/b.txt dfs)\n");
		file1 = s.next();
		file2 = s.next();

		try {
			text1 = new String(Files.readAllBytes(Paths.get(file1)));
			text2 = new String(Files.readAllBytes(Paths.get(file2)));
		} catch (IOException e) {
			System.err.println("Cannot open files " + file1 + " and " + file2 + ". Exiting.");
			System.exit(0);
		}

		String algo = s.next();
		String[] result = {""};

		switch (algo) {
		case "dfs":
			result = findLCSdfs(text1, text2);
			break;
		case "dyn":
			result = findLCSdyn(text1, text2);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Overlapping characters: %d\nLongest string alignment:\n%s\n\n%s\n", longest, result[0], result[1]);	
	}
}
