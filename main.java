/**
 * @author - Matthew DeGuzman
 * 
 * Mastermind.java
 * 
 * The program selects a random arrangement of certain colors, and the
 * player has ten attempts to guess the pattern.  The program tells
 * the player if a certain color is in the correct spot or if it the correct
 * color.
 * 
 * Input: Choose to see the rules, play the game, see the highest score,
 * 		  or quit.  The user's color choice when guessing the pattern
 * 
 * Processing: 1. Display menu and prompt user for choices
 *	  					- See Rules
 *	  					- Play Game
 *	  					- See Highest Score
 *	  					- Quit
 *	  			   - Execute option 1 if selected (See Rules)
 *	  					Display the rules
 *	  			   - Execute option 2 if selected (Play Game)
 *	  					Create a random three color pattern using red, blue, green, white, or yellow
 *	  					Prompt player to choose three colors
 *	  						Input Validation
 *	  						Provide feedback on the selected colors
 *	  							- Red Pin (R): A color is part of the code and 
 *	  										   in the correct position
 *	  							- White Pin (W): A color is part of the code, but
 *	  											 is in the wrong position
 *	  							- No Pin (O): The color is incorrect.
 *	  					If the player guesses all three colors in the correct order in ten 
 *						or less attempts the player wins
 *	  							Check if highscore.txt file exists
 *	  								if it does not, create a new highscore.txt file and 
 *	 								place players score in the file
 *	  								if it does exist, check if player's score is lower
 *	  								than the high score, and replace it if it is
 *	  					If the player could not guess the pattern in ten attempts, they lose			
 *	  			   - Execute option 3 if selected (See Highest Score)
 *	  					if highscore.txt exists then open and display the player name and score in the file
 *	 		  	   - Execute option 4 if selected (quit)
 *	 					Exit the program
 *
 * Output: The rules of the game, the highest score, and pin feedback
 */

package mastermind;

// Import classes
import java.util.Scanner;
import java.util.Random;
import java.io.*;

public class main {
	
	// Creating Scanner object
	static Scanner stdIn = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException
	{
		// Variables
		int option, row, attempt;
		boolean win = false;
		char[] pattArr = new char[3];
		char[][] gameBoard = new char[10][3];
		
		// Intro 
		System.out.println("Welcome to Mastermind");
		
		// Display menu
		do {
			System.out.println("\nPlease select an option:");
			System.out.print("\t1. See Rules"
							+ "\n\t2. Play Game"
							+ "\n\t3. See Highest Score"
							+ "\n\t4. Quit");
			System.out.print("\nOption: ");
			option = stdIn.nextInt();
			
			// input validation
			if (option < 1 || option > 4)
				System.out.print("\nError ... Invalid Option. Please Try Again");
			
			// Execute option 1 - See Rules	
			if (option == 1)
				getRules();
			
			// Execute option 2 - Play game
			if (option == 2)
			{	
				// Flush buffer
				stdIn.nextLine();	
				
				System.out.println("--------------------------------------------------------");
				System.out.println("Colors:\nRed (R), Blue (B), Green (G), Yellow (Y), White (W)");
				pattArr = createPattern();	// Creates Random Pattern
				attempt = 0; 				// Initialize accumulator
				
				// Gives the user ten attempts to guess the pattern
				for (row = 0; row < gameBoard.length && !win; row++)
				{
					attempt = row + 1;
					
					// Prompt user to choose three colors
					System.out.print("\nAttempt " + attempt + ": ");
					gameBoard = promptPlayer(gameBoard, row, attempt);
					
					// Determine if player has guessed the correct pattern
					win = feedback(gameBoard, pattArr, row, attempt);
				}
				
				// Display code
				System.out.print("\n--------------------------------------------------------\nCode: ");
				for (int r = 0; r < pattArr.length; r++)
					System.out.print(pattArr[r] + " ");
				System.out.println();
				
				// If the player has won, a congratulations message is displayed
				if (win)
				{
					System.out.println("\nCongratulations! You win!");
					System.out.print("Score: " + attempt);				// Display Score
					highScore(attempt);									// Store score in highscore.txt if it's higher or the first high score
					win = false;										// Resets win value to false
				}
				
				// If the player loses, a losing message is displayed
				else
					System.out.print("\nYou lost!");
				
				System.out.print("\n-------------------------");
			}
			
			// Execute option 3 - See Highest Score
			if (option == 3)
				getHighScore();
							
		} while (option != 4);
										
		stdIn.close();			// Close Scanner Object
		
		// Execute option 4 - quit
		if (option == 4)
			System.out.println("\nThanks For Playing!");
	}
	
