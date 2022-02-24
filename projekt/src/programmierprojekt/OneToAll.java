package programmierprojekt;

import java.util.LinkedList;

class OneToAll {

	void dijkstra(Node source) {
		
		Heap heap = new Heap(Import.nodes);
		Node.doDijkstra = true;
		LinkedList<Edges> list;
		source.setGCost(0);
		heap.add(source);
		
		while (heap.getCurrentItemCount() > 0) {

			Node currentNode = heap.removeFirst();

			list = currentNode.getOutgoingEdges();
			for (Edges edge : list) {

				Node destination = edge.getDestination();

				if (!destination.getClosedSet()) {
					int currentDistance = currentNode.getGCost() + edge.getWeight();
					if (!heap.contains(destination)) {
						destination.setGCost(currentDistance);
						heap.add(destination);
					} else if (currentDistance < destination.getGCost()) {
						destination.setGCost(currentDistance);
						heap.sortUp(destination);
					}
				}

			}

		}

	}
	
}
