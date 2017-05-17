/**
 * The Gameplay class holds the main logic of the game
 *
 * @author   Anthony Castronuovo
 * @version  1.0
 * Created:  5.15.2017
 */

import java.io.*;
import sun.audio.*;

public class Gameplay{

    /**
     * The deck that holds all cards, playable, or penalty
     */
    public static Card[] deck;
    
    
    /**
     * The list of all players in the game
     * Since the number of players is between 2 and 4, the size of this array will vary between those numbers
     */
    public static Player[] players;
    
    
    /**
     * For displaying what turn the game is currently on
     */
    public static int turn;
    
    
    /**
     * Starts the game
     * Each player will get a turn based on where they are in the players array. 
     * The players array is shuffled so that any player may go first
     * @throws IOException 
     * @throws InterruptedException 
     */
	@SuppressWarnings("deprecation")
	public static void playGame() throws IOException, InterruptedException{
    	
    	//Path to the audio file
    	String pathToSound = "C:/Users/Anthony/workspace/CardGame/sounds/notificationSound.wav";
    	
        //Keeps track of the when players go
        int i = 0;
        
        //Start keeping track of what turn
        turn = 0;
        
        //Shuffles the deck of cards
        deck = Utilities.shuffleDeck(deck);

        //Shuffles the order of players
        players = Utilities.shufflePlayers(players);
        
        //For drawing of cards
        String anyKey = "";
        
        //Set the nullCount in Player to 0
        Player.setNullCount(0);
        
        
        while(true){
        	
            //If it is the first turn of the game, each player is given 2 cards to start
            if(turn == 0){
                deck = Utilities.drawInitial(players[i], deck);
                i++;
                if(i == players.length){
                    i = 0;
                    turn++;
                    continue;
                }
                continue;
            }
            
            System.out.println("\n\n|||***************************");
            System.out.println("|||   " + players[i].getPlayerName() + "'s turn   ");
            
            //Handles the audio fx for the next turn notification sound
            InputStream in = new FileInputStream(pathToSound);
            AudioStream sound = new AudioStream(in);
            AudioPlayer.player.start(sound);
            Thread.sleep(500);
            AudioPlayer.player.stop();
            sound.close(); 
            in.close();
        	
            System.out.println("|||   Press any key to draw:");
            System.out.println("|||***************************");
            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
            anyKey = read.readLine();
            deck = players[i].draw(deck);
            
            //If the deck runs out of cards, we end the game and the player with the highest score wins
            if(deck == null){
            	Utilities.calculateScore(players, 1);
            }
            
            
            //Lists the options that the player can do
            listOptions(players[i]);
            
            //If i reaches the last element of the players array, we need to get back to the start again
            if(i == players.length-1){
                i = 0;
                
                Utilities.calculateScore(players, 0);
                
               	//Check if the player is no longer there. If he is not there, go onto the next player
            	if(players[i] != null){
            		
    	            //Check to see if the current player has a score 21 or greater and is leading by 2 points. If not, continue the game
    	            if(players[i].getScore() >= 21 && Utilities.leadingByTwo(players[i], players)){
    	                congrats(players[i]);
    	            }
    	            
            	}
            	else{
    				if(i == players.length){
    					i = 0;
    					turn++;
    					continue;
    				}
            		else{
    					i++;
    					continue;
            		}
            	}
            	
                turn++;
                continue;
            }
            
            //Next players turn
            i++;
        }
        
    }
    
    
    /**
     * This method is called everytime a player is up to play
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void listOptions(Player player) throws IOException, InterruptedException{
        
    	BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    	
        int option;
        
        select: while(true){
            System.out.println("\n\n|||***************************");
            System.out.println("|||   " + player.getPlayerName());
            System.out.println("|||   Your Player Number: " + player.getPlayerID());
            System.out.println("|||   Your Score: " + player.getScore());
            System.out.println("|||   What will you do?   |||");
            System.out.println("|||   1) View your hand   |||");
            System.out.println("|||   2) Play a card      |||");
            System.out.println("|||   3) Who's winning?   |||");
            System.out.println("|||   4) Quit the game    |||");
            System.out.println("|||***************************");


            try{
                option = Integer.parseInt(read.readLine());
                
                switch(option){
                    case 1: player.printHand();
                        break;
                    case 2: player.selectCard();
                        break select;
                    case 3: player.whosWinning(players);
                		break;
                    case 4: player.leaveGame(players);
                    	break select;
                    default: System.out.println("\n|||   You must select an option   |||");
                        break;
                }
            }
            catch(NumberFormatException e){
                System.out.println("\nYou must enter either 2, 3, or 4");
            }
            
        }
        
    }
    
    
    /**
     * The end of the game.
     * Displays the congratulations message for the winning player
     * 
     * @param    player The player that won the game
     * @throws IOException 
     * @throws InterruptedException 
     */
	@SuppressWarnings("deprecation")
	public static void congrats(Player player) throws IOException, InterruptedException{
    	String pathToSound = "C:/Users/Anthony/workspace/CardGame/sounds/win.wav";
    	
        System.out.println("|||****************************|||");
        System.out.println("|||       Congratulations!     |||");
        System.out.println("|||        " + player.getPlayerName() + " wins!");
        System.out.println("|||****************************|||");

        
        //Handles the audio fx for game win
    	InputStream in = new FileInputStream(pathToSound);
    	AudioStream sound = new AudioStream(in);
    	AudioPlayer.player.start(sound);
    	Thread.sleep(2000);
    	AudioPlayer.player.stop();
    	sound.close(); 
    	in.close();

        System.out.println("\nGame Over!");
        System.out.println("Thanks for playing!");
        
        System.exit(0);
    }
    
    
    
    
    public static void main(String[] args) throws IOException, InterruptedException{
        
        //User will input number of players into this variable
        int numPlayers = 0;
        
        //For reading user input
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        
        
        System.out.println("|||****************************|||");
        System.out.println("|||   Welcome to Card Royale!  |||");
        System.out.println("|||****************************|||");

        System.out.println();
        
        while(true){
            
            System.out.println("|||****************************|||");
            System.out.println("|||   How many players? (2-4)  |||");
            System.out.println("|||****************************|||");
        
            //If the user attempts to insert anything that is not a number or a number not in the range [2, 4], then they will be continually asked to put in the correct value
            try{
                numPlayers = Integer.parseInt(read.readLine());
                
                if(numPlayers == 2 || numPlayers == 3 || numPlayers == 4){
                    break;
                }
                else{
                    System.out.println("\nYou must enter either 2, 3, or 4.\n\n");
                    continue;
                }
            }
            catch(NumberFormatException e){
                System.out.println("\nYou must enter either 2, 3, or 4.\n\n");
            }
            
        }
        
        players = Utilities.setUpPlayers(numPlayers);
        System.out.println();
        
        System.out.println("|||****************************|||");
        System.out.println("|||     Setting up the deck    |||");
        System.out.println("|||****************************|||");
        
        deck = Utilities.setUpDeck(deck);
        System.out.println();
        
        System.out.println("|||****************************|||");
        System.out.println("|||          Game Start!       |||");
        System.out.println("|||****************************|||");
        
        playGame();

    }
}
