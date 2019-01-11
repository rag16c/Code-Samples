// Creator: Riley Garrison

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

// This is the 2nd main class, where it user most of the other classes
// to actually run the game. Hence why this is around 700 lines of code
public class Frogger extends JFrame
{	
	private JMenuBar Menu;     // The MenuBar
	private JPanel MasterPanel;// Displays main game
	private JPanel DeathPanel; // Displays on death
	private JPanel WinPanel;   // Displays on getting all goals
	private JFrame ScoreFrame; // Displays high scores
	private PlayerStats Player; // Variable for PlayerStats
	private ChFrog Frog;  // Frog Stats
	private CarNum1 Car1; // Car1 Stats
	private CarNum2 Car2; // Car2 Stats
	private CarNum3 Car3; // Car3 Stats
	private CarNum4 Car4; // Car4 Stats
	private CarNum5 Car5; // Car5 Stats
	private Log[] Logs;   // Log Stats
	private Turtle[] Turtles; // Turtle Stats
	private BackgroundStuff BackGround;  // Holds BackGround images
	private Goals GoalProps;             // Goal Stats

	public Frogger() 
	{
		
		super("Game");

		try {
			// Creates most of the variables defined above
			Frog = new ChFrog("Froggy.png");
			Car1 = new CarNum1("Car1.png");
			Car2 = new CarNum2("Car2.png");
			Car3 = new CarNum3("Car3.png");
			Car4 = new CarNum4("Car4.png");
			Car5 = new CarNum5("Car5.png");
			Logs = new Log[3];
			Logs[0] = new Log("Log.png", 4, 4.5, 1.23);
			Logs[1] = new Log("Log.png", 3, 6.5, 1.41);
			Logs[2] = new Log("Log.png", 4, 3, 1.7);
			Turtles = new Turtle[2];
			Turtles[0] = new Turtle("Turtle.png", "TurtleDown.png", -1.76, 4);
			Turtles[1] = new Turtle("Turtle.png", "TurtleDown.png", -1.11, 4);
			BackGround = new BackgroundStuff("Dirt.jpg", "Road.jpg", "Grass.jpg", "Sea.jpg", "End.png");
			GoalProps = new Goals("Goal.png", "GoalFilled.png");
		}
		catch( IOException e )
		{
			System.err.println("Image error");
			System.exit(0);
		}
	
	Player = new PlayerStats();
		
	PaintHere();
	// Creates the Master Panel through the function and paints
	
	KeyAdapter KL = new KeyAdapter() {
		
		int ResetKey = 0;
		// This listens for WASD input for movement 
		public void keyPressed(KeyEvent Event) 
		{
			// Doesn't allow for continuous input by holding dont a key
			if ( ResetKey == 0 || ResetKey == 1 )
			{
			   if ( Event.getKeyCode() == KeyEvent.VK_LEFT )
			   {
				   // Move Left
				   Frog.SetDirection(1);
				   MasterPanel.repaint(5);
      			}
			   else if ( Event.getKeyCode() == KeyEvent.VK_UP )
			   {
				   // Move Up
				   if ( Frog.SetYPosition(-1) )
					   Player.ChangeScore(10 + Frog.getYPositon());
				   MasterPanel.repaint(5);
			   }
			   else if ( Event.getKeyCode() == KeyEvent.VK_RIGHT )
			   {
				   // Move Right
				   Frog.SetDirection(-1);
				   MasterPanel.repaint(5);
			   }
			   else if ( Event.getKeyCode() == KeyEvent.VK_DOWN )
			   {
				   // Move Down
				   Frog.SetYPosition(1);
				   MasterPanel.repaint(5);
			   }
			
			   ResetKey++;
			}
		}
		public void keyReleased(KeyEvent Event)
		{
			// Resets ResetKey so that you can keep moving 
			int key = Event.getKeyCode();
			
			if ( key == KeyEvent.VK_LEFT || key == KeyEvent.VK_UP || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_DOWN )
				ResetKey = 0;
		}
	};

	MasterPanel.setFocusable(true);
	MasterPanel.addKeyListener(KL);	
	add(MasterPanel);	

	Player.StartTime();
	
	CreateMenu();
	setJMenuBar(Menu);
	// Creates the MenuBar
	
	WindowAdapter Focuser = new WindowAdapter() {
	
		public void windowActivated( WindowEvent e )
		{
			// When the Window is activated the time starts
			Player.StartTime();
		}
		
		public void windowDeactivated( WindowEvent e )
		{
			// When the window is deactivated, the time stops
			Player.StopTime();
		}
	};
	
	addWindowListener(Focuser);
	
	ActionListener Time = new ActionListener() {
	
		// ActionListener for the Timer that repaints every 10 milliseconds
		public void actionPerformed(ActionEvent e )
		{
			MasterPanel.repaint();
		}
	};
	
	Timer move = new Timer(10, Time);
	move.start();
	move.setRepeats(true);
	// Creates a timer that fires every 10 milliseconds
			
	}