	/**
	 * Displays the rules of the Mastermind game
	 */
	static void getRules()
	{
		// Display the rules
		System.out.println("\n--------------------------------------------------------------------------------");
		System.out.printf("%45s", "Mastermind Rules\n");
		System.out.println("\nMastermind is a code-breaking game between two players: the codemaker\n"
							+ "(in this case the computer) and the codebreaker (the player).");
		System.out.println("\n\t1. The codemaker generates a random pattern cosisting of\n\t  "
							+ " three of the five following colors: "
							+ "\n\t   Red (R), Blue (B), White (W), Yellow (Y), Green (G)");
		System.out.println("\n\n\t2. The Player has ten attempts to guess the pattern."
							+ "\n\n\t     -Red Pin (R) will appear when a color is part of the code"
							+ "\n\t      and in the correct position"
							+ "\n\n\t     -White Pin (W) will appear when a color is part of the"
							+ "\n\t      code but in the wrong position"
							+ "\n\n\t     -No Pin (O) will appear when the color is incorrect");
		System.out.println("\n\n\t3. If the player guesses the correct pattern in ten or less attempts,"
							+ "\n\t   the player wins! However, if the player cannot guess the pattern"
							+ "\n\t   within ten attepmts, the player loses!");
		System.out.println("\n----------------------------------------------------------------------------------");
	}
	
	/**
	 * Generates a random list of three colors from a range
	 * of Red, Blue, White, Yellow, Green
	 * @return - A pattern[] with three random colors
	 */
	static char[] createPattern()
	{
		// Variables
		final int RANGE = 5;				// Sets the bound for the random object
		final int SIZE = 3;
		
		// Create array filled with all possible colors
		char[] selection = {'R', 'B', 'W', 'Y', 'G'};
		char[] pattern = new char[SIZE];
		
		// Create random object to assign colors
		Random randPat = new Random();
		
		// Assign Random colors to all elements in pattern[]
		for (int i = 0; i < pattern.length; i++)
			pattern[i] = selection[randPat.nextInt(RANGE)];

		return pattern;		
	}
	
	/**
	 * Checks for the high score and displays it if
	 * it exists
	 * @throws IOException
	 */
	static void getHighScore() throws IOException
	{
		// Variables
		int hs;			// High Score From File
		String name; 	// Player who set score
		
		// Creating file object to read from file
		File scoreFile = new File("highscore.txt");
		
		System.out.println("\n--------------------------------------------------------------------------------");
		System.out.printf("%45s", "High Score\n");
		
		// Checks if a high score has been set
		if (!scoreFile.exists())
		{
			// Displays message if the file does not exist
			System.out.printf("\n%57s", "A High Score Has Not Been Set Yet!");
			System.out.printf("\n%52s", "Be The First To Set One!");
		}
		
		else 
		{
			// Open File For Reading
			Scanner inputFile =  new Scanner(scoreFile);
			
			// Read High Score and name from file
			name = inputFile.nextLine();
			hs = inputFile.nextInt();
			System.out.print("\nThe High Score set by " + name + " is "+ hs);
			
			// Close Scanner
			inputFile.close();
		}
		System.out.println("\n--------------------------------------------------------------------------------");
	}
	
	/**
	 * Prompts player for three colors
	 * @param gameBoard - The game board
	 * @param row 		- row of the gameBoard[]
	 * @param attempt 	- attempt the Player is on
	 * @return   		- game board with new colors
	 */
	static char[][] promptPlayer(char[][] gameBoard, int row, int attempt)
	{
		// Variables
		final int L = 5;	// Length of the string
		int index = 0;		// Index to select the character of the string at
		String i;			// User input
		
		do {	
			// Prompt player for colors
			i = stdIn.nextLine();
			
			// Validate if input is the correct length
			if (i.length() != L || i.charAt(1) != ' ' || i.charAt(3) != ' ' || notEqual(i))
				System.out.print("\nError ... Invalid Input\nAttempt " + attempt + ": ");		// Display error message
			
		} while (i.length() != L || i.charAt(1) != ' ' || i.charAt(3) != ' ' || notEqual(i));
		
		// Puts the colors into gameBoard[]
		for (int col = 0; col < gameBoard[row].length; col++)
		{
			// Setting the elements of the array equal to the colors inputed by the player
			gameBoard[row][col] = i.toUpperCase().charAt(index);
			
			// Increment the index by two
			index += 2;
		}
		
		return gameBoard;
	}
	
