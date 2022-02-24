package programmierprojekt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class Import {

	static String currentLine = "";
	static String[] splitLine;
	static int line = 1;
	static int nodes = 0;
	static int nodesLastIndex = 0;
	static int edges = 0;
	static int edgesLastIndex = 0;
	static int[] spaces = new int[4];
	static int spaceCounter = 0;

	static int nodeID = 0;
	static double latitude = 0.0;
	static double longitude = 0.0;
	static double longmin = 0.0;
	static double longmax = 0.0;
	static double latmin = 0.0;
	static double latmax = 0.0;

	static int srcIDX = 0;
	static int trgIDX = 0;
	static int cost = 0;
	static int lineLength = 0;
	static boolean firstpart;

	static Graph graph = null;

	static Graph read(String pathString) throws IOException {

		Path path = Paths.get(pathString);
		System.out.println(path.toString());
		System.out.println(path.toAbsolutePath().toString());

		BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
		while (line < 6) {
			reader.readLine();
			line++;
		}
		nodes = Integer.parseInt(reader.readLine());
		edges = Integer.parseInt(reader.readLine());
		line += 2;

		graph = new Graph(nodes);

		nodesLastIndex = 8 + nodes;
		while (line < nodesLastIndex) {
			currentLine = reader.readLine();
			splitLine = currentLine.split("\\s+");

			nodeID = line - 8;
			latitude = Double.parseDouble(splitLine[2]);
			longitude = Double.parseDouble(splitLine[3]);

			graph.addNode(new Node(nodeID, latitude, longitude), nodeID);

			if (longitude > longmax) {
				longmax = longitude;
			}
			if (latitude > latmax) {
				latmax = latitude;
			}
			if (longitude < longmin) {
				longmin = longitude;
			}
			if (latitude < latmin) {
				latmin = latitude;
			}

			line++;
		}

		edgesLastIndex = 8 + nodes + edges;
		while (line < edgesLastIndex) {
			currentLine = reader.readLine();
			splitLine = currentLine.split("\\s+");

			srcIDX = Integer.parseInt(splitLine[0]);
			trgIDX = Integer.parseInt(splitLine[1]);
			cost = Integer.parseInt(splitLine[2]);

			graph.getIndex(srcIDX).addEdge(new Edges(trgIDX, cost));
			line++;
		}
		reader.close();
		return graph;

	}

}
