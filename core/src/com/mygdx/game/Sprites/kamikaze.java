package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MainGame;
import com.mygdx.game.Pantallas.PlayScreen;

public class kamikaze extends EnemigoVertical{
    private float stateTime;
    private TextureRegion normal;
    private Array<TextureRegion> Muerte;
    private boolean destruida;

    public kamikaze(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTime = 0;
        Muerte = new Array<TextureRegion>();
        destruida = false;
        for (int i = 0; i < 6; i++){
            Muerte.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), i * 16, 0, 32, 32)); // rev
        }
        normal = new TextureRegion(screen.getAtlas().findRegion("enemigo002"), 64 , 0, 32, 32); //rev
        setBounds(getX(), getY(), 32 / MainGame.PPM, 32 / MainGame.PPM);
        b2body.setLinearVelocity(new Vector2(0,3));
    }

    //Actualiza la posicion del avion
    public void update(float dt) {
        if (!destruida){
            stateTime += dt;
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(normal); //Rev
        }
        else world.destroyBody(b2body);
    }

    //Puntos por matar al avion
    public int puntos(){
        return 100;
    }

    public void setDestruida(){
        destruida = true;
    }

    public boolean getDestruida(){
        return destruida;
    }

    public void draw(Batch batch){
        if(!destruida) super.draw(batch);
    }

    @Override
    protected void defineEnemy(){
        BodyDef bdef = new BodyDef();
        bdef.position.set((MainGame.V_Width/2) / MainGame.PPM, 2);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / MainGame.PPM);
        fdef.filter.categoryBits = MainGame.KAMIKAZE_BIT;
        fdef.filter.maskBits = MainGame.AVION_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);
    }
}
