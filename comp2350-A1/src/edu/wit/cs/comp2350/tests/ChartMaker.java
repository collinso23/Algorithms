package edu.wit.cs.comp2350.tests;


import java.awt.Color;
import java.util.Random;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import edu.wit.cs.comp2350.A1;


public class ChartMaker extends ApplicationFrame {
	private static final long serialVersionUID = 1L;
	private static final int DATASETS = 4;

	/**
	 *
	 * @param title  the frame title.
	 */
	public ChartMaker(String title) {
		super(title);
		JPanel chartPanel = createDemoPanel();
		chartPanel.setPreferredSize(new java.awt.Dimension(850, 500));
		setContentPane(chartPanel);
	}

	/**
	 * Creates a chart with four datasets of algorithm runtimes and
	 * sets appearance of chart objects.
	 *
	 * @return A chart instance.
	 */
	private static JFreeChart createChart() {
		XYDataset[] data = new XYDataset[DATASETS];

		data[0] = createDataset('c', "counting");
		data[1] = createDataset('r', "radix");
		data[2] = createDataset('i', "insertion");
		data[3] = createDataset('s', "system");


		JFreeChart chart = ChartFactory.createXYLineChart(
				"sorting solution runtimes",   	// chart title
				"number of items",       	   	// x axis label
				"time(ms)",                		// y axis label
				data[0],                   		// data
				PlotOrientation.VERTICAL, 
				true,                     		// include legend
				true,                      		// tooltips
				false                      		// urls
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getDomainAxis().setLowerMargin(0.0);
		plot.getDomainAxis().setUpperMargin(0.0);
		XYLineAndShapeRenderer renderer[] = new XYLineAndShapeRenderer[DATASETS];

		for (int i = 0; i < DATASETS; i++) {
			plot.setDataset(i, data[i]);
			renderer[i] = new XYLineAndShapeRenderer(); 
			plot.setRenderer(i, renderer[i]);
		}

		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red); 
		plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);
		plot.getRendererForDataset(plot.getDataset(2)).setSeriesPaint(0, Color.darkGray);
		plot.getRendererForDataset(plot.getDataset(3)).setSeriesPaint(0, Color.magenta);

		return chart;
	}

	/**
	 * Creates a dataset based on algorithm runtimes.
	 *
	 * @param algo  The string used to represent algorithm
	 * @param name  The name of algorithm to appear in the legend
	 * 
	 * @return A sample dataset.
	 */
	public static XYDataset createDataset(char algo, String name) {
		XYDataset result = DatasetUtils.sampleFunction2D(new AddRuns(algo),
				0.0, 10000.0, 21, name);
		return result;
	}

	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		JFreeChart chart = createChart();
		return new ChartPanel(chart);
	}

	/**
	 * Generates all the runtimes for the graph.
	 */
	static class AddRuns implements Function2D {
		private char algo;

		public AddRuns(char a) { algo = a; }

		// t is the number of integers in each list
		public double getValue(double t) {
			double sec = 0;

			if (t == 0)
				return 0;

			for (int i = 0; i < 10; i++) {
				int[] arr = generateRandArray(Double.valueOf(t).intValue());

				long start = System.nanoTime();

				switch (algo) {
				case 'c':
					A1.countingSort(arr);
					break;
				case 'r':
					A1.radixSort(arr);
					break;
				case 'i':
					A1.insertionSort(arr);
					break;
				case 's':
					A1.systemSort(arr);
					break;
				default:
					System.out.println("Invalid algorithm");
					System.exit(0);
					break;
				}

				long end = System.nanoTime();

				sec += (end-start)/1E6;
			}
			return sec/5;
		}

		private int[] generateRandArray(int size) {
			int[] ret = new int[size];

			Random r = new Random();
			for (int i = 0; i < size; i++) {
				ret[i] = r.nextInt(A1.MAX_INPUT+1);
			}
			return ret;
		}

	}

	/**
	 * Starting point for the chartmaker.
	 *
	 * @param args  ignored.
	 */
	public static void main(String[] args) {
		ChartMaker cm = new ChartMaker(
				"Chart Window");
		cm.pack();
		RefineryUtilities.centerFrameOnScreen(cm);
		cm.setVisible(true);
	}

}
