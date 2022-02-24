package programmierprojekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class OneToOne { 
		
	List<Node> computeAStar(Node source, Node target) {
		
		/*
		 * OPEN //the set of nodes to be evaluated CLOSED //the set of nodes already
		 * evaluated add the start node to OPEN
		 * 
		 * loop current = node in OPEN with the lowest f_cost remove current from OPEN
		 * add current to CLOSED
		 * 
		 * if current is the target node //path has been found return
		 * 
		 * foreach neighbour of the current node if neighbour is not traversable or
		 * neighbour is in CLOSED skip to the next neighbour
		 * 
		 * if new path to neighbour is shorter OR neighbour is not in OPEN set f_cost of
		 * neighbour set parent of neighbour to current if neighbour is not in OPEN add
		 * neighbour to OPEN
		 */
			
		Heap openSet = new Heap(Import.nodes);
		 		
		Node.doDijkstra = false;
		source.setGCost(0);
		openSet.add(source);
		
		while(openSet.getCurrentItemCount() > 0) {
			
			Node currentNode = openSet.removeFirst();
			
			if(currentNode == target) {
				return this.retraceSteps(source, target);
			}
			
			LinkedList<Edges> list = currentNode.getOutgoingEdges();
			for(Edges edge : list) {
			
				if(!edge.getDestination().getClosedSet()){
					int newNeighboureCost = currentNode.getGCost() + edge.getWeight();
					if(!openSet.contains(edge.getDestination())) {
						edge.getDestination().setGCost(newNeighboureCost);
						edge.getDestination().setHCost(this.getDistance(edge.getDestination(), target));
						edge.getDestination().setAStarParentIndex(currentNode.getIndex());
						openSet.add(edge.getDestination());
					}
					else if(newNeighboureCost < edge.getDestination().getGCost()) {
						edge.getDestination().setGCost(newNeighboureCost);
						edge.getDestination().setHCost(this.getDistance(edge.getDestination(), target));
						edge.getDestination().setAStarParentIndex(currentNode.getIndex());
					}
					
				}
				
			}
		}
		return null;
		
	}

	List<Node> retraceSteps(Node source, Node target) {
		
		List<Node> path = new ArrayList<Node>();
		Node currentNode = target;
		
		while(currentNode != source) {
			path.add(currentNode);
			currentNode = Graph.nodes[currentNode.getAStarParentIndex()];
		}
		
		path.add(source);
		Collections.reverse(path);
		return path;

	}

	double getDistance(Node a, Node b) {
		int n = 100000;
		double x = Math.abs((a.getLongitude()*n) - (b.getLongitude()*n));
		double y = Math.abs((a.getLatitude()*n) - (b.getLatitude()*n));
		return Math.sqrt((x * x) + (y * y));
	}

}
