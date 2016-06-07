package Chess;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class ChessBoard extends PApplet {
	
	private static final long serialVersionUID = 1L;
	private final static int width = 1000, height = 500;
	private int chessX=100, chessY=50, chessBoardWidth=550;
	public PImage white, black, whiteCurrent, blackCurrent;
	private int size=9;
	private ArrayList<Stone> stones;
	
	public void setup() {
		size(width, height);
		this.white=loadImage("white.PNG");
		this.black=loadImage("black.PNG");
		this.whiteCurrent=loadImage("white_current.png");
		this.blackCurrent=loadImage("black_current.png");
		stones=new ArrayList<Stone>();
	}
	
	public void draw() 
	{
		background(52,203,41);
		fill(168,134,87);
        stroke(200);
		rect(chessX,chessY,chessBoardWidth,chessBoardWidth);
		stroke(0);
		for(int i=0; i<size ; i++){
			line(chessX+i*chessBoardWidth/(size-1),chessY,chessX+i*chessBoardWidth/(size-1),chessY+chessBoardWidth);
		}
		for(int i=0; i<size ; i++){
			line(chessX,chessY+i*chessBoardWidth/(size-1),chessX+chessBoardWidth,chessY+i*chessBoardWidth/(size-1));
		}
		
		if(!stones.isEmpty()){
			for(Stone stone: stones){
				if(stone.color.equals("black"))
					image(black,stone.x, stone.y);
				else if(stone.color.equals("white"))
					image(white,stone.x, stone.y);
				}
		}
		
	}
	
	@Override
    public void mouseClicked(){
		
		stones.add(new Stone(1,"black",this,this));
   }

	
	

}
