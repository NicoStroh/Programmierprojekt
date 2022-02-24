package programmierprojekt;

import java.util.List;

class Graph {

	static double[][] kords;
	static Node[] nodes;

	Graph(final int length) {

		nodes = new Node[length];
		kords = new double[3][length];

	}

	void addNode(Node node, final int pos) {
		nodes[pos] = node;
	}

	Node getIndex(final int index) {
		return nodes[index];
	}

	int getLength() {
		return nodes.length;
	}

	void resetClosedSet() {
		int length = nodes.length;
		for (int i = 0; i < length; i++) {
			nodes[i].setClosedSet(false);
		}
	}

	List<Node> oneToOne(Node startnode, Node endnode) {
		OneToOne oneToOne = new OneToOne();
		List<Node> path = oneToOne.computeAStar(startnode, endnode);
		// reset closedset to prevent error
		this.resetClosedSet();
		return path;
	}

	void oneToAll(Node startnode) {
		OneToAll oneToAll = new OneToAll();
		oneToAll.dijkstra(startnode);
	}

	String convertPathToString(List<Node> path) {

		StringBuilder string = new StringBuilder();
		string.insert(0, "[");
		for (Node node : path) {
			string.append("[")
			.append(node.getLongitude())
            .append(", ")
            .append(node.getLatitude())
            .append("],");			
		}
		string.deleteCharAt(string.length()-1);
		string.append("]");

		return string.toString();

	}

}