	// This Methods creates the JPanel which holds the main painting function
	private void PaintHere()
	{
			
		MasterPanel = new JPanel() {
			
			long PreviousScore = 0;
			// Used to add new lives
			
			int LengthHolder = 0;
			// A temp int variable
			
			float ZoneY = 0;
			//  Keeps The height of Zones
			
			public void paintComponent(Graphics g)
			{
				ZoneY = getHeight()/15;
				
				for ( int i = 0; i < 15; i++ )
				{
					LengthHolder = 0;
		
					while ( LengthHolder + 80 <= getWidth() && i < 13 )
					{
						// Draws the background images
						g.drawImage(BackGround.ImgNeeded(i+1), LengthHolder, (int)ZoneY*(i+1), 80, (int)ZoneY, null);
						LengthHolder += 80;
					}
						
					// Keeps drawing the Images if it doesnt reach the end of the screen
					if ( getWidth() - LengthHolder > 0 && i < 13 )
						g.drawImage(BackGround.ImgNeeded(i+1), LengthHolder, (int)ZoneY*(i+1), getWidth()-LengthHolder, (int)ZoneY, null);
		
					if ( i == 0 )
					{
						// Used to Draw the Player Lives and the Player Score
						g.setColor(new Color(255,192,203));
						g.fillRect(0,0,getWidth(),(int)ZoneY*1);
						
						g.setColor(Color.BLACK);
						g.setFont(new Font(null, Font.PLAIN, 28));
						g.drawString("Lives: " + Player.CheckLives() , 10, (int)(ZoneY*i + ZoneY/4 + ZoneY*2/3) - 10  );
						g.drawString("Score: " + Player.GetScore(), getWidth()/2 + 10, (int)(ZoneY*i + ZoneY/4 + ZoneY*2/3) - 10 ); 
					}
					else if ( i == 1 )
					{
						// Used to Draw the Goals
						GoalProps.SetPosition( getWidth() );
								
						for ( int j = 0; j < 5; j++ )
							g.drawImage(GoalProps.GoalNeeded(j), (int)GoalProps.GoalWidth(j), (int)ZoneY*(i), 80, (int)ZoneY, null);
					
					}
					else if ( i == 2 )
					{
						// Used to draw the Logs on the top
						Logs[0].SetPosition( getWidth() );
						
						for ( int j = 0; j < Logs[0].NumLogs(); j++ )
							g.drawImage(Logs[0].Img(), (int)Logs[0].LogWidth(j), (int)ZoneY*i, (int)Logs[0].ImgWidth(), (int)ZoneY, null);
					}
					else if ( i == 3 )  
					{
						// Used to draw the top row of Turtles
						Turtles[0].SetPosition( getWidth() );
					
						for ( int j = 0; j < 12; j++ )
							g.drawImage(Turtles[0].TurtleImg(), (int)Turtles[0].TurtleWidth(j), (int)ZoneY*i, (int)Turtles[0].ImgWidthT(), (int)ZoneY, null);
					
					}
					else if ( i == 4 )
					{
						// Used to draw the middle row of logs
						Logs[1].SetPosition( getWidth() );
						
						for ( int j = 0; j < Logs[1].NumLogs(); j++ )
							g.drawImage(Logs[1].Img(), (int)Logs[1].LogWidth(j), (int)ZoneY*i, (int)Logs[1].ImgWidth(), (int)ZoneY, null);
					
						if ( Logs[1].ExtraFrogActivated() )
							{
								// Draws the lady frog if it is activated
								Logs[1].ExtraFrogMove(getWidth());
								g.drawImage(Logs[1].LadyFrogImg(), (int)Logs[1].LadyX1(), (int)ZoneY*i, (int)Logs[1].LadyFrogImg().getWidth(), (int)ZoneY, null);
							}
					}
					else if ( i == 5 )  
					{
						// Draws the bottom row of turtles
						Turtles[1].SetPosition( getWidth() );
						
						for ( int j = 0; j < 12; j++ )
							g.drawImage(Turtles[1].TurtleImg(), (int)Turtles[1].TurtleWidth(j), (int)ZoneY*i, (int)Turtles[1].ImgWidthT(), (int)ZoneY, null);
					}
					else if ( i == 6 )
					{
						// Draws the bottom row of logs
						Logs[2].SetPosition( getWidth() );
					
						for ( int j = 0; j < Logs[2].NumLogs(); j++ )
							g.drawImage(Logs[2].Img(), (int)Logs[2].LogWidth(j), (int)ZoneY*i, (int)Logs[2].ImgWidth(), (int)ZoneY, null);
					}
					else if ( i == 8 )
					{
						// Draws the top row of cars
						Car1.SetPosition(getWidth());
						
							g.drawImage(Car1.Img(),(int)Car1.CarWidth(0),(int)ZoneY*i, Car1.ImgWidth(), (int)ZoneY, null );
							g.drawImage(Car1.Img(),(int)Car1.CarWidth(1),(int)ZoneY*i, Car1.ImgWidth(), (int)ZoneY, null );
								
					}
					else if ( i == 9 )
					{
						// Draws the row of cars under the top row of cars
						Car2.SetPosition(getWidth());
						
						g.drawImage(Car2.Img(),(int)Car2.CarWidth(),(int)ZoneY*i, Car2.ImgWidth()*5, (int)ZoneY, null );
								
					}
					else if ( i == 10 )
					{
						// Draw the middle row of cars
						Car3.SetPosition(getWidth());
							
							g.drawImage(Car3.Img(),(int)Car3.CarWidth(0),(int)ZoneY*i, Car3.ImgWidth(), (int)ZoneY, null );
							g.drawImage(Car3.Img(),(int)Car3.CarWidth(1), (int)ZoneY*i, Car3.ImgWidth(), (int)ZoneY, null);
							g.drawImage(Car3.Img(),(int)Car3.CarWidth(2),(int)ZoneY*i, Car3.ImgWidth(), (int)ZoneY, null);
							
					}
					else if ( i == 11 )
					{
						// Draw the row of cars under the middle row of cars
						Car4.SetPosition(getWidth());
						
						for ( int j = 0; j < 6; j++ )
							g.drawImage(Car4.Img(),(int)Car4.CarWidth(j),(int)ZoneY*i, Car4.ImgWidth(), (int)ZoneY, null );
	
					}
					else if ( i == 12 )
					{
						// Draws the bottom row of cars
						Car5.SetPosition(getWidth());
								
						for ( int j = 0; j < 5; j++ )
						{
							g.drawImage(Car5.Img(), (int)Car5.CarWidth(j), (int)ZoneY*i, Car5.ImgWidth(), (int)ZoneY, null );
						}
																
					}
					else if ( i == 14 )
					{
						// Draws the entire Bottom Zone:
						// Green Time bar as well as the word Time
						g.setColor(new Color(255,192,203));
						g.fillRect(0, (int)ZoneY*i, getWidth(), (int)ZoneY+ 10);
					
						g.setColor(Color.GREEN);
						g.fillRect(10 + ( 60 - Player.GetTime())*(getWidth() - 200)/60, (int)(ZoneY*i + ZoneY/4), 10 + (getWidth() - 200) - ( 60 - Player.GetTime())*(getWidth() - 200)/60, (int)(ZoneY*2/3));
						
						g.setColor(Color.BLACK);
						g.setFont(new Font(null, Font.PLAIN, 28));
						g.drawString("Time", getWidth() - 150 , (int)(ZoneY*i + ZoneY/4 + ZoneY*2/3) - 10 );
						
					}
					
					if ( Frog.getYPositon() == i )  
					{
						// Draws the Frog 
						Frog.SetPosition(getWidth());
						
						// If the frog is touching the Lady Frog, it starts guiding it and is replaced by the lady image
						// else it uses the regular frog image
						if ( Frog.isTouchingLady() == false )
							g.drawImage(Frog.Img(),(int)Frog.GetX1(),(int)ZoneY*i, (int)Frog.ImgWidth(), (int)ZoneY, null );
						else 
							g.drawImage(Logs[1].LadyFrogImg(),(int)Frog.GetX1(),(int)ZoneY*i, (int)Frog.ImgWidth(), (int)ZoneY, null );
						
						if ( Player.CollisionCheck() == 1 )
							{
							// If the collision is on, check if the frog collides with ANYTHING
							// else the frog interacts with nothing
							
							
							// Then checks if the frog won or died
								if ( Frog.getYPositon() == 1 )
									CheckWin();	
								else 
									CheckDeath(i);
							}								
					}
					
					// If Player score exceeds 10000 or 20000, you get an extra life
					if ( Player.GetScore() >= 10000 && PreviousScore < 10000 )
						Player.GainLives();
					else if ( Player.GetScore() >= 20000 && PreviousScore < 20000 )
						Player.GainLives();
					
					PreviousScore = Player.GetScore();
				}
			}
		};	
	}	
	
