package edu.wit.cs.comp2350;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* Calculates the minimal spanning tree of a graph 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Assignment 9
 * 
 */

public class A9 {

	// TODO document this method
   public static Graph FindMST(Graph g) {
	   
	   //init all vertex in tree
	   for (Vertex v : g.getVertices()) {
		   v.makeSet();
	   }
	 
	   //arraylist of edges to be sorted
	   ArrayList<Edge> edges = new ArrayList<Edge>();
       for (Vertex u : g.getVertices()) {
           for (Vertex v : g.getVertices()) {
               if (u == v)
                   continue;
               edges.add(new Edge(u, v, Math.sqrt(Math.pow(u.x - v.x, 2) + Math.pow(u.y - v.y, 2))));
           }
       }

       //comparator and collection.sort edges for each cost (u,v)
       Collections.sort(edges, new Comparator<Edge>() {
    	   public int compare(Edge u, Edge v) {
    		   return Double.compare(u.cost, v.cost);
           }
       }); 
 
       //for all edges in sorted order do:
       for (Edge e : edges) {
           if (e.src.findSet() != e.dst.findSet()) {
               g.addEdge(e.src, e.dst);
               union(e.src, e.dst);
           }
       }
	return g;
   }
   
  // Private help methods //
   
   //check for sets for (u,v) and assigns representative and rank.
   private static void union(Vertex u, Vertex v) {
       Vertex uRep = u.findSet();
       Vertex vRep = v.findSet();

       if (uRep.rank > vRep.rank)
           vRep.parent = uRep;
       
       else {
           uRep.parent = vRep;
           if (uRep.rank == vRep.rank)
               vRep.rank++;
       }
   }  
   
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/


	// reads in an undirected graph from a specific file formatted with one
	// x/y node coordinate per line:
	private static Graph InputGraph(String file1, double epsilon) {

		Graph g = new Graph(epsilon);
		try (Scanner f = new Scanner(new File(file1))) {
			while(f.hasNextDouble()) // each vertex listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}

		return g;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;

		System.out.printf("Enter <points file> <edge neighborhood>\n");
		System.out.printf("(e.g: points/small .5)\n");
		file1 = s.next();

		// read in vertices
		Graph g = InputGraph(file1, s.nextDouble());

		Graph mst = FindMST(g);

		s.close();

		System.out.printf("Weight of tree: %f\n", mst.getTotalEdgeWeight());
	}

}
