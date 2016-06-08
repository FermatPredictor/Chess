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
	private float unit=(float)chessBoardWidth/(size-1);
	private int nowStep=1;
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
		rect(chessX-20,chessY-20,chessBoardWidth+40,chessBoardWidth+40);
		stroke(0);
		for(int i=0; i<size ; i++){
			line(chessX+i*unit,chessY,chessX+i*unit,chessY+chessBoardWidth);
		}
		for(int i=0; i<size ; i++){
			line(chessX,chessY+i*unit,chessX+chessBoardWidth,chessY+i*unit);
		}
		
		
		if(getCoordinate()[0]>0 && getCoordinate()[1]>0){
			if(nowStep%2==1){
				fill(0);
				ellipse(chessX+(getCoordinate()[0]-1)*unit, chessY+(getCoordinate()[1]-1)*unit,50,50);
			}
			else if(nowStep%2==0){
				fill(255);
				ellipse(chessX+(getCoordinate()[0]-1)*unit, chessY+(getCoordinate()[1]-1)*unit,50,50);
			}
		}
		
		
		if(!stones.isEmpty()){
			for(Stone stone: stones){
				if(stone.color.equals("black"))
					image(black,chessX+(stone.x-1)*unit-unit/2, chessY+(stone.y-1)*unit-unit/2);
				else if(stone.color.equals("white"))
					image(white,chessX+(stone.x-1)*unit-unit/2, chessY+(stone.y-1)*unit-unit/2);
				}
		}
		
	}
	
	public int[] getCoordinate(){
		int point[]=new int[2];
		float unit=(float)chessBoardWidth/(size-1);
		for(int i=1; i<=size;i ++){
			if(mouseX>=chessX+(i-1)*unit-unit/2 && mouseX<chessX+(i-1)*unit+unit/2){
				point[0]=i;
			}
		}
		for(int j=1; j<=size;j ++){
			if(mouseY>=chessY+(j-1)*unit-unit/2 && mouseY<chessY+(j-1)*unit+unit/2){
				point[1]=j;
			}
		}
		//System.out.println(point[0]+" "+point[1]);
		return point;
	}
	
	@Override
    public void mouseClicked(){
		if(getCoordinate()[0]>0 && getCoordinate()[1]>0){
			if(nowStep%2==1)
				stones.add(new Stone(getCoordinate(),nowStep,"black",this,this));
			else if(nowStep%2==0)
				stones.add(new Stone(getCoordinate(),nowStep,"white",this,this));
			nowStep++;
		}
		
   }


}
