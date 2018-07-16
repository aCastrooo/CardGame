/**
 * The Card class defines a card in the game
 *
 * @author   Anthony Castronuovo
 * @version  1.1
 * Created:  5.15.2017
 */

public class Card{
    
    /**
     * The rank of the card used in scoring
     */
    private int rank;
    
    
    /**
     * The suit of the card represented as an integer
     * Club = 0
     * Diamond = 1
     * Heart = 2
     * Spade = 3
     * Penalty Cards = 4
     */
    public int suit;
    
    
    /**
     * The value of the cards
     * Jack = 11
     * Queen = 12
     * King = 13
     * Ace = 14 (Since ace is considered as the highest numbered card instead of a 1)
     */
    public int cardNumber;
    
    
    /**
     * The face values as they actually are. (Two, Three, Jack, Queen, King, etc.)
     */
    public String cardName;
    
    
    /**
     * States whether the card is in play or not
     */
    public boolean inPlay;

    
    /**
     * The constructor that creates and sets up a new player
     *
     * @param    cardName The card's face value name
     * @param    cardNumber The card's true value represented as an integer
     * @param    suit The card's suit represented as an integer
     */
    public Card(int cardNumber, int suit){
        
        this.cardNumber = cardNumber;
        
        this.suit = suit;
        
        inPlay = false;
        
        getNameFromNumber();
        
        calculateRank();
        
    }
    
    
    /**
     * @return    rank The rank of the selected card
     */
    public int checkRank(){
        return rank;
    }
    
    
    /**
     * @return    inPlay If the card is in play or not
     */
    public boolean isInPlay(){
        return inPlay;
    }
    
    
    /**
     * @return    cardName The card's name
     */
    public String getCardName(){
        return cardName;
    }
    
    
    /**
     * Calculates the rank of the card
     * If the card is a penalty card, rank is set to -1, if not do rank calculation normally
     *
     * @return    rank The calculated rank of the card for scoring
     */
    private void calculateRank(){
        
        rank = (getCardName().equals("Penalty")) ? -1 : cardNumber + (suit * 2);
    
    }
    
    
    /**
     * This constructs the name of the cards based on what the number value is, and what suit the card belongs to
     * It sets the newly constructed name to cardName
     */
    private void getNameFromNumber(){
        
        StringBuilder name = new StringBuilder();
        
        switch(cardNumber){
            case 2: name.append("Two");
                break;
            case 3: name.append("Three");
                break;
            case 4: name.append("Four");
                break;
            case 5: name.append("Five");
                break;
            case 6: name.append("Six");
                break;
            case 7: name.append("Seven");
                break;
            case 8: name.append("Eight");
                break;
            case 9: name.append("Nine");
                break;
            case 10: name.append("Ten");
                break;
            case 11: name.append("Jack");
                break;
            case 12: name.append("Queen");
                break;
            case 13: name.append("King");
                break;
            case 14: name.append("Ace");
                break;
        }
        
        switch(suit) {
            case 0: name.append(" of Clubs");
                break;
            case 1: name.append(" of Diamonds");
                break;
            case 2: name.append(" of Hearts");
                break;
            case 3: name.append(" of Spades");
                break;
            case 4: name.append("Penalty");
                break;
        }
        
        cardName = name.toString();
        
    }
}
