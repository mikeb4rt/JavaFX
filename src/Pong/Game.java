package Pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Game extends Application {
    public static Bolla bolla = new Bolla(15, Color.BLUE);
    public static Paleta paleta1 = new Paleta(0,150);
    public static Paleta paleta2 = new Paleta(400 - 20, 150);
    public static Pane canvas;

    static class Paleta{
        class posicio{
            int posX;
            int posY;
            int puntuacio = 0;
            public posicio(int X, int Y){
                this.posX = X;
                this.posY = Y;
            }
        }
        Rectangle paleta;
        posicio posicio;
        int velocitat = 10;
        int puntuacio = 0;

        //Constructor
        public Paleta(int X, int Y){
            this.posicio =  new posicio(X,Y);
        }

        /**
         * Mou bolla cap amunt
         */
        public void mouAmunt() {
            posicio.posY=posicio.posY-this.velocitat;
            this.repinta();
            System.out.println("Amunt pitjat");
        }

        /**
         * Mou bolla cap abaix
         */
        public void mouAbaix() {
            posicio.posY=posicio.posY+this.velocitat;
            this.repinta();
            System.out.println("Abaix pitjat");
        }

        private void repinta(){
            this.paleta.setLayoutY(posicio.posY);
        }
    }

    static class Bolla {
        public double deltaX;
        public double deltaY;
        Circle cercle;

        public Bolla(int radi,Color color) {
            this.cercle=new Circle(radi, color);
            this.deltaX=1;
            this.deltaY=1;
        }
    }

    @Override
    public void start(final Stage primaryStage) {

        canvas = new Pane();
        final Scene escena = new Scene(canvas, 400, 400);

        primaryStage.setTitle("Bolla Rebotant");
        primaryStage.setScene(escena);
        primaryStage.show();

        int radi=15;
        bolla.cercle = new Circle(radi, Color.BLUE);
        bolla.cercle.relocate(200-radi, 200-radi);

        int amplada = 20;
        int altura = 70;
        paleta1.paleta = new Rectangle(amplada,altura,Color.RED);
        paleta1.paleta.relocate(amplada,150);

        paleta2.paleta = new Rectangle(amplada,altura, Color.RED);
        paleta2.paleta.relocate(400 - (amplada * 2),150);

        canvas.getChildren().addAll(bolla.cercle);
        canvas.getChildren().addAll(paleta1.paleta);
        canvas.getChildren().addAll(paleta2.paleta);

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
            double angle_en_radians =Math.toRadians(30);
            int velocitat=1;
            double deltaX = velocitat*Math.cos(angle_en_radians);
            double deltaY = velocitat*Math.sin(angle_en_radians);

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
                    // Multiplicam pel signe de deltaX per mantenir la trajectoria
                    bolla.cercle.relocate(200-radi, 200-radi);

                }
                if (alLimitInferior || alLimitSuperior) {
                    // Multiplicam pel signe de deltaX per mantenir la trajectoria
                    deltaY = Math.signum(deltaY);
                    deltaY *= -1;
                }
                if (impactePaleta){
                    deltaY *= Math.signum(deltaY);
                    deltaX *=-1;
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