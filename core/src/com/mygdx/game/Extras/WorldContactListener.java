package com.mygdx.game.Extras;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.MainGame;
import com.mygdx.game.Sprites.Balas;
import com.mygdx.game.Sprites.Jugador;
import com.mygdx.game.Sprites.kamikaze;

public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        //Con esto deberian ser suficientes
        switch (cDef){
            case MainGame.AVION_BIT | MainGame.KAMIKAZE_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.AVION_BIT){
                    ((Jugador)fixA.getUserData()).setDestruida();
                    ((kamikaze)fixB.getUserData()).setDestruida();
                }
                else{
                    ((Jugador)fixB.getUserData()).setDestruida();
                    ((kamikaze)fixA.getUserData()).setDestruida();
                }
                break;
            case MainGame.BALA_BIT | MainGame.KAMIKAZE_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.BALA_BIT){
                    ((Balas)fixA.getUserData()).setDestruida();
                    ((kamikaze)fixB.getUserData()).setDestruida();
                }
                else{
                    ((Balas)fixB.getUserData()).setDestruida();
                    ((kamikaze)fixA.getUserData()).setDestruida();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
