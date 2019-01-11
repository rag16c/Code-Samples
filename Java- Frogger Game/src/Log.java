// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// This holds info about the logs, and holds the 
// LadyFrog object
public class Log 
{
	private BufferedImage Log;
	private Dimension[] XPos;
	private double Speed;
	private double NewLogLength;
	private double[] StartPos;
	private ChLadyFrog LadyFrog;
	
	public Log(String imgName, int AmtLogs, double SizeOfLogs, double SpeedNum) throws IOException
	{
		Log = ImageIO.read(getClass().getResource(imgName));
	
		NewLogLength = SizeOfLogs * Log.getWidth();
		// Stores the length of the log after the Stretch
		
		Speed = SpeedNum;
		
		XPos = new Dimension[AmtLogs];
		StartPos = new double[AmtLogs];
		
	
		for ( int i = 0; i < AmtLogs; i++ )
		{
			XPos[i] = new Dimension(0,0);
			StartPos[i] = -NewLogLength;
		}
		
		LadyFrog = new ChLadyFrog( NewLogLength );
	}
	
	// Sets the position of the Logs
	public void SetPosition( int ScreenWidth )
	{
		// Checks if the Logs are out of Bounds
		for ( int i = 0; i < XPos.length; i++ )
		{
			if ( OutOfBoundsCheck( XPos[i], ScreenWidth ) )
			{	
				StartPos[i] = -((ScreenWidth + NewLogLength)*i/XPos.length + NewLogLength );
			}
		}
		
		for ( int i = 0; i < XPos.length; i++ ) 
		{
			XPos[i].setSize(StartPos[i] + (ScreenWidth + NewLogLength)*i/XPos.length, StartPos[i] + (ScreenWidth + NewLogLength)*i/XPos.length + NewLogLength);
			StartPos[i] += Speed;
		}
		
	}
	
	// Checks if the logs are out of bounds
	private boolean OutOfBoundsCheck( Dimension TheLog,  int ScreenWidth )
	{
		if ( TheLog.getWidth() >= ScreenWidth )
			return true;
		else
			return false;
	}
	
	// Multiplies the Speed by 1.25
	public void AddSpeed()
	{
		Speed = Speed*1.50;
	}
	
	// Returns the left bound of the log
	public double LogWidth( int Log )
	{
		return XPos[Log].getWidth();
	}
	
	// Returns the BufferedImage
	public BufferedImage Img()
	{
		return Log;
	}
	
	// Returns the width of the image
	public double ImgWidth()
	{
		return NewLogLength;
	}
	
	// Returns the Number of logs being used in that row 
	public int NumLogs()
	{
		return XPos.length;
	}
	
	// Returns the Speed of the logs
	public double SpeedNum()
	{
		return Speed;
	}
	
	// Returns if the Lady Frog should appear on screen
	public boolean ExtraFrogActivated()
	{
		return LadyFrog.Activated();
	}
	
	// Deactivates the Lady Frog
	// Says she shouldnt appear on the screen
	public void ExtraFrogDeactivate()
	{
		LadyFrog.Deactivate();
	}
	
	// Moves the Lady Frog as long as there is collision
	// With the Logs
	public void ExtraFrogMove( int ScreenWidth)
	{
		if ( LogCollision(LadyFrog.LadyX1(), LadyFrog.LadyX2()) )
			LadyFrog.SetPosition( Speed, ScreenWidth);
	}
	
	// Returns the BufferedImage of the Lady Frog
	public BufferedImage LadyFrogImg()
	{
		return LadyFrog.LadyImg();
	}	
	
	// Returns the Left Bound of the LadyFrog
	public double LadyX1()
	{
		return LadyFrog.LadyX1();
	}
	
	// Checks Whether the Frog and the Log are colliding
	public boolean LogCollision( double FrogX1, double FrogX2 )
	{
		for ( int i = 0; i < XPos.length; i++ )
		{
			if ( FrogX2 >= XPos[i].getHeight() && FrogX1 <= XPos[i].getHeight() )
			{
				if ( XPos[i].getHeight() - FrogX1 >= ( FrogX2 - FrogX1 )/2  )
					return true;
			}
			else if ( FrogX1 <= XPos[i].getWidth() && FrogX2 >= XPos[i].getWidth() )
			{
				if ( FrogX2 - XPos[i].getWidth()  >= ( FrogX2 - FrogX1)/2 )
					return true;
			}
			else if ( FrogX1 >=XPos[i].getWidth() && FrogX2 <= XPos[i].getHeight() )
				return true;
		
		}
		
		return false;
		
	}
	
	// Checks if the Frog and the Lady Frog are colliding
	public boolean LadyCollision( double FrogX1, double FrogX2 )
	{
		if ( LadyFrog.LadyX1() <= FrogX2 && LadyFrog.LadyX2() >= FrogX1 )
			return true;
	
		return false;
	}
	
	// Resets the Lady Frog
	// Done if you reach a goal or die
	public void ResetLady()
	{
		LadyFrog.OnDeathorGoal();
	}
	
}
