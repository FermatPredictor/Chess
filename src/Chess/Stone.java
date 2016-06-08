package Chess;

import processing.core.PApplet;

public class Stone {
	
	public int step;
	String color;
	boolean isDead=false;
	int x,y;
	int boardSize;
	protected PApplet parent;
	protected ChessBoard board;
	
	public Stone(int[] coordinate, int step, String color, PApplet parent, ChessBoard board){

		this.x=coordinate[0];
		this.y=coordinate[1];
		this.step=step;
		this.color=color;
		this.parent=parent;
		this.board=board;
	}
	

}
