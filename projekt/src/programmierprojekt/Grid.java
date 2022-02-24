package programmierprojekt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Grid {

	boolean isBuilt;

	double latmin;
	double latmax;
	double longmin;
	double longmax;

	double latdiff;
	double longdiff;

	short subsections = 300;
	double latboxlen;
	double longboxlen;

	private List<LinkedList<Integer>> list;

	Grid(double longmin, double longmax, double latmin, double latmax) {

		this.isBuilt = false;
		this.longmin = longmin;
		this.longmax = longmax;
		this.latmax = latmax;
		this.latmin = latmin;
		latdiff = latmax - latmin;
		longdiff = longmax - longmin;
		latboxlen = latdiff / subsections;
		longboxlen = longdiff / subsections;
		
	}

	void build() {

		int k = (subsections * subsections) + 1;

		this.list = new ArrayList<LinkedList<Integer>>(k + 1);

		for (int i = 0; i < k + 1; i++) {
			LinkedList<Integer> temp = new LinkedList<Integer>();
			this.list.add(temp);
		}

		this.isBuilt = true;

	}

	int grid(int a, int b) {
		return a * 300 + b;
	}
	
	void addGraph(Graph graph) {
		
		for (int i = 0; i < graph.getLength(); i++) {
			this.add(graph.getIndex(i));
		}
		
	}

	void add(Node node) {

		int lonidex = this.getLongCellIndex(node.getLongitude());
		int latindex = this.getLatCellIndex(node.getLatitude());
		this.list.get(this.grid(lonidex, latindex)).add(node.getIndex());

	}

	int getLongCellIndex(double lon) {

		double longpos = lon - longmin;
		int long_ = (int) (longpos / longboxlen);

		if (long_ < 0) {
			return 0;
		} else if (long_ > this.subsections - 1) {
			return this.subsections - 1;
		} else {
			return long_;
		}

	}

	int getLatCellIndex(double lat) {

		double latpos = lat - latmin;
		int lat_ = (int) (latpos / latboxlen);
		if (lat_ < 0) {
			return 0;
		} else if (lat_ > this.subsections - 1) {
			return this.subsections - 1;
		} else {
			return lat_;
		}

	}

	Node findNearestEntry(double latitude, double longitude) {

		int latcellmin = this.getLatCellIndex(latitude);
		int latcellmax = this.getLatCellIndex(latitude);
		int longcellmin = this.getLongCellIndex(longitude);
		int longcellmax = this.getLongCellIndex(longitude);

		Node nearestNode = null;
		Node temp;

		double distance;
		double mindistance = Double.POSITIVE_INFINITY;

		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.addAll(this.list.get(this.grid(longcellmin, latcellmin)));

		while (nearestNode == null || !queue.isEmpty()) {

			while (!queue.isEmpty()) {
				temp = Graph.nodes[queue.poll()];
				distance = temp.getGreatCircleDistance(latitude, longitude);
				if (Double.compare(distance, mindistance) < 0) {
					nearestNode = temp;
					mindistance = distance;
				}
			}

			if (Double.compare(nearestWall(latcellmax, latcellmin, longcellmax, longcellmin, latitude, longitude),
					mindistance) < 0) {
				if (latcellmin > 0) {
					latcellmin--;
				}
				if (latcellmax < this.subsections) {
					latcellmax++;
				}
				if (longcellmin > 0) {
					longcellmin--;
				}
				if (longcellmax < this.subsections) {
					longcellmax++;
				}

				for (int i = latcellmin; i <= latcellmax; i++) {

					for (int j = longcellmin; j <= longcellmax; j++) {

						if (j < longcellmax && j > longcellmin && i < latcellmax && i > longcellmin) {
						} else {
							if (i >= 0 && i < subsections && j >= 0 && j < subsections) {
								queue.addAll(this.list.get(this.grid(j, i)));
							}
						}

					}

				}

			}

		}

		//System.out.println("Nearest node has id " + nearestNode.getIndex() + " and is " + mindistance + " away from point " + latitude + "/" + longitude);
		return nearestNode;

	}

	double nearestWall(int latmax, int latmin, int longmax, int longmin, double lon, double lat) {
		double min1 = this.getGreatCircleDistance((((latmax + 1) * this.latboxlen) + this.latmin), lon, lat, lon);
		double min2 = this.getGreatCircleDistance(((latmin * this.latboxlen) + this.latmin), lon, lat, lon);
		double min3 = this.getGreatCircleDistance(lat, (((longmax + 1) * this.latboxlen) + this.longmin), lat, lon);
		double min4 = this.getGreatCircleDistance(lat, ((longmin * this.latboxlen) + this.longmin), lat, lon);
		return Math.min(Math.min(min1, min2), Math.min(min3, min4));
	}

	double getGreatCircleDistance(double latitude, double longitude, double latitude_, double longitude_) {
		// Haversine formula
		return 12742000 * Math.asin(Math.sqrt(Math
				.pow(Math.sin((Math.toRadians(latitude) - Math.toRadians(latitude_)) / 2.0), 2.0)
				+ Math.cos(Math.toRadians(latitude_)) * Math.cos(Math.toRadians(latitude))
						* Math.pow(Math.sin((Math.toRadians(longitude) - Math.toRadians(longitude_)) / 2.0), 2.0)));
	}

}
