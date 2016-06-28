package Chess;

import java.util.Random;

import processing.core.PApplet;

public class AI {
	
	protected PApplet parent;
	protected ChessBoard board;
	int size;
	private int[][] AIBoard;
	
	public AI(int size, PApplet parent, ChessBoard board){
		this.parent=parent;
		this.board=board;
		this.size=size;
		AIBoard=new int[size+1][size+1];
	}
	
	 //this will decide a coordinate that AI want to play.
    public int[] AIaction(char color){
    	int point[]=new int[2];
    	int branch=1;
    	int choose;
    	for(int i=1; i<=size ;i++)
			for(int j=1; j<=size ;j++)
				AIBoard[i][j]=0;
    	
    	for(int i=1; i<=size ;i++)
			for(int j=1; j<=size ;j++)
				if(board.points[i][j]=='n' && !board.judgeForbiddenPoint(i,j,color)){
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

}
