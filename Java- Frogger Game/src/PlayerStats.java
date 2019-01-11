// Creator: Riley Garrison

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import javax.swing.Timer;

// Holds info about the player, such as lives, score, and time
public class PlayerStats implements ActionListener
{
	private int Lives;
	private long Score;
	private int Collision;
	private boolean[] Traversed;
	private Timer TheTime;
	private int TimeLength;
	private File TopScores;
	private BufferedReader Reader;
	private	BufferedWriter Writer;
	private Path Directory;
	
	public PlayerStats()
	{
		Lives = 5;
		Score = 0;
		
		Collision = 1;
		// When it is 1 it is on, 0 means not on
		
		TimeLength = 60;
		// Counted in half seconds, so the real time is 30 seconds
		
		TheTime = new Timer(500, this);
		
		Traversed = new boolean[13];
		
		for (int i = 0; i < 12; i++ )
			Traversed[i] = false;
	
		Traversed[12] = true;
		// You get points for moving forward, and the counter resets when
		// you get a goal. This keeps track of how forward you go until
		// you get a goal
		
		try {
		
		Directory = Paths.get(System.getProperty("user.home"), "FroggerFiles");
		// Finds the directory FroggerFiles in your Home Directory
		// Creates it if it doesnt exist
		
		TopScores = new File(Directory.toString());
		if ( TopScores.isDirectory() == false )
			TopScores.mkdir();
		
		
		Directory = Paths.get(Directory.toString() + "\\HighScores.txt");
		TopScores = new File(Directory.toString());
		// Finds the file HighScores.txt in the directory
		// Runs the following code to create the default file if it doesnt exist
		
		if ( TopScores.isFile() == false )
		{
			TopScores.createNewFile();
			
			Writer = new BufferedWriter( new FileWriter(TopScores));
			
			Writer.write("1000 Bobby");
			Writer.newLine();
			Writer.write("800 James");
			Writer.newLine();
			Writer.write("600 Thrasher");
			Writer.newLine();
			Writer.write("400 Peter");
			Writer.newLine();
			Writer.write("200 Joe");
			
			Writer.close();
		}
		
		}
		catch ( IOException e )
		{
			System.out.println("High Score file unable To be created");
		}
	}
	
	// Makes you lose a life
	public void LoseLives()
	{
		Lives--;
	}
	
	// Makes you gain a life
	public void GainLives()
	{
		Lives++;
	}
	
	// Changes the collision on and off
	public void ChangeCollision()
	{
		if ( Collision == 1 )
			Collision = 0;
		else
			Collision = 1;
	}
	
	// Checks if the Collision is on or not
	public int CollisionCheck()
	{
		return Collision;
	}
	
	// Checks how many lives you have
	public int CheckLives()
	{
		return Lives;
	}
	
	// Returns the Score of the Player
	public long GetScore()
	{
		return Score;
	}
	
	// Changes the Score by adding to it
	public void ChangeScore(int What)
	{
		if ( What > 10 ) 
		{
			// If the player moved up, go here
			if ( Traversed[What - 11] == false)
			{
				// If the player hasnt moved up here before
				// Add 10 Points
				Traversed[What - 11] = true;
				Score += 10;
			}
		}
		else if ( What == 1 )
		{
			// If the Player gets a goal add 50 points
			// and add the TimeLeft Times 10 
			Score += 50;
			Score += 10*TimeLength;
		}
		// If you got extra points from the lady frog or the bug
		// Add 200 points
		else if ( What == 2 )
			Score += 200;
	}
	
	// If you got a goal, reset traversed so you continue earning 
	// points for moving forward
	public void resetTraversed()
	{
		for ( int i = 0; i < 12; i++ )
			Traversed[i] = false;
	}

	// Returns the Time Left
	public int GetTime()
	{
		return TimeLength;
	}
	
	// Starts the Timer
	public void StartTime()
	{
		TheTime.start();
		TheTime.setRepeats(true);
	}
	
