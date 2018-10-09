package player;

import java.util.Scanner;
import world.World;
import java.util.ArrayList;
import java.util.Random;

/**
 * Greedy guess player (task B).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class GreedyGuessPlayer  implements Player{

    // Guess Concept:
    // ?

    // Answer Concept:
    // Make a copy of world (ships and it's coordinats) since world is not public
    // Once enemy guesses hit, remove from arraylist


    // introduced variables
    
    public Boolean myShots [][]; // simple my guesses grid to track my shots
    public ArrayList<World.ShipLocation> myShipsAreSinking = new ArrayList<>(); // clone of world.shipLocations
    public ArrayList<Guess> myTargetList = new ArrayList<>(); // arraylist to keep track of what to hunt
    public ArrayList<Guess> myGuesses = new ArrayList<>(); // arraylist to keep track of my guesses
    public ArrayList<Answer> myAnswers = new ArrayList<>(); // arralist to keep track of my answers
    public int boardRow = 0; // size of grid of board
    public int boardCol = 0; // size of grid of board
    public boolean debug = false; // debug general
    public boolean debugGuess = true; // debug guesses specifically
    public boolean firstCheckerPattern = true; // whether checkerboard pattern has run it's first course

    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
        
        // set boolean grid for hunting
        boardRow = world.numRow;
        boardCol = world.numColumn;
        myShots = new Boolean[boardRow][boardCol];

        // init guesses
        for (int i = 0; i < myShots.length; i++) { 
            for (int j = 0; j < myShots[i].length; j++) { 
                myShots[i][j] = false;
            } 
        }

        // make a copy of world (ships and it's coordinats) since world is not public
        // once enemy guesses hit, remove from arraylist
        for (int i = 0;i < world.shipLocations.size(); i++) {
            myShipsAreSinking.add(world.shipLocations.get(i));
        }
  
        // view my ships' locations: for checking purposes
        if (debug) {
            for (World.ShipLocation shipLoc : myShipsAreSinking) {
                System.out.println(shipLoc.ship.name() + " | " + shipLoc.ship.len() + " | " + shipLoc.ship.width());
                for (World.Coordinate cdn : shipLoc.coordinates) {
                    System.out.println(cdn.row + " x " + cdn.column);
                }
            }
        }

    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.
        Answer a = new Answer ();
        a.isHit = false;

        // check enemy's guess against my board
        if (guess != null && guess.row <= boardRow && guess.column <= boardCol) {

            // traverse through number of ships left
            for (int i = 0; i < myShipsAreSinking.size(); i++) {

                // traverse though ship's remaining coordinates
                for (int j = 0; j < myShipsAreSinking.get(i).coordinates.size(); j++) {
                    int r = myShipsAreSinking.get(i).coordinates.get(j).row;
                    int c = myShipsAreSinking.get(i).coordinates.get(j).column;
                    if (debug) {
                        System.out.println("Try  : " + r + " x " + c);
                    }
         
                    // hit
                    if (guess.row == r && guess.column == c) {
                        if (debug) {       
                            System.out.println("Hit 1: " + myShipsAreSinking.get(i).ship.name() + " | Cells Left: "  
                                + myShipsAreSinking.get(i).coordinates.size()  + " / "
                                + (myShipsAreSinking.get(i).ship.width() * myShipsAreSinking.get(i).ship.len()) );
                        }
                        // remove coordinate if hit
                        myShipsAreSinking.get(i).coordinates.remove(j);
                        if (debug) {       
                            System.out.println("Hit 2: " + myShipsAreSinking.get(i).ship.name() + " | Cells Left: "  
                                + myShipsAreSinking.get(i).coordinates.size()  + " / "
                                + (myShipsAreSinking.get(i).ship.width() * myShipsAreSinking.get(i).ship.len()) );
                        }
                        // set isHit
                        a.isHit = true;
                        break;
                    }
                }
                // if ship no longer have coordinates ==> sunk!
                if (myShipsAreSinking.get(i).coordinates.size() == 0) {
                    if (debug) {
                        System.out.println("Sunk : " + myShipsAreSinking.get(i).ship.name());
                    }
                    a.shipSunk = myShipsAreSinking.get(i).ship;
                    myShipsAreSinking.remove(i);
                }

                if (a.isHit) {
                    break;
                }  
            }
        }
        return a;
    } // end of getAnswer()

    @Override
    public Guess makeGuess() {
        // To be implemented.
        return (myTargetList.size() > 0 ? targetMode():huntMode());
        
    } // end of makeGuess()

    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
        myAnswers.add(answer);
        myGuesses.add(guess);

        if (answer.isHit) {           
            // to-do add potential coordinates to myTargetList - check up, down, left, right
            Guess g = new Guess();
            int r = guess.row;
            int c = guess.column;
            
            // up
            if (r + 1 < boardRow - 1 && !myShots[r + 1][c]) 
                addToList(r + 1, c);
            
            // down
            if (r - 1 >= 0 && !myShots[r - 1][c] ) 
                addToList(r - 1, c);
            
            // left
            if (c - 1 >= 0 && !myShots[r][c - 1] ) 
                addToList(r, c - 1);
            
            // right
            if (c + 1 < boardCol - 1 && !myShots[r][c + 1] ) 
                addToList(r, c + 1);
            
        }   

    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.
        // simple way of checking that there are no ships left as ship and their corresponding coordinates
        // will be remove if all coordinates hit
        return ((myShipsAreSinking.size() == 0) ? true : false);
    } // end of noRemainingShips()

    // introduced methods

    // generate shots based on checkerboard pattern
    public Guess huntMode () {

        Guess g = new Guess ();
        int r;
        int c;
        boolean found = false;
        boolean noCellsLeft = true;
        int shotGuessController; // control how the shots are generated based on checkerboard

        // simple boolean check to see whether any cells left
        for (int i = 0; i < myShots.length; i++) { 
            for (int j = 0; j < myShots[i].length; j++) { 
                if (!myShots[i][j])  {
                    noCellsLeft = false;
                    break;
                }
            } 
            if (!noCellsLeft) {
                break;
            }
        }
       
        found = false;
        if (!noCellsLeft) {
		    while (!found) {
                for (int i = 0; i < myShots.length; i++) {
                    r = i;
                    // if had reached last cell after 1st checkerboard pattern, do alternate type
                    if (firstCheckerPattern) {
                        shotGuessController = i % 2;
                        if (debugGuess) {
                            System.out.println("1st iteration: " + shotGuessController);
                        }
                    }
                    else {
                        if (i % 2 == 0) 
                            shotGuessController = (i % 2) + 1;
                        else
                            shotGuessController = (i % 2) - 1;

                        if (debugGuess) {
                            System.out.println("2nd iteration: " + shotGuessController);
                        }
                    }

                    for (int j = shotGuessController; j < myShots[i].length; j = j + 2) {
                        if (i == myShots.length - 1 && j == myShots[i].length - 1)
                            firstCheckerPattern = false; // if had reached last cell after 1st checkerboard pattern, do alternate type
                        c = j;
                        if (!myShots[r][c]) {
                            myShots[r][c] = true;
                            
                            if (debugGuess) {
                                System.out.println("Guess: " + r + " | " + c);
                            }
                            g.row = r;
                            g.column = c;
                            found = true;
                            break;
                        }
                    }
                    if (found)
                        break;
                }
            }
        }
        // return guess if empty cell found, null otherwise
        return ((found) ? g : null);
    }

    // add to targetting list if not already in
    public void addToList(int r, int c) {
        Guess g = new Guess();
        g.row = r;
        g.column = c;
        boolean canAddToList = true;
        for (int i = 0; i < myTargetList.size(); i++) {
            if (myTargetList.get(i).row == g.row && myTargetList.get(i).column == g.column) {
                canAddToList = false;
            }
        }
        if (canAddToList)
            myTargetList.add(g);

    }

    // generate shots based on checkerboard pattern
    public Guess targetMode () {
        Guess g = new Guess();
        boolean found = false;
        if (myTargetList.size() > 0) {
            g = myTargetList.get(0);
            myTargetList.remove(0);
            found = true;
            myShots[g.row][g.column] = true;
        }
        return ((found) ? g : null);
    }
    
} // end of class GreedyGuessPlayer