	/**
	 * Validates if color input is a valid color
	 * @param s - String that will be validated
	 * @return  - True if it is not a valid input, False if it is valid
	 */
	static boolean notEqual(String s)
	{
		// Variables
		final int L = 5;	// Correct length of string
		char f;
		boolean n = false;
		
		// Check if length is correct
		if (s.length() != L)
			return n;
		else 
			for (int i = 0; i < L; i+= 2)
			{	
				// Setting f equal to the color input
				f = s.toLowerCase().charAt(i);
				
				// Comparing f to see if it matches the correct colors
				if (f != 'r' && f != 'b' && f != 'w' && f != 'y' && f != 'g')
				{
					// If the input is not equal to any colors, it is not a valid color
					n = true;
					return n;
				}
			}
			

		
		// Returns false if it is a valid input
		return n;
	}
		
	/**
	 * Compares the player's guess to the computer generated code
	 * @param arr  - Colors that the user inputs
	 * @param patt - Colors the code generates
	 * @param row  - Current row of arr[] that will be compared
	 * @param attempt - The current attempt the player is on
	 */
	static boolean feedback(char[][] pin, char[] patt, int row, int attempt)
	{
		// Variables
		final char FILLER = 'x';		// Replaces player guess if it matches one in pattern
		final char PF = 'z';			// Replaces code color if it matches a color
		boolean w = false;
		char nPin = 'O',				// Feedback variables		
			 wPin = 'W',		
			 rPin = 'R';
		char[] nPatt = {patt[0], patt[1], patt[2]};		// Creates array to compare the pattern and player input

		// Checks where to display pins indicator
		if (attempt != 10)
			System.out.printf("%11s", "Pins: ");
		else 
			System.out.printf("%12s", "Pins: ");
			
		// If all colors match, then three Red Pins are displayed and the player wins
		if (pin[row][0] == patt[0] && pin[row][1] == patt[1] && pin[row][2] == patt[2])
		{
			System.out.print(rPin + " " + rPin + " " + rPin);
			w = true;
			return w;
		}
		
		// Prints all red pins and fills all matched colors with fillers
		for (int c = 0; c < nPatt.length; c++)
			if (nPatt[c] == pin[row][c])
			{
				System.out.print(rPin +" ");
				pin[row][c] = FILLER;			// If the color and position match, it is replaced with a filler character so it cannot be compared again
				nPatt[c] = PF;
			}
		
		// Displays all white pin 
		for (int k = 0; k < nPatt.length; k++)
			for (int j = 0; j < pin[row].length; j++)
				if (nPatt[k] == pin[row][j])	// If the colors match, a white pin will be displayed
				{
					System.out.print(wPin + " ");
					pin[row][j] = FILLER;
					nPatt[k] = PF;
					break;
				}
		
		// Displays the No Pin if there was no white or red pin displayed
		for (int i = 0; i < pin[row].length; i++)
			if (pin[row][i] != FILLER)
				System.out.print(nPin + " ");		
		
		System.out.println();
		
		return w;
	}
	
	/**
	 * Creates new high score file and places player score if the high score file
	 * doesn't exist.  If it does exist, it checks if the players score is better than
	 * the previous high score, if it is it replaces the player name and score. 
	 * @param score - The player's score
	 * @throws IOException - Creates an IOException if file is not found
	 */
	static void highScore(int score) throws IOException
	{
		// Variables
		String playerName;
		
		// Create file object and scanner object to read from file
		File scoreFile = new File("highscore.txt");
		
		// Checking if highscore.txt exists
		if (!scoreFile.exists())
		{
			// Creating high score file if highscore.txt does not exist
			PrintWriter newFile = new PrintWriter("highscore.txt");
			
			// Prompt user for name
			System.out.print("\nName: ");
			playerName = stdIn.nextLine();
			
			newFile.println(playerName);	// Write player name to file
			newFile.print(score);			// Writing the new high score in the file
			newFile.close();				// Closing the file
		}
		
		else 
		{	
			Scanner inputFile = new Scanner(scoreFile);		// Create Scanner  Object to read from old high score file
			inputFile.nextLine();							// Skip over name in file
			
			// Checks if players score is higher than old high score
			if (score < inputFile.nextInt())
			{
				PrintWriter newScore = new PrintWriter("highscore.txt");	// Overwrite old high score by creating a new highscore.txt file
				
				System.out.print("\nName: ");		// Prompt user for name
				playerName = stdIn.nextLine();
				newScore.println(playerName);		// Write player name to new high score
				newScore.print(score);				// Write new high score in file
				newScore.close();					// Close new high score file
			}
			
			inputFile.close();						// Close file Scanner
		}
	}
}
