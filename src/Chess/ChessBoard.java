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
	private char[][] points=new char[size+1][size+1]; //b:black; w:white; n:null
	private char[][] judgeBoard=new char[size+1][size+1];
	private boolean judgeing;
	
	public void setup() {
		size(width, height);
		this.white=loadImage("white.PNG");
		this.black=loadImage("black.PNG");
		this.whiteCurrent=loadImage("white_current.png");
		this.blackCurrent=loadImage("black_current.png");
		stones=new ArrayList<Stone>();
		for(int i=1; i<=9 ;i++)
			for(int j=1; j<=9 ;j++)
				points[i][j]='n';
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
				if(!stone.isDead){
					if(stone.color.equals("black"))
						image(black,chessX+(stone.x-1)*unit-unit/2, chessY+(stone.y-1)*unit-unit/2);
					else if(stone.color.equals("white"))
						image(white,chessX+(stone.x-1)*unit-unit/2, chessY+(stone.y-1)*unit-unit/2);
				}
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
	
	 public void judgeing(int boardSize, int now_x, int now_y, char c){

		 if(judgeing){
			 judgeBoard[now_x][now_y]='j';
			 if(now_x+1<=boardSize){
				 if(judgeBoard[now_x+1][now_y]=='n')judgeing=false;
				 else if(judgeBoard[now_x+1][now_y]==c)judgeing(boardSize,now_x+1,now_y,c);
			 }
			 if(now_x-1>0){
				 if(judgeBoard[now_x-1][now_y]=='n')judgeing=false;
				 else if(judgeBoard[now_x-1][now_y]==c)judgeing(boardSize,now_x-1,now_y,c);
			 }
			 if(now_y+1<=boardSize){
				 if(judgeBoard[now_x][now_y+1]=='n')judgeing=false;
				 else if(judgeBoard[now_x][now_y+1]==c)judgeing(boardSize,now_x,now_y+1,c);
			 }
			 if(now_y-1>0){
				 if(judgeBoard[now_x][now_y-1]=='n')judgeing=false;
				 else if(judgeBoard[now_x][now_y-1]==c)judgeing(boardSize,now_x,now_y-1,c);
			 }
		 }
	 }
	
	//judge whether can placed chess at coordinate x,y
	 public void judgeChessDead(int x, int y, char c){
		 char d=' ';
		 if(c=='b')d='w';
		 else if(c=='w')d='b';
		 boolean upChessDead, leftChessDead, rightChessDead, downChessDead;		 
			for(int i=1; i<=size ;i++)
				for(int j=1; j<=size ;j++)
					judgeBoard[i][j]=points[i][j];
			judgeBoard[x][y]=c;
			
			judgeing=true;
			judgeing(size,x+1, y, d);
			if(judgeing){
				rightChessDead=true;
				System.out.println("true");
			}
			
			
			judgeing=true;
			judgeing(size,x-1, y, d);
			if(judgeing){
				leftChessDead=true;
				System.out.println("true");
			}
			
			judgeing=true;
			judgeing(size,x, y+1, d);
			if(judgeing){
				downChessDead=true;
				System.out.println("true");
			}
			
			judgeing=true;
			judgeing(size,x, y-1, d);
			if(judgeing){
				upChessDead=true;
				System.out.println("true");
			}
		 
	 }
	 
	@Override
    public void mouseClicked(){
		int x=getCoordinate()[0];
		int y=getCoordinate()[1];
		if(x>0 && y>0){
			if(nowStep%2==1){
				judgeChessDead(x,y,'b');
				points[x][y]='b';
				stones.add(new Stone(x,y,nowStep,"black",this,this));
			}
			else if(nowStep%2==0){
				judgeChessDead(x,y,'w');
				points[x][y]='w';
				stones.add(new Stone(x,y,nowStep,"white",this,this));
			}
			nowStep++;
		}
		
   }


}
