package Pong;

import javafx.scene.shape.Rectangle;

public class Paleta {
    static class posicio {
        int posX;
        int posY;
        int puntuacio = 0;

        public posicio(int X, int Y) {
            this.posX = X;
            this.posY = Y;
        }

    }

    public int getPuntuacio() {
        return puntuacio;
    }

    public void golRebut(){
        this.puntuacio ++;
    }

    Rectangle paleta;
    posicio posicio;
    int velocitat = 10;
    int puntuacio = 0;

    //Constructor
    public Paleta(int X, int Y) {
        this.posicio = new posicio(X, Y);
    }


    /**
     * Mou bolla cap amunt
     */
    public void mouAmunt() {
        posicio.posY = posicio.posY - this.velocitat;
        this.repinta();
        System.out.println("Amunt pitjat");
    }

    /**
     * Mou bolla cap abaix
     */
    public void mouAbaix() {
        posicio.posY = posicio.posY + this.velocitat;
        this.repinta();
        System.out.println("Abaix pitjat");
    }

    private void repinta() {
        this.paleta.setLayoutY(posicio.posY);
    }
}
