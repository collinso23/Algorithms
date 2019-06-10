package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/* Sorts integers from command line using various algorithms 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Assignment 1
 * 
 */

public class A1 {
	//for (int i = 0; i < a.length; i++) {}
	// TODO: document this method
	public static int[] countingSort(int a[]) {
		//TODO: implement this method
		
		//initialize vars 
		int max = 0;
		int min = 0;
		int n = a.length;
		
		//find max/min
		for (int i = 0; i < n; i++) {
			if (a[i] > max) {max = a[i];} 
		}
		for (int i = 0; i < n; i++) {
			if (a[i] < min) {min = a[i];} 
		}
		
		//initialize count arr and perform count operation
		int count[] = new int[max+1];
		for (int i = 0; i < n; i++) {
			++count[a[i]];
		}
		
		//assign counts back to array in correct position
		int j = 0;
		for (int i = min; i <= max; i++) {
			 while (count[i-min] > 0) {
				 a[j++] = i;
				 count[i-min]--;
			 }
		}
		return a;	// return an array with sorted values 
		// The output character array that will have sorted array 
        
	}

	// TODO: document this method
	public static int[] radixSort(int[] a) {
		//initialize vars
		int radix = 10;
		int width = String.valueOf(MAX_INPUT).length();
		
		//perform radix sort on each var in arr
		for (int i = 0; i < width; i++) {
			radSort(a, i, radix);
		}
        return a;	// return an array with sorted values
	}
	
	//method for radix sort
	private static void radSort(int[] a, int pos, int radix) {
		//initialize vars and arr
		int num = a.length;
		int[] count = new int[radix];
		
		//calculate which digits algo is working on
		for (int value: a) {
			count[getDigit(pos,value, radix)]++;
		}
		//Initialize count arr and perform stable counting sort for radi
		for (int j = 1; j < radix; j++) {
			count[j]+=count[j-1];
		}
		int[] temp = new int[num];
		for(int tmpI = num -1; tmpI >= 0; tmpI--) {
			temp[--count[getDigit(pos, a[tmpI], radix)]] = a[tmpI];
		}
		
		//reassign vars from temp[] to a[]
		for (int tmpI = 0; tmpI < num; tmpI++) {
			a[tmpI] = temp[tmpI];
		}	
	}
	
	//method to to get the digit the algo is currently working on ie 1,10,100, or ect.
	private static int getDigit(int pos, int value, int radix) {
		return value / (int) Math.pow(radix,pos)%radix;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	public final static int MAX_INPUT = 524287;
	public final static int MIN_INPUT = 0;

	// example sorting algorithm
	public static int[] insertionSort(int[] a) {

		for (int i = 1; i < a.length; i++) {
			int tmp = a[i];
			int j;
			for (j = i-1; j >= 0 && tmp < a[j]; j--)
				a[j+1] = a[j];
			a[j+1] = tmp;
		}

		return a;
	}

	/* Implementation note: The sorting algorithm is a Dual-Pivot Quicksort by Vladimir Yaroslavskiy,
	 *  Jon Bentley, and Joshua Bloch. This algorithm offers O(n log(n)) performance on many data 
	 *  sets that cause other quicksorts to degrade to quadratic performance, and is typically 
	 *  faster than traditional (one-pivot) Quicksort implementations. */
	public static int[] systemSort(int[] a) {
		Arrays.sort(a);
		return a;
	}

	// read ints from a Scanner
	// returns an array of the ints read
	private static int[] getInts(Scanner s) {
		ArrayList<Integer> a = new ArrayList<Integer>();

		while (s.hasNextInt()) {
			int i = s.nextInt();
			if ((i <= MAX_INPUT) && (i >= MIN_INPUT))
				a.add(i);
		}

		return toIntArray(a);
	}

	// copies an ArrayList of Integer to an array of int
	private static int[] toIntArray(ArrayList<Integer> a) {
		int[] ret = new int[a.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the sorting algorithm to use ([c]ounting, [r]adix, [i]nsertion, or [s]ystem): ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the integers to sort, followed by a non-integer character: ");
		int[] unsorted_values = getInts(s);
		int[] sorted_values = {};

		s.close();

		switch (algo) {
		case 'c':
			sorted_values = countingSort(unsorted_values);
			break;
		case 'r':
			sorted_values = radixSort(unsorted_values);
			break;
		case 'i':
			sorted_values = insertionSort(unsorted_values);
			break;
		case 's':
			sorted_values = systemSort(unsorted_values);
			break;
		default:
			System.out.println("Invalid sorting algorithm");
			System.exit(0);
			break;
		}

		System.out.println(Arrays.toString(sorted_values));
	}

}
