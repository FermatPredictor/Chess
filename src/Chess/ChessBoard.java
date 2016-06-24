package Chess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PImage;

public class ChessBoard extends PApplet {
	
	private static final long serialVersionUID = 1L;
	private final static int width = 1200, height = 500;
	private ControlP5 cp5;
	private int chessX=100, chessY=50, chessBoardWidth=550;
	public PImage white, black, whiteCurrent, blackCurrent;
	private int size=9;
	private float unit=(float)chessBoardWidth/(size-1);
	private int nowStep=1;
	private ArrayList<Stone> stones;
	private char[][] points=new char[size+1][size+1]; //b:black; w:white; n:null
	private char[][] judgeBoard=new char[size+1][size+1];
	private boolean judgeing;
	private boolean canPlaceChess=true;
	private boolean isForbiddenPoint=false;
	public boolean isClicked = false;
	private int caps=0;
	private int capsPoint[]=new int[2];//the coordinate of captured chess, only use in judge ko. 
	private int judgeCaps=0;
	String information="";
	private boolean isWhiteAIOn=false;
	private boolean isBlackAIOn=false;
	private boolean isAITurn=false;
	
	public ChessBoard() {
		try {
			FileReader fr = new FileReader("record.txt");
			BufferedReader br = new BufferedReader(fr);
			
			while(br.ready()) {
				//System.out.print((char)br.read());
				information  = br.readLine();
			}
			fr.close();
			System.out.println(information);
			
		} catch (IOException e) {
		}
	}
	
	public void setup() {
		size(width, height);
		cp5= new ControlP5(this);
		this.white=loadImage("white.PNG");
		this.black=loadImage("black.PNG");
		this.whiteCurrent=loadImage("white_current.png");
		this.blackCurrent=loadImage("black_current.png");
        white.resize((int)unit, (int)unit);
        black.resize((int)unit, (int)unit);
        whiteCurrent.resize((int)unit, (int)unit);
        blackCurrent.resize((int)unit, (int)unit);
		stones=new ArrayList<Stone>();
		
		loading();
		
		//button
		cp5.addButton("undo").setLabel("Undo")		
                             .setPosition(700,50)
                             .setSize(100, 50);
		cp5.addButton("pass").setLabel("Pass")
		                     .setPosition(810,50)
                             .setSize(100, 50);
		cp5.addButton("resign").setLabel("Resign")
                               .setPosition(920,50)
                               .setSize(100, 50);
		cp5.addButton("estimate").setLabel("Estimate")
                                 .setPosition(1030,50)
                                 .setSize(100, 50);
	}
	
