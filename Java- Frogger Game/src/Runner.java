// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

// Main Class
public class Runner 
{
	public static void main(String args[]) 
	{
	Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
	Frogger game = new Frogger();
	
	game.setDefaultCloseOperation(game.EXIT_ON_CLOSE);
	game.setSize((int)Screen.getWidth(),(int)Screen.getHeight());
	game.setVisible(true);
	game.requestFocus();
	game.setResizable(false);
	}
}
