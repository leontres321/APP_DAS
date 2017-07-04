package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MainGame;
import com.mygdx.game.Pantallas.PlayScreen;

public class Balas extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    private boolean destruida;
    private TextureRegion frame;

    public Balas(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        destruida = false;
        frame = new TextureRegion(screen.getAtlas().findRegion("bullet", -1)); //caaaasi se ve bien
        setRegion(frame);
        setBounds(x, y, 11 / MainGame.PPM, 22 / MainGame.PPM);
        defineBala();
    }

    public void setDestruida() {
        destruida = true;
    }

    public boolean getDestruida() {
        return destruida;
    }

    //TEST
    public void update(float dt) {
        if (!destruida) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        } else world.destroyBody(b2body);
    }

    protected void defineBala() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY() + 25 / MainGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4 / MainGame.PPM, 4 / MainGame.PPM); // deberia estar bien pero la fixture se ve mal
        fdef.filter.categoryBits = MainGame.BALA_BIT;
        fdef.filter.maskBits = MainGame.KAMIKAZE_BIT; //al salir de la pantalla deberiiiiiiiiia eliminarse nomas y no existir otro tipo de colision
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(0, 5)); // ver si se mueve mas lento que el avion
    }

    public void kill() {
        world.destroyBody(b2body);
    }
}