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

    /////////////////////////////////////////////
    // Guess Concept:
    /////////////////////////////////////////////
    // Create array class GuessMatrix which consists of int score to keep track of probability of that cell having a ship
    //      and a boolean (shotAttempted) which tracks whether that cell's has been attempted; this boolean is similar to what
    //      was implemented in the Random and Greedy player model
    // A cell score (probability) is increased when it can fit the length of the ship upwards, downwards, rightwards and leftwards
    // This is done for each row and each column for each ship and for cells which has not been sent as a guess
    // If the matrix is not cleared, this implementation takes longer; it will take as long as Random player implementation
    // This is probably due to large scores being kept
    // In hunt mode, we find the cell with the largest probabilty score and push out as a guess
    // Initially, we took the first occurrence of the largest score but decided to make it random
    //      as first occurrence will traverse a very big board like 50x50 slowly
    // Once a cell has been indicated as hit/miss, the matrix is cleared and probabilites recalculated again
    // If a ship is sunk, again, the matrix is cleared and probablilites recalculated but with 1 lesser ship
    // No invalid/repeated guesses were made

    // If there is a hit, it goes into target mode which is the same exact implementation of Greedy player where coordinates of the
    //      right, down, left and up of the hit cell is added to a target list
    // Once the target list is not empty, coordinates are popped out and sent as a guess
    // Iterate through target list. If hit again, add on more adjacent coordinates.
    // Again, target list is not cleared when ship is sunk as there could be adjacent ships

    /////////////////////////////////////////////
    // Answer Concept (same as Random):
    /////////////////////////////////////////////
    // Make a copy of world (ships and it's coordinates) since world is not public
    // Once enemy guesses hit, remove coordinate from arraylist
    // Once all of a ship's coordinates are hit, ship is removed from arraylist myShipsAreSinking
    // Send myAnswer accordingly

    /////////////////////////////////////////////
    // Average Wins/Losses against Random Player
    /////////////////////////////////////////////

    // 10 x 10 Board: Location 1 vs Location 2
    // Game 1: random vs prob
    // Random   - 95 rounds to destroy opponent's ships.
    // Prob     - 54 rounds to destroy opponent's ships.

    // Game 2: random vs prob
    // Random   - 97 rounds to destroy opponent's ships.
    // Prob     - 49 rounds to destroy opponent's ships.

    // Game 3: random vs prob
    // Random   - 94 rounds to destroy opponent's ships.
    // Prob     - 54 rounds to destroy opponent's ships.

    // Game 4: prob vs random
    // Random   - 100 rounds to destroy opponent's ships.
    // Prob     - 79 rounds to destroy opponent's ships.

    // Game 5: prob vs random
    // Random   - 98  rounds to destroy opponent's ships.
    // Prob     - 71  rounds to destroy opponent's ships.

    // Game 6: prob vs random
    // Random   - 96  rounds to destroy opponent's ships.
    // Prob     - 66  rounds to destroy opponent's ships.

    // Games in non-rendering mode 10 x 10: prob vs random
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | ProbabilisticGuessPlayer: 88 *
    // P2 | RandomGuessPlayer: 96
    // P1 | ProbabilisticGuessPlayer: 95 *
    // P2 | RandomGuessPlayer: 97
    // P1 | ProbabilisticGuessPlayer: 89 *
    // P2 | RandomGuessPlayer: 98
    // P1 | ProbabilisticGuessPlayer: 86 *
    // P2 | RandomGuessPlayer: 99
    // P1 | ProbabilisticGuessPlayer: 95 *
    // P2 | RandomGuessPlayer: 97
    // P1 | ProbabilisticGuessPlayer: 95 *
    // P2 | RandomGuessPlayer: 99
    // P1 | ProbabilisticGuessPlayer: 88 *
    // P2 | RandomGuessPlayer: 98
    // P1 | ProbabilisticGuessPlayer: 89 *
    // P2 | RandomGuessPlayer: 91
    // P1 | ProbabilisticGuessPlayer: 89
    // P2 | RandomGuessPlayer: 87 *
    // P1 | ProbabilisticGuessPlayer: 91
    // P2 | RandomGuessPlayer: 89 *
    
    
    // Games in non-rendering mode 10 x 10: random vs prob
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | RandomGuessPlayer: 95
    // P2 | ProbabilisticGuessPlayer: 51 *
    // P1 | RandomGuessPlayer: 98
    // P2 | ProbabilisticGuessPlayer: 71 *
    // P1 | RandomGuessPlayer: 97
    // P2 | ProbabilisticGuessPlayer: 53 *
    // P1 | RandomGuessPlayer: 100
    // P2 | ProbabilisticGuessPlayer: 66 *
    // P1 | RandomGuessPlayer: 95
    // P2 | ProbabilisticGuessPlayer: 66 *
    // P1 | RandomGuessPlayer: 95
    // P2 | ProbabilisticGuessPlayer: 68 *
    // P1 | RandomGuessPlayer: 97
    // P2 | ProbabilisticGuessPlayer: 60 *
    // P1 | RandomGuessPlayer: 100
    // P2 | ProbabilisticGuessPlayer: 41 *
    // P1 | RandomGuessPlayer: 99
    // P2 | ProbabilisticGuessPlayer: 67 *
    // P1 | RandomGuessPlayer: 90
    // P2 | ProbabilisticGuessPlayer: 60 *
    

    // 50 x 50 Board: Location 1 vs Location 2
    // TimeUnit.MILLISECONDS.sleep(10)
    // 1 minute to render the board
    // 3 minutes to complete the game

    // Game 1: random vs prob
    // Random   - 2449 rounds to destroy opponent's ships.
    // Prob     - 810 rounds to destroy opponent's ships.

    // Game 2: prob vs random
    // Random   - 2495 rounds to destroy opponent's ships.
    // Prob     - 936 rounds to destroy opponent's ships.

    // Games in non-rendering mode 50 x 50: prob vs random
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | ProbabilisticGuessPlayer: 1689 *
    // P2 | RandomGuessPlayer: 2314
    // P1 | ProbabilisticGuessPlayer: 1637 *
    // P2 | RandomGuessPlayer: 2443
    // P1 | ProbabilisticGuessPlayer: 1647 *
    // P2 | RandomGuessPlayer: 2427
    // P1 | ProbabilisticGuessPlayer: 1759 *
    // P2 | RandomGuessPlayer: 2323
    // P1 | ProbabilisticGuessPlayer: 1636 *
    // P2 | RandomGuessPlayer: 2488
    // P1 | ProbabilisticGuessPlayer: 1680 *
    // P2 | RandomGuessPlayer: 2492
    // P1 | ProbabilisticGuessPlayer: 1687 *
    // P2 | RandomGuessPlayer: 2423
    // P1 | ProbabilisticGuessPlayer: 1727 *
    // P2 | RandomGuessPlayer: 2424
    // P1 | ProbabilisticGuessPlayer: 1822 *
    // P2 | RandomGuessPlayer: 2447
    // P1 | ProbabilisticGuessPlayer: 1694 *
    // P2 | RandomGuessPlayer: 2452


    // Games in non-rendering mode 50 x 50: random vs prob
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | RandomGuessPlayer: 2467
    // P2 | ProbabilisticGuessPlayer: 1160 *
    // P1 | RandomGuessPlayer: 2319
    // P2 | ProbabilisticGuessPlayer: 1035 *
    // P1 | RandomGuessPlayer: 2290
    // P2 | ProbabilisticGuessPlayer: 844 *
    // P1 | RandomGuessPlayer: 2446
    // P2 | ProbabilisticGuessPlayer: 867 *
    // P1 | RandomGuessPlayer: 2459
    // P2 | ProbabilisticGuessPlayer: 924 *
    // P1 | RandomGuessPlayer: 2417
    // P2 | ProbabilisticGuessPlayer: 987 *
    // P1 | RandomGuessPlayer: 2489
    // P2 | ProbabilisticGuessPlayer: 677 *
    // P1 | RandomGuessPlayer: 2436
    // P2 | ProbabilisticGuessPlayer: 545 *
    // P1 | RandomGuessPlayer: 2398
    // P2 | ProbabilisticGuessPlayer: 1485 *
    // P1 | RandomGuessPlayer: 2459
    // P2 | ProbabilisticGuessPlayer: 633 *

    /////////////////////////////////////////////
    // Observations:
    /////////////////////////////////////////////
    
    // Prob always wins against Random
    // Probability seems to take longer when it is player 1 but is very good when it is player 2
    // Again, Random takes about > 95% of the rounds
    // In smaller boards, Prob takes average of 62% of rounds
    // For bigger grids, Random is > 95% whilst Prob is about 37% efficiency

    /////////////////////////////////////////////
    // Average Wins/Losses against Greedy Player
    /////////////////////////////////////////////

    // 10 x 10 Board: Location 1 vs Location 2
    // Game 1: greedy vs prob
    // Greedy   - 69 rounds to destroy opponent's ships.
    // Prob     - 67 rounds to destroy opponent's ships.

    // Game 2: greedy vs prob
    // Greedy   - 69 rounds to destroy opponent's ships.
    // Prob     - 49 rounds to destroy opponent's ships.

    // Game 3: greedy vs prob
    // Greedy   - 69 rounds to destroy opponent's ships.
    // Prob     - 52 rounds to destroy opponent's ships.

    // Game 4: prob vs greedy
    // Greedy   - 58 rounds to destroy opponent's ships.
    // Prob     - 72 rounds to destroy opponent's ships.

    // Game 5: prob vs greedy
    // Greedy   - 58 rounds to destroy opponent's ships.
    // Prob     - 75 rounds to destroy opponent's ships.

    // Game 6: prob vs greedy
    // Greedy   - 58 rounds to destroy opponent's ships.
    // Prob     - 73 rounds to destroy opponent's ships.

    // Games in non-rendering mode 10 x 10: prob vs greedy
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | ProbabilisticGuessPlayer: 91
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 91
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 94
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 92
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 94
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 94
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 89
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 90
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 93
    // P2 | GreedyGuessPlayer: 58 *
    // P1 | ProbabilisticGuessPlayer: 90
    // P2 | GreedyGuessPlayer: 58 *
    


    // Games in non-rendering mode 10 x 10: greedy vs prob
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 60 *
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 55 *
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 61 *
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 58 *
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 64 *
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 57 *
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 55 *
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 69
    // P1 | GreedyGuessPlayer: 69
    // P2 | ProbabilisticGuessPlayer: 61 *
    // P1 | GreedyGuessPlayer: 69 *
    // P2 | ProbabilisticGuessPlayer: 73

    // 50 x 50 Board: Location 1 vs Location 2
    // TimeUnit.MILLISECONDS.sleep(10)
    // 1 minute to render the board
    // 1 minute to complete the game

    // Game 1: greedy vs prob
    // Greedy   - 229 rounds to destroy opponent's ships.
    // Prob     - 931 rounds to destroy opponent's ships.

    // Game 2: prob vs greedy
    // Greedy   - 178 rounds to destroy opponent's ships.
    // Prob     - 942 rounds to destroy opponent's ships.

    // Games in non-rendering mode 50 x 50: prob vs greedy
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | ProbabilisticGuessPlayer: 1760
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1719
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1707
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1636
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1680
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1791
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1697
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1797
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1733
    // P2 | GreedyGuessPlayer: 178 *
    // P1 | ProbabilisticGuessPlayer: 1624
    // P2 | GreedyGuessPlayer: 178 *
    

    // Games in non-rendering mode 50 x 50: greedy vs prob
    // Amendments made to BattleShipMain.java to check whether P1/P2 were instanceof 
    //      RandomGuessPlayer/GreedyGuessPlayer/ProbabilisticGuessPlayer

    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 1278
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 930
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 1013
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 924
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 844
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 880
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 867
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 783
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 703
    // P1 | GreedyGuessPlayer: 229 *
    // P2 | ProbabilisticGuessPlayer: 727

    /////////////////////////////////////////////
    // Observations:
    /////////////////////////////////////////////
    // Given the same location files 1 & 2, Greedy Player will always have the same number of rounds as
    //      it's very predictable due to the checkerboard pattern
    // Again, Probability seems to take longer when it is player 1 but is very good when it is player 2
    // In bigger boards, Greedy will take longer to execute if the ships are further away from the starting checkerboard pattern
    // Since the provided layouts are relatively closer to the bottom left of the grid, Greedy will always win
    // For smaller boards, it's almost a draw as Probability (Player 2) will win against Greedy
    //      and will lose when Greedy is Player 2
    // This seems to suggest that for small boards, layout 2 is the layout to avoid as you will lose first!
  
    // introduced variables
   
    public ArrayList<World.ShipLocation> myShipsAreSinking = new ArrayList<>(); // clone of world.shipLocations
    public ArrayList<Guess> myTargetList = new ArrayList<>(); // arraylist to keep track of what to hunt
    public ArrayList<Guess> myGuesses = new ArrayList<>(); // arraylist to keep track of my guesses
    public ArrayList<Answer> myAnswers = new ArrayList<>(); // arraylist to keep track of my answers (reponse to other player's shots)
    public int boardRow = 0; // size of grid of board
    public int boardCol = 0; // size of grid of board
    public boolean debug = false; // debug general
    public boolean debugGuess = false; // debug guesses specifically
    public ArrayList<World.ShipLocation> myProbableShipGuesses = new ArrayList<>(); // another shipLocations but using only ship length

    // guess matric class: similar to Random and Greedy myShots tracking method except with the addition of a probability score
    class GuessMatrix {
        boolean shotAttempted = false;
        int score = 0;

        private GuessMatrix() {
		}
    }

    public GuessMatrix myProbableGuesses[][]; // matrix of probability scores & attempted guesses

    // direction of checks
    final int RIGHTWARDS = 0;
    final int DOWNWARDS = 1;
    final int LEFTWARDS = 2;
    final int UPWARDS = 3;
    
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
            myProbableGuesses[guess.row][guess.column].score = 0; // redundant - shot had been sent, shotAttempt = true

            // clear matrix
            clear_matrix();

            // recalculate probability matrix
            calcProbabilty();
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

                // remove ship to recalculate probability matrix
                for (int i = 0; i < myProbableShipGuesses.size(); i++) {
                    if (answer.shipSunk.name() == myProbableShipGuesses.get(i).ship.name()) {
                        myProbableShipGuesses.remove(i);
                    }
                }

                // clear matrix
                clear_matrix();
                
                // recalculate probability matrix all over again but with 1 lesser ship
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
                // as first occurrence will traverse a very big board 50x50 slowly in draw mode
                // in non-rendering mode, we can probably get away with using the first largest score
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
                    for (int s = 0; s < myProbableShipGuesses.size(); s++) { // for each remaining ship, do:
                        calculateShipLayoutProb (i, j, s, RIGHTWARDS);  // check right
                        calculateShipLayoutProb (i, j, s, DOWNWARDS);   // check down
                        calculateShipLayoutProb (i, j, s, LEFTWARDS);   // check left
                        calculateShipLayoutProb (i, j, s, UPWARDS);     // check up
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
    // switch used to enhance readability and reusability of code; initially, we had 4 separate methods:
    // - calculateShipLayoutRightwards    (int r, int c, int s)
    // - calculateShipLayoutDownwards     (int r, int c, int s)
    // - calculateShipLayoutLeftwards     (int r, int c, int s)
    // - calculateShipLayoutUpwards       (int r, int c, int s)
    public void calculateShipLayoutProb(int r, int c, int s, int direction) {
        int row = r;
        int col = c;

        switch (direction) {
            case RIGHTWARDS:
                col = c + myProbableShipGuesses.get(s).ship.len();
                break;
            case DOWNWARDS:
                row = r + myProbableShipGuesses.get(s).ship.len();
                break;
            case LEFTWARDS:
                col = c - myProbableShipGuesses.get(s).ship.len();
                break;
            case UPWARDS:
                col = c + myProbableShipGuesses.get(s).ship.len();
                break;
        }


		if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
			for (int i = 0; i < myProbableShipGuesses.get(s).ship.len(); i++) {
                switch (direction) {
                    case RIGHTWARDS:
                        col = c + i;
                        break;
                    case DOWNWARDS:
                        row = r - i;
                        break;
                    case LEFTWARDS:
                        col = c - i;
                        break;
                    case UPWARDS:
                        row = r + i;
                        break;
                }
                
                if (row >= 0 && row < boardRow && col >= 0 && col < boardCol) {
                    if (!myProbableGuesses[row][col].shotAttempted) {
                        myProbableGuesses[row][col].score++;
                    }
                }
            }
        }
    }

    // set all scores to 0
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