	// Checks to see if you got a goal
	// If you didnt you die
	private void CheckWin()
	{
		if ( GoalProps.GoalCheck( Frog.GetX1(), Frog.GetX2() )  == true )
			{
				// If you got a goal
				Player.StopTime();
				Frog.ResetPosition();
				
				Player.ChangeScore(10 + Frog.getYPositon());
				// Add 10 points
				
				Player.ChangeScore(1);
				// add 50 points
				
				if ( GoalProps.IsExtraGoal() == true )
					Player.ChangeScore(2);
				// If you got an extra point from the bug, add 200 
				
				if ( Frog.isTouchingLady() )
					Player.ChangeScore(2);
				// If you got extra points from the lady frog add 200
				
				// If you got all the goals, run LevelRun else reset some stuff 
				if ( GoalProps.AllGoals() )
					LevelWon();
				else
				{
					GoalProps.OnDeathorGoal();
					Logs[1].ResetLady();
					Player.ResetTime();
					Frog.ResetLadyTouch();
					Player.resetTraversed();
				}
			}
		else
			Death();
				
	
	
	}
	
	// Main Checker for if you died
	private void CheckDeath(int YPos)
	{
		if ( YPos < 8 )
			{
			// Checks to see if you died by the water
			if (CheckWaterDeath(YPos))
				{
				GoalProps.OnDeathorGoal();
				Logs[1].ResetLady();
				Death();
				}
			}
		else 
			{
			// Checks to see if you got hit by a car or if time ran out
			if ( CheckCarDeath(YPos) == true )
				{
				GoalProps.OnDeathorGoal();
				Logs[1].ResetLady();
				Death();
				}
			else if ( Player.GetTime() <= 0 )
				{
				GoalProps.OnDeathorGoal();
				Logs[1].ResetLady();
				Death();
				}
			}
	}
	
