package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Balas {
    public Vector2 BalaPos = new Vector2(0,0);
    public Vector2 BalaVel = new Vector2(0,0);
    private int ancho;
    private int alto;

    public Balas(Vector2 Pos, Vector2 Vel){
        BalaPos = new Vector2(Pos.x + 15, Pos.y + 30);
        BalaVel = new Vector2(Vel.x, Vel.y);
        ancho=8;
        alto=8;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void updateBala(){ //Solo sube
        BalaPos.y += BalaVel.y;
    }
}
