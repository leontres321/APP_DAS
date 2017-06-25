package com.mygdx.game.Sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.MainGame;
import com.mygdx.game.Pantallas.PlayScreen;

public class kamikaze extends EnemigoVertical {

    public kamikaze(PlayScreen screen, float x, float y) {
        super(screen, x, y);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((MainGame.V_Width/2) / MainGame.PPM, 50/ MainGame.PPM); //temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / MainGame.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef); //Amor, alegria y sacrificios
        b2body.createFixture(fdef).setUserData("Enemigo");
    }
}
