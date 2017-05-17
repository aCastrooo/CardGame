/**
 * Methods for maintaining the game
 *
 * @author   Anthony Castronuovo
 * @version  1.0
 * Created:  5.15.2017
 */

import java.util.Random;
import java.io.*;
import java.lang.Math;

public class Utilities{
    
    /**
     * Shuffles the deck using simple method of generating random spot in the array,
     * and then switching the current position with the randomly generated position.
     * This is done for all elements of the array
     *
     * @param    toBeShuffled The array to be shuffled
     * @return   shuffled The newly shuffled array
     */
    public static Card[] shuffleDeck(Card[] toBeShuffled){
        
        Random element = new Random();
        
        Card[] shuffled = toBeShuffled;
        
        for(int i = 0; i < shuffled.length; i++){
            
            //newindex is a randomly generated number in the interval [0, length of deck which is 56)
            int newindex = element.nextInt(shuffled.length);
            
            //Swap the new cards
            Card temp = shuffled[i];
            shuffled[i] = shuffled[newindex];
            shuffled[newindex] = temp;
            
        }
        
        return shuffled;
        
    }
    
    
    /**
     * Shuffles the player order using simple method of generating random spot in the array,
     * and then switching the current position with the randomly generated position.
     * This is done for all elements of the array
     *
     * @param    toBeShuffled The array to be shuffled
     * @return   shuffled The newly shuffled array
     */
    public static Player[] shufflePlayers(Player[] toBeShuffled){
        
        Random element = new Random();
        
        Player[] shuffled = toBeShuffled;
        
        for(int i = 0; i < shuffled.length; i++){
            
            //newindex is a randomly generated number in the interval [0, length of deck which is 56)
            int newindex = element.nextInt(shuffled.length);
            
            //Swap the new cards
            Player temp = shuffled[i];
            shuffled[i] = shuffled[newindex];
            shuffled[newindex] = temp;
            
        }
        
        return shuffled;
        
    }
    
    
    /**
     * Creates all the cards and sets up the deck
     *
     * @param    deck The deck to be filled with cards
     * @return   newDeck The newly filled deck
     */
    public static Card[] setUpDeck(Card[] deck){
        
        deck = new Card[56];
        
        //The place in the deck where the cards will initially go
        int d = 0;
        
        //Sets up all the standard cards
        for(int i = 0; i < 4; i++){
            
            //13 cards in a suit
            //j starts at 2 because card numbers start at 2
            for(int j = 2; j < 15; j++){
                Card newCard = new Card(j, i);
                deck[d] = newCard;
                d++;
            }
        }
        
        //Sets up all the penalty cards
        for(int i = 0; i < 4; i++){
            //Penalty cards are represented with suit = 4
            Card newCard = new Card(1, 4);
            deck[d] = newCard;
            d++;
        }
        
        System.out.println("|||****************************|||");
        System.out.println("|||    Deck setup complete!    |||");
        System.out.println("|||****************************|||");
        
        return deck;
    }
    
    
    /**
     * Initializes all the players and inserts them into the players array
     *
     * @param    numPlayers The number of players
     * @return   players The array of players
     * @throws IOException 
     */
    public static Player[] setUpPlayers(int numPlayers) throws IOException{
        
        //BufferedReader for the reading of the player's name into 'name'
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        String name = "";
        
        //Randomly generated ID
        int playerID;
        
        //Using this random object to generate a random number for the playerID
        Random toPID = new Random();
        
        //Initialize the players list
        Player[] players = new Player[numPlayers];
        
        for(int i = 0; i < numPlayers; i++){
            
            System.out.println("|||****************************|||");
            System.out.println("|||         Player " + (i+1) + "           |||");
            System.out.println("|||      What is your name?    |||");
            System.out.println("|||****************************|||");
            
            name = read.readLine();
            
            //playerID can be any number between 1 and 128
            playerID = toPID.nextInt(128) + 1;
            
            //If there is a generated ID that is the same as one that was already created, we need to create a different one
            while(checkIfSameID(playerID, players)){
            	playerID = toPID.nextInt(128) + 1;
            }
            
            Player newPlayer = new Player(playerID, name);
            players[i] = newPlayer;
            
        }
        
        System.out.println("|||****************************|||");
        System.out.println("|||   Player setup complete!   |||");
        System.out.println("|||****************************|||");
        
        return players;
        
    }
    
    
    private static boolean checkIfSameID(int playerID, Player[] players) {
    	
    	for(int i = 0; i < players.length; i++){
    		if(players[i] != null){
    			if(players[i].getPlayerID() == playerID){
    				return true;
    			}
    		}
    	}
    	
		return false;
	}


