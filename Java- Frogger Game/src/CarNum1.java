// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Holds everything about Car1, the Red one
public class CarNum1 
{
	private BufferedImage Car1;
	private Dimension[] XPos; 
	private static double[] StartPos;
	private double Speed;
	
	public CarNum1(String imgName) throws IOException
	{
		Car1 = ImageIO.read(getClass().getResource(imgName));
		
		// Will hold the starting and ending position of the car
		// Images at all times
		// Also the number of Dimensions are how many cars
		// There are
		XPos = new Dimension[2];
		XPos[0] = new Dimension(0,0);
		XPos[1] = new Dimension(0,0);
		
		// Holds a position that allows the speed to work as a 
		// double
		StartPos = new double[2];
		StartPos[0] = -Car1.getWidth();
		StartPos[1] = -Car1.getWidth();
		
		// How fast the car will go
		Speed = 2.3;
	}
	
	public void SetPosition(int ScreenWidth)
	{
		// If a car is out of bounds, it will set the car at the 
		// edge of the screen 
		if ( OutOfBoundsCheck( XPos[0], ScreenWidth ) )
			{
				StartPos[0] = -Car1.getWidth();
			}
		if ( OutOfBoundsCheck( XPos[1], ScreenWidth ) )
			{
				StartPos[1] = -Car1.getWidth() - (ScreenWidth + Car1.getWidth())/2;
			}
	
		// Sets the new position of the cars
		for ( int i = 0; i < 2; i++ )
		{
			XPos[i].setSize(StartPos[i] + (ScreenWidth + Car1.getWidth())*i/2, StartPos[i] + (ScreenWidth + Car1.getWidth())*i/2 + Car1.getWidth());
			StartPos[i] += Speed;
		}
	}
	
	// Checks if a car is out of bounds
	private boolean OutOfBoundsCheck(Dimension Car, int ScreenWidth)
	{
		if ( Car.getWidth() >= ScreenWidth )
			return true;
		else 
			return false;
	}

	// Checks if the frog has collided with the car
	public boolean CheckCar1Death(double FrogX1, double FrogX2)
	{
		for ( int i = 0; i < 2; i++ )
		{
			if ( (int)XPos[i].getWidth() <= (int)FrogX2 && (int)XPos[i].getHeight() >= (int)FrogX1 )
				return true;
		}
		
		return false;
	}
	
	// Multiplies the Speed by a factor of 1.25
	public void AddSpeed()
	{
		Speed = Speed*1.50;
	}
	
	// Returns the left end of the car
	public double CarWidth( int Car )
	{
		return XPos[Car].getWidth();
	}
	
	// Returns the BufferedImage
	public BufferedImage Img()
	{
		return Car1;
	}
	
	// Returns the width of the car image
	public int ImgWidth()
	{
		return Car1.getWidth();
	}
	

}


