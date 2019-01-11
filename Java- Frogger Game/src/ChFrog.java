// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// Holds info about Frogger, the frog you control
public class ChFrog 
{
	private static BufferedImage Frog;
	private Dimension XPos;
	private int YPosition;
	private int Direction;
	private double Increment;
	private double StaticPosition;  // Always Keeps lefthand position of frog
	private boolean LadyIntersect;
	
	public ChFrog( String imgName ) throws IOException
	{
		Frog = ImageIO.read(getClass().getResource(imgName));
		XPos = new Dimension(0, Frog.getWidth());
		YPosition = 13;
		Direction = 0;
		Increment = 0;
		
		StaticPosition = 0;
		// I need this for the frog to not glitch out with double Speed Nums
		
		LadyIntersect = false;
		// Stores whether it is guiding the lady frog or not
	}
	
	// Sets the Position of the frog
	public void SetPosition(int ScreenWidth )
	{
		if ( Direction == -1 && XPos.getHeight() + Frog.getWidth() <= ScreenWidth )
		{
			// If you move right and you dont hit the end of the frame
			StaticPosition += Frog.getWidth();
			XPos.setSize(StaticPosition, StaticPosition + Frog.getWidth());
		}
		else if ( Direction == -1 && XPos.getHeight() + Frog.getWidth() >= ScreenWidth )
		{
			// If you move right and hit or go over the edge of the frame
			StaticPosition = ScreenWidth - Frog.getWidth();
			XPos.setSize(StaticPosition, StaticPosition + Frog.getWidth());
		}
		else if ( Direction == 1 && XPos.getWidth() - Frog.getWidth() >= 0 )
		{
			// If you move left and you dont hit the end of the frame
			StaticPosition -= Frog.getWidth();
			XPos.setSize(StaticPosition, StaticPosition + Frog.getWidth());
		}
		else if ( Direction == 1 && XPos.getWidth() - Frog.getWidth() <= 0 )
		{
			// If you move left and you hit the end of the frame
			StaticPosition = 0;
			XPos.setSize(StaticPosition, StaticPosition + Frog.getWidth());
		}
		
		// Resets direction so that you dont keep moving the direction you typed
		Direction = 0;
		AutoPosition( ScreenWidth );
	}
	
	// This Moves you along if you are on a log or a turtle
	public void AutoPosition( int ScreenWidth )
	{
		if ( Increment < 0 && XPos.getWidth() + Increment < 0 )
		{
			// If the object is moving left and you havent hit the end of the frame
			StaticPosition = 0;
			XPos.setSize(StaticPosition, StaticPosition + Frog.getWidth() );
		}
		else if ( Increment > 0 && XPos.getHeight() + Increment > ScreenWidth )
		{
			// If the object is moving right and you havent hit the end of the frame
			StaticPosition = ScreenWidth - Frog.getWidth();
			XPos.setSize(StaticPosition, StaticPosition + Frog.getWidth());
		}
		else
		{
			// If increment is 0
			StaticPosition += Increment;
			XPos.setSize(StaticPosition, StaticPosition + Frog.getWidth());
		}
		
		// Resets increment so you dont keep moving after you go back to land
		Increment = 0;
	}
	
	// Sets the direction that you want to move 
	// 1 for left
	// -1 for right
	public void SetDirection( int Num )
	{
		Direction = Num;
	}

	// Sets the Increment. Used when frog is on a moving object
	public void SetIncrement ( double Speed )
	{
		Increment = Speed;
	}
	
	// Sets the Y Position of the frog. Used When W or S is pressed
	public boolean SetYPosition( int Num )
	{
		if ( YPosition == 13 && Num == 1 ) // If moving down at the bottom of the screen
			return false;
		else if ( YPosition == 1 && Num == -1 ) // If moving up at the top of the screen
			return false;
		else
		{
			YPosition = YPosition + Num;
			return true;
		}
	}
	
	// Resets position. Used when you get a goal or die
	public void ResetPosition()
	{
		YPosition = 13;
		StaticPosition = 0;
		XPos.setSize(0,Frog.getWidth());
	}
	
	// Enables when you touch the lady frog
	public void LadyTouch()
	{
		LadyIntersect = true;
	}
	
	// Returns whether you are guiding the lady frog or not
	public boolean isTouchingLady()
	{
		return LadyIntersect;
	}
	
	// Resets you back to your normal state
	public void ResetLadyTouch()
	{
		LadyIntersect = false;
	}
	
	// Returns the BufferedImage
	public BufferedImage Img()
	{
		return Frog;
	}
	
	// Returns the width of the Image
	public int ImgWidth()
	{
		return Frog.getWidth();
	}
	
	// Returns the left bound of the frog
	public double GetX1()
	{
		return XPos.getWidth();
	}
	
	// Returns the right bound of the frog
	public double GetX2()
	{
		return XPos.getHeight();
	}

	// Returns the YPosition of the frog
	public int getYPositon()
	{
		return YPosition;
	}
	
	
	}
