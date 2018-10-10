// ChessPiece SuperClass, has methods such as returning the type of piece, getting the color, returning the row and column, etc


class ChessPiece {

	protected int row;
	protected int col;
	protected char type = '1';
	protected char color = '1';
	protected ChessPiece data;

	public ChessPiece(int r, int c, char type) {
		this.row = r;
		this.col = c;
		this.color = Character.isLowerCase(type) ? 'w' : 'b'; //defines that lower case is white piece, and upper case is black piece
		this.type = type;
		this.data = data;
	}

	//returns type of piece 
	public char getType() {
		return this.type;
	}

	//returns color of piece
	public char getColor() {
		return this.color;
	}

	//returns row of piece
	public int getRow() {
		return this.row;
	}

	//returna column of piece
	public int getCol() {
		return this.col;
	}

	//stores location of piece (row, col)
	public int [] getLocation() {
		int [] loc = new int [2];
		loc[0] = row;
		loc[1] = col;
		return loc;
	}

	public void setLocation(int[] loc) {
		this.row = loc[0];
		this.col = loc[1];
	}

	//returns either true or false depending on attacking method for each piece
	public boolean isAttacking (ChessPiece p) {
		return false;
	}

	public boolean isValidMove(int[] from, int[] to){
		return true;
	}

	//prints type of piece
	public String toString() {
		return "" + this.getType() + ": (" + this.getColor() + ") - " + this.getRow() + "," + this.getCol();
	}

	public ChessPiece getData1(){
		return this.data;
	}
}