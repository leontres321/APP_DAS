package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class BalasEnemigos {
    public Vector2 BalaEPos = new Vector2(0,0);
    public Vector2 BalaEVel = new Vector2(0,0);
    private int alto=32;
    private int ancho=32;

    public BalasEnemigos(Vector2 Pos, Vector2 Vel){
        BalaEPos = new Vector2(Pos.x + 15, Pos.y + 30); //Posicion del enemigo revisar
        BalaEVel = new Vector2(Vel.x, Vel.y);
    }

    public void updateBalaEnemigo(){ //Solo baja la bala debe ser mas fast
        BalaEPos.y -= BalaEVel.y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }
}

