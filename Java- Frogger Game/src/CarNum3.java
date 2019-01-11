// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Holds info about Car3, the Orange one or Middle one
public class CarNum3
{
	private BufferedImage Car3;
	private Dimension[] XPos;
	private double[] StartPos;
	private double Speed;
	
	public CarNum3 ( String imgName ) throws IOException
	{
		Car3 = ImageIO.read(getClass().getResource(imgName));
		
		XPos = new Dimension[3];
		
		XPos[0] = new Dimension(0,0);
		XPos[1] = new Dimension(0,0);
		XPos[2] = new Dimension(0,0);
		
		StartPos = new double[3];
		StartPos[0] = Car3.getWidth();
		StartPos[1] = Car3.getWidth();
		StartPos[2] = Car3.getWidth();
		
		Speed = -2.5; // A negative speed means it moves left
	}
	
	// Sets the new positions of the cars
	public void SetPosition(int ScreenWidth)
	{
		// Checks if the cars are off of the screen and resets 
		// Their Positions
		if ( OutOfBoundsCheck(XPos[0]) )
			StartPos[0] = ScreenWidth*2/3 + Car3.getWidth();
		if ( OutOfBoundsCheck(XPos[1]) )
			StartPos[1] = ScreenWidth/3 + Car3.getWidth()*2/3;
		if ( OutOfBoundsCheck(XPos[2]) )
			StartPos[2] = Car3.getWidth()/3;
		
		for ( int i = 0; i < 3; i++ )
		{
			XPos[i].setSize(StartPos[i] + (ScreenWidth*(i+1) + Car3.getWidth()*i)/3, StartPos[i] + Car3.getWidth() + (ScreenWidth*(i+1) + Car3.getWidth()*i)/3);
			StartPos[i] += Speed;
		}
	}
	
	// Checks if a car is out of bounds
	private boolean OutOfBoundsCheck(Dimension Car)
	{
		if ( Car.getHeight() < Speed )
			return true;
		else
			return false;
	}
	
	// Checks if the frog collides with the cars
	public boolean CheckCar3Death(double FrogX1, double FrogX2)
	{
		for ( int i = 0; i < 3; i++ )
		{
			if ( (int)FrogX1 <= XPos[i].getHeight() && (int)FrogX2 >= XPos[i].getWidth() )
				return true;
		}
		
		return false;
	}
	
	// Multiplies the speed by 1.25
	public void AddSpeed()
	{
		Speed = Speed*1.50;
	}

	// Returns the left side of the car
	public double CarWidth(int Car)
	{
		return XPos[Car].getWidth();
	}
	
	// Returns the BufferedImage
	public BufferedImage Img()
	{
		return Car3;
	}
	
	// Returns the width of the car
	public int ImgWidth()
	{
		return Car3.getWidth();
	}
	
}
