package programmierprojekt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Benchmark {

	public static void main(String[] args) throws IOException {
		// read parameters (parameters are expected in exactly this order)
		String graphPath = args[1];
		double lon = Double.parseDouble(args[3]);
		double lat = Double.parseDouble(args[5]);
		String quePath = args[7];
		int sourceNodeId = Integer.parseInt(args[9]);

		// run benchmarks
		System.out.println("Reading graph file " + graphPath);
		long graphReadStart = System.currentTimeMillis();
		Graph graph = Import.read(graphPath);
		long graphReadEnd = System.currentTimeMillis();
		System.out.println("\tgraph read took " + (graphReadEnd - graphReadStart) + "ms");

		System.out.println("Computing one-to-all Dijkstra from node id " + sourceNodeId);
		long oneToAllStart = System.currentTimeMillis();
		graph.oneToAll(graph.getIndex(sourceNodeId));
		long oneToAllEnd = System.currentTimeMillis();
		System.out.println("\tone-to-all Dijkstra took " + (oneToAllEnd - oneToAllStart) + "ms");

		// ask user for a target node id
		Scanner scan = new Scanner(System.in);

		System.out.print("Enter target node id... ");
		//int targetNodeId = scan.nextInt();
		int targetNodeId = 16743651;
		int oneToAllDistance = (int) graph.getIndex(targetNodeId).getGCost();
		System.out.println("Distance from " + sourceNodeId + " to " + targetNodeId + " is " + oneToAllDistance);
		scan.close();

		graph.resetClosedSet();
		System.out.println("Running one-to-one Dijkstras for queries in .que file " + quePath);
		long queStart = System.currentTimeMillis();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(quePath))) {
			String currLine;
			while ((currLine = bufferedReader.readLine()) != null) {
				int oneToOneSourceNodeId = Integer.parseInt(currLine.substring(0, currLine.indexOf(" ")));
				int oneToOneTargetNodeId = Integer.parseInt(currLine.substring(currLine.indexOf(" ") + 1));
				graph.oneToOne(graph.getIndex(oneToOneSourceNodeId), graph.getIndex(oneToOneTargetNodeId));
				int oneToOneDistance = (int) graph.getIndex(oneToOneTargetNodeId).getGCost();
				System.out.println("Distance from " + oneToOneSourceNodeId + " to " + oneToOneTargetNodeId + " is "
									+ oneToOneDistance);
				graph.resetClosedSet();
			}
			bufferedReader.close();
		} catch (Exception e) {
			System.out.println("Exception...");
			e.printStackTrace();
		}
		long queEnd = System.currentTimeMillis();
		System.out.println("\tprocessing .que file took " + (queEnd - queStart) + "ms");
		
		System.out.println("Building grid");
		Grid grid = new Grid(Import.longmin - 0.000001, Import.longmax + 0.000001, Import.latmin - 0.000001,
					Import.latmax + 0.000001);
		grid.build();
		for (int i = 0; i < graph.getLength(); i++) {
			grid.add(graph.getIndex(i));
		}
		System.out.println("Grid complete");

		System.out.println("Finding closest node to coordinates " + lon + " " + lat);
		long nodeFindStart = System.currentTimeMillis();
		grid.findNearestEntry(lat, lon);
		long nodeFindEnd = System.currentTimeMillis();
		System.out.println("\tfinding node took " + (nodeFindEnd - nodeFindStart) + "ms");

	}

}
