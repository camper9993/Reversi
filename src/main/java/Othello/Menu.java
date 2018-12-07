package Othello;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


class Menu {

    private Pane pane = new Pane();
    private Button bot = new Button("Play with bot");
    private Button play = new Button("1 VS 1");
    private Button exit = new Button("Exit");
    static boolean botwork = false;

    Menu() {
        setPlay();
        setExit();
        setBot();
        pane.setPrefSize(720,640);
        pane.setStyle("-fx-background-color: #188018");
        pane.getChildren().addAll(bot,exit,play);
    }

    private void setExit() {
        exit.setLayoutX(340);
        exit.setLayoutY(340);
        exit.setOnMouseClicked(e -> System.exit(0));
    }
    private void setBot() {
        bot.setLayoutX(340);
        bot.setLayoutY(300);
        bot.setOnMouseClicked(e -> {
            pane.toBack();
            botwork = true;
        });
    }

    private void setPlay() {
        play.setLayoutX(340);
        play.setLayoutY(260);
        play.setOnMouseClicked(e -> pane.toBack());
    }

    Pane getMenu() {
        return pane;
    }
}
