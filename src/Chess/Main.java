package Chess;

import javax.swing.JFrame;

public class Main extends JFrame{
	
	private final static int width = 1200, height = 700;
	public static void main(String [] args){
		
		ChessBoard applet1 = new ChessBoard();
		applet1.init();
		applet1.start();
		applet1.setFocusable(true);
		
		JFrame window=new JFrame("Chess");
		window.setLocation(100, 50);
		window.setContentPane(applet1);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(width, height);
		window.setVisible(true);
		window.setResizable(false);
	}

}
