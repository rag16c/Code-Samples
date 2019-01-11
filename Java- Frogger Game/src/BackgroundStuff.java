// Creator: Riley Garrison

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// Holds the Background images for the frogger game
// Doesnt include the goals or the objects that can move
public class BackgroundStuff 
{
	private BufferedImage Dirt;
	private BufferedImage Grass;
	private BufferedImage Road;
	private BufferedImage Sea;
	private BufferedImage End;
	
	public BackgroundStuff(String DirtImg, String RoadImg, String GrassImg, String SeaImg, String EndImg) throws IOException
	{
		Dirt   	   = ImageIO.read(getClass().getResource(DirtImg));
		Grass 	   = ImageIO.read(getClass().getResource(GrassImg));
		Road	   = ImageIO.read(getClass().getResource(RoadImg));
		Sea 	   = ImageIO.read(getClass().getResource(SeaImg));
		End        = ImageIO.read(getClass().getResource(EndImg));
	}
	
	// Returns the BufferedImage that is requested
	public BufferedImage ImgNeeded( int i )
	{
		if ( i == 13 )
			return Dirt;
		else if ( i > 7 )
			return Road;
		else if ( i == 7 )
			return Grass;
		else if ( i > 1 )
			return Sea;
		else
			return End;
	}
	
	
}
