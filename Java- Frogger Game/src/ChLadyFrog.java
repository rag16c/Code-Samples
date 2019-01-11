// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Timer;

// This is the lady frog, which frogger can pick up and 
// take with him to the goal for extra points
public class ChLadyFrog implements ActionListener
{
	private BufferedImage LadyFrog;
	private Timer ActivationTime;
	private boolean isActivated;
	private Random Chooser;
	private int Time; 
	private Dimension XPos;
	private double Speed;
	private double LogLength;
	private double StaticPosition;
	
	public ChLadyFrog( double Length ) throws IOException
	{
		LadyFrog = ImageIO.read(getClass().getResource("GirlFrog.png"));
		
		isActivated = false;
		// The Frog is not on screen
		
		Chooser = new Random();
		Time = Chooser.nextInt(65) + 1;
		// Time stores the time the frog will be on screen in 
		// Half seconds
		
		LogLength = Length;
		// Stores the length of the log it is on
		
		XPos = new Dimension(0,0);
		StaticPosition = -LogLength/2;
		XPos.setSize(StaticPosition, StaticPosition+LadyFrog.getWidth());
		// Sets the initial position at half the log length off 
		// the screen
		
		Speed = 0;
		// Doesn't move unless on the log
		
		ActivationTime = new Timer(500*Time, this);
		ActivationTime.start();
		ActivationTime.setRepeats(false);
		// Creates a timer that Activates the frog when it fires	
	}

	// Sets position of the lady frog
	public void SetPosition( double SpeedNum, int ScreenWidth )
	{
		Speed = SpeedNum;

		if ( isActivated == true )
		{
			// If activated update the position
			XPos.setSize(StaticPosition, StaticPosition+LadyFrog.getWidth());
			StaticPosition += Speed;
		}
		else if ( XPos.getWidth() >= ScreenWidth + LadyFrog.getWidth() )
			isActivated = false;
		// Else if it is off the screen deactivate it
	}
	
	// Resets timer, happens when a goal is met or you die
	public void ResetTimer()
	{
		isActivated = false;
		XPos.setSize(-LogLength,-LogLength+LadyFrog.getWidth());
		ActivationTime.stop();
		Time = Chooser.nextInt(65) + 1;
		ActivationTime = new Timer(500*Time, this);
		ActivationTime.start();		
	}
	
	// Returns if the lady frog is activated
	public boolean Activated()
	{
		return isActivated;
	}
	
	// Deactivates the lady frog
	public void Deactivate()
	{
		isActivated = false;
	}
	
	// Returns the BufferedImage
	public BufferedImage LadyImg()
	{
		return LadyFrog;
	}
	
	// Returns left side of the frogs current position
	public double LadyX1()
	{
		return XPos.getWidth();
	}
	
	// Returns right side of the frogs current position
	public double LadyX2()
	{
		return XPos.getHeight();
	}
	
	// Runs either when you die or you get a goal
	// This resets the position of the frog and resets the 
	// timer
	public void OnDeathorGoal()
	{
		StaticPosition =  -LogLength/2;
		XPos.setSize(StaticPosition, StaticPosition + LadyFrog.getWidth());
		Speed = 0;
		ResetTimer();	
	}
	
	// When the Timer fires, it stops and the lady frog
	// is activated
	public void actionPerformed(ActionEvent e) 
	{
		if ( e.getSource() == ActivationTime )
		{
			isActivated = true;
			ActivationTime.stop();
		}
	}
}

