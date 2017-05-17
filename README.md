# Card Royale
This is a card game implementation for Maxeta Technologies

## About the game:
This is a card game that pits up to 4 players against each other.
The aim of the game is to put down a card that is worth more than your opponents.    
  
Each player starts with three cards in their hand and may choose any card to put into play.  
  
The game is over when either one player remains (from other players leaving), or if one player reaches 21 points with a 2 point lead over everyone else, at which point, that player wins the game.  
### Cards:
Cards are represented via their name, suit, and value. They have a hidden rank that denotes which cards are worth more than others. Rank calculation: (card number) + (suit * 2) = rank.  
Suits are ranked in the following order from highest worth to least worth:  
  
Spades  
Hearts  
Diamonds  
Clubs  
### Players:
Players are represented via a unique ID number. This ID is randomized between 1 and 128. This allows players with the same name to play the game.

## How To Play:
The game will initially start players off with drawing two cards for them.  
When it is a player's turn they will be notified via a sound que to draw a card.  
At this point if the player draws a penalty card, they will lose 1 point. If they draw a penalty card the player may choose to draw again, if players keep drawing penalty cards, they will continually be asked if they wish to draw again. Whether or not the draw will happen is up to the player.  
Players may only hold up to 3 cards in their hand. 
#### Once a player draws, they must select between 4 options:
1) View their hand - Prints out your hand
2) Play a Card     - Select a card to put in play
3) Whos' winning?  - See who is currently in the lead
4) Quit the Game   - Leaves the game  
  
## How To Run:
Running is simple. In your terminal, cd to the /src/ folder where you downloaded this and copy/paste the following in there:
  
```terminal  
javac Gameplay.java Player.java Card.java Utilities.java  
java Gameplay  
```
  
