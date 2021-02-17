package Pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bolla {
        public double deltaX;
        public double deltaY;
        Circle cercle;

        public Bolla(int radi, Color color) {
            this.cercle = new Circle(radi, color);
            this.deltaX = 1;
            this.deltaY = 1;
        }
}

