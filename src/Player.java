/**
 * The Player class simulates a player in the card game
 *
 * @author   Anthony Castronuovo
 * @version  1.1
 * Created:  5.15.2017
 */

import java.io.*;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Player{

    /**
     * playerID contains the unique ID of each player in the game
     * It will never be a negative number
     */
    private int playerID;
    
    
    /**
     * playerName holds the readable name of the player
     * There can be multiple players with the same playerName
     */
    public String playerName;
    
    
    /**
     * playerScore contains the score starting at 0
     */
    public int playerScore;
    
    
    /**
     * playerHand is the array of cards that each player has in their hand
     * Players can only hold a maximum of 3 cards in their hand
     */
    private Card[] playerHand;
    
    
    /**
     * playerDiscardPile is the array of cards that each player has discarded
     * Players can only hold a maximum of 3 cards in their hand
     */
    private Card[] playerDiscardPile;
    
    
    /**
     * nullCount keeps track of how many players have left the game
     */
	private static int nullCount;
	
	
	/**
	 * Keeps track of what the number of cards are in a player's hand
	 */
	private int numCardsInHand;

    
    
    /**
     * The constructor that creates and sets up a new player
     *
     * @param    playerID The newly created playerID
     * @param    playerHand The initial hand of 3 cards drawn at the start of the game
     * @see      Player
     */
    public Player(int playerID, String playerName){
        
        this.playerID = playerID;
        
        this.playerName = playerName;
        
        playerScore = 0;
        
        playerHand = new Card[3];
        
        playerDiscardPile = new Card[56];
        
        numCardsInHand = 0;
    }
    
    
    /**
     * @param    newScore Updates the player's score. Can be from a win, tie, or if they drew a penalty card
     */
    public void updateScore(int pointsAwarded){
        playerScore += pointsAwarded;
    }
    
    
    /**
     * @return    playerScore The player's current score
     */
    public int getScore(){
        return playerScore;
    }
        
        
    /**
     * @return    playerID The player's unique ID
     */
    public int getPlayerID(){
        return playerID;
    }
    
    
    /**
     * @return    playerName The player's name
     */
    public String getPlayerName(){
        return playerName;
    }
    
    
    /**
     * @return    playerHand The player's hand
     */
    public Card[] getHand(){
    	return playerHand;
    }
    
    
    /**
     * Updates the hand of the player
     */
    public void setHand(Card[] newHand){
    	playerHand = newHand;
    }
    
    
    /**
     * So other classes can update the nullCount
     */
    public static void setNullCount(int newNullCount){
    	nullCount = newNullCount;
    }
    
    
    /**
     * Prints out all the cards currently in the player's hand
     */
    public void printHand(){
        
        System.out.println("\nYour current hand");
        
        for(int i = 0; i < playerHand.length; i++){
            if(playerHand[i] != null){
                System.out.println(" | " + (i+1) + ") " + playerHand[i].getCardName());
            }
        }
    }
    
    
    /**
     * Draw takes the top card of the deck and puts it in the player's hand.
     * If the hand is full, a new card will not be drawn
     *
     * @return   deck The updated deck
     * @throws IOException 
     */
    public Card[] draw(Card[] deck) throws IOException{
        
        for(int i = 0; i < playerHand.length; i++){
            
            if(playerHand[i] == null){
            	
            	if(deck[0] == null){
            		return null;
            	}
            	
                playerHand[i] = deck[0];
                numCardsInHand += 1;
                
                //To make sure the player's score gets updated if they continually draw penalties
                while(playerHand[i].getCardName().equals("Penalty")){
                	System.out.println("Oh No! You drew a penalty card!\n"
                			+ "You lose 1 point!");
                   
                	//As long as there are still cards in the player's hands, we can discard
                	if(numCardsInHand != 0){
                    	updateScore(-1);
                    	discard(playerHand[i]);
                    	numCardsInHand -= 1;
                	}
                
                	boolean drew = drawAnother(deck, i);
                	if(!drew){
                		break;
                	}
                }
                
                deck = Utilities.shiftDeck(deck);
                break;
            }           
        }
        
        return deck;

    }
    
    
    /**
     * Adds the card to the discard pile and removes it from play
     *
     * @param   toDiscard The card to discard
     */
    public void discard(Card toDiscard){
        
        int i = checkHand(toDiscard);
        if(i != -1){
            
            playerHand[i].inPlay = false;
            playerHand[i] = null;
                
            //reminder: playerDiscardPile length = 56
            for(int j = 0; j < playerDiscardPile.length; j++){
                    
                if(playerDiscardPile[j] == null){
                    playerDiscardPile[j] = toDiscard;
                    break;
                }
                    
            }
    
        }
        
    }
    
    
    /**
     * Lets the player choose which card they wish to play
     *
     * @throws IOException 
     */
    public void selectCard() throws IOException{
        
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        
        //The number of the card the player wishes to play
        int cardSelect = 0;
        
        while(true){
            
            printHand();
            System.out.println("\nWhich card would you like to select? Choose the number next to the card");
        
            try{
                cardSelect = Integer.parseInt(read.readLine());
            }
            catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                System.out.println("\n\nYou must enter the number corresponding to the card.");
                continue;
            }
            
            if((cardSelect - 1) < playerHand.length){
            	if(playerHand[cardSelect - 1] != null){
	                playCard(playerHand[cardSelect - 1]);
	                break;
            	}
            }
            else{
            	System.out.println("\nSelect a proper card");
            }
            
        }
    }
   
        
    /**
     * Plays the card and changes the boolean inPlay to true
     *
     * @param   toPlay The card to play
     */
    private void playCard(Card toPlay){
        
    	int i = checkHand(toPlay);
        if(i != -1){
            playerHand[i].inPlay = true;
        }else{
            System.out.println("\nYou cannot play " + toPlay.cardName + ". It is not in your hand.\n");
        }
    }
    
        
    /**
     * Checks the hand for a certain card
     *
     * @param     toCheck The card to check
     * @return    i The index of the card in the player's hand
     * @return    -1 If the card is not in the player's hand
     */
    private int checkHand(Card toCheck){
        
        for(int i = 0; i < playerHand.length; i++){
        	if(playerHand[i] != null){
	            if(playerHand[i].cardName.equals(toCheck.cardName)){
	                return i;
	            }
        	}
        }
        return -1;
    }
    
    
    /**
     * Asks if the player wants to draw another card
     * 
     * @param    deck The deck
     * @param	 i The index of the player's hand to draw to
     * @return   True If the player drew another card
     * @return	 False If the player opted to not draw another card
     * @throws IOException 
     */
    private boolean drawAnother(Card[] deck, int i) throws IOException{
    	
    	String yn = "";
    	BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    	
    	while(true){
    		
    		System.out.println("Draw another card? (y/n)");
    		
    		//If the player chooses not to draw another card, the method will return false
    		//If the player chooses to draw another, then the draw will happen, and method will return true.
	    	yn = read.readLine();
	    	if(yn.equalsIgnoreCase("n") || yn.equalsIgnoreCase("y")){
		    	if(yn.equals("n")){
		    		return false;
		    	}
		    	else{
		    		System.out.println("Press any key to draw:");
		    		yn = read.readLine();
		    		deck = Utilities.shiftDeck(deck);
		    		playerHand[i] = deck[0];
		    		numCardsInHand += 1;
		    		return true;
		    	}
	    	}
	    	else{
	    		System.out.println("\nYou must enter either y or n");
	    		continue;
	    	}
    	}
    }


    /**
     * Removes a player from the game if they no longer wish to play
     * 
     * @throws IOException 
     * @throws InterruptedException 
     */
	@SuppressWarnings("deprecation")
	public void leaveGame(Player[] players) throws IOException, InterruptedException {
		
    	String pathToSound = "../sounds/game_over.wav";
		    	
		//Sets the player who decided to leave to null in the players array and updates the nullCount
		for(int i = 0; i < players.length; i++){
			if(players[i] != null){
				if(players[i].getPlayerID() == getPlayerID()){
					System.out.println("Thanks for playing!");
					players[i] = null;
					nullCount++;
				}
			}
		}
		
		//If all players leave the game, or one player is left, the game will end
		if(nullCount == players.length){
			
			System.out.println("\n|||****************************|||");
			System.out.println("|||    All players have left   |||");
			System.out.println("|||      The game will end     |||");
			System.out.println("|||****************************|||");
			
			//Handles the audio fx for game over/no win
			InputStream in = new FileInputStream(pathToSound);
			AudioStream sound = new AudioStream(in);
			AudioPlayer.player.start(sound);
			Thread.sleep(1500);
			AudioPlayer.player.stop();
			sound.close(); 
			in.close();
			
			System.exit(0);
		}
		else if(players.length - nullCount == 1){
			System.out.println("\n|||****************************|||");
			System.out.println("|||   Only one player remains  |||");
			System.out.println("|||      The game will end     |||");
			System.out.println("|||****************************|||");
	        
			//Handles the audio fx for game over/no win
			InputStream in = new FileInputStream(pathToSound);
			AudioStream sound = new AudioStream(in);
			AudioPlayer.player.start(sound);
			Thread.sleep(1500);
			AudioPlayer.player.stop();
			sound.close(); 
			in.close();
	        
			System.exit(0);
		}
	}


	/**
	 * Displays the current player with the most points.
	 * In the event of a tie at that moment, this will output the first player with the highest score
	 * 
	 * @param     players The players array
	 */
	public void whosWinning(Player[] players) {
		
		//Start a number that can't be reached by scoring.
		//Even if players draw all the penalty cards
		int highestScore = -10;
		Player highestScorer = null;
		
		for(int i = 0; i < players.length; i++){
			if(players[i] != null){
				if(players[i].getScore() > highestScore){
					highestScore = players[i].getScore();
					highestScorer = players[i];
				}
			}
		}
		
		System.out.println("\n|||****************************|||");
		System.out.println("|||            " + highestScorer.getPlayerName());
		System.out.println("|||  Is currently leading with |||");
		System.out.println("|||          " + highestScore + " points");
		System.out.println("|||****************************|||");
	}
    
}
