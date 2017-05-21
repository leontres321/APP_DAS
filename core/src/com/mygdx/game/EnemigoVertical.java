package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class EnemigoVertical {
    Vector2 posEnemigo;
    Vector2 velEnemigo;
    static int alto=32;
    static int ancho=32;

    public EnemigoVertical(Vector2 Pos, Vector2 Vel){
        posEnemigo = new Vector2(Pos.x, Pos.y); //Apareceria arriba de la pantalla y luego saldria a verse
        velEnemigo = new Vector2(Vel.x, Vel.y);
    }

    public void updateEnemigo(){posEnemigo.y -= velEnemigo.y;}

    public static int getAncho() {
        return ancho;
}

    public static int getAlto() {
        return alto;
    }
}
