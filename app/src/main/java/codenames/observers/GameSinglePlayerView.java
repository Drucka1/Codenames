package codenames.observers;

import codenames.structure.AI.*;

import codenames.structure.CardType;
import codenames.structure.GameSinglePlayer;
import codenames.structure.PlayableCard;
import javafx.fxml.FXML;
import javafx.geometry.Pos;

import java.util.Optional;
import java.util.Random;

import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameSinglePlayerView extends GameView {
    private AllyAI allyAI;
    private OpponentAI opponentAI;

    public GameSinglePlayerView(GameSinglePlayer game) {
        super(game);
        this.game.addObserver(this);
    }

    public void setAllyAI(AllyAI allyAI) {
        this.allyAI = allyAI;
    }

    public void setOpponentAI(OpponentAI opponentAI) {
        this.opponentAI = opponentAI;
    }

    public AI getAllyAI() {
        return allyAI;
    }

    public AI getOpponentAI() {
        return opponentAI;
    }

    public void alertAllyAIHint(String hint, int number) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Ally");
        alert.setContentText(
                "Your spymaster tells you the following hint: " + hint + " corresponding to " + number + " cards");
        alert.showAndWait();
    }

    public void processCardSelection(PlayableCard card) {
        if (game.isBlueTurn() && game.isOnGoing() && game.getRemainingCardGuess() > 0 && !card.isGuessed()) {

            String path = null;
            Random rand = new Random();

            switch (card.getCardType()) {
                case Black:
                    path = String.valueOf(getClass().getResource("/assassin.jpg"));
                    loadingBarView.stop();
                    game.wrongGuess(CardType.Black, loadingBarView.getElapsedSeconds());
                    game.ends();
                    button.setVisible(false);
                    displayStatistics();
                    break;
                case White:
                    if (rand.nextBoolean()) {
                        path = String.valueOf(getClass().getResource("/White1.jpg"));
                    } else {
                        path = String.valueOf(getClass().getResource("/White2.jpg"));
                    }
                    loadingBarView.stop();
                    game.wrongGuess(CardType.White, loadingBarView.getElapsedSeconds());
                    break;
                case Blue:
                    if (rand.nextBoolean()) {
                        path = String.valueOf(getClass().getResource("/Blue1.jpg"));
                    } else {
                        path = String.valueOf(getClass().getResource("/Blue2.jpg"));
                    }
                    game.correctGuess();
                    break;
                case Red:
                    if (rand.nextBoolean()) {
                        path = String.valueOf(getClass().getResource("/Red1.jpg"));
                    } else {
                        path = String.valueOf(getClass().getResource("/Red2.jpg"));
                    }
                    loadingBarView.stop();
                    game.wrongGuess(CardType.Red, loadingBarView.getElapsedSeconds());
                    break;
                default:
                    break;
            }

            ImageView cover = new ImageView(new Image(path));
            cover.fitHeightProperty().bind(card.getStackPane().heightProperty());
            cover.fitWidthProperty().bind(card.getStackPane().widthProperty());
            StackPane.setAlignment(cover, Pos.CENTER);
            card.getStackPane().getChildren().add(cover);
            

            card.guessed();
        }

        if (!game.isBlueTurn() && game.isOnGoing() && game.getRemainingCardGuess() > 0 && !card.isGuessed()) {

            String path = null;
            Random rand = new Random();

            switch (card.getCardType()) {
                case Black:
                    path = String.valueOf(getClass().getResource("/assassin.jpg"));
                    loadingBarView.stop();
                    game.wrongGuess(CardType.Black, loadingBarView.getElapsedSeconds());
                    game.ends();
                    button.setVisible(false);
                    displayStatistics();
                    break;
                case White:
                    if (rand.nextBoolean()) {
                        path = String.valueOf(getClass().getResource("/White1.jpg"));
                    } else {
                        path = String.valueOf(getClass().getResource("/White2.jpg"));
                    }
                    loadingBarView.stop();
                    game.wrongGuess(CardType.White, loadingBarView.getElapsedSeconds());
                    break;
                case Blue:
                    if (rand.nextBoolean()) {
                        path = String.valueOf(getClass().getResource("/Blue1.jpg"));
                    } else {
                        path = String.valueOf(getClass().getResource("/Blue2.jpg"));
                    }
                    loadingBarView.stop();
                    game.wrongGuess(CardType.Blue, loadingBarView.getElapsedSeconds());
                    break;
                case Red:
                    if (rand.nextBoolean()) {
                        path = String.valueOf(getClass().getResource("/Red1.jpg"));
                    } else {
                        path = String.valueOf(getClass().getResource("/Red2.jpg"));
                    }
                    game.correctGuess();
                    break;
                default:
                    break;
            }

            ImageView cover = new ImageView(new Image(path));
            cover.fitHeightProperty().bind(card.getStackPane().heightProperty());
            cover.fitWidthProperty().bind(card.getStackPane().widthProperty());
            StackPane.setAlignment(cover, Pos.CENTER);
            card.getStackPane().getChildren().add(cover);

            card.guessed();

        }
        if (game.getRemainingCardGuess() == 0 && game.isOnGoing() && game.isBlueTurn()) {

            game.changeTurn(0);
            opponentAI.play();

        } else if (game.getNumberOfRemainingCardsToFind() == 0 && game.isOnGoing()) {
            game.ends();
            displayStatistics();
            button.setVisible(false);
        }
        game.notifyObservers();
    }

    @FXML
    public void handleButton() {
        if (game.getRemainingCardGuess() == 0) {
            allyAI.play();
            askForNumberGuess().ifPresent(n -> {
                int N = Integer.parseInt(n);
                if (N > 0 && N <= game.getNumberOfOpponentRemainingCardsToFind())
                    game.changeTurn(N);
                else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Wrong Number Of Cards");
                    alert.setContentText("Please enter a number less than the number of cards you have left to guess");
                    alert.showAndWait();
                }
                loadingBarView.start();
            });
        }
        game.notifyObservers();
    }

    private Optional<String> askForNumberGuess() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Number of guess");
        dialog.setHeaderText("Enter the number of guess");
        dialog.setContentText("Number :");

        return dialog.showAndWait();
    }

    public void alertOpponentAIMove(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Opponent");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
