package player;

import java.util.Scanner;
import world.World;
import java.util.ArrayList;
import java.util.Random;

/**
 * Probabilistic guess player (task C).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */

 /** 
 * Rudi Basiran <s3665980@student.rmit.edu.au>
 * John Michael Ariola Tubera <s3682951@student.rmit.edu.au>
 */

public class ProbabilisticGuessPlayer  implements Player{

    // Guess Concept:
    // ?

    // Answer Concept:
    // Make a copy of world (ships and it's coordinats) since world is not public
    // Once enemy guesses hit, remove from arraylist


    // introduced variables
    
    public Boolean myShots [][]; // simple my guesses grid to track my shots
    public ArrayList<World.ShipLocation> myShipsAreSinking = new ArrayList<>(); // clone of world.shipLocations
    public ArrayList<Guess> myGuesses = new ArrayList<>(); // arraylist to keep track of my guesses
    public ArrayList<Answer> myAnswers = new ArrayList<>(); // arralist to keep track of my answers
    public int boardRow = 0; // size of grid of board
    public int boardCol = 0; // size of grid of board
    public boolean debug = false; // debug general
    public boolean debugGuess = true; // debug guesses specifically

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

        Guess g = new Guess();

        int r;
        int c;
        boolean found = false;
        boolean noCellsLeft = true;

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

        // look for empty cell randomly
        found = false;
        if (!noCellsLeft) {
            while (!found) {
                r = randomNumberInRange(0, boardRow - 1);
                c = randomNumberInRange(0, boardCol - 1);
                if (!myShots[r][c]) {
                    myShots[r][c] = true;
                    g.row = r;
                    g.column = c;
                    found = true; 
                    if (debugGuess) {
                        System.out.println("Guess: " + r + " | " + c);
                    }
                }
            }
        }
        // return guess if empty cell found, null otherwise
        return ((found) ? g : null);
    } // end of makeGuess()

    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
        myAnswers.add(answer);
        myGuesses.add(guess);
    } // end of update()

    @Override
    public boolean noRemainingShips() {
        // To be implemented.
        // simple way of checking that there are no ships left as ship and their corresponding coordinates
        // will be remove if all coordinates hit
        return ((myShipsAreSinking.size() == 0) ? true : false);
    } // end of noRemainingShips()

    // introduced methods
    
    // random row and column generator based on size of board
    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

} // end of class ProbabilisticGuessPlayer
