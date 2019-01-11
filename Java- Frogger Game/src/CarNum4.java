// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Holds info about Car4, the Purple one
public class CarNum4
{
	private BufferedImage Car4;
	private Dimension[] XPos;
	private double[] StartPos;
	private double Speed;
	private int BufferSpace;
	
	public CarNum4 ( String imgName ) throws IOException
	{
		Car4 = ImageIO.read(getClass().getResource(imgName));	
		
		XPos = new Dimension[6];
		
		for ( int i = 0; i < 6; i++ )
			XPos[i] = new Dimension(0,0);
	
		StartPos = new double[2];
		StartPos[0] = -Car4.getWidth();
		StartPos[1] = - Car4.getWidth();
		Speed = 1.89;
		//5.75
		// This is the spacing between the cars that are close 
		// to each other
		BufferSpace = 75;
		
	}	
	
	// Sets the position of the cars
	public void SetPosition( int ScreenWidth )
	{
		// If the cars are out of bounds, reset their positions
		if ( OutOfBoundsCheck( 0, ScreenWidth) )
			StartPos[0] = -Car4.getWidth();
		else if ( OutOfBoundsCheck( 1, ScreenWidth) )
			StartPos[1] = - (Car4.getWidth()*3 + (ScreenWidth-Car4.getWidth() - BufferSpace)/2 + BufferSpace*2);
		
		for ( int i = 0; i < 3; i++ )
		{
			XPos[i].setSize(StartPos[0] - Car4.getWidth()*i - BufferSpace * i, StartPos[0] - BufferSpace*i - Car4.getWidth()*i + Car4.getWidth());
			XPos[i+3].setSize(StartPos[1] + Car4.getWidth()*(i) + BufferSpace*(i) + (ScreenWidth - Car4.getWidth() - BufferSpace)/2, StartPos[1] + BufferSpace*(i) + Car4.getWidth()*(i+1) + (ScreenWidth - Car4.getWidth() - BufferSpace)/2);
		}
		
		StartPos[0] += Speed;
		StartPos[1] += Speed;
	}
	
	// Checks if a car is out of bounds
	private boolean OutOfBoundsCheck( int CarGroup, int ScreenWidth )
	{
		if ( CarGroup == 0 )
		{
			if ( XPos[2].getWidth() >= ScreenWidth )
				return true;
			else
				return false;
		}
		else
		{
			if ( XPos[3].getWidth() >= ScreenWidth )
				return true;
			else
				return false;
		}
		
	}
	
	// Checks if the frog collided with the cars
	public boolean CheckCar4Death(double FrogX1, double FrogX2)
	{
		for ( int i = 0; i < 6; i++ )
		{
			if ( (int)XPos[i].getWidth() <= (int)FrogX2 && (int)XPos[i].getHeight() >= (int)FrogX1 ) 
				return true;
		}
		
		return false;
	}
	
	// Multiplies the speed by 1.25
	public void AddSpeed()
	{
		Speed = Speed*1.50;
	}
	
	// Returns the left bound of the car
	public double CarWidth(int Car)
	{
		return XPos[Car].getWidth();
	}	
	
	// Returns the BufferedImage
	public BufferedImage Img()
	{
		return Car4;
	}
	
	// Returns the width of the image
	public int ImgWidth()
	{
		return Car4.getWidth();
	}
}
