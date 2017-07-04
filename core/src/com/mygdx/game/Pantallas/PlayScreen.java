package com.mygdx.game.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Extras.WorldContactListener;
import com.mygdx.game.MainGame;
import com.mygdx.game.Scenas.Hud;
import com.mygdx.game.Sprites.Balas;
import com.mygdx.game.Sprites.Jugador;
import com.mygdx.game.Sprites.kamikaze;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    private MainGame game;

    //Camara y HUD
    private OrthographicCamera camaraGame;
    private Viewport gamePort; //Magia para diferences ratios de pantalla
    private Hud hud;

    //MAPA
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private TextureAtlas atlas;

    //Mundo y Debug
    private World world;
    private Box2DDebugRenderer b2dr; //Muestra lineas para las colisiones

    //Jugador y Enemigos?
    private Jugador avionJugador;
    private kamikaze enemigo;

    //"Musica"
    private Music music;
    public AssetManager manager;

    public PlayScreen(MainGame juego){
        //Inicializacion de las variables
        this.game = juego;
        camaraGame = new OrthographicCamera();
        gamePort = new FitViewport(MainGame.V_Width / MainGame.PPM, MainGame.V_Height / MainGame.PPM, camaraGame); //Con esto matiene el ratio
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Stage.tmx"); //El mapa hecho de tiles, el mapa es mas grande asi que hacer otro o recortar o algo asi
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / MainGame.PPM);
        camaraGame.position.set(gamePort.getWorldWidth()/2 , gamePort.getWorldHeight()/2, 0); //0 porque tiene x y z
        atlas = new TextureAtlas("Todo.txt");

        //Creacion del mundo y debug
        world = new World(new Vector2(0,0),true); //true para objetos que no se mueven (sleep), no hace todos los calculos y ahorra calculos
        b2dr = new Box2DDebugRenderer();
        avionJugador = new Jugador(world, this);

        //Para colisiones
        world.setContactListener(new WorldContactListener());
        manager = game.manager;
        //music = manager.get("Musica");

        //Creacion de las paredes, lo deje aqui por ser poco codigo por el momento, mover a otro si es mas
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rec = ((RectangleMapObject)object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rec.getX() + rec.getWidth()/2) / MainGame.PPM, (rec.getY() + rec.getHeight()/2) / MainGame.PPM);
            body = world.createBody(bodyDef);
            shape.setAsBox((rec.getWidth()/2) / MainGame.PPM, (rec.getHeight()/2) / MainGame.PPM);
            fdef.filter.categoryBits = MainGame.MURALLA_BIT;
            fdef.filter.maskBits = MainGame.AVION_BIT;
            fdef.shape = shape;
            fdef.friction = 0;
            body.createFixture(fdef);
        }

        enemigo = new kamikaze(this, .32f, .32f);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void handleInput(float dt){
        //Movimiento automatico hacia arriba del mapa
        if(avionJugador.b2body.getLinearVelocity().y <=1) {
            //avionJugador.b2body.applyLinearImpulse(new Vector2(0, 0.1f), avionJugador.b2body.getWorldCenter(), true);
            avionJugador.b2body.setLinearVelocity(new Vector2(0,1)); //Detiene el deslis de del avion
        }

        //Cuando se toca la pantalla pasa lo de aca
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (Gdx.input.getX() < MainGame.V_Width/2 && avionJugador.b2body.getLinearVelocity().x>=-2){
                //avionJugador.b2body.applyLinearImpulse(new Vector2(-0.1f,0), avionJugador.b2body.getWorldCenter(), true);
                avionJugador.b2body.setLinearVelocity(new Vector2(-2,1));
            }
            if(Gdx.input.getX() > MainGame.V_Width/2 && avionJugador.b2body.getLinearVelocity().x<=2){
                //avionJugador.b2body.applyLinearImpulse(new Vector2(0.1f,0), avionJugador.b2body.getWorldCenter(), true);
                avionJugador.b2body.setLinearVelocity(new Vector2(2,1));
            }
        }
    }

    public void update(float dt){
        //Inputs del jugador
        handleInput(dt);

        //Subida de puntitos por el hecho de estar vivo
        if(avionJugador.vivo) hud.masPuntaje(1); //Puntaje por estar vivo para test y si se queda meh

        //Update de la posicion del jugador
        avionJugador.update(dt);
        enemigo.update(dt);

        //Pasos del mundo (algo asi como fps)
        world.step(1/60f, 6, 2);

        //Posicion de la camara, se mueve segun el jugador
        camaraGame.position.y = avionJugador.b2body.getPosition().y+2;

        //Update de la camara y renderisa lo que ve
        camaraGame.update();
        renderer.setView(camaraGame);
    }

    public TiledMap getMap(){return tiledMap;}

    public World getWorld(){return world;}

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Logica separada del render
        update(delta);

        //Limpieza de pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderisa el mapa
        renderer.render();

        //render de debug
        b2dr.render(world, camaraGame.combined);

        game.batch.setProjectionMatrix(camaraGame.combined);
        game.batch.begin();
        avionJugador.draw(game.batch);
        enemigo.draw(game.batch);
        game.batch.end();

        //Para dibujar el hud
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined); //Magia para que se vean
        hud.stage.draw(); //Aca por fin se ve el hud

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        tiledMap.dispose();
        renderer.dispose();
        hud.dispose();
        //this.dispose(); ???????
    }
}
