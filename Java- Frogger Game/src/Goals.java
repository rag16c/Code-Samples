// Creator: Riley Garrison

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// This holds stats about the goals and well as 
// The properties about the goals
public class Goals
{
	private Dimension[] XPos;
	private Dimension[] GoalX;
	private boolean[] GoalsAchieved;
	private int GoalWidth;
	private BufferedImage Goal;
	private BufferedImage GoalFilled;
	private Bug BugExtra;
	private int GoalNumber; 

	public Goals(String GoalImg, String GoalFilledImg) throws IOException
	{
		Goal = ImageIO.read(getClass().getResource(GoalImg));
		GoalFilled = ImageIO.read(getClass().getResource(GoalFilledImg));
		
		XPos = new Dimension[5];
		// Holds the positions of the goals in respect to
		// How far apart they are
		
		GoalX = new Dimension[5];
		// Holds positions of the goals in respect to
		// their left and right bound
		
		GoalsAchieved = new boolean[5];
		// Stores whether the goal has been gotten by the frog 
		
		for ( int i = 0; i < 5; i++ )
		{
			XPos[i] = new Dimension(0,0);
			GoalX[i] = new Dimension(0,0);
			GoalsAchieved[i] = false;
		}

		GoalWidth = 55;
		// The size of the goal
		
		GoalNumber = 0;
		// Just a place holder for which goal the bug should have
		
		BugExtra = new Bug();
	}
	
	// Sets the position of where the goals should be
	public void SetPosition( int ScreenWidth )
	{
		for ( int i = 0; i < 5; i++ )
		{
			XPos[i].setSize(50 + 80*i + (ScreenWidth - 500)*i/4, 50 + 80*(i+1) + (ScreenWidth - 500)*i/4);
			GoalX[i].setSize(XPos[i].getWidth() + (Goal.getWidth() - GoalWidth)/2, XPos[i].getWidth() + (Goal.getWidth() + GoalWidth)/2 );
		}
		
	}

	// Returns the specified buffered image based on whether
	// You have gotten the goal or not
	// or whether the bug is activated and should appear
	public BufferedImage GoalNeeded(int i )
	{
		 
		if ( BugExtra.GetGoalNum()== GoalNumber && BugExtra.Activated() )
		{
			GoalNumber++;
			ResetGoalNumber(i);
			return BugExtra.getBugImg();
		}
		else if ( GoalsAchieved[i] == false )
		{	
			GoalNumber++;
			ResetGoalNumber(i);
			return Goal;
		}
		else
		{
			ResetGoalNumber(i);
			return GoalFilled;
		}
	
	}
	
	// Resets the placeholder int called GoalNumber
	public void ResetGoalNumber(int i)
	{
		if ( i == 4 )
			GoalNumber = 0;
	}
	
	// Used in paintComponent for where to print the goal
	public double GoalWidth( int i )
	{
		return XPos[i].getWidth();
	}
	
	// Used with the collision Check of the frog and the Goals
	public double GoalBound ( int i )
	{
		return GoalX[i].getWidth();
	}
	
	// Checks whether the frog has collided with the goal
	public boolean GoalCheck ( double FrogX1, double FrogX2 )
	{
		for ( int i = 0; i < 5; i++ )
		{
			// If the frog intersects with the higher part of the goal
			// but not the lower part
			if ( FrogX2 >= GoalX[i].getHeight() && FrogX1 <= GoalX[i].getHeight() )
			{
				// If at least half the frog + 2 pixels intersects 
				// With the goal
				if ( GoalX[i].getHeight() - FrogX1 + 2 >= (FrogX2 - FrogX1)/2 )
				{
					// If the Goal has not already been gotten
					if ( GoalsAchieved[i] == false )
						{
							GoalsAchieved[i] = true;
							BugExtra.StopTime();
							return true;
						}
					else 
						return false;
				}
			}
			// If the Frog intersects with only the bottom of the goal and not its top
			else if ( FrogX1 <= GoalX[i].getWidth() && FrogX2 >= GoalX[i].getWidth() )
			{
				if ( FrogX2 - GoalX[i].getWidth() + 2 >= (FrogX2 - FrogX1)/2 )
				{
					if ( GoalsAchieved[i] == false )
					{
						GoalsAchieved[i] = true;
						BugExtra.StopTime();
						return true;
					}
					else 
						return false;
				}
			}
			// If the frog is completely in the goal
			else if ( FrogX1 >= GoalX[i].getWidth() && FrogX2 <= GoalX[i].getHeight() )
			{
				if ( GoalsAchieved[i] == false )
				{
					GoalsAchieved[i] = true;
					BugExtra.StopTime();
					return true;
				}
				else 
					return false;
			}
			
			// If the Frog position is less than the goal stop checking 
			if ( FrogX2 < GoalX[i].getWidth() )
				break;
		}
		
		return false;
	}
	
	// Resets goals to not gotten
	public void ResetGoals()
	{
		for ( int i = 0; i < 5; i++ )
			GoalsAchieved[i] = false;
	}
	
	// If all goals have been achieved return true
	public boolean AllGoals()
	{
		for ( int i = 0; i < 5; i++ )
		{
			if ( GoalsAchieved[i] == false )
				return false;
		}
		
		return true;
	}
	
	// Gets the number of goals not gotten yet and 
	// Passes it to the Bugs reset time
	public void OnDeathorGoal()
	{
		int j = 0;
		for ( int i = 0; i < 5; i++ )
		{
			if ( GoalsAchieved[i] == false )
				j++;
		}
		
		GoalNumber = 0;
		BugExtra.ResetTime(j);
		
	}
	
	// Returns whether the Bug is activated or not
	public boolean IsExtraGoal()
	{
		return BugExtra.Activated();
	}
}
