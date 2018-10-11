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

/** 
 * Rudi Basiran <s3665980@student.rmit.edu.au>
 * John Michael Ariola Tubera <s3682951@student.rmit.edu.au>
 */


public class GreedyGuessPlayer  implements Player{

    // Guess Concept:
    // Fire shots in checkerboard pattern beginning with coordinate 0,0
    // If hit, add adjacent's cells coordinates - right, down, left, up - to a target list
    // Right, down, left, up was chosen instead of north, south, east, west as it's easier for
    //      the eye to spot/anticipate the next shot in a clockwise-like movement
    // If target list is empty, push out a guess (target mode), else look for next in checkerboard pattern (hunt mode)
    // Iterate through target list. If hit, add on more adjacent coordinates. If ship sunk, clear target list and return to hunt more

    // Weakness: 
    // If 2 ships are adjacent, a ship sunk will clear the target list and next shots are hunting mode
    // Thus, myTargetList.clear() is commented out
    // Tutor can uncomment to see the effects with adjacent ships layout
    // E.g., as per below where AircraftCarrier and Cruiser are adjacent:
    // AircraftCarrier 1 4 E S
    // Frigate 6 6 S E
    // Submarine 9 1 S E
    // Cruiser 0 7 E N
    // PatrolCraft 9 8 S W

    // Answer Concept:
    // Make a copy of world (ships and it's coordinats) since world is not public
    // Once enemy guesses hit, remove from arraylist

    // Average Wins/Losses against Random Player

    
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

        // check whethere there are targets to shoot at -> look at stored coordinates from update method
        // else enter checkerboard hunting pattern
        return (myTargetList.size() > 0 ? targetMode():huntMode());
        
    } // end of makeGuess()

    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
        myAnswers.add(answer);
        myGuesses.add(guess);

        if (!answer.isHit) {
            if (debugGuess) {
                System.out.println("Shot Miss: " + guess.row + " | " + guess.column);
            }
        }
        else if (answer.isHit) {
            if (debugGuess) {
                System.out.println("Shot Hit: " + guess.row + " | " + guess.column);
            }
            if (answer.shipSunk != null) {
                if (debugGuess) {
                    System.out.println("Shot Sunk: " + answer.shipSunk.name());
                }

                // clear remaining target list since ship already sunk
                // however, a myTargetList.clear() might fool the AI as there could be adjacent ships
                // thus, commented
                // uncomment the next line to show impact for adjacent ship layout
                // myTargetList.clear(); 
                
            }
            else {
                // add potential coordinates to myTargetList - check clockwise : right, down, left, up
                // right, down, left, up was chosen instead of north, south, east, west as it's easier for
                // the eye to spot/anticipate the next shot in a clockwise-like movement 
                int r = guess.row;
                int c = guess.column;

                // right
                if (c + 1 < boardCol) {
                    addToTargetList(r, c + 1);
                }

                // down
                if (r - 1 >= 0) {
                    addToTargetList(r - 1, c);
                }
                    
                // left
                if (c - 1 >= 0) {
                    addToTargetList(r, c - 1);
                }
                   
                // up
                if (r + 1 < boardRow) {
                    addToTargetList(r + 1, c);
                }
            }
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

        // simple boolean check to see whether any cells left
        // modified from random guess as we know that 1 checkerboard pattern will definitely hit a ship

        for (int i = 0; i < myShots.length; i++) {
            for (int j = (i % 2); j < myShots[i].length; j = j + 2) {
                if (!myShots[i][j]) {
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
                    for (int j = (i % 2); j < myShots[i].length; j = j + 2) {
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
    public void addToTargetList(int r, int c) {
    
        boolean found = false;
        if (!myShots[r][c]) {
            for (int i = 0; i < myTargetList.size(); i++) {
                // check to see whether coordinate already added to targetted list
                if (myTargetList.get(i).row == r && myTargetList.get(i).column == c) {
                    found = true;
                    break;
                }
            }
            
            // coordinate not found in target list, thus add
            if (!found) {
                Guess g = new Guess();
                g.row = r;
                g.column = c;
                myTargetList.add(g);
                if (debugGuess) {
                    System.out.println("Added to Target: " + r + " | " + c);
                }
            }
        }
    }

    // generate shots based on previous hit guesses
    public Guess targetMode () {
        Guess g = new Guess();
        boolean found = false;
        if (myTargetList.size() > 0) {
            g.row = myTargetList.get(0).row;
            g.column = myTargetList.get(0).column;
            myTargetList.remove(0);
            found = true;
            myShots[g.row][g.column] = true;
            if (debugGuess) {
                System.out.println("Lining up Next Shot: " + g.row + " | " + g.column);
            }
        }
        return ((found) ? g : null);
    }
    
} // end of class GreedyGuessPlayer
