package Pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Game extends Application {
    public static Pane canvas;
    public static Bolla bolla = new Bolla(15, Color.BLUE);
    public static Label punts;


    @Override
    public void start(final Stage primaryStage) {

        canvas = new Pane();
        final Scene escena = new Scene(canvas, 500, 500);
        Paleta paleta1 = new Paleta((int) canvas.getMaxWidth(), (int)canvas.getHeight() / 2);
        Paleta paleta2 = new Paleta((int)canvas.getHeight() - 20, (int) canvas.getHeight() / 2);

        primaryStage.setTitle("Pong");
        primaryStage.setScene(escena);
        primaryStage.show();

        int radi = 15;
        bolla.cercle = new Circle(radi, Color.BLUE);
        bolla.cercle.relocate((canvas.getHeight() / 2) - radi, (canvas.getWidth() / 2) - radi);

        int amplada = 20;
        int altura = 70;
        paleta1.paleta = new Rectangle(amplada, altura, Color.RED);
        paleta1.paleta.relocate(amplada, canvas.getHeight() / 2);

        paleta2.paleta = new Rectangle(amplada, altura, Color.RED);
        paleta2.paleta.relocate(canvas.getWidth() - (amplada * 2), canvas.getHeight() / 2);

        punts = new Label(paleta1.getPuntuacio() + " - " + paleta2.getPuntuacio());
        punts.relocate(canvas.getWidth() / 2,0);

        canvas.getChildren().addAll(bolla.cercle);
        canvas.getChildren().addAll(paleta1.paleta);
        canvas.getChildren().addAll(paleta2.paleta);
        canvas.getChildren().addAll(punts);

        primaryStage.show();
        canvas.requestFocus();
        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> paleta2.mouAmunt();
                case DOWN -> paleta2.mouAbaix();
                case W -> paleta1.mouAmunt();
                case S -> paleta1.mouAbaix();
            }
        });


        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(5), new EventHandler<ActionEvent>() {
            // Formula en radians
            //double deltaX = 3*Math.cos(Math.PI/3);
            //double deltaY = 3*Math.sin(Math.PI/3);

            // Formula en graus
            double angle_en_radians = Math.toRadians(30);
            int velocitat = 1;
            double deltaX = velocitat * Math.cos(angle_en_radians);
            double deltaY = velocitat * Math.sin(angle_en_radians);

            final Bounds limits = canvas.getBoundsInLocal();

            @Override
            public void handle(final ActionEvent t) {
                bolla.cercle.setLayoutX(bolla.cercle.getLayoutX() + deltaX);
                bolla.cercle.setLayoutY(bolla.cercle.getLayoutY() + deltaY);

                final boolean impactePaleta = bolla.cercle.getBoundsInParent().intersects(paleta1.paleta.getBoundsInParent()) || bolla.cercle.getBoundsInParent().intersects(paleta2.paleta.getBoundsInParent());
                final boolean alLimitDret = bolla.cercle.getLayoutX() >= (limits.getMaxX() + bolla.cercle.getRadius());
                final boolean alLimitEsquerra = bolla.cercle.getLayoutX() <= (limits.getMinX() - bolla.cercle.getRadius());
                final boolean alLimitInferior = bolla.cercle.getLayoutY() >= (limits.getMaxY() - bolla.cercle.getRadius());
                final boolean alLimitSuperior = bolla.cercle.getLayoutY() <= (limits.getMinY() + bolla.cercle.getRadius());

                if (alLimitDret || alLimitEsquerra) {
                    bolla.cercle.relocate((canvas.getHeight() / 2) - radi, (canvas.getWidth() / 2) - radi);
                    if (alLimitDret){
                        paleta1.golRebut();
                    }
                    if (alLimitEsquerra){
                        paleta2.golRebut();
                    }
                    punts.setText(paleta1.getPuntuacio() + " - " + paleta2.getPuntuacio());
                }
                if (alLimitInferior || alLimitSuperior) {
                    //Multiplicam pel signe de deltaY per mantenir la trajectoria
                    deltaY = Math.signum(deltaY);
                    deltaY *= -1;
                }
                if (impactePaleta) {
                    if (paleta1.paleta.getLayoutY() == bolla.cercle.getLayoutY() + radi || paleta2.paleta.getLayoutY() > bolla.cercle.getLayoutY() - radi){
                    deltaY *= Math.signum(deltaY);}
                    if (paleta1.paleta.getLayoutX() == bolla.cercle.getLayoutX() + radi || paleta2.paleta.getLayoutX() > bolla.cercle.getLayoutX() - radi){
                    deltaX *= -1;}
                }
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}