package Blackjack;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Play extends JFrame implements ActionListener
{
    JFrame frame = new JFrame();

    Deck playingDeck = new Deck(); //deck of cards in use
    Deck playerCards = new Deck(); //player's (your) hand of cards
    Deck dealerCards = new Deck(); //dealer's hand of cards
    double amount;
    String betAmount;
    String betString = "       ";
    static private double wallet = 100;
    //if there isn't any name written in the name.txt file
    static JLabel playerLabel = new JLabel("                                                   Player Vs. Dealer");
    private JButton hitButton   = new JButton("Hit Me");
    private JButton stickButton = new JButton("Stick");
    private JButton dealButton  = new JButton("Deal");
    private JLabel playerDraw = new JLabel();
    private JLabel playerValue = new JLabel();
    private JLabel dealerValue = new JLabel();
    private JLabel dealerDraw = new JLabel();
    private JLabel status = new JLabel(" ", JLabel.CENTER);
    private JLabel currentMoney = new JLabel("You start with 100 dollars");
    private JTextField betText = new JTextField("enter bet");
    private JButton bet = new JButton("Bet!");
    private JButton doubleDown = new JButton("Double Down");
    private JLabel doubled = new JLabel(""); //display users new bet if they doubled down
    private static JButton exit = new JButton("Exit");
    double start;
    double startEndDiff;
    double end;
    static String str = ""; 

    Play() {
       
        // hit, stick, deal, bet, exit, double buttons and bet text box
        JPanel buttons = new JPanel();
        buttons.setPreferredSize (new Dimension ()); //layout for interface 
        buttons.setLayout (null); //lets me set where every label/button goes
        
        buttons.add(hitButton);      
        buttons.add(stickButton);       
        buttons.add(dealButton);
        buttons.add(bet);
        buttons.add(betText);
        buttons.add(exit);
        buttons.add(doubleDown);

        hitButton.addActionListener(this);
        stickButton.addActionListener(this);
        dealButton.addActionListener(this);
        exit.addActionListener(this);
        doubleDown.addActionListener(this);
        betText.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                betText.setText("");
            } //looked up how to use mouse listener so the text(bet) will clear on the text box when clicked
        });
        //if the user can click the button
        hitButton.setEnabled(false);
        stickButton.setEnabled(false);
        bet.setActionCommand("Bet");
        bet.addActionListener(this);
        bet.setEnabled(true);
        betText.setEnabled(true);
        doubleDown.setEnabled(false);
        
        //adds to panel which gets added to frame
        buttons.add(playerValue);
        buttons.add(doubled);
        buttons.add(dealerValue);
        buttons.add(playerDraw);
        buttons.add(dealerDraw);
        buttons.add(status);
        buttons.add(currentMoney);
        buttons.add(playerLabel);
        
        //.setBounds(x, y, width, length);
        hitButton.setBounds (20, 25, 100, 20);
        stickButton.setBounds (135, 25, 100, 20);
        dealButton.setBounds (250, 25, 100, 20);
        bet.setBounds (365, 25, 100, 20);
        doubleDown.setBounds(480, 25, 100, 20);
        betText.setBounds (595, 25, 100, 20);
        playerLabel.setBounds (115, 65, 365, 25);
        playerDraw.setBounds (10, 125, 645, 27);
        playerValue.setBounds (10, 160, 645, 27);
        dealerValue.setBounds (10, 195, 645, 27);
        dealerDraw.setBounds (10, 230, 645, 27);
        currentMoney.setBounds (250, 390, 185, 60);
        status.setBounds (5, 355, 645, 25);
        exit.setBounds (10, 400, 100, 20);
        doubled.setBounds(520, 65, 170, 20);
        
        frame.add(buttons);
        status.setForeground(Color.RED); //makes status label red
  
        //starts a timer when the game is first started that will run until the user quits
        start = System.currentTimeMillis();
    
        pack();
        frame.setSize(700, 500);
        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
        playingDeck.createFullDeck(); //creates a deck of 52 cards
        playingDeck.shuffle(); //shuffles cards 
        
    }
 
    //when exit button is pushed stopTimer will be called to stop the timer so the user knows how long they were playing
    //time played will be printed to the outfile, along with the final amount of money the user ended up with
    public void stopTimer() 
    {
        end = System.currentTimeMillis();
        startEndDiff = (end - start) / 1000; //total time played in seconds
    }

    // deal one card to given player from the deck
    private void hit(Deck playerCards) 
    {
      playerCards.draw(playingDeck);

    }

    // deal out two cards each, shuffling if too few cards
    private void deal() 
    {
      hit(playerCards);
      hit(dealerCards);
      hit(playerCards);
      hit(dealerCards);
    }
    //method that looks at what the player/dealer has and processes who won/lost and the players money
    public void checkWin()
    {
        //allows dealer to draw when total is less than 17
         while(dealerCards.cardsValue() < 17 && dealerCards.cardsValue() < 22)
            {
                dealerCards.draw(playingDeck);
                dealerDraw.setText("Dealer draws: " + dealerCards.getCard(dealerCards.deckSize() - 1).toString());
                dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
             }
         dealerDraw.setText("Dealers Final Hand:" + dealerCards.toString()); 
         dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
         if (playerCards.cardsValue() == dealerCards.cardsValue())
         {
            wallet = wallet;
            dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
            status.setText("Push(tie). You keep your money!");
            currentMoney.setText("You currently have $" + wallet);
         }
         else if ((playerCards.cardsValue() >  dealerCards.cardsValue()) && playerCards.cardsValue() < 22) 
         {
            wallet = wallet + amount;  
            dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
            status.setText("YOU WIN!!! You earn $" + amount + "!");
            currentMoney.setText("You currently have $" + wallet);
         }
         else if ((dealerCards.cardsValue() > playerCards.cardsValue()) && dealerCards.cardsValue() < 22)
         {
             wallet = wallet - amount;
             dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
             status.setText("Dealer beats you " + dealerCards.cardsValue() + " to " + playerCards.cardsValue() + ". DEALER WINS. You lose $" + amount + "!");
             currentMoney.setText("You currently have $" + wallet);
         }
    
         else if(dealerCards.cardsValue() >  21 && playerCards.cardsValue() < 22)    
         {           
            wallet = wallet + amount;
            dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
            status.setText("Dealer busts, YOU WIN. You earn $" + amount + "!");
            currentMoney.setText("You currently have $" + wallet);
         }
         else if(playerCards.cardsValue()  >  21)   
         {         
            wallet = wallet - amount;
            dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
            status.setText("You bust :(, dealer wins. You lose $" + amount + "!");
            currentMoney.setText("You currently have $" + wallet);
         }
       
         else         
         {                               
          status.setText("ERROR");
         }
            //places all the cards back in the deck for the next round
            playerCards.moveAllToDeck(playingDeck);
            dealerCards.moveAllToDeck(playingDeck);
            
            hitButton.setEnabled(false);
            stickButton.setEnabled(false);
            dealButton.setEnabled(true);
            betText.setEnabled(true); //user can place a new bet
            bet.setEnabled(true);
            doubleDown.setEnabled(false);
            betText.setText("enter bet");
    }
    
    // processes what happens when one of the buttons is pushed
    public void actionPerformed(ActionEvent event) 
    {
           if (event.getSource() == bet)
        {
            //looked up try/catch to make sure what the user types in is a string
            try{
                amount = Double.parseDouble(betText.getText().trim()); //parses text as an integer then converts to string
                String betAmount = Double.toString(amount);
                betString = betAmount;
            }
            catch(NumberFormatException exception){
               status.setText("Please enter a correct bet"); //if the user presses the bet button but never typed in a bet
               return;
            } 
            doubled.setText("");
        }       
        if (event.getSource() == dealButton) {
            deal();
            status.setText(" ");
            betText.setText(betString);
            playerDraw.setText("Your Hand:" + playerCards.toString()); 
            playerValue.setText("Current hand value: " +  playerCards.cardsValue());
            dealerValue.setText("Dealer Hand: " + dealerCards.getCard(0).toString() + " and [hidden]");
            status.setText(" ");
            dealerDraw.setText(" ");
            doubled.setText("");
            hitButton.setEnabled(true);
            stickButton.setEnabled(true);
            bet.setEnabled(true);
            betText.setEnabled(true);
            dealButton.setEnabled(false);
            doubleDown.setEnabled(true); //can only double down after user is first dealt their cards
            //if player is dealt blackjack 
            if(playerCards.cardsValue() == 21)
            {
                hitButton.setEnabled(false);
                stickButton.setEnabled(false);
                dealButton.setEnabled(true);
                betText.setEnabled(true);
                bet.setEnabled(true);
                doubleDown.setEnabled(false);
                status.setText("You have BLACKJACK!!! You earn $" + amount + "!");
                wallet = wallet + amount;
                currentMoney.setText("You currently have $" + wallet);
                dealerDraw.setText("Dealers Final Hand:" + dealerCards.toString()); 
                dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
                playerCards.moveAllToDeck(playingDeck);
                dealerCards.moveAllToDeck(playingDeck);
                betText.setText("enter bet");
            }
        }
        if(event.getSource() == doubleDown) //user's intial bet will be doubled and they will be dealt one more card
            {
                playerCards.draw(playingDeck);
                amount = amount * 2; //bet is doubled
                playerDraw.setText("Your Hand:" + playerCards.toString());
                playerValue.setText( "Current hand value: " +  playerCards.cardsValue());
                doubled.setText("Your bet is now $" + amount); //tell user what their new bet is     
                checkWin();
            }
        if (event.getSource() == hitButton) 
        {
            playerCards.draw(playingDeck);
            playerDraw.setText("Your Hand:" + playerCards.toString());
            playerValue.setText( "Current hand value: " +  playerCards.cardsValue());
            currentMoney.setText("");
            doubleDown.setEnabled(false);
            if(playerCards.cardsValue() > 21){
                //round will automatically end if player busts
                hitButton.setEnabled(false);
                stickButton.setEnabled(false);
                dealButton.setEnabled(true);
                //allow player to make new bet after busting
                betText.setEnabled(true);
                bet.setEnabled(true);
                doubleDown.setEnabled(false);
                status.setText("You bust :(, dealer wins. You lose $" + amount + "!");
                wallet = wallet - amount;
                currentMoney.setText("You currently have $" + wallet);
                dealerDraw.setText("Dealers Final Hand:" + dealerCards.toString()); 
                dealerValue.setText("Dealers hand value: " + dealerCards.cardsValue());
                playerCards.moveAllToDeck(playingDeck);
                dealerCards.moveAllToDeck(playingDeck);
                betText.setText("enter bet");
            }
        }
        if(event.getSource() == exit)
        {
            //print out to file the users ending amount of money and time played and exits the program when the user presses the exit button
            stopTimer(); //stops the ongoing timer
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream("README.TXT"));
                //PrintWriter out = new PrintWriter("README.TXT");
                out.printf("\n" + str + ": Total amount of money you ended with: $" + wallet + "\nTotal time played: " + startEndDiff + " seconds\n");    
                out.close();
                //resets for next time user plays
                startEndDiff = 0;
                start = 0;
                end = 0;
                System.exit(0); //quits out of the game
            } catch(FileNotFoundException ex) {
                System.out.println("ERROR PRINTING");
            }
        }
           if (event.getSource() == stickButton) 
        {
            checkWin();
        }  
    }
    // play Blackjack
    public static void main(String [] args) throws FileNotFoundException
    {
        new Play();
        Scanner in1 = new Scanner(new File("NAME.txt")); 
        // Open output file
        //reads file for name (only thing in it) and prints name to interface
       while (in1.hasNext())
       {
           str = in1.nextLine();
           playerLabel.setText("                                                   " + str + " Vs. Dealer");
        }
       in1.close(); 
    }
}