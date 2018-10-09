package player;

import java.util.Scanner;
import world.World;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random guess player (task A).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class RandomGuessPlayer implements Player{

    public Boolean myShots [][]; // my guesses based on player type
    public ArrayList<World.ShipLocation> myShipsAreSinking = new ArrayList<>(); // clone of world.shipLocations

    public ArrayList<Guess> myGuesses = new ArrayList<>();
    public ArrayList<Answer> myAnswers = new ArrayList<>();
    public int boardRow = 0;
    public int boardCol = 0;
    public boolean debug = true;
    public boolean debugGuess = false;

    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
      
        boardRow = world.numRow;
        boardCol = world.numColumn;
        myShots = new Boolean[boardRow][boardCol];

        // make a copy of world
        for (int i = 0;i < world.shipLocations.size(); i++) {
            myShipsAreSinking.add(world.shipLocations.get(i));
        }
  
        // init guesses
        for (int i = 0; i < myShots.length; i++) { 
            for (int j = 0; j < myShots[i].length; j++) { 
                myShots[i][j] = false;
            } 
        }

        // view my ships' locations
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

        if (guess != null && guess.row <= boardRow && guess.column <= boardCol) {
            for (int i = 0; i < myShipsAreSinking.size(); i++) {
                for (int j = 0; j < myShipsAreSinking.get(i).coordinates.size(); j++) {
                    int r = myShipsAreSinking.get(i).coordinates.get(j).row;
                    int c = myShipsAreSinking.get(i).coordinates.get(j).column;
                    if (debug) {
                        System.out.println("Try  : " + r + " x " + c);
                    }
         
                    if (guess.row == r && guess.column == c) {
                        if (debug) {       
                            System.out.println("Hit 1: " + myShipsAreSinking.get(i).ship.name() + " | Cells Left: "  
                                + myShipsAreSinking.get(i).coordinates.size()  + " / "
                                + (myShipsAreSinking.get(i).ship.width() * myShipsAreSinking.get(i).ship.len()) );
                        }
                        myShipsAreSinking.get(i).coordinates.remove(j);
                        if (debug) {       
                            System.out.println("Hit 2: " + myShipsAreSinking.get(i).ship.name() + " | Cells Left: "  
                                + myShipsAreSinking.get(i).coordinates.size()  + " / "
                                + (myShipsAreSinking.get(i).ship.width() * myShipsAreSinking.get(i).ship.len()) );
                        }
                        a.isHit = true;
                        break;
                    }
                }
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

        if (found) {
            return g;
        }
        else {
            return null;
        }
           
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
        boolean allHit = false;

        if(myShipsAreSinking.size() == 0) {
            allHit  = true;
        }
        return allHit;
    } // end of noRemainingShips()

    // introduced methods
    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }


} // end of class RandomGuessPlayer
