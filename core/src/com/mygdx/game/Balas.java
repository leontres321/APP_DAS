package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class Balas {
    public Vector2 BalaPos = new Vector2(0,0);
    public Vector2 BalaVel = new Vector2(0,0);
    public static int ancho=8;
    public static int alto=8;
    public Balas(Vector2 Pos, Vector2 Vel){
        BalaPos = new Vector2(Pos.x + 15, Pos.y + 30);
        BalaVel = new Vector2(Vel.x, Vel.y);
    }

    public static int getAncho() {
        return ancho;
    }

    public static int getAlto() {
        return alto;
    }

    public void updateBala(){ //Solo sube
        BalaPos.y += BalaVel.y;
    }
}
