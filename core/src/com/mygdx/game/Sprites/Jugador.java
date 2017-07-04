package com.mygdx.game.Sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MainGame;
import com.mygdx.game.Pantallas.PlayScreen;

import java.util.ArrayList;

public class Jugador extends Sprite {
    private enum state {QUIETO, EXPLOSION, VACIO};
    private state actual;
    private state anterior;
    private World world;
    private PlayScreen screen;
    public Body b2body;
    private TextureRegion stands;
    private Animation<TextureRegion> quieto;
    private Animation<TextureRegion> explosion;
    private float relojEstado;
    public boolean vivo;
    private boolean destruida;

    //Balas
    private float reloj;
    private Array<Balas> balasManager = new Array<Balas>();

    //todo una funcion para que cuando muera suene la explosion, aparte de los sonidos de balas

    public Jugador (World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("avion_quieto"));
        this.world = world;
        this.screen = screen;
        vivo = true;
        actual = state.QUIETO;
        anterior = state.QUIETO;
        relojEstado = 0;
        int aux = 1;
        Array<TextureRegion>frames = new Array<TextureRegion>();

        //animaciones del avion
        for (int i = 0; i < 3; i++){
            frames.add(new TextureRegion(getTexture(), aux, 79, 62, 65));
            aux += 65;
        }
        quieto = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        aux = 46;
        for (int i = 0; i < 7; i++){
            frames.add(new TextureRegion(getTexture(), aux, 1, 64, 55));
            aux += 64;
        }
        explosion = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        stands = new TextureRegion(getTexture(), 451, 32, 64, 64);
        setBounds(0, 0, 64/ MainGame.PPM, 64 /MainGame.PPM);
        setRegion(stands);
        defineJugador();

        balasManager = new Array<Balas>();
    }

    //Mueve el avion
    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));

        //La parte de las balas
        reloj += dt;
        if (vivo && reloj >= 0.1f){
            balasManager.add(new Balas(screen, b2body.getPosition().x, b2body.getPosition().y));
            reloj = 0;
        }

        //Actualiza las balas y las elimina del array si mueren
        for(Balas bala : balasManager){
            bala.update(dt);
            //parche
            if (bala.b2body.getPosition().y - b2body.getPosition().y >= 4.5){
                //Se podria dejar en 3 y luego tener un power up para alargar la distancia de disparo, pero lo vas a programar tu?
                    bala.kill();
                    balasManager.removeValue(bala,true);
            }
            if(bala.getDestruida()) {
                bala.kill();
                balasManager.removeValue(bala,true);
            }
        }
    }

    public void draw (Batch batch){
        super.draw(batch);
        for (Balas bala : balasManager){
            bala.draw(batch);
        }
    }

    //Cambio de las animaciones del jugador
    public TextureRegion getFrame(float dt){
        actual = getState();
        TextureRegion region;
        switch (actual){
            case QUIETO:
                region = quieto.getKeyFrame(relojEstado, true); //true for loop
                break;
            default:
                region = explosion.getKeyFrame(relojEstado);
                if(explosion.isAnimationFinished(relojEstado)) world.destroyBody(b2body); //Ver si esto no da errores
                break;
        }
        relojEstado = actual == anterior ? relojEstado + dt: 0;
        anterior = actual;
        return region;
    }

    //Entrega el estado del jugador
    public state getState(){
        if (vivo) return state.QUIETO;
        return state.EXPLOSION;
    }

    //REV utilidad de estos
    public void setDestruida(){
        destruida = true;
    }

    public boolean getDestruida(){
        return destruida;
    }

    //Definicion del cuerpo en box2d y magia negra
    private void defineJugador(){
        BodyDef bdef = new BodyDef();
        bdef.position.set((MainGame.V_Width/2) / MainGame.PPM, 50/ MainGame.PPM); //temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / MainGame.PPM);
        fdef.filter.categoryBits = MainGame.AVION_BIT;
        fdef.filter.maskBits = MainGame.KAMIKAZE_BIT | MainGame.MURALLA_BIT; //no se, ya no se pibe
        fdef.shape = shape;
        b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);
    }
}