	// Checks if you collided with a log or turtles
	// If you didnt, you die
	// If you did, Set Frog Increment to the speed of the moving object
	private boolean CheckWaterDeath(int YPos)
	{
		if ( YPos == 2 )
		{
			if ( Logs[0].LogCollision( Frog.GetX1(), Frog.GetX2()) )
			{
				Frog.SetIncrement(Logs[0].SpeedNum());
				return false;
			}
		}
		else if ( YPos == 3 )
		{
			if ( Turtles[0].TurtleCollision( Frog.GetX1(), Frog.GetX2() ) )
			{
				Frog.SetIncrement(Turtles[0].SpeedNum());
				return false;
			}
		}
			else if ( YPos == 4 )
		{
			if ( Logs[1].LogCollision( Frog.GetX1(), Frog.GetX2()) )
			{
				Frog.SetIncrement(Logs[1].SpeedNum());
				
				if ( Logs[1].LadyCollision( Frog.GetX1(), Frog.GetX2()) )
					{
						// If you collide with the lady frog, set Frogger to guide her
						Frog.LadyTouch();	
						Logs[1].ExtraFrogDeactivate();
					}
				return false;
			}
		}
		else if ( YPos == 5 )
		{
			if ( Turtles[1].TurtleCollision( Frog.GetX1(), Frog.GetX2() ) )
			{
				Frog.SetIncrement(Turtles[1].SpeedNum());
				return false;
			}
		}
	    else if ( YPos == 6 )
		{
			if ( Logs[2].LogCollision( Frog.GetX1(), Frog.GetX2()) )
			{
				Frog.SetIncrement(Logs[2].SpeedNum());
				return false;
			}
		}
	    else if ( YPos == 7 )
	    	return false;
		
		// YPos == 7 is the spot between the cars and the water
		
		return true;
	}
	
