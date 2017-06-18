package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
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

public class Jugador extends Sprite { //meh no actor por el momento
    public enum state {QUIETO, EXPLOSION, VACIO};
    public state actual;
    public state anterior;
    public World world;
    public Body b2body;
    private TextureRegion stands;
    private Animation<TextureRegion> quieto;
    private Animation<TextureRegion> explosion;
    private float relojEstado;
    public boolean vivo;

    //todo una funcion para que cuando muera suene la explosion, aparte de los sonidos de balas

    public Jugador (World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("avion_quieto"));
        this.world = world;
        vivo = true;
        actual = state.QUIETO;
        anterior = state.QUIETO;
        relojEstado = 0;
        int aux = 1;
        Array<TextureRegion>frames = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++){
            frames.add(new TextureRegion(getTexture(), aux, 79, 62, 65)); //TEST
            aux += 65;
        }
        quieto = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        aux = 46;
        for (int i = 0; i < 7; i++){
            frames.add(new TextureRegion(getTexture(), aux, 1, 64, 55)); //Con más Y se ve lo de abajo
            aux += 64;
        }
        explosion = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        stands = new TextureRegion(getTexture(), 451, 32, 64, 64); //Ver si no corta otras cosas
        setBounds(0, 0, 64/ MainGame.PPM, 64 /MainGame.PPM);
        setRegion(stands);
        defineJugador();

    }

    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        actual = getState();

        TextureRegion region;
        switch (actual){
            case QUIETO:
                region = quieto.getKeyFrame(relojEstado, true); //true for loop
                break;
            case VACIO:
                region = new TextureRegion(); //Ver si funciona
                break;
            default:
                region = explosion.getKeyFrame(relojEstado);
                break;
        }
        relojEstado = actual == anterior ? relojEstado + dt: 0;
        anterior = actual;
        return region;
    }

    public state getState(){
        if (vivo) return state.QUIETO; //es la animacion de andar nomas
        //Ver como meter VACIO POR ACA
        return state.EXPLOSION; //muricio, ver como no hace na
    }

    private void defineJugador(){
        BodyDef bdef = new BodyDef();
        bdef.position.set((MainGame.V_Width/2) / MainGame.PPM, 50/ MainGame.PPM); //temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / MainGame.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef); //definicion del cuerpo y emmm... no se que mas... magia negra
        b2body.createFixture(fdef).setUserData("Jugador");
    }
}
