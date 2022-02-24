package programmierprojekt;

class Edges {

	//the destination of this edge
    private final int destination;
    //the weight of this edge 
    private final int weight;

    Edges(final int destination, final int weight){

        this.destination = destination;
        this.weight = weight;
 
    }

    Node getDestination() {
        return Graph.nodes[this.destination];
    }

    int getWeight() {
        return this.weight;
    }
    
}
