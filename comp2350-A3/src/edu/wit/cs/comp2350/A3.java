package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/* Sorts geographic points in place in an array
 * by surface distance to a specific point 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Assignment 3
 * 
 */

public class A3 {
	/*public static void QS(a) {
	_QS(A,O,a.length-1)
  }

  private _QS(A,left,right) {
	actual code in here
	_QS(A,Left,right)
  }*/

//TODO: document this method
	
//pass dest to helper QS method
public static void quickSort(Coord[] destinations) {
	_QS(destinations, 0, destinations.length - 1);
}

//private method for inputing more vals for methods
//get spec dist is go to obj get val from obj compare val of one obj to another swap the coord obj not the the values.
private static void _QS(Coord[] destinations, int start, int end) {
//A= Coord[] destinations
//p = pivot 
//r = end
	if (start < end) {
		int pivot = (int) partition(destinations,start,end);
		_QS(destinations,start,pivot - 1);
		_QS(destinations,pivot + 1,end);
	}
}

//partition the array sends val < p to pInd-1 and val > p to pInd+1
public static double partition(Coord[] destinations,int start,int end) {
//A = Coord[] dest
//l = pivInd
//r = end
		double pivot = destinations[end].getDist();
		int pivInd = start;
		for (int j = start; j < end; j++) {
			if (destinations[j].getDist() <= pivot) {
				swap(destinations,pivInd,j);
				pivInd = pivInd+ 1;
			}
		}
		swap(destinations,pivInd,end);
		return pivInd;
	}
	
//method for swapping values in dest with piv
public static void swap(Coord[] destinations, int i, int j) {
	
	Coord tmp = destinations[i];
	destinations[i] = destinations[j];
	destinations[j] = tmp;
}

//TODO: document this method

//init method and pass initial val to method
public static void randQuickSort(Coord[] destinations) {

	_randQS(destinations, 0, destinations.length-1);
}

//private QS to control 
private static void _randQS(Coord[] destinations, int start, int end) {
	
	if (start < end) {
		int pivot = (int)randPartition(destinations,start,end);
			_randQS(destinations,start,pivot - 1);
			_randQS(destinations,pivot + 1,end);
	}
}

//selects random piv each time and parts around that piv
public static double randPartition(Coord[] destinations, int start, int end) {
	
	int randPiv = randPivPt(start,end);	
	swap(destinations,end,randPiv);

	return partition(destinations,start,end);

}

//pick a random pivot point within arr
public static int randPivPt(int startNum, int endNum) {

	return (int) (Math.random() * (endNum - startNum + 1) + startNum);
}

/********************************************
 * 
 * You shouldn't modify anything past here
 * 
 ********************************************/

// Call system sort with a lambda expression on the comparator
public static void systemSort(Coord[] destinations) {
	Arrays.sort(destinations, (a, b) -> Double.compare(a.getDist(), b.getDist()));
}

// Insertion sort eventually sorts an array
public static void insertionSort(Coord[] a) {

	for (int i = 1; i < a.length; i++) {
		Coord tmpC = a[i];
		int j;
		for (j = i-1; j >= 0 && tmpC.getDist() < a[j].getDist(); j--)
			a[j+1] = a[j];
		a[j+1] = tmpC;
	}
}

private static Coord getOrigin(Scanner s) {
	double lat = s.nextDouble();
	double lon = s.nextDouble();

	Coord ret = new Coord(lat, lon);
	return ret;
}

private static Coord[] getDests(Scanner s, Coord start) {
	ArrayList<Coord> a = new ArrayList<>();

	while (s.hasNextDouble()) {
		a.add(new Coord(s.nextDouble(), s.nextDouble(), start));
	}

	Coord[] ret = new Coord[a.size()];
	a.toArray(ret);

	return ret;
}

private static void printCoords(Coord start, Coord[] a) {

	System.out.println(start.toColorString("black"));

for (int i = 0; i < a.length; ++i) {
	System.out.println(a[i].toColorString("red"));
}
System.out.println();
System.out.println("Paste these results into http://www.hamstermap.com/custommap.html if you want to visualize the coordinates.");
}

public static void main(String[] args) {
	Scanner s = new Scanner(System.in);

	System.out.printf("Enter the sorting algorithm to use [i]nsertion sort, [q]uicksort, [r]andomized quicksort, or [s]ystem quicksort): ");
char algo = s.next().charAt(0);

System.out.printf("Enter your starting coordinate in \"latitude longitude\" format as doubles: (e.g. 42.3366322 -71.0942150): ");
Coord start = getOrigin(s);

System.out.printf("Enter your end coordinates one at a time in \"latitude longitude\" format as doubles: (e.g. 38.897386 -77.037400). End your input with a non-double character:%n");
Coord[] destinations = getDests(s, start);

s.close();

switch (algo) {
case 'i':
	insertionSort(destinations);			
	break;
case 'q':
	quickSort(destinations);
	break;
case 'r':
	randQuickSort(destinations);
	break;
case 's':
	systemSort(destinations);
	break;
default:
	System.out.println("Invalid search algorithm");
			System.exit(0);
			break;
		}

		printCoords(start, destinations);

	}

}