	public void draw() 
	{   
		if(isWhiteAIOn && nowStep%2==0){
			isAITurn=true;
			placeChessForAI('w');
			isAITurn=false;
		}
		else if(isBlackAIOn && nowStep%2==1){
			isAITurn=true;
			placeChessForAI('b');
			isAITurn=false;
		}
		
		if(mousePressed && canPlaceChess && !isForbiddenPoint && !isAITurn){
			placeChess();
			canPlaceChess=false;
			isClicked = true;
		}
		
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
		
		int x=getCoordinate()[0];
		int y=getCoordinate()[1];
		if(nowStep%2==1)
			isForbiddenPoint=judgeForbiddenPoint(x,y,'b');
		else if(nowStep%2==0)
			isForbiddenPoint=judgeForbiddenPoint(x,y,'w');
		
		if(x>0 && y>0 && !isForbiddenPoint && points[x][y]=='n'){
			if(nowStep%2==1){
				fill(0);
				ellipse(chessX+(getCoordinate()[0]-1)*unit, chessY+(getCoordinate()[1]-1)*unit,unit*(float)0.8,unit*(float)0.8);
			}
			else if(nowStep%2==0){
				fill(255);
				ellipse(chessX+(getCoordinate()[0]-1)*unit, chessY+(getCoordinate()[1]-1)*unit,unit*(float)0.8,unit*(float)0.8);
			}
		}
		
		
		if(!stones.isEmpty()){
			for(Stone stone: stones){
				if(stone.step==nowStep-1){
					if(stone.color.equals("black"))
						image(blackCurrent,chessX+(stone.x-1)*unit-unit/2, chessY+(stone.y-1)*unit-unit/2);
					else if(stone.color.equals("white"))
						image(whiteCurrent,chessX+(stone.x-1)*unit-unit/2, chessY+(stone.y-1)*unit-unit/2);
				}
				else if(!stone.isDead){
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
	 
	 
	 //when some chess be eaten, removed it
	 public void cleaning(int boardSize, int now_x, int now_y, char c){
		 
		 for(Stone stone: stones){
			 if(!stone.isDead && stone.x==now_x && stone.y==now_y){
				 stone.isDead=true;
				 caps++;
				 capsPoint[0]=stone.x;
				 capsPoint[1]=stone.y;
			 }
		 }
		 points[now_x][now_y]='n';
		 if(now_x+1<=boardSize){
			 if(points[now_x+1][now_y]==c)cleaning(boardSize,now_x+1,now_y,c);
		 }
		 if(now_x-1>0){
			 if(points[now_x-1][now_y]==c)cleaning(boardSize,now_x-1,now_y,c);
		 }
		 if(now_y+1<=boardSize){
			 if(points[now_x][now_y+1]==c)cleaning(boardSize,now_x,now_y+1,c);
		 }
		 if(now_y-1>0){
			 if(points[now_x][now_y-1]==c)cleaning(boardSize,now_x,now_y-1,c);
		 }
	 }
	 
	 //reset the judgeBoard
	 public void reset(){
		 for(int i=1; i<=size ;i++)
				for(int j=1; j<=size ;j++)
					judgeBoard[i][j]=points[i][j];
	 }
	
	//if the chess places at coordinate x,y and captures some chess, then clear the captured chess.
	 public void judgeChessDead(int x, int y, char c){

		 char d=' ';
		 if(c=='b')d='w';
		 else if(c=='w')d='b';	
		 caps=0;
		    reset();
		    judgeBoard[x][y]=c;
		    if(x+1<=size)
		    	if(points[x+1][y]==d){
		    		judgeing=true;
		    		judgeing(size,x+1, y, d);
		    		if(judgeing)
		    			cleaning(size,x+1, y, d);
		    }
			
			reset();
			judgeBoard[x][y]=c;
			if(x-1>0)
				if(points[x-1][y]==d){
					judgeing=true;
					judgeing(size,x-1, y, d);
					if(judgeing)
						cleaning(size,x-1, y, d);

			}
			
			reset();
			judgeBoard[x][y]=c;
			if(y+1<=size)
				if(points[x][y+1]==d){
					judgeing=true;
					judgeing(size,x, y+1, d);
					if(judgeing)
						cleaning(size,x, y+1, d);
			}
			
			reset();
			judgeBoard[x][y]=c;
			if(y-1>0)
				if(points[x][y-1]==d){
					judgeing=true;
					judgeing(size,x, y-1, d);
					if(judgeing)
						cleaning(size,x, y-1, d);
			}	
		 
	 }
	 
	//use in function "judgeForbiddenPoint" that when some chess be eaten, count it(if>1, may repeat count)
	 private void countCaps(int boardSize, int now_x, int now_y, char c){
		 
		     judgeCaps++;
			 judgeBoard[now_x][now_y]='j';
			 if(now_x+1<=boardSize){
				 if(judgeBoard[now_x+1][now_y]==c)countCaps(boardSize,now_x+1,now_y,c);
			 }
			 if(now_x-1>0){
				 if(judgeBoard[now_x-1][now_y]==c)countCaps(boardSize,now_x-1,now_y,c);
			 }
			 if(now_y+1<=boardSize){
				 if(judgeBoard[now_x][now_y+1]==c)countCaps(boardSize,now_x,now_y+1,c);
			 }
			 if(now_y-1>0){
				 if(judgeBoard[now_x][now_y-1]==c)countCaps(boardSize,now_x,now_y-1,c);
			 }
	 }
	 
	//use in function "draw" that always judge whether the chess can place at this place
	 public boolean judgeForbiddenPoint(int x, int y, char c){
		 boolean isEatSomeChess=false;
		 char d=' ';
		 if(c=='b')d='w';
		 else if(c=='w')d='b';	
		 boolean isForbiddenPoint=false;
		 judgeCaps=0;
		 
		    reset();
		    judgeBoard[x][y]=c;
		    if(x+1<=size)
		    	if(points[x+1][y]==d){
		    		judgeing=true;
		    		judgeing(size,x+1, y, d);
		    		if(judgeing){
		    			isEatSomeChess=true;
		    			reset();
		    			countCaps(size,x+1, y, d);
		    		}
		    }
			
			reset();
			judgeBoard[x][y]=c;
			if(x-1>0)
				if(points[x-1][y]==d){
					judgeing=true;
					judgeing(size,x-1, y, d);
					if(judgeing){
						isEatSomeChess=true;
						reset();
						countCaps(size,x-1, y, d);
					}
			}
			
			reset();
			judgeBoard[x][y]=c;
			if(y+1<=size)
				if(points[x][y+1]==d){
					judgeing=true;
					judgeing(size,x, y+1, d);
					if(judgeing){
						isEatSomeChess=true;
						reset();
						countCaps(size,x, y+1, d);
					}
			}
			
			reset();
			judgeBoard[x][y]=c;
			if(y-1>0)
				if(points[x][y-1]==d){
					judgeing=true;
					judgeing(size,x, y-1, d);
					if(judgeing){
						isEatSomeChess=true;
						reset();
						countCaps(size,x, y-1, d);
					}
			}	
			
			if(caps==1 && x==capsPoint[0] && y==capsPoint[1] && judgeCaps==1)//rule of ko
				isForbiddenPoint=true;
			else if(!isEatSomeChess){
				judgeing=true;
				reset();
				judgeing(size,x,y,c);
				if(judgeing)
					isForbiddenPoint=true;				
			}
			
			return isForbiddenPoint;	 
	 }

   
	//judge in function "draw", if mousePressed, placed the chess.(released then can placed again)
    public void placeChess(){

		int x=getCoordinate()[0];
		int y=getCoordinate()[1];
		if(x>0 && y>0 && points[x][y]=='n'){
			if(nowStep%2==1){
				judgeChessDead(x,y,'b');
				points[x][y]='b';
				stones.add(new Stone(x,y,nowStep,"black",this,this));
				char ch_x=(char)((int)'a'+x-1);
				char ch_y=(char)((int)'a'+y-1);
				information=information.concat(";B["+ch_x+ch_y+"]");
			}
			else if(nowStep%2==0){
				judgeChessDead(x,y,'w');
				points[x][y]='w';
				stones.add(new Stone(x,y,nowStep,"white",this,this));
				char ch_x=(char)((int)'a'+x-1);
				char ch_y=(char)((int)'a'+y-1);
				information=information.concat(";W["+ch_x+ch_y+"]");
			}
				nowStep++;
				
				try{
					FileWriter fw = new FileWriter("record.txt");
					fw.write(information + "\r\n");
					fw.flush();
					fw.close();
					System.out.println(information);
				} catch (IOException e) {
				}
		}
		
   }
   
    
	//use in the function "loading"
    private void placeChess(char color, int x, int y){

		if(x>0 && y>0 && points[x][y]=='n'){
			if(color=='b'){
				judgeChessDead(x,y,'b');
				points[x][y]='b';
				stones.add(new Stone(x,y,nowStep,"black",this,this));
			}
			else if(color=='w'){
				judgeChessDead(x,y,'w');
				points[x][y]='w';
				stones.add(new Stone(x,y,nowStep,"white",this,this));
			}
				nowStep++;	
		}
   }
    
    //load the record, and place chess from the first step to the last.
    private void loading(){
    	
    	stones.clear();
    	nowStep=1;
		for(int i=1; i<=size ;i++)
			for(int j=1; j<=size ;j++)
				points[i][j]='n';
		
    	int begin=information.indexOf(';',1);
    	System.out.println(begin);
    	if(begin!=-1)
    		while(begin<information.length()){
    			int x=information.charAt(begin+3)-'a'+1;
    			int y=information.charAt(begin+4)-'a'+1;
    			if(nowStep%2==1){
    				placeChess('b',x,y);
    			}
    			else if(nowStep%2==0){
    				placeChess('w',x,y);
    			}
    			System.out.println(x+" "+y);
    			begin+=6;
    		}
    	System.out.println(nowStep);
    }
    
    //this will decide a coordinate that AI want to play.
    private int[] AIaction(char color){
    	int point[]=new int[2];
    	Random random=new Random();
    	do{
    	point[0]=random.nextInt(size)+1;
    	point[1]=random.nextInt(size)+1;
    	}
    	while(judgeForbiddenPoint(point[0],point[1],color));
    	
    	return point;
    }
    
    private void placeChessForAI(char color){
    	int point[]=new int[2];
    	point=AIaction(color);
		int x=point[0];
		int y=point[1];
		if(x>0 && y>0 && points[x][y]=='n'){
			if(nowStep%2==1){
				judgeChessDead(x,y,'b');
				points[x][y]='b';
				stones.add(new Stone(x,y,nowStep,"black",this,this));
				char ch_x=(char)((int)'a'+x-1);
				char ch_y=(char)((int)'a'+y-1);
				information=information.concat(";B["+ch_x+ch_y+"]");
			}
			else if(nowStep%2==0){
				judgeChessDead(x,y,'w');
				points[x][y]='w';
				stones.add(new Stone(x,y,nowStep,"white",this,this));
				char ch_x=(char)((int)'a'+x-1);
				char ch_y=(char)((int)'a'+y-1);
				information=information.concat(";W["+ch_x+ch_y+"]");
			}
				nowStep++;
				
				try{
					FileWriter fw = new FileWriter("record.txt");
					fw.write(information + "\r\n");
					fw.flush();
					fw.close();
					System.out.println(information);
				} catch (IOException e) {
				}
		}
		
   }
    
    public void mouseReleased(){
    	canPlaceChess=true;
    }
    
    //button
    public void undo(){
    	if(nowStep>1){
    		information=information.substring(0,information.length()-6);
			try{
				FileWriter fw = new FileWriter("record.txt");
				fw.write(information + "\r\n");
				fw.flush();
				fw.close();
				System.out.println(information);
			} catch (IOException e) {
			}
			
			loading();
    	}
    }
    
    public void pass(){
    	
    }
    
    public void resign(){
    	
    }
    
    public void estimate(){
    	
    }
    
}
