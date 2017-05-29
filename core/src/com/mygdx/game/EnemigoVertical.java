package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class EnemigoVertical {
    public Vector2 posEnemigo;
    public Vector2 velEnemigo;
    private int alto=32;
    private int ancho=32;

    public EnemigoVertical(Vector2 Pos, Vector2 Vel){
        posEnemigo = new Vector2(Pos.x, Pos.y); //Apareceria arriba de la pantalla y luego saldria a verse
        velEnemigo = new Vector2(Vel.x, Vel.y);
    }

    public void updateEnemigo(){posEnemigo.y -= velEnemigo.y;}

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }
}