	// This checks if you got hit by a car, if you did it returns true
	private boolean CheckCarDeath(int YPos)
	{
		if ( YPos == 8 )
			return Car1.CheckCar1Death(Frog.GetX1(), Frog.GetX2());	
		else if ( YPos == 9 )
			return Car2.CheckCar2Death(Frog.GetX1(), Frog.GetX2());
		else if ( YPos == 10 )
			return Car3.CheckCar3Death(Frog.GetX1(), Frog.GetX2());
		else if ( YPos == 11 )
			return Car4.CheckCar4Death(Frog.GetX1(), Frog.GetX2());
		else if ( YPos == 12 )
			return Car5.CheckCar5Death(Frog.GetX1(), Frog.GetX2());
		else
			return false;
	
	}
	
	// If you died this function runs
	private void Death()
	{	
		Player.LoseLives();
		Player.StopTime();
		// You lose a life and time stops
		
		DeathPanel = new JPanel() {
		// Creates a new JPanel that replaces the MasterPanel for a bit
		// Says that you died and that you have X Lives left, or Says that you died 
		// And the game is over
			public void paintComponent(Graphics g)
			{
				// Covers the panel with an orange background and has black text
				g.setColor(Color.ORANGE);
				g.fillRect(0,0,getWidth(),getHeight());
				
				g.setColor(Color.BLACK);
				g.setFont(new Font(null,Font.PLAIN, 50));
				
				if ( Player.CheckLives() > 0 )
				{
					// If you have lives this runs
					g.drawString("You have " + Player.CheckLives() + " lives left", getWidth()/2 - 200, getHeight()/2);
				}
				else
				{
					// If you have no lives this runs
					g.drawString("You have died", getWidth()/2 - 130, getHeight()/2);
				}
			}
		};
		
		setVisible(false);
		add(DeathPanel);
		remove(MasterPanel);
		setVisible(true);
		// DeathPanel replaces MasterPanel
		
		ActionListener Resume = new ActionListener() {
		// After 3 seconds this ActionLister gets an event	
			public void actionPerformed(ActionEvent e )
			{
				if ( Player.CheckLives() > 0 )
				{
					// If you have lives, MasterPanel replaces DeathPanel
					// Frogs position and the time is reset
					setVisible(false);
					remove(DeathPanel);
					add(MasterPanel);
					
					Frog.ResetPosition();
					setVisible(true);
					
					Player.ResetTime();
					MasterPanel.requestFocus();
				}
				else 
				{
					// If you have no lives, checks to see if you made a high score and then quits
					Player.UpdateHighScores();
					System.exit(0);
				}
			}
		};
		
		Timer wait = new Timer(3000, Resume );
		wait.start();	
		wait.setRepeats(false);
	}
	
	// This is ran if you got all of the goals
	private void LevelWon()
	{
		// The time is stopped and a new panel is created
		Player.StopTime();
		
		WinPanel = new JPanel() {
			
			public void paintComponent(Graphics g)
			{
			// Covers the background in orange and has black text
				g.setColor(Color.ORANGE);
				g.fillRect(0,0,getWidth(),getHeight());
				
				g.setColor(Color.BLACK);
				g.setFont(new Font(null,Font.PLAIN, 35));
				
				g.drawString("Congratulations! Now Lets Speed Things Up", getWidth()/2 - 350, getHeight()/2);
				
			}
		};
		
		setVisible(false);
		add(WinPanel);
		remove(MasterPanel);
		setVisible(true);
		// Replaces MasterPanel with WinPanel
		
		ActionListener Resume = new ActionListener() {
		// After 3 seconds, this actionListener gets an event
			public void actionPerformed(ActionEvent e )
			{
					setVisible(false);
					remove(WinPanel);
					add(MasterPanel);
					// Replaces WinPanel with MasterPanel
					
					Frog.ResetPosition();
					Car1.AddSpeed();
					Car2.AddSpeed();
					Car3.AddSpeed();
					Car4.AddSpeed();
					Car5.AddSpeed();
					Logs[0].AddSpeed();
					Logs[1].AddSpeed();
					Logs[2].AddSpeed();
					Turtles[0].AddSpeed();
					Turtles[1].AddSpeed();
					// Changes the Speed of every object and resets the frogs position
					
					Logs[1].ResetLady();
					GoalProps.ResetGoals();
					GoalProps.OnDeathorGoal();
					Frog.ResetLadyTouch();
					Player.resetTraversed();
					Logs[1].ResetLady();
					// Gets ready the Lady Frog, resets the goals
					// Resets some of the frogs, logs, and player properties
					
					setVisible(true);
					
					Player.ResetTime();
					// Resets the time
					
					MasterPanel.requestFocus();
				
			}
		};
		
		Timer wait = new Timer(3000, Resume );
		wait.start();	
		wait.setRepeats(false);		
	}
	
