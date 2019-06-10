package edu.wit.cs.comp2350.tests;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.wit.cs.comp2350.A9;
import edu.wit.cs.comp2350.Vertex;
import edu.wit.cs.comp2350.Edge;
import edu.wit.cs.comp2350.Graph;

public class GraphMaker extends JFrame
{

	private static final long serialVersionUID = -2707712944901661771L;

	public GraphMaker()
	{
		super("Minimal spanning tree");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
			Graph retG = A9.FindMST(InputGraph("points/picasso", 2.0));
			Object[] verts = new Object[retG.size()];

			for (Vertex v: retG.getVertices()) {
				verts[v.ID] = graph.insertVertex(parent, null, "",  v.x*550, v.y*550, 3,
						3, "strokeColor=black;fillColor=black;shape=ellipse");
			}

			for (Edge e: retG.getEdges())
				graph.insertEdge(parent, null, "", verts[e.src.ID], verts[e.dst.ID],
						"endArrow=none");
		}
		catch (NullPointerException ex)
		{
			System.err.println("Returned graph was null!");
			System.exit(0);
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	public static void main(String[] args)
	{
		GraphMaker frame = new GraphMaker();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}

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

}
