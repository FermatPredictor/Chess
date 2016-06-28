package Chess;

import java.util.Random;

import processing.core.PApplet;

public class AI {
	
	protected PApplet parent;
	protected ChessBoard board;
	int size;
	private boolean judgeing;
	private int caps=0;
	private int capsPoint[]=new int[2];//the coordinate of captured chess, only use in judge ko. 
	private int judgeCaps=0;
	private int[][] AIBoard;
	private int[][] valueBoard;
	private char[][] simulateBoard; //b:black; w:white; n:null
	private char[][] judgeBoard;
	
	public AI(int size, PApplet parent, ChessBoard board){
		this.parent=parent;
		this.board=board;
		this.size=size;
		AIBoard=new int[size+1][size+1];
		simulateBoard=new char[size+1][size+1];
		judgeBoard=new char[size+1][size+1];
		valueBoard=new int[size+1][size+1];
	}
	
	private void judgeing(int boardSize, int now_x, int now_y, char c){

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
	private void cleaning(int boardSize, int now_x, int now_y, char c){
		 

	     caps++;
		 capsPoint[0]=now_x;
		 capsPoint[1]=now_y;

		 simulateBoard[now_x][now_y]='n';
		 if(now_x+1<=boardSize){
			 if(simulateBoard[now_x+1][now_y]==c)cleaning(boardSize,now_x+1,now_y,c);
		 }
		 if(now_x-1>0){
			 if(simulateBoard[now_x-1][now_y]==c)cleaning(boardSize,now_x-1,now_y,c);
		 }
		 if(now_y+1<=boardSize){
			 if(simulateBoard[now_x][now_y+1]==c)cleaning(boardSize,now_x,now_y+1,c);
		 }
		 if(now_y-1>0){
			 if(simulateBoard[now_x][now_y-1]==c)cleaning(boardSize,now_x,now_y-1,c);
		 }
	 }
	
	 //reset the judgeBoard
	private void reset(){
		 for(int i=1; i<=size ;i++)
				for(int j=1; j<=size ;j++)
					judgeBoard[i][j]=simulateBoard[i][j];
	 }
	
	//if the chess places at coordinate x,y and captures some chess, then clear the captured chess.
	private void judgeChessDead(int x, int y, char c){

		 char d=' ';
		 if(c=='b')d='w';
		 else if(c=='w')d='b';	
		 caps=0;
		    reset();
		    judgeBoard[x][y]=c;
		    if(x+1<=size)
		    	if(simulateBoard[x+1][y]==d){
		    		judgeing=true;
		    		judgeing(size,x+1, y, d);
		    		if(judgeing)
		    			cleaning(size,x+1, y, d);
		    }
			
			reset();
			judgeBoard[x][y]=c;
			if(x-1>0)
				if(simulateBoard[x-1][y]==d){
					judgeing=true;
					judgeing(size,x-1, y, d);
					if(judgeing)
						cleaning(size,x-1, y, d);

			}
			
			reset();
			judgeBoard[x][y]=c;
			if(y+1<=size)
				if(simulateBoard[x][y+1]==d){
					judgeing=true;
					judgeing(size,x, y+1, d);
					if(judgeing)
						cleaning(size,x, y+1, d);
			}
			
			reset();
			judgeBoard[x][y]=c;
			if(y-1>0)
				if(simulateBoard[x][y-1]==d){
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
	 
	 private boolean judgeForbiddenPoint(int x, int y, char c){
		 boolean isEatSomeChess=false;
		 char d=' ';
		 if(c=='b')d='w';
		 else if(c=='w')d='b';	
		 boolean isForbiddenPoint=false;
		 judgeCaps=0;
		 
		    reset();
		    judgeBoard[x][y]=c;
		    if(x+1<=size)
		    	if(simulateBoard[x+1][y]==d){
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
				if(simulateBoard[x-1][y]==d){
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
				if(simulateBoard[x][y+1]==d){
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
				if(simulateBoard[x][y-1]==d){
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
	
	 private void simulatePlaceChess(char color, int x, int y){
		if(x>0 && y>0 && x<=size && y<=size && simulateBoard[x][y]=='n'){
			if(color=='b'){
				judgeChessDead(x,y,'b');
				simulateBoard[x][y]='b';
			}
			else if(color=='w'){
				judgeChessDead(x,y,'w');
				simulateBoard[x][y]='w';
			}
		}
	}
	 

	 private int[] simulateAction(char color){
	    	int point[]=new int[2];
	    	int branch=1;
	    	int choose;

	    	for(int i=1; i<=size ;i++)
				for(int j=1; j<=size ;j++)
					AIBoard[i][j]=0;
	    	
	    	for(int i=1; i<=size ;i++)
				for(int j=1; j<=size ;j++)
					if(simulateBoard[i][j]=='n' && !judgeForbiddenPoint(i,j,color)){
						AIBoard[i][j]=branch;
						branch++;
					}
	    	Random random=new Random();
	    	if(branch>1){
	    		choose=random.nextInt(branch-1)+1;
	    		for(int i=1; i<=size ;i++)
	    			for(int j=1; j<=size ;j++)
	    				if(AIBoard[i][j]==choose){
	    					point[0]=i;
	    					point[1]=j;
	    				}
	    	}
	    	else{
	    		point[0]=size+1;
				point[1]=size+1;
	    	}
	    	
	    	return point;
	    }
	 
	 private void copyBoard(){
			 for(int i=1; i<=size ;i++)
					for(int j=1; j<=size ;j++)
						simulateBoard[i][j]=board.points[i][j];
		 }
	
	 //this will decide a coordinate that AI want to play.
    public int[] AIaction(char color){
    	int point[]=new int[2];
    	
    	char d=' ';
		if(color=='b')d='w';
	    else if(color=='w')d='b';	
		
    	for(int i=1; i<=size ;i++)
			for(int j=1; j<=size ;j++)
				valueBoard[i][j]=0;
    	
		int simulatePoint[]=new int[2];
		int x,y;
		boolean isEnding=false;
		
    	for(int i=1; i<=size ;i++)
			for(int j=1; j<=size ;j++)
				if(board.points[i][j]=='n' && !board.judgeForbiddenPoint(i,j,color)){
					for(int k=1; k<=20 ;k++){
						isEnding=false;
						copyBoard();
						simulatePlaceChess(color,i,j);
						while(!isEnding){
							simulatePoint=simulateAction(d);
							x=simulatePoint[0];
							y=simulatePoint[1];
							if(x>size || y>size || x<1 || y<1){
								isEnding=true;
								valueBoard[i][j]++;
								break;
							}
							simulatePlaceChess(d,x,y);
							simulatePoint=simulateAction(color);
							x=simulatePoint[0];
							y=simulatePoint[1];
							if(x>size || y>size || x<1 || y<1){
								isEnding=true;
								break;
							}
							simulatePlaceChess(color,x,y);
						}
					}
				}
    	
    	point[0]=1;
		point[1]=1;
		for(int i=1; i<=size ;i++){
			for(int j=1; j<=size ;j++){
				if(valueBoard[i][j]>valueBoard[point[0]][point[1]]){
					point[0]=i;
					point[1]=j;
				}
				System.out.printf("%2d ",valueBoard[j][i]);
			}
		System.out.println("");
	    }

    	System.out.println(point[0]+" "+point[1]);
    	System.out.println("");
    	return point;
    }
    
    

}
