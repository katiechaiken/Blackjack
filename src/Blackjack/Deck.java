package Blackjack;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JApplet;

public class Deck  extends JApplet
{

    private ArrayList<Card> cards;
    
    public Deck(){
        //Create a new deck of playing cards
        this.cards = new ArrayList<Card>();
    
    }
    
    //Add 52 playing cards to a deck
    public void createFullDeck(){
        //Generate Cards
        //Loop Through Suits
        for(Suit cardSuit : Suit.values()){
            //Loop through Values
            for(Value cardValue : Value.values()){
                //Add new card to the mix
                this.cards.add(new Card(cardSuit, cardValue));
            }
        }
    }
        
   //Shuffle deck of cards
    public void shuffle()
   {
     //Create a new arraylist to hold the shuffled cards temporarily
     ArrayList<Card> tmpDeck = new ArrayList<Card>();
     //Randomly pick from the old deck and copy values to the new deck
     Random random = new Random();
     int randomCardIndex = 0;

     for(int i = 0; i < this.cards.size();i++){
        //generates a random number (index point) 
        randomCardIndex = random.nextInt((this.cards.size()-1 - 0) + 1) + 0;

        tmpDeck.add(this.cards.get(randomCardIndex));
        
        //removes cards that were dealt from deck
        this.cards.remove(randomCardIndex);
     }
     //sets the 'new' shuffled deck to the original so the original is shuffled
     this.cards = tmpDeck;
   }
        
    //Remove a card from the deck
    public void removeCard(int i)
    {
        this.cards.remove(i);
    }
    //Get card from deck
    public Card getCard(int i)
    {
        return this.cards.get(i);
    }
    
    //Add card to deck
    public void addCard(Card addCard)
    {
        this.cards.add(addCard);
    }
    
    //Draw a top card from deck
    public void draw(Deck comingFrom)
    {
        //takes card from deck 
        this.cards.add(comingFrom.getCard(0));
        //remove the card from the deck that was drawn
        comingFrom.removeCard(0);
    }
    
    //Use to print out deck
    public String toString(){
        String list = "";
        int i = 0;
        for(Card aCard : this.cards){
            list += "\n" + aCard.toString();
            i++;
        }
        return list;
    }
    
    public void moveAllToDeck(Deck moveTo){
        int thisDeckSize = this.cards.size();
        //put cards in moveTo deck
        for(int i = 0; i < thisDeckSize; i++){
            moveTo.addCard(this.getCard(i));
        }
        //empty out the deck
        for(int i = 0; i < thisDeckSize; i++){
            this.removeCard(0);
        }
    }
    
    public int deckSize(){
        return this.cards.size();
    }
    
    //Calculate the value of deck
    public int cardsValue(){
        int totalValue = 0;
        int aces = 0;
        //For every card in the deck
        for(Card aCard : this.cards){
            //switch case for possible values
            switch(aCard.getValue()){
            case TWO: totalValue += 2; 
            break;
            case THREE: totalValue += 3; 
            break;
            case FOUR: totalValue += 4; 
            break;
            case FIVE: totalValue += 5; 
            break;
            case SIX: totalValue += 6; 
            break;
            case SEVEN: totalValue += 7; 
            break;
            case EIGHT: totalValue += 8; 
            break;
            case NINE: totalValue += 9; 
            break;
            case TEN: totalValue += 10; 
            break;
            case JACK: totalValue += 10; 
            break;
            case QUEEN: totalValue += 10; 
            break;
            case KING: totalValue += 10; 
            break;
            case ACE: aces += 1; 
            break;
            }           
        }
        
        //Determine the total current value with aces
        //Aces worth 11 or 1 - if 11 would go over 21 make it worth 1
        for(int i = 0; i < aces; i++){
            //if the user has more than 10, ace is 1, else it is 11 so they dont bust
            if (totalValue + 11 < 22){
                totalValue = totalValue + 11;
            }
            else if (totalValue + 11 > 21)
            {
                totalValue = totalValue + 1;
            }
        }
    
        //Return
        return totalValue;
    
    }
    
    
}