	// Stops the Timer
	public void StopTime()
	{
		TheTime.stop();
	}
	
	// Resets the Timer
	public void ResetTime()
	{
		TheTime.restart();
		TimeLength = 60;
		TheTime.start();
	}
	
	// Returns the High Scores as a String array
	public String[] GetHighScores() 
	{
		String[] Score = new String[5];
	
		try {
		
		Reader = new BufferedReader( new FileReader(TopScores));
		
		while ( Reader.ready() )
		{
			// Reads the line of scores with their names
			// To the array of strings
			for ( int i = 0; i < 5; i++ )
				Score[i] = Reader.readLine();
		}
		
		Reader.close();
		
		}
		catch ( IOException e )
		{
			System.out.println("Error reading High Scores");
		}
		
		return Score;
	}
	
	// Updates the High Scores after you completely die
	public void UpdateHighScores()
	{
		long OldScore = 0;
		int LineNum = 0;
		String ScoreString = new String();
		String Name;
		String[] OldList = new String[5];
		String[] NewList = new String[5];
		int CharNum;
		
		try {
			
		Reader = new BufferedReader( new FileReader(TopScores));
		
		for ( int j = 0; j < 5; j++ )
		{
			LineNum = j;
			
		 while ( j >= 0 )
		 {
			// Stores the Scores of the j-th place
			// in the String ScoreString
			CharNum = Reader.read();
			
			if ( CharNum >= 65 || CharNum == 32 )
				break;
			else 
				ScoreString += (char)CharNum;
			
		 }
		 
		 // Turns the Old Score gotten from the while loop
		 // Into an int
		 OldScore = Integer.valueOf(ScoreString);
		 
		 if ( Score > OldScore )
		 {
			// If your Score is greater than the Score gotten from the High Scores
			// This will put you on the leader board
			 
			Reader.close();
			
			Name = JOptionPane.showInputDialog(null, "Enter Your Name Here:", "High Score: " + Score, JOptionPane.INFORMATION_MESSAGE);
			// Creates a JOptionPane prompting you for your name
			// If you dont put anything the while loop makes sure you 
			// Put at least something in the Box
			
			while ( Name.isEmpty() )
				Name = JOptionPane.showInputDialog(null, "Please Enter a Name: ", "High Score: " + Score, JOptionPane.INFORMATION_MESSAGE);
				
			OldList = GetHighScores();
			// Gets all the old High Scores
			
			NewList[LineNum] = new String(Long.toString(Score) + ' ' + Name);
			// Adds your Score and your name to the j-th place of the NewList 
					
			for ( int i = 0; i < 5; i++ )
			{
				// Adds the old scores to the New List
				// If the place of the old score overlaps with the new addition
				// to the high scores it moves it one place down
				if ( i == LineNum )
					i++;
				
				if ( i >= LineNum )
					NewList[i] = OldList[i-1];
				else
					NewList[i] = OldList[i];
			}
			
			TopScores.delete();
			TopScores.createNewFile();
			// Deletes and creates a new High Score file with the Updated
			// High Scores
			
			Writer = new BufferedWriter( new FileWriter( TopScores));
			
			for ( int i = 0; i < 5; i++ )
			{
				// Writes the updated high scores to the file
				Writer.write(NewList[i]);
				
				if ( i != 5 )
					Writer.newLine();
			}
			
			Writer.close();
			break;
		 }
		 
		 // If you didn't make a high score (yet)
		 // Finish reading the rest of the line and set ScoreString 
		 // as a new string
			Reader.readLine();
			ScoreString = new String();
			
		}
		
		}
		catch ( IOException e )
		{
			System.out.println("Error reading High Scores");
		}
		catch ( NullPointerException e )
		{
			// Catches if you press the exit button on the high scores
			// Doesnt record your high scores
			System.out.println("Ok we will not record your high score");
			System.exit(0);
		}
	}	
	
	public void actionPerformed(ActionEvent e)
	{
		// Every 30 seconds the TimeLength will descrease by one
		TimeLength--;
	}
}
