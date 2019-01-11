// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// Holds everything about Car2, the very long one
public class CarNum2 
{
	private BufferedImage Car2;
	private Dimension XPos;
	private int StartPos;
	private double Speed;
	
	public CarNum2 ( String imgName ) throws IOException
	{
		Car2 = ImageIO.read(getClass().getResource(imgName));
		XPos = new Dimension (0,0);
		StartPos = 0;
		Speed = 2;
	}
	// Sets the new position of the car
	public void SetPosition(int ScreenWidth)
	{
		// If it is out of bounds reset it to right off of the 
		// screen
		if ( OutOfBoundsCheck(ScreenWidth) )
			StartPos = -Car2.getWidth()*5;
		
		XPos.setSize(StartPos, StartPos + Car2.getWidth()*5);
		
		StartPos += Speed;
			
	}
	
	// Checks if the car is off of the screen
	private boolean OutOfBoundsCheck(int ScreenWidth)
	{
		if ( XPos.getWidth() >= ScreenWidth + Speed )
			return true;
		else
			return false;
	}
	
	// Checks if the car collided with the frog
	public boolean CheckCar2Death(double FrogX1, double FrogX2 )
	{
		if ( (int)FrogX1 <= (int)XPos.getHeight() && (int)FrogX2 >= (int)XPos.getWidth() )
			return true;
		else
			return false;
	}
	
	// Multiplies the speed by 1.25
	public void AddSpeed()
	{
		Speed = Speed*1.50;
	}
	
	// Returns the left side of the car
	public double CarWidth()
	{
		return XPos.getWidth();
	}
	
	// Returns the BufferedImage
	public BufferedImage Img()
	{
		return Car2;
	}
	
	// Returns the width of the image
	public int ImgWidth()
	{
		return Car2.getWidth();
	}

}

