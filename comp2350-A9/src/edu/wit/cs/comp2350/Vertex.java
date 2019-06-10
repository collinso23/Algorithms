package edu.wit.cs.comp2350;

// represents a vertex in a graph, including a unique ID to keep track of vertex
public class Vertex {
	
	//utilize union-find data structure
	public Vertex parent;
	public int rank;
	
	//construct disjoint set of vertex
	public void makeSet() {
		this.rank = 0;
		this.parent = this;
	}
	
	//path compression to speed up find runtime
	public Vertex findSet() {
		if (this.parent == this)
			return this;
		else {
			Vertex next = this.parent.findSet();
			this.parent = next;
			return next;
		}
		
	}
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/
	
	public double x;
	public double y;
	public int ID;
}
