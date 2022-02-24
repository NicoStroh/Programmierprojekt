package programmierprojekt;

class Heap {

	Node[] heapArray;
	int currentItemCount;
	
	Heap(int maxHeapSize) {

		this.heapArray = new Node[maxHeapSize];

	}

	int getCurrentItemCount() {
		return this.currentItemCount;
	}

	// returns the first element of the heap
	Node peek() {
		return this.heapArray[0];
	}

	// returns and removes the first element of the heap
	Node removeFirst() {

		Node firstItem = this.heapArray[0];
		currentItemCount--;
		firstItem.setClosedSet(true);

		if (currentItemCount > 0) {
			this.heapArray[0] = this.heapArray[currentItemCount];
			this.heapArray[0].setHeapIndex(0);
			sortDown(this.heapArray[0]);
		} else {
			this.heapArray[0] = null;
		}

		return firstItem;

	}

	int currentSize() {
		return this.currentSize();
	}

	void add(Node input) {

		input.setHeapIndex(currentItemCount);
		this.heapArray[currentItemCount] = input;
		this.sortUp(input);
		this.currentItemCount++;

	}

	boolean contains(Node item) {

		if (this.isEmpty()) {
			return false;
		}
		return item.equals(this.heapArray[item.getHeapIndex()]);

	}

	void swap(Node a, Node b) {

		this.heapArray[a.getHeapIndex()] = b;
		this.heapArray[b.getHeapIndex()] = a;
		int temp = a.getHeapIndex();
		a.setHeapIndex(b.getHeapIndex());
		b.setHeapIndex(temp);

	}

	boolean isEmpty() {
		return this.currentItemCount == 0;
	}

	//every heapitem has 5 childs, we search for the one with the smallest gCost and sort the item down if the smallest child is smaller
	void sortDown(Node item) {

		while (true) {

			int firstChildIndex = (item.getHeapIndex() * 5) + 1;
			int secondChildIndex = (item.getHeapIndex() * 5) + 2;
			int thirdChildIndex = (item.getHeapIndex() * 5) + 3;
			int fourthChildIndex = (item.getHeapIndex() * 5) + 4;
			int fifthChildIndex = (item.getHeapIndex() * 5) + 5;
			//int sixthChildIndex = (item.getHeapIndex() * 6) + 6;
			int swapIndex;

			if (firstChildIndex < this.currentItemCount) {
				
				swapIndex = firstChildIndex;
				
				if (secondChildIndex < this.currentItemCount) {
					
					if (this.heapArray[secondChildIndex].compareTo(this.heapArray[firstChildIndex]) < 0) {
						swapIndex = secondChildIndex;
					}
					
					if (thirdChildIndex < this.currentItemCount) {
						
						if (this.heapArray[thirdChildIndex].compareTo(this.heapArray[swapIndex]) < 0) {
							swapIndex = thirdChildIndex;
						}
						
						if (fourthChildIndex < this.currentItemCount) {
							
							if (this.heapArray[fourthChildIndex].compareTo(this.heapArray[swapIndex]) < 0) {
								swapIndex = fourthChildIndex;
							}
							
							if (fifthChildIndex < this.currentItemCount) {
								
								if (this.heapArray[fifthChildIndex].compareTo(this.heapArray[swapIndex]) < 0) {
									swapIndex = fifthChildIndex;
								}		
								
								/*if (sixthChildIndex < this.currentItemCount) {
									
									if (this.heapArray[sixthChildIndex].compareTo(this.heapArray[swapIndex]) < 0) {
										swapIndex = sixthChildIndex;
									}	
																		
								}*/
								
							}
							
						}
						
					}
					
				}
				if (this.heapArray[swapIndex].compareTo(item) < 0) {
					this.swap(item, this.heapArray[swapIndex]);
				} else {
					return;
				}
			} else {
				return;
			}

		}

	}

	// sort for 5 tree
	void sortUp(Node item) {

		if (item.getHeapIndex() > 0) {

			int parentIndex = (item.getHeapIndex() - 1) / 5;
			Node parentItem = this.heapArray[parentIndex];

			while (item.compareTo(parentItem) < 0) {

				this.swap(item, parentItem);

				if (item.getHeapIndex() == 0) {
					return;
				}

				parentIndex = (item.getHeapIndex() - 1) / 5;
				parentItem = this.heapArray[parentIndex];

			}
		}

	}


}
