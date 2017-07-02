package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Pantallas.PlayScreen;

public abstract class EnemigoVertical extends Sprite{
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public EnemigoVertical(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        //velocity = new Vector2(0, 2);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
}