	// Creates a JMenuBar for *this* 
	private void CreateMenu()
	{
		Menu = new JMenuBar();
		
		JMenu DropDown = new JMenu("Menu");
		JMenu Cheats = new JMenu("Cheats");
		JMenu Scores = new JMenu("Scores");
		// Has 3 DropDowns: DropDown, Cheats and Scores.
		// DropDown contains Cheats and Scores
		
		JCheckBoxMenuItem CollisionSet = new JCheckBoxMenuItem("Collision");
		JCheckBoxMenuItem StopTime = new JCheckBoxMenuItem("Stop Time");
		// Two CheckBoxMenuItems that will go in Cheats
		
		JCheckBoxMenuItem HighScore = new JCheckBoxMenuItem("HighScore");
		HighScore.setSelected(false);
		// HighScore will go in Scores
		
		ActionListener MenuItems = new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
				if ( e.getSource() == CollisionSet )
				{
				// If CollisionSet is selected, Change collision
					if ( CollisionSet.isSelected() == true )
						Player.ChangeCollision();
					else if ( CollisionSet.isSelected() == false )
						Player.ChangeCollision();						
				}
				else if ( e.getSource() == StopTime )
				{
					// If stoptime is selected, either stop or start the time
					if ( StopTime.isSelected() == true )
						Player.StopTime();
					else if ( StopTime.isSelected() == false )
						Player.StartTime();
				}
						
				if ( HighScore.isSelected() && e.getSource() == HighScore )
				{
				// If high score is selected, display the current high scores in a new JPanel
					HighScore.setSelected(false);
					Player.StopTime();
	    			DisplayScores( Player.GetHighScores() );
				}
			}	
		};
		
		CollisionSet.setSelected(true);
		CollisionSet.addActionListener(MenuItems);
		StopTime.setSelected(false);
		StopTime.addActionListener(MenuItems);
		HighScore.addActionListener(MenuItems);
		// Adds the ActionListener to the MenuItems
		
		Scores.add(HighScore);
		Cheats.add(CollisionSet);
		Cheats.add(StopTime);
		DropDown.add(Cheats);
		DropDown.addSeparator();
		DropDown.add(Scores);
		// Adds the items to their corresponding JMenus
		
		Menu.add(DropDown);
	}
	
	// Displays the current high scores to the users
	private void DisplayScores ( String[] Scores )
	{
	// Creates a New JFrame and a New JPanel that will display the scores
		ScoreFrame = new JFrame();
		ScoreFrame.setSize(400, 250 );
		
		JPanel ScorePanel = new JPanel() {
			
			public void paintComponent( Graphics g )
			{
			// Creates a Panel that has a black background
			// and white text
			// Displays the Scores then the owner of that score
				
				g.setColor(Color.BLACK);
				g.fillRect(0,0,getWidth(),getHeight());
				
				g.setColor(Color.WHITE);
				for ( int i = 0; i < 5; i++ )
				{
					g.setFont(new Font(null, Font.BOLD, 20));
					g.drawString(Scores[i], 10, 30 + i*40);
				}
			}
		};
		
		WindowAdapter WA = new WindowAdapter() {
		// Used so that when the window closes, the time starts again
			public void windowClosing(WindowEvent e) 
			{
				Player.StartTime();
			}
		};
		
		ScoreFrame.addWindowListener(WA);
		ScoreFrame.add(ScorePanel);
		ScoreFrame.setVisible(true);
	}
}