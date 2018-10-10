// Linked List class, consists of several functions to add a piece, find a piece, see whether its attacking another piece, and check for validity

class LinkedList {

	protected Node head;
    protected Node tail;
    private int length;


	void LinkedList() {
		head = null;
        tail = null;
        length = 0;
	}

    // Function that inserts an element at the end
    public void addPiece(ChessPiece value)
    {   
    	Node node = new Node(value);
        if(head == null){
            head = node;
            tail = head;
        }
    	tail.setNext(node);
    	tail = node;
        length++;
    }

    // function that checks for validity aka whether two pieces are not in the same place 
    public boolean checkPiece() {

        Node ptr = head;
        ChessPiece p1, p2;

		do {
		  p1 = ptr.getData();
          p2 = this.findPiece(p1.getRow(), p1.getCol());
          if(p2 != null && p1 != p2){
            return false;
		  }
		  ptr = ptr.getNext();
		
		} while(ptr != null && ptr != head);
		return true;
	}

    // finds the piece that is wanted by the input file
	public ChessPiece findPiece(int row, int col) {
        Node location = head;
		do {
			if(location.getData().getRow() == row && location.getData().getCol() == col ) {
				return location.getData();
			}
			location = location.getNext();
		} while(location != null && location != head);
		
		return null;	
	}

// checks if a piece is in the attacking range of another piece by first checking whether they are different colors and proceeding to see if one is attacking the other. Resets the node at the end to the next node.  
	public boolean attacking(ChessPiece piece) {
		Node node1 = head;
		ChessPiece p1;

	
		while(node1 != null) {
			p1 = node1.getData();
			
				if((p1.getColor() != piece.getColor()) && (p1 != piece)) {

					if(piece.isAttacking(p1)) {

						return true;
					}
                }
			node1 = node1.getNext();
			}  
		return false;
	}
}