package programmierprojekt;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Main {

	public static Graph graph;
	public static Grid grid;

	public static void main(String[] args) throws IOException {

		System.out.println("Starting");
		System.out.println();

		System.out.println("Loading graph...");
		// testing
		if (args.length != 0) {
			build(args[0]);
		} else {
			build("resources/germany.fmi");
		}		
		System.out.println("Graph loaded.");
		System.out.println();

		System.out.println("Starting server...");
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

		// ajax request which generates the map
		HttpContext context = server.createContext("/start");
		context.setHandler(Main::generateMap);

		// ajax request which finds the nearest node
		HttpContext contextAjaxFindNode = server.createContext("/findnode");
		contextAjaxFindNode.setHandler(Main::findNode);

		// ajax request which calculates the shortest route
		HttpContext contextAjaxCalculateRoute = server.createContext("/calculate");
		contextAjaxCalculateRoute.setHandler(Main::calculateRoute);

		server.setExecutor(null);
		server.start();
		System.out.println("Server started. Connect to localhost:8080");

		open();

	}

	/*
	 * this method reads the file "path" and builds a graph and a grid
	 */
	public static void build(String path) throws IOException {

		graph = Import.read(path);
		grid = new Grid(Import.longmin - 0.000001, Import.longmax + 0.000001, Import.latmin - 0.000001,
				Import.latmax + 0.000001);
		grid.build();
		grid.addGraph(graph);

	}

	/*
	 * generates the leaflet map
	 */
	public static void generateMap(HttpExchange exchange) throws IOException {

		File indexFile = new File("mapV2.html");
		byte[] indexFileByteArray = new byte[(int) indexFile.length()];

		BufferedInputStream requestStream = new BufferedInputStream(new FileInputStream(indexFile));
		requestStream.read(indexFileByteArray, 0, indexFileByteArray.length);
		requestStream.close();

		exchange.getResponseHeaders().add("Content-Type", "text/html");
		exchange.sendResponseHeaders(200, indexFile.length());

		OutputStream responseStream = exchange.getResponseBody();
		responseStream.write(indexFileByteArray, 0, indexFileByteArray.length);
		responseStream.close();

	}

	/*
	 * finds nearest node
	 */
	public static void findNode(HttpExchange exchange) throws IOException {

		System.out.println("Finding node.");

		exchange.getResponseHeaders().put("Access-Control-Allow-Origin", Arrays.asList("*"));
		String requestUrl = exchange.getRequestURI().toString();
		// requesturl is LatLng("lat",%"lon")

		int latStart = requestUrl.lastIndexOf("(");
		int lonStart = requestUrl.lastIndexOf("%");
		// charindex where lat and lon starts

		Double lat = Double.parseDouble(requestUrl.substring(latStart + 1, lonStart - 1));
		Double lon = Double.parseDouble(requestUrl.substring(lonStart + 3, requestUrl.length() - 2));

		Node node = grid.findNearestEntry(lat, lon);
		String response = String.valueOf(node.getLatitude()) + "x" + String.valueOf(node.getLongitude()) + "y"
				+ node.getIndex();
		// response is like "lat"x"lon"y"index"

		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();

		System.out.println("Node found");

	}

	/*
	 * calculates a route with astar
	 */
	public static void calculateRoute(HttpExchange exchange) throws IOException {

		System.out.println("Calculating route.");

		exchange.getResponseHeaders().put("Access-Control-Allow-Origin", Arrays.asList("*"));
		String requestUrl = exchange.getRequestURI().toString();
		// requesturl is like "source"x"destination"

		int sourceStart = requestUrl.lastIndexOf("?");
		int destinationStart = requestUrl.lastIndexOf("x");

		Node source = graph.getIndex(Integer.parseInt(requestUrl.substring(sourceStart + 1, destinationStart)));
		Node destination = graph.getIndex(Integer.parseInt(requestUrl.substring(destinationStart + 1)));

		List<Node> path = graph.oneToOne(source, destination);
		String response = graph.convertPathToString(path);
		// response is the geojson format of the nodes

		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();

		System.out.println("Route calculated.");

	}

	/*
	 * opens localhost and shows map
	 */
	static void open() throws MalformedURLException {

		URL url = new URL("http://localhost:8080/start");

		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(url.toURI());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

	}

}