// Creator: Riley Garrison

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Creates a bug that sits in a goal and if frogger gets
// That goal he gets extra points
public class Bug implements ActionListener
{
	private BufferedImage Bug;
	private Timer ActivationTime;
	private boolean IsActivated;
	private Random Chooser;
	private int GoalNum;
	private int TimeActivated;
	
	public Bug() throws IOException
	{
		Bug = ImageIO.read(getClass().getResource("Bug.png"));

		IsActivated = false;
		// The bug is not in a goal
		
		Chooser = new Random();
		GoalNum = Chooser.nextInt(5);
		// Stores which goal should hold the bug
		
		TimeActivated = Chooser.nextInt(65) + 1;
		// Stores when the bug should activate in half seconds
		
		ActivationTime = new Timer(500*TimeActivated, this);
		ActivationTime.setActionCommand("Enable");
		ActivationTime.start();
		// Creates the timer that activates the bug when the timer
		// fires
	}

	// Resets the timer when you die or get a goal
	// This chooses a new goal based on the number of goals left
	// and chooses a new time for the bug to show up
	public void ResetTime( int GoalsLeft )
	{
		ActivationTime.stop();
		IsActivated = false;
		GoalNum = Chooser.nextInt( GoalsLeft );
		TimeActivated = Chooser.nextInt(65) + 1;
		ActivationTime = new Timer(500*TimeActivated, this);
		ActivationTime.setActionCommand("Enable");
		ActivationTime.start();
	}
	
	// Returns which goal should have the bug
	public int GetGoalNum()
	{
		return GoalNum;
	}
	
	// Returns whether the bug is Activated
	public boolean Activated()
	{
		return IsActivated;
	}
	
	// Returns the BufferedImage
	public BufferedImage getBugImg()
	{
		return Bug;
	}
	
	// Stops the Timer
	public void StopTime()
	{
		ActivationTime.stop();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if ( e.getActionCommand().compareTo("Enable") == 0 )
		{
			// If the timer is set to enable then it creates
			// A new timer that deactivates the bug in 15 secs
			IsActivated = true;
			ActivationTime.stop();
			ActivationTime = new Timer(15000, this);
			ActivationTime.setActionCommand("Disable");
			ActivationTime.start();
		}
		else if ( e.getActionCommand().compareTo("Disable") == 0 )
		{
			// When the disable timer fires, it deactivates
			// the bug and stops the timer
			IsActivated = false;
			ActivationTime.stop();
		}
	}
}


