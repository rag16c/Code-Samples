// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Holds info about Car5, the Brown one, or the Bottom car
public class CarNum5
{
	private BufferedImage Car5;
	private Dimension[] XPos;
	private double[] StartPos;
	private double Speed;
	
	public CarNum5 ( String imgName ) throws IOException
	{
		Car5 = ImageIO.read(getClass().getResource(imgName));
		
		XPos = new Dimension[5];
		StartPos = new double[5];
		
		for ( int i = 0; i < 5; i++ )
		{
			XPos[i] = new Dimension(0,0);
			StartPos[i] = 0;
		}
		
		Speed = 1.43;
	}
	
	// Sets the position of the cars
	public void SetPosition( int ScreenWidth )
	{
		// Checks if the cars are out of bounds
		for ( int i = 0; i < 5; i++ ) 
		{
			if ( OutOfBoundsCheck ( XPos[i], ScreenWidth ) )
				{
					StartPos[i] = -( ScreenWidth*i/5 + Car5.getWidth() );
					break;
				}
		}
			
		for ( int i = 0; i < 5; i++ )  
		{
			XPos[i].setSize(StartPos[i] + ScreenWidth*i/5, StartPos[i] + Car5.getWidth() + ScreenWidth*i/5);
			StartPos[i] += Speed;
		}
		
	
	}
	
	// Checks if a car is out of bounds
	private boolean OutOfBoundsCheck( Dimension Car, int ScreenWidth )
	{
		if ( Car.getWidth() >= ScreenWidth + Speed )
			return true;
		else
			return false;
	}
	
	// Checks if the cars collided with the frog
	public boolean CheckCar5Death(double FrogX1, double FrogX2)
	{
		for ( int i = 0; i < 5; i++ )
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
	
	// Returns the left bound of a car
	public double CarWidth( int Car )
	{
		return XPos[Car].getWidth();
	}
	
	// Returns the BufferedImage
	public BufferedImage Img()
	{
		return Car5;
	}
	
	// Returns the width of the image
	public int ImgWidth()
	{
		return Car5.getWidth();
	}
}
