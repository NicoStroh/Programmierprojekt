package programmierprojekt;

import java.util.LinkedList;

class Node implements Comparable<Node> {

	// compareTo depends on whether you use dijkstra or aStar
	static boolean doDijkstra = true;

	// position in graph array
	private final int index;

	private LinkedList<Edges> outgoingEdges = new LinkedList<Edges>();

	// distance to startnode
	private int gCost;

	// predecessor in aStar shortest path
	private int aStarParent;

	// index in heap array
	private int heapIndex;

	// is node in closed set?
	private boolean closedSet;

	Node(final int index, final double latitude, final double longitude) {

		this.index = index;
		Graph.kords[0][index] = latitude;
		Graph.kords[1][index] = longitude;
		this.heapIndex = index;
		this.closedSet = false;

	}

	int getIndex() {
		return this.index;
	}

	double getLatitude() {
		return Graph.kords[0][this.index];
	}

	double getLongitude() {
		return Graph.kords[1][this.index];
	}

	LinkedList<Edges> getOutgoingEdges() {
		return this.outgoingEdges;
	}

	int getGCost() {
		return this.gCost;
	}

	void setGCost(final int g) {
		this.gCost = g;
	}

	Double getHCost() {
		return Graph.kords[2][this.index];
	}

	void setHCost(final Double h) {
		Graph.kords[2][this.index] = h;
	}

	double getFCost() {
		return this.gCost + Graph.kords[2][this.index];
	}

	int getAStarParentIndex() {
		return this.aStarParent;
	}

	void setAStarParentIndex(final int aStarParentIndex) {
		this.aStarParent = aStarParentIndex;
	}

	int getHeapIndex() {
		return this.heapIndex;
	}

	void setHeapIndex(final int heapIndex) {
		this.heapIndex = heapIndex;
	}

	boolean getClosedSet() {
		return this.closedSet;
	}

	void setClosedSet(final boolean closedSet) {
		this.closedSet = closedSet;
	}

	void addEdge(Edges edge) {
		this.outgoingEdges.add(edge);
	}

	double getGreatCircleDistance(double latitude, double longitute) {
		// Haversine formula
		return 12742000 * Math.asin(Math.sqrt(Math
				.pow(Math.sin((Math.toRadians(latitude) - Math.toRadians(this.getLatitude())) / 2.0), 2.0)
				+ Math.cos(Math.toRadians(this.getLatitude())) * Math.cos(Math.toRadians(latitude)) * Math
				.pow(Math.sin((Math.toRadians(longitute) - Math.toRadians(this.getLongitude())) / 2.0), 2.0)));
	}

	@Override
	public int compareTo(Node anotherNode) {

		if (doDijkstra) {
			return Integer.compare(this.getGCost(), anotherNode.getGCost());

		} else {
			int compare = Double.compare(this.getFCost(), anotherNode.getFCost());
			if (compare == 0) {
				compare = Double.compare(Graph.kords[2][this.index], Graph.kords[2][anotherNode.index]);
			}
			return compare;
		}

	}

}
