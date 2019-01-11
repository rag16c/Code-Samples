// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Holds info about the turtles
public class Turtle 
{
	private BufferedImage Turtle;
	private Dimension[] XPos;
	private double[] StartPos;
	private double Speed;
	private int BufferSpace;
	private int TurtleGroups;
	
	public Turtle(String Turtle1, String Turtle2, double SpeedNum, int GroupTurtles) throws IOException
	{
		Turtle = ImageIO.read(getClass().getResource(Turtle1));

		TurtleGroups = GroupTurtles;
		
		XPos = new Dimension[TurtleGroups*3];
		StartPos = new double[TurtleGroups];
		
		for ( int i = 0; i < TurtleGroups*3; i++ )
			XPos[i] = new Dimension(0,0);
		
		for ( int i = 0; i < TurtleGroups; i++ )
			StartPos[i] = 0;
		
		Speed = SpeedNum;
		
		BufferSpace = 5;
		// Space between turtles next to each other
		
	}

	// Sets the position of the turtles
	public void SetPosition( int ScreenWidth )
	{
	
		for ( int i = 0; i < 3; i++ ) 
		{
			// Sets 4 groups of turtles, each group has 3 turtles
			XPos[i].setSize(StartPos[0] + ScreenWidth + BufferSpace*(-i) + Turtle.getWidth()*(-1-i), StartPos[0] + ScreenWidth + BufferSpace*(-i) + Turtle.getWidth()*(i));
			XPos[i+3].setSize(StartPos[1] + (ScreenWidth*3 + Turtle.getWidth()*(-3-4*i) + BufferSpace*(-2-4*i))/4,StartPos[1] + (ScreenWidth*3 + Turtle.getWidth()*(1-4*i) + BufferSpace*(-2-4*i))/4);
			XPos[i+6].setSize(StartPos[2] + (ScreenWidth*2 + Turtle.getWidth()*(-6-4*i) + (-4-4*i)*BufferSpace)/4, StartPos[2] + (ScreenWidth*2 + Turtle.getWidth()*(-2-4*i) + (-4-4*i)*BufferSpace)/4);
			XPos[i+9].setSize(StartPos[3] + (ScreenWidth + Turtle.getWidth()*(-9-4*i) + (-6-4*i)*BufferSpace)/4, StartPos[3] + (ScreenWidth + Turtle.getWidth()*(-5-4*i) + (-6-4*i)*BufferSpace)/4);
		
		}
	
		StartPos[0] += Speed;
		StartPos[1] += Speed;
		StartPos[2] += Speed;
		StartPos[3] += Speed;
		
		// Checks if the turtles are out of bounds and resets the positions
		if ( OutOfBoundsCheck( 0, ScreenWidth ) )
			StartPos[0] = BufferSpace*2 + Turtle.getWidth()*3;
		else if ( OutOfBoundsCheck( 1, ScreenWidth) )
			StartPos[1] = (Turtle.getWidth()*11 + BufferSpace*10 + ScreenWidth)/4;
		else if ( OutOfBoundsCheck( 2, ScreenWidth)  )
			StartPos[2] = (Turtle.getWidth()*14 + ScreenWidth*2 + BufferSpace*12)/4 ;
		else if ( OutOfBoundsCheck( 3, ScreenWidth)  )
			StartPos[3] = (Turtle.getWidth()*17 + BufferSpace*14 + ScreenWidth*3)/4;
	
	}
	
	// Checks if the turtles are out of bounds
	private boolean OutOfBoundsCheck( int TurtleGroup, int ScreenWidth )
	{
		if ( TurtleGroup == 0 && XPos[0].getHeight() <= 0 )
			return true;
		else if ( TurtleGroup == 1 && XPos[3].getHeight() <= 0)
			return true;
		else if ( TurtleGroup == 2 && XPos[6].getHeight() <= 0 )
			return true;
		else if ( TurtleGroup == 3 && XPos[9].getHeight() <= 0 )
			return true;
		else
			return false;
	}
	
	// Checks if the turtles collide with the frog
	public boolean TurtleCollision( double FrogX1, double FrogX2 )
	{
		for ( int i = 0; i < 12; i+=3 )
		{
			if ( FrogX2 >= XPos[i].getHeight() && FrogX1 <= XPos[i].getHeight() )
			{
				if ( XPos[i].getHeight() - FrogX1 >= (FrogX2 - FrogX1)/2 )
					return true;
			}
			else if ( FrogX1 <= XPos[i+2].getWidth() && FrogX2 >= XPos[i+2].getWidth() )
			{
				if ( FrogX2 - XPos[i+2].getWidth() >= (FrogX2-FrogX1)/2 )
					return true;
			}
			else if ( FrogX1 >= XPos[i+2].getWidth() && FrogX2 <= XPos[i].getHeight() )
				return true;
		}
		
		return false;
	}
	
	// Returns the BufferedImage
	public BufferedImage TurtleImg()
	{
		return Turtle;
	}
	
	// Returns the width of the image
	public int ImgWidthT()
	{
		return Turtle.getWidth();
	}
	
	// Returns the speed of the turtles
	public double SpeedNum()
	{
		return Speed;
	}
	
	// Returns the left side of the turtle
	public double TurtleWidth( int Turt)
	{
		return XPos[Turt].getWidth();
	}
	
	// Multiplies the speed by 1.25
	public void AddSpeed()
	{
		Speed = Speed*1.50;
	}
}
