package Chess;

import processing.core.PApplet;

public class ChessBoard extends PApplet{
	
	private static final long serialVersionUID = 1L;
	private final static int width = 1000, height = 500;
	private int size;
	
	public void setup() {
		size(width, height);
	}
	
	public void draw() 
	{
		background(255);
	}

}