	/**
     * The initial draw of three cards at the start of the game
     *
     * @param    player The player who is drawing
     * @param    deck The deck the player is drawing from
     * @return   deck The newly shifted deck
     */
    public static Card[] drawInitial(Player player, Card[] deck){
        
        System.out.println("\nDrawing two cards for " + player.getPlayerName() +"...");
        Card[] hand = player.getHand();
        
        for(int i = 0; i < 2; i++){
            
            //Put the first card of the deck into the player's hand
            hand[i] = deck[0];
            
            //To make sure the player's score gets updated if they continually draw penalties
            while(hand[i].getCardName().equals("Penalty")){
            	
            	System.out.println("\nOh No! Drew a penalty card!\n"
            			+ "You lose 1 point.\n"
            			+ "Drawing again...");
            	
                player.updateScore(-1);
                player.discard(hand[i]);
                deck = shiftDeck(deck);
                hand[i] = deck[0];
            }
            
            //Now we need to shift the contents of the deck to the top
            deck = shiftDeck(deck);
            
        }
        
        player.setHand(hand);
        return deck;
        
    }
    
    
    /**
     * Calculates the score after all players have played their cards
     *
     * @param    players The players in the game
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void calculateScore(Player[] players, int deckOut) throws IOException, InterruptedException{
        
        Player topScorer = null;
        int topRank = 0;
        
        for(int i = 0; i < players.length; i++){
        	if(players[i] != null){
            	//Check if the player has a card in play
	            if(checkInPlay(players[i])){
	                int newRank = findTopRank(players[i]);
	                
	                //If the player's card has a higher rank than someone else, the higher rank gets taken, and that player becomes the top scorer
	                if(newRank > topRank){
	                    topRank = newRank;
	                    topScorer = players[i];
	                }
	            }
        	}
        }
        
        topScorer.updateScore(2);
        
        if(deckOut == 1){
        	Gameplay.congrats(topScorer);
        }
        
        System.out.println("|||****************************|||");
        System.out.println("|||    Winner of the round:    |||");
        System.out.println("|||        " + topScorer.getPlayerName());
        System.out.println("|||****************************|||");
    }
    
    
    /**
     * Shifts the contents of the deck
     * Once the top card is drawn, it is not there anymore
     *
     * @param    deck The deck to shift
     * @return   deck The shifted deck
     */
    public static Card[] shiftDeck(Card[] deck){
        
    	int i = 0;
        for(i = 0; i < deck.length-1; i++){
        	if(deck[i + 1] != null){
	            deck[i] = deck[i + 1];
	            deck[i + 1] = null;
        	}
        }
        
        return deck;
    }
    
    
    /**
     * Checks if the player is leading by two against all the other players
     *
     * @param    player The player that might win
     * @param    players The list of all players in the game
     * @return   True If the player is leading by two
     * @return   False If the player is not leading by two
     */
    public static boolean leadingByTwo(Player player, Player[] players){
        
        boolean isLeading = false;
        
        for(int i = 0; i < players.length; i++){
        	
        	if(players[i] != null){
            
	            //Do not compare the player with himself, so the loop just continues
	            //If they are different IDs, then just compare the scores
	            if(players[i].getPlayerID() == player.getPlayerID()){
	                continue;
	            }
	            else{
	                
	                //If the player is not leading by two against all other players, the else block code will run and will return false
	                if(Math.abs(player.getScore() - players[i].getScore()) >= 2){
	                    isLeading = true;
	                }
	                else{
	                    isLeading = false;
	                }
	            }
	            
        	}
            
        }
        
        return isLeading;
        
    }
    
    /**
     * Checks if the player has a card in play
     *
     * @param    player The player to check
     * @return   True If there is a card in play
     * @return   False If there is no card in play
     */
    private static boolean checkInPlay(Player player){
        
    	Card[] hand = player.getHand();
    	
        for(int i = 0; i < hand.length; i++){
            if(hand[i].isInPlay() == true){
                return true;
            }
        }
        
        return false;
        
    }
    
    
    /**
     * Gets the rank of the card that is in play
     *
     * @param    player The player to check
     * @return   rank The rank of the card in play
     */
    private static int findTopRank(Player player){
        
    	Card[] hand = player.getHand();
    	
        for(int i = 0; i < hand.length; i++){
            
        	//While checking we need to discard the cards
        	//These are the cards already in play, and once scoring is finished, those cards must be removed from play
            if(hand[i].isInPlay() == true){
            	int rank = hand[i].checkRank();
            	player.discard(hand[i]);
                return rank;
            }
            
        }
        
        //If there are no cards in play we return -2 because -1 is the rank of a penalty card
		return -2;
    }
    
    
    
}
