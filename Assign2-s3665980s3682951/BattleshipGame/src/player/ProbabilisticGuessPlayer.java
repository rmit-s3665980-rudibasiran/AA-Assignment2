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
    // Create array class GuessMatrix which consists of int score to keep track of probability of that cell having a ship
    // and a boolean (shotAttempted) which tracks whether that cell's has been attempted; this boolean is similar to what
    // was implemented in the Random and Greedy player model
    // A cell score is increased when it can fit the length of the ship upwards, downwards, rightwards and leftwards
    // In hunt mode, we find the cell with the largest probabilty score and push out as a guess
    // Initially, we took the first occurrence of the largest but decided to make it random
    // as first occurrence will traverse a very big board like 50x50 slowly

    // If there is a hit, it goes into target mode which is the same exact implementation of Greedy player where coordinates of the
    // right, down, left and up of the hit cell is added to a target list
    // Once the target list is not empty, coordinates are popped out and sent as a guess
    // Iterate through target list. If hit again, add on more adjacent coordinates.
    // Again, target list is not cleared when ship is sunk as there could be adjacent ships

    // Answer Concept:
    // Make a copy of world (ships and it's coordinates) since world is not public
    // Once enemy guesses hit, remove from arraylist

    // Average Wins/Losses against Random Player


    // Average Wins/Losses against Greedy Player

    // introduced variables
   
    public ArrayList<World.ShipLocation> myShipsAreSinking = new ArrayList<>(); // clone of world.shipLocations
    public ArrayList<Guess> myTargetList = new ArrayList<>(); // arraylist to keep track of what to hunt
    public ArrayList<Guess> myGuesses = new ArrayList<>(); // arraylist to keep track of my guesses
    public ArrayList<Answer> myAnswers = new ArrayList<>(); // arralist to keep track of my answers
    public int boardRow = 0; // size of grid of board
    public int boardCol = 0; // size of grid of board
    public boolean debug = false; // debug general
    public boolean debugGuess = true; // debug guesses specifically
    public ArrayList<World.ShipLocation> myProbableShipGuesses = new ArrayList<>(); // another shipLocations but only use ship length

    // guess matric class
    class GuessMatrix {
        boolean shotAttempted = false;
        int score = 0;

        private GuessMatrix() {
		}
    }

    public GuessMatrix myProbableGuesses[][]; // matrix of probability scores & attempted guesses
    
    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
        // set matrix for hunting & scoring
        boardRow = world.numRow;
        boardCol = world.numColumn;
        myProbableGuesses = new GuessMatrix[boardRow][boardCol];

        // init matrix
        for (int i = 0; i < myProbableGuesses.length; i++) { 
            for (int j = 0; j < myProbableGuesses[i].length; j++) { 
                myProbableGuesses[i][j] = new GuessMatrix();
                myProbableGuesses[i][j].shotAttempted = false;
                myProbableGuesses[i][j].score = 0;
            }
        }

        // make a copy of world (ships and it's coordinates) since world is not public
        // once enemy guesses hit, remove from arraylist myShipsAreSinking
        // myProbableShipGuesses will be used to calculate probability of ship alignment and scores
        for (int i = 0;i < world.shipLocations.size(); i++) {
            myShipsAreSinking.add(world.shipLocations.get(i));
            myProbableShipGuesses.add(world.shipLocations.get(i));
        }
  
        // initialise matrix
        // calcProbabilty();

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

        // check whether there are targets to shoot at -> look at stored coordinates from update method
        // else check probabilty scores
        return (myTargetList.size() > 0 ? targetMode():huntMode());
    } // end of makeGuess()

    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.

        myAnswers.add(answer);
        myGuesses.add(guess);
        if (!answer.isHit) {
            // set score to 0 as it has missed so that it will not be amongst those with the largest probability score
            myProbableGuesses[guess.row][guess.column].score = 0;

            // clear matrix - should?
            // clear_matrix();

            // recalculate probability matrix?
            // calcProbabilty();
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

                // remove ship to recalculate probability matrix all over again with 1 lesser ship
                for (int i = 0; i < myProbableShipGuesses.size(); i++) {
                    if (answer.shipSunk.name() == myProbableShipGuesses.get(i).ship.name()) {
                        myProbableShipGuesses.remove(i);
                    }
                }

                // clear matrix - should?
                // clear_matrix();
                
                // recalculate probability matrix
                calcProbabilty();

            }
            else {
                // add potential coordinates to myTargetList - check clockwise : right, down, left, up
                // right, down, left, up was chosen instead of north, south, east, west as it's easier for
                // the eye to spot/anticipate the next shot in a clock-like movement 
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
    
    // generate shots based on probability scores
    public Guess huntMode() {

        Guess g = new Guess ();
        int r = -1;
        int c = -1;
        boolean found = false;
        boolean noCellsLeft = true;
 
        // simple boolean check to see whether any cells left
        for (int i = 0; i < myProbableGuesses.length; i++) {
            for (int j = 0; j < myProbableGuesses[i].length; j++) {
                if (!myProbableGuesses[i][j].shotAttempted) {
                    noCellsLeft = false;
                    break;
                }
            }
            if (!noCellsLeft) {
                break;
            }
        }

        found = false;
        int largest = -1;
        if (!noCellsLeft) {
		    while (!found) {
                if (largest < 0) {
                    // look for highest probability score
                    for (int i = 0; i < myProbableGuesses.length; i++) {
                        for (int j = 0; j < myProbableGuesses[i].length; j++) {
                            if (!myProbableGuesses[i][j].shotAttempted) {
                                if (myProbableGuesses[i][j].score > largest) {
                                    largest = myProbableGuesses[i][j].score;
                                    r = i;
                                    c = j;
                                }
                            }
                        }
                    }
                }
               
                // initially, we took the first occurrence of the largest but decided to make it random
                // as first occurrence will traverse a very big board 50x50 slowly
                boolean randomLargestFound = false;
                while (!randomLargestFound) {
                    
                    // randomly look for cells with highest probability score
                    r = randomNumberInRange(0, boardRow - 1);
                    c = randomNumberInRange(0, boardCol - 1);

                    if (!myProbableGuesses[r][c].shotAttempted && myProbableGuesses[r][c].score == largest ) {
                        myProbableGuesses[r][c].score = 0;
                        myProbableGuesses[r][c].shotAttempted = true;
                        g.row = r;
                        g.column = c;
                        found = true;
                        randomLargestFound = true;
                        break;
                    }
                }
            }
        }
        // return guess if empty cell found, null otherwise
        return ((found) ? g : null);
    }

    // add to targetting list if not already in
    public void addToTargetList(int r, int c) {
    
        boolean found = false;
        if (!myProbableGuesses[r][c].shotAttempted) {
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

    // generate shots based on previous hit guesses, remove after return
    // targetlist is not cleared if ship sunk to cater for probable adjacent ship layout placement
    public Guess targetMode () {
        Guess g = new Guess();
        boolean found = false;
        if (myTargetList.size() > 0) {
            g.row = myTargetList.get(0).row;
            g.column = myTargetList.get(0).column;
            myTargetList.remove(0);
            found = true;
            myProbableGuesses[g.row][g.column].shotAttempted = true;
            if (debugGuess) {
                System.out.println("Lining up Next Shot: " + g.row + " | " + g.column);
            }
        }
        return ((found) ? g : null);
    }

    // calculate probability of a cell having a ship
    public void calcProbabilty() {
    
        for (int i = 0; i < myProbableGuesses.length; i++) {
            for (int j = 0; j < myProbableGuesses[i].length; j++) {
                if (!myProbableGuesses[i][j].shotAttempted) {
                    for (int s = 0; s < myProbableShipGuesses.size(); s++) {
                        calculateShipRightwards (i, j, s);      // check right
                        calculateShipDownwards  (i, j, s);      // check down
                        calculateShipLeftwards  (i, j, s);      // check left
                        calculateShipUpwards    (i, j, s);      // check up
                    }
                }
            }
        }
		
        if (debugGuess) {
            System.out.println("Probable Matrix: ");
		    for (int i = 0; i < myProbableGuesses.length; i++) {
                for (int j = 0; j < myProbableGuesses[i].length; j++) {
                    if (myProbableGuesses[i][j].shotAttempted) {
                        System.out.print("." + " ");    
                    }
                    else {
                        System.out.print(myProbableGuesses[i][j].score + " ");
                    }
                }
                System.out.println("");
		    }
        }
    }

    // see whether the remaining ships can fit into that cell based on length
    // do for rightwards, downwards, leftwards and upwards
    public void calculateShipRightwards(int r, int c, int s) {
        int row = r;
        int col = c + myProbableShipGuesses.get(s).ship.len();
		if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
			for (int i = 0; i < myProbableShipGuesses.get(s).ship.len(); i++) {
                col = c + i;
                if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
                    if (!myProbableGuesses[row][col].shotAttempted) {
                        myProbableGuesses[row][col].score++;
                    }
                }
            }
        }
    }

    public void calculateShipDownwards(int r, int c, int s) {
        int row = r - myProbableShipGuesses.get(s).ship.len();
        int col = c;
		if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
			for (int i = 0; i < myProbableShipGuesses.get(s).ship.len(); i++) {
                row = r - i;
                if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
                    if (!myProbableGuesses[row][col].shotAttempted) {
                        myProbableGuesses[row][col].score++;
                    }
                }
            }
        }
    }

    public void calculateShipLeftwards(int r, int c, int s) {
        int row = r;
        int col = c - myProbableShipGuesses.get(s).ship.len();
		if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
			for (int i = 0; i < myProbableShipGuesses.get(s).ship.len(); i++) {
                col = c - i;
                if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
                    if (!myProbableGuesses[row][col].shotAttempted) {
                        myProbableGuesses[row][col].score++;
                    }
                }
            }
        }
    }

    public void calculateShipUpwards(int r, int c, int s) {
        int row = r + myProbableShipGuesses.get(s).ship.len();
        int col = c;
		if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
			for (int i = 0; i < myProbableShipGuesses.get(s).ship.len(); i++) {
                row = r + i;
                if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
                    if (!myProbableGuesses[row][col].shotAttempted) {
                        myProbableGuesses[row][col].score++;
                    }
                }
            }
        }
    }

    public void clear_matrix() {
        for (int i = 0; i < myProbableGuesses.length; i++) { 
            for (int j = 0; j < myProbableGuesses[i].length; j++) { 
                myProbableGuesses[i][j].score = 0;
            } 
        }
    }
    // random row and column generator based on size of board
    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

} // end of class ProbabilisticGuessPlayer
