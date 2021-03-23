import java.util.*;
import java.io.*;

public class Menu extends Game {
  private String name;
  private Scanner sc = new Scanner(System.in);
  private String[] randomGames;
  private Game game;
  private boolean continueGame = true;

public Menu(String l) throws IOException {
  super(l);
}

//menu display starting the game
public void begin() throws IOException {
  System.out.println("Hello. What is your name?");
  name = sc.nextLine();
  System.out.println("Hi " + name + "!");
  System.out.println("***** WELCOME TO THE SPELLING BEE");
  
  System.out.println("Would you like to select the seven letters for the Spelling Bee Game or would you like to play a random Spelling Bee Game?");
  System.out.println("Enter 0 to select your own letters or 1 to select a random game.");
  int gameNum = getUserInputForGameMode();
  if(gameNum == 0) {
    userZero();
  }
  if(gameNum == 1)
  {
    userOne();
  }
  sc.nextLine();
  addValidWords();
  calculateRanks();
  printInfo();
  while(continueGame)
  {
    printGame();
    String input = sc.nextLine();
    if(input.charAt(0) == '!') {
      menuOptions(input);
    }
    else {
     if(checkFormedWords(input))
     {
      updatePoints(input);
      wordsUsed.add(input);
      calculateCurrentRank();
     }
    }
  }
  end();
}

//menu display after the player chooses to quit the current game
public void end() throws IOException
{
  System.out.println("Hi " + name + ". Thanks for playing");
  System.out.println("Rank: " + rank + "Points: " + points);
  System.out.println("Would you like to see the solution for the Spelling Bee gam you just played?");
  System.out.println("Type 0 to see the word list.");
  System.out.println("Type 1 to pass");
  System.out.println("Please enter an integer between 0 and 1.");
  int num = getUserInputForGameMode();
  if(num == 0)
  {
    for(int i = 0; i < validWords.size(); i++)
    {
      if(wordsUsed.contains(validWords.get(i)))
      {
        System.out.print("* ");
      }
      System.out.print(validWords.get(i));
      if(wordsUsed.contains(validWords.get(i)))
      {
        System.out.print(" - word used by player *");
      }
      if(checkIfPangram(validWords.get(i)))
      {
      System.out.print(" - pangram");
      }
      System.out.println();
    }
  }
  System.out.println(name + ", would you like to play again?");
  System.out.println("Type 0 to play again");
  System.out.println("Type 1 to quit");
  System.out.println("Please enter an integer between 0 and 1.");
  int num1 = getUserInputForGameMode();
  if(num1 == 0)
  {
    continueGame = true;
    beginAfterGame();
  }
  else{
    System.out.println("Good Bye " + name + " ! Thanks for playing.");
  }
}

// If player choose to play game after !quit
public void beginAfterGame() throws IOException {  
  System.out.println("Would you like to select the seven letters for the Spelling Bee Game or would you like to play a random Spelling Bee Game?");
  System.out.println("Enter 0 to select your own letters or 1 to select a random game.");
  int gameNum = getUserInputForGameMode();
  if(gameNum == 0) {
    userZero();
  }
  if(gameNum == 1)
  {
    userOne();
  }
  sc.nextLine();
  addValidWords();
  calculateRanks();
  printInfo();
  while(continueGame)
  {
    printGame();
    String input = sc.nextLine();
    if(input.charAt(0) == '!') {
      menuOptions(input);
    }
    else {
      if (checkFormedWords(input)) {
        updatePoints(input);
        wordsUsed.add(input);
        calculateCurrentRank();
      }
    }
  }
  end();
}


//validating input 0 or 1
public int getUserInputForGameMode() throws IOException
 {
   int num = -1;
   boolean no = true;
   while (no) {
 	 try {
 	  num = sc.nextInt();
     if(num == 0 || num == 1)
       no=false;
     else
       System.out.println("Input is Invalid. Please type in a 0 or 1");
 	  }
    catch (Exception e) {
 	    System.out.println("Input is invalid. Try Again");
 	    sc.next();
    }
   }
   return num;
  }

//player chooses to input their own letters
public void userZero() throws IOException
{
  while (pangrams.size() == 0) {
    String s = getUserInputForLetters(); 
    game = new Game(s);
    addValidWords();
    if (pangrams.size() == 0) {
      System.out.println("These letters do not make any pangrams. Please enter a new set of letters.");
    }
  }
}

//takes in user input and checks that the set of letters is valid for a game
public String getUserInputForLetters()
 {
   boolean no = true;
   while (no) {

 	 System.out.println("Type 7 distict letters, and then press enter" + "\n These are 7 letters that will be used in the Spelling Bee Game." + "\n The first letter will be the center letter.");
 	 letters = sc.next();
     if(letters.length() == 7) {
      //for loop
      no = false;
      for(int i = 0; i < letters.length();i++)
      {
        char letter = letters.charAt(i);
        if(letter < 97 && letter > 122)
        {
          no = true;
        }
      }
      for(int i = 0; i < letters.length(); i++)
      {
          char indexUsed = letters.charAt(i);
          for(int j = i+1; j < letters.length()-1; j++)
          {
            if(indexUsed == letters.charAt(j))
            {
              no = true;
              System.out.println("You inputted two same letters. Try Again");
            }
          }
      }
    }
    else {
      System.out.println("You didn't input 7 letters. Try Again.");
    }
 	 }
    return letters;
 }

//player chooses to get a random game
public void userOne() throws IOException
{
  randomGames = new String[]{"iktchop", "cahimrt", "aeutlhf", "fapolge", "efvunlg", "caztoni", "iaurnmj", "acyvtni", "ubytlig"};
  int index = (int) (Math.random()*9);
  System.out.println(randomGames[index]);
  game = new Game(randomGames[index]);
}

//display information based on the player's calls to menu options
public void menuOptions (String s) 
{
  //!rank --> display all ranks
  if(s.equals("!ranks"))
  {
    System.out.println("Ranks are based on a percentage of possible points in a puzzle." + " \n The minimum scores to reach each rank for today's are: ");

    for(int i = 0; i < rankNames.length; i++)
    {
      System.out.println(rankNames[i] + ": " + benchmarkPoints[i]);
    }
  }
  //!shuffle --> shuffle the letters displayed
   else if(s.equals("!shuffle")) {
    game.shuffle();
  }

  //!list --> list the words the player has found already
  else if (s.equals("!list")) {
    if(wordsUsed.size() == 0)
    {
      System.out.println("You have not inputted any words.");
    }
    for (String n: wordsUsed) {
      System.out.println(n);
    }
  }

  //!quit --> end the game 
  else if (s.equals("!quit")) {
    continueGame = false;
  }

  //!info --> detailed rules
  else if(s.equals("!info"))
  {
    printInfo();
  }
  //If the player adds an ! but doesn't put an
  //appropriate menu option, it prompts the player
  // to try again
  else{
    System.out.println("You didn't input a correct menu option. Try Again.");
  }
}

//outputs game instructions in console
public void printInfo()
{
    System.out.println("HOW TO PLAY:");
    System.out.println("Words must contain at least 4 letters.");
    System.out.println("Words must include the center letter.");
    System.out.println("Our word list does not include words that are obscure, hyphenated, or proper nouns");
    System.out.println("No cussing either, sorry.");
    System.out.println("Letters can be used more than once.\n \n");
    System.out.println("SCORING RULES: ");
    System.out.println("4-letter words are worth 1 point each.");
    System.out.println("Each puzzle includes at least one \"pangram\" ");
    System.out.println("which uses every letter. These are worth 7 extra points!");
}
  
}
