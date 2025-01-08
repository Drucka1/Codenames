package codenames.controller;

import codenames.structure.Game;
import codenames.structure.PlayableCard;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameSoloController extends GameController{

    public GameSoloController(){
        super();
    }
    
    public GameSoloController(Game game){
        super(game);
    }

    void processCardSelection(PlayableCard card) {
        switch (card.getCardType()) {
            case Black:
                alertWrongGuest("Black Card selected, you lose");
                game.wrongGuess();
                game.ends();
                if (game.isBlueTurn()) info.setText("Red Team win");
                else info.setText("Blue Team win");
                button.setVisible(false);
                displayStatistics();
                break;
            case White:
                game.wrongGuess();
                alertWrongGuest("White Card selected, your turn ends");
                break;
            case Blue:
                if (game.isBlueTurn()) {
                    game.correctGuess();
                } else {
                    game.wrongGuess();
                    alertWrongGuest("Red Card selected, your turn ends");
                }
                break;
            case Red:
                if (!game.isBlueTurn()) {
                    game.correctGuess();
                } else {
                    game.wrongGuess();
                    alertWrongGuest("Blue Card selected, your turn ends");
                }
                break;
            default:
                break;
        }
        

        if (game.getNumberOfRemainingCardsToFind() == 0 && game.isOnGoing()){
            game.ends();
            displayStatistics();
            if (game.isBlueTurn()) info.setText("Blue Team win");
            else info.setText("Red Team win");
            button.setVisible(false);
        }
        else if (game.getRemainingCardGuess() == 0 && game.isBlueTurn() && game.isOnGoing()){
            // l'IA opponent joue
            // l'IA equipier joue
        }
    }

    @FXML 
    public void handleButton(){
        
        if (game.isBlueTurn()){
            askForNumberGuess().ifPresent( n -> {
                int N = Integer.parseInt(n);
                if (N > 0 && N <= game.getNumberOfOpponentRemainingCardsToFind()) game.changeTurn(N);
                else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Wrong Number Of Cards");
                    alert.setContentText("Please enter a number less than the number of cards you have left to guess");
                    alert.showAndWait();
                }
            });
        }
        
    }

}
