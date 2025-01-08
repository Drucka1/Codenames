package codenames.controller;

import java.io.IOException;

import codenames.structure.Game;
import codenames.structure.ImageCard;
import codenames.structure.PlayableCard;
import codenames.structure.TextCard;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public abstract class GameController {
    
    @FXML GridPane gridPane;
    @FXML Button button;
    @FXML Label info;
    @FXML ImageView imageView;

    protected Game game;

    protected LoadingBarController loadingBarController = null;

    public GameController(){}
    
    public GameController(Game game){
        this.game = game;
    }

    public void setLoadingBarController(LoadingBarController loadingBarController){
        this.loadingBarController = loadingBarController;
    }

    protected void handleTimerEnd(){
        if (game.isOnGoing()){
            game.setRemainingCardGuess(0);

        }
    }

    @FXML 
    public void initialize() {
        info.setText("Click above to start");
        int cols = game.getCols();
        final int[] currentPos = {0, 0};

        game.getDeck().forEach(playableCard -> {

            StackPane stackPane = new StackPane();

            if (playableCard.getCard() instanceof TextCard) {
                Label label = new Label(((TextCard) playableCard.getCard() ).getText());
                label.setTextFill(playableCard.getColor());
                
                gridPane.add(stackPane, currentPos[1], currentPos[0]);
                stackPane.getChildren().add(label);

                label.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        if (event instanceof MouseEvent) {
                            MouseEvent mouseEvent = (MouseEvent) event;
                            if (mouseEvent.getButton() == MouseButton.PRIMARY && !playableCard.isGuessed() && game.isOnGoing()) {
                                Rectangle transparency = new Rectangle(label.getWidth(),label.getHeight());
                                transparency.setFill(playableCard.getColor().deriveColor(0,1,1,0.5));
                                stackPane.getChildren().add(transparency);
                                playableCard.guessed();
                                processCardSelection(playableCard);
                            }
                        }
                    }
                });
                
            } else {
                ImageView imgView = new ImageView(new Image(((ImageCard) playableCard.getCard() ).getUrl()));

                gridPane.add(stackPane, currentPos[1], currentPos[0]);
                stackPane.getChildren().add(imgView);

                imgView.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        if (event instanceof MouseEvent) {
                            MouseEvent mouseEvent = (MouseEvent) event;
                            if (mouseEvent.getButton() == MouseButton.PRIMARY && !playableCard.isGuessed() && game.isOnGoing()) {
                                Rectangle transparency = new Rectangle(imgView.getFitWidth(),imgView.getFitHeight());
                                transparency.setFill(playableCard.getColor().deriveColor(0,1,1,0.5));
                                stackPane.getChildren().add(transparency);
                                playableCard.guessed();
                                processCardSelection(playableCard);
                            }
                        }
                    }
                });
                
                
            }   
            
            currentPos[1]++; 
            if (currentPos[1] >= cols) {
                currentPos[1] = 0;
                currentPos[0]++;  
            }
        });
    }

    public abstract void processCardSelection(PlayableCard card);

    protected void alertWrongGuest(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Wrong Card");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML public abstract void handleButton();

    protected void displayStatistics(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Statistics.fxml"));

        StatisticsController controller = new StatisticsController(game.getBlueStatistics(),game.getRedStatistics());

        loader.setController(controller);

        HBox hbox = null;
        try {
            hbox = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(hbox);

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setMaximized(false);
        newStage.setResizable(false);
        newStage.setTitle("Statistics");
        newStage.show();
    }
    
}
