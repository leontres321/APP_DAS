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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Extras.WorldContactListener;
import com.mygdx.game.MainGame;
import com.mygdx.game.Scenas.Hud;
import com.mygdx.game.Sprites.Jugador;

public class PlayScreen implements Screen {

    private MainGame game;

    private OrthographicCamera camaraGame;
    private Viewport gamePort; //Magia para diferences ratios de pantalla
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private TextureAtlas atlas;

    private World world;
    private Box2DDebugRenderer b2dr; //Muestra lineas para las colisiones

    private Jugador avionJugador;

    private Music music;
    public AssetManager manager;

    public PlayScreen(MainGame juego){
        this.game = juego;
        camaraGame = new OrthographicCamera();
        gamePort = new FitViewport(MainGame.V_Width / MainGame.PPM, MainGame.V_Height / MainGame.PPM, camaraGame); //Con esto matiene el ratio
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Stage.tmx"); //El mapa hecho de tiles, el mapa es mas grande asi que hacer otro o recortar o algo asi
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / MainGame.PPM);
        camaraGame.position.set(gamePort.getWorldWidth()/2 , gamePort.getWorldHeight()/2, 0); //0 porque tiene x y z
        atlas = new TextureAtlas("Todo.txt");

        world = new World(new Vector2(0,0),true); //true para objetos que no se mueven (sleep), no hace todos los calculos y ahorra calculos
        b2dr = new Box2DDebugRenderer();
        avionJugador = new Jugador(world, this);

        world.setContactListener(new WorldContactListener());
        manager = game.manager;
        //music = manager.get("Musica");

        //Creacion de las paredes, lo deje aqui por ser poco codigo por el momento, mover a otro cuando sea más
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
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }


    //TODO VER POSIBILIDAD DE PONER OBJETOS TRANSPARENTES COMO BORDES Y HACER QUE LA WEA DE AVION NO SALGA DE LA PANTALLA
    public void handleInput(float dt){
        if(avionJugador.b2body.getLinearVelocity().y <=1) {
            avionJugador.b2body.applyLinearImpulse(new Vector2(0, 0.1f), avionJugador.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (Gdx.input.getX() < MainGame.V_Width/2 && avionJugador.b2body.getLinearVelocity().x>=-2){
                avionJugador.b2body.applyLinearImpulse(new Vector2(-0.1f,0), avionJugador.b2body.getWorldCenter(), true);
            }
            if(Gdx.input.getX() > MainGame.V_Width/2 && avionJugador.b2body.getLinearVelocity().x<=2){
                avionJugador.b2body.applyLinearImpulse(new Vector2(0.1f,0), avionJugador.b2body.getWorldCenter(), true);
            }
        }
    }

    public void update(float dt){
        handleInput(dt); //ver si hay inputs

        if(avionJugador.vivo) hud.masPuntaje(1); //Puntaje por estar vivo para test y si se queda meh

        avionJugador.update(dt);

        world.step(1/60f, 6, 2); //60fps y los otros son weas que la documentacion menciona dejar asi pero igual se pueden cambiar

        camaraGame.position.y = avionJugador.b2body.getPosition().y+2; //esto arregla la camara al parecer

        camaraGame.update(); //update de la camarita
        renderer.setView(camaraGame); //solo renderisa lo que ve la camara
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
