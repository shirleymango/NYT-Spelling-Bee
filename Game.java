import java.util.*;
import java.io.*;

public class Game {
   static String letters;
   static char[] displayedLetters;
   static ArrayList<String> validWords;
   int points;
   String rank = "Beginner";
   static ArrayList<String> wordsUsed;
   int[] benchmarkPoints;
   String[] rankNames = new String[]{"Beginner", "Good Start", "Moving Up", "Good", "Solid", "Nice", "Great", "Amazing", "Genius", "Queen Bee"};
   String[] praiseNames = new String[]{"Good!!!","Nice!!!","Wow!!!","Perfect!!!","Amazing!!!","Awesome!!!","Magnificent!!!!!"};
   static  ArrayList<String> pangrams;
   static Scanner sc;
  
public Game (String l) throws IOException {
  letters = l;
  sc = new Scanner(new File("dictionary.txt"));
  validWords = new ArrayList<String>();
  wordsUsed = new ArrayList<String>();
  pangrams = new ArrayList<String>();
  displayedLetters = new char[7];
  for (int i = 0; i < letters.length(); i++) {
    displayedLetters[i] = letters.charAt(i);
  }
}

//checks that the inputted word is valid to earn points
public boolean checkFormedWords(String l)
{
  boolean isValidWord = true;
  char centerLetter = letters.charAt(0);
  if(l.length() <= 3)
  {
    isValidWord = false;
    System.out.println("The word that you entered is too short.");
  }  
  else if (l.indexOf(centerLetter) < 0) {
      isValidWord = false;
      System.out.println("The word you enter does not contain the center letter, which is " + centerLetter + ".");
  } 
  else if(!validWords.contains(l))
  {
    isValidWord = false;
    System.out.println("The word is not in the list.");
  }
  else if(wordsUsed.contains(l))
  {
    isValidWord = false;
    System.out.println("The word that you entered has already been used.");
  }
  return isValidWord;
}

//creates a list of all valid words in dictionary.txt according to the game letters
public void addValidWords()
{
  while(sc.hasNext()) {
    String word = sc.next();
    boolean isValid = true;
    //checks if the word contains the core letter
    char coreLetter = letters.charAt(0);
    if (word.indexOf(coreLetter) < 0) {
      isValid = false;
    }
    else {
      //makes isValid false if a letter in word is not one of the game letters
      for (int i = 0; i < word.length(); i++) {
        if (letters.indexOf(word.charAt(i)) < 0) {
          isValid = false; break;
        }
      }
    }
    if (isValid) {
      validWords.add(word);
      if (checkIfPangram(word)) {
        pangrams.add(word);
      }
    }
  }
}


//update the user's points based on length of word and whether or not the word is a pangram
public void updatePoints(String l) 
{
  if(l.length() == 4)
  {
    points += 4;
    System.out.println(praiseNames[0]);
  }
  if(l.length() > 4)
  {
    points += l.length();
    if(l.length() > 10)
    {
      System.out.println(praiseNames[6]);
    }
    else{
      System.out.println(praiseNames[l.length()-4]);
    }
  }
  if(checkIfPangram(l))
  {
    points += 7;
    System.out.println("Pangram Found!!!");
  }
}

//returns true if all letters are used in given word
public boolean checkIfPangram(String w)
{
     int counter = 0;
     for(int i = 0; i < letters.length(); i++)
     {
        if(w.contains("" + letters.charAt(i)))
        {
          counter++;
        }
     }
     return counter == letters.length();
}

//calculating the number of points needed to get to each rank
public void calculateRanks()
{
  int possiblePoints = 0;
  benchmarkPoints = new int[10];
  for(int i = 0; i < validWords.size(); i++)
  {
    possiblePoints += validWords.get(i).length();
  }
  benchmarkPoints[0] = 0;
  benchmarkPoints[1] = (int) (possiblePoints * .02);
  benchmarkPoints[2] = (int) (possiblePoints * .05);
  benchmarkPoints[3] = (int) (possiblePoints * .08);
  benchmarkPoints[4] = (int) (possiblePoints * .15);
  benchmarkPoints[5] = (int) (possiblePoints * .25);
  benchmarkPoints[6] = (int) (possiblePoints * .40);
  benchmarkPoints[7] = (int) (possiblePoints * .50);
  benchmarkPoints[8] = (int) (possiblePoints * .70);
  benchmarkPoints[9] = possiblePoints;
}

//returning the user's current rank
public void calculateCurrentRank()
{
  String r = "Beginner";
  for(int i = 0; i < benchmarkPoints.length; i++)
  {
    if(points >= benchmarkPoints[i])
    {
      r = rankNames[i];
    }
  }
  rank = r;
}

//shuffling the order of the game letters that show up in the console
public void shuffle()
{
  String nonCoreLetters = letters.substring(1);
  for (int i = 1; i <= 6; i++) {
    int index = (int) (Math.random()*nonCoreLetters.length());
    displayedLetters[i] = nonCoreLetters.charAt(index);
    nonCoreLetters = nonCoreLetters.substring(0, index) + nonCoreLetters.substring(index + 1);
  }
}

//displays the game letters in the console
public void displayLetters() {
  System.out.println("***********");
  System.out.println("     " + displayedLetters[1] + "     ");
  System.out.println("   " + displayedLetters[2] + "   " + displayedLetters[3] + "   ");
  System.out.println("     " + displayedLetters[0] + "     ");
  System.out.println("   " + displayedLetters[4] + "   " + displayedLetters[5] + "   ");
  System.out.println("     " + displayedLetters[6] + "     ");
  System.out.println("***********");
}

//prints the game in the console each time after the user inputs a word
public void printGame()
{
  System.out.println("Rank: " + rank + " Points: " + points);
  displayLetters();
  System.out.println("Type out any word using letters above. You must use the center letter.");
  System.out.println("Type !info for detailed rules.");
  System.out.println("Type !list for the list of words you've already found.");
  System.out.println("Type !shuffle to shuffle the words display.");
  System.out.println("Type !ranks to display all ranks.");
  System.out.println("Type !quit to quit game.");

}

}
