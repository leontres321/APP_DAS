package com.mygdx.game.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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

    public PlayScreen(MainGame juego, AssetManager manager1){
        this.game = juego;
        camaraGame = new OrthographicCamera();
        gamePort = new FitViewport(MainGame.V_Width / MainGame.PPM, MainGame.V_Height / MainGame.PPM, camaraGame); //Con esto matiene el ratio
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Stage2.tmx"); //El mapa hecho de tiles, el mapa es mas grande asi que hacer otro o recortar o algo asi
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / MainGame.PPM);
        camaraGame.position.set(gamePort.getWorldWidth()/2 , gamePort.getWorldHeight()/2, 0); //0 porque tiene x y z
        atlas = new TextureAtlas("Todo.txt");

        world = new World(new Vector2(0,0),true); //true para objetos que no se mueven (sleep), no hace todos los calculos y ahorra calculos
        b2dr = new Box2DDebugRenderer();
        avionJugador = new Jugador(world, this);

        world.setContactListener(new WorldContactListener());
        manager = manager1;
        //music = manager.get("Musica");
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }


    //TODO VER POSIBILIDAD DE PONER OBJETOS TRANSPARENTES COMO BORDES Y HACER QUE LA WEA DE AVION NO SALGA DE LA PANTALLA
    public void handleInput(float dt){//TESTT
        System.out.println("X: "+ avionJugador.b2body.getPosition().x + " Y:" + avionJugador.b2body.getPosition().y);
        //entre 0.3 y 5.algo X es la pantalla, esto debe estar en metros por box2d
        //entre 0.5 y 29.5 Y es hasta donde deberia moverse la pantalla y quedar quieta para el boss o algo asi
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && avionJugador.b2body.getLinearVelocity().x>=-2){
            avionJugador.b2body.applyLinearImpulse(new Vector2(-0.1f,0), avionJugador.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && avionJugador.b2body.getLinearVelocity().x<=2){
            avionJugador.b2body.applyLinearImpulse(new Vector2(0.1f,0), avionJugador.b2body.getWorldCenter(), true);
        }
        //TODO VER SI ES EFECTIVAMENTE EL FINAL DEL MAPA
        if(avionJugador.b2body.getPosition().y <= 25 && avionJugador.b2body.getLinearVelocity().y<=1){
            avionJugador.b2body.applyLinearImpulse(new Vector2(0,0.1f), avionJugador.b2body.getWorldCenter(), true);
        }
        if(avionJugador.b2body.getPosition().y > 25) {
            avionJugador.b2body.setLinearVelocity(0, 0); //Esto hace que la camara se detenga pero no puedes moverte
        }
        /* para test
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && avionJugador.b2body.getLinearVelocity().y<=2){
            avionJugador.b2body.applyLinearImpulse(new Vector2(0,0.1f), avionJugador.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && avionJugador.b2body.getLinearVelocity().y >=-2){
            avionJugador.b2body.applyLinearImpulse(new Vector2(0,-0.1f), avionJugador.b2body.getWorldCenter(), true);
        }*/
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && avionJugador.b2body.getLinearVelocity().x>=-2) {
            if (Gdx.input.getX() < MainGame.V_Width && avionJugador.getWidth() /2  >= 0){
                avionJugador.b2body.applyLinearImpulse(new Vector2(-0.1f,0), avionJugador.b2body.getWorldCenter(), true);
                System.out.println("X: "+ Gdx.input.getX());
            }
        }
    }

    public void update(float dt){
        handleInput(dt); //ver si hay inputs

        if(avionJugador.vivo) hud.masPuntaje(1); //Puntaje por estar vivo para test y si se queda meh

        avionJugador.update(dt);

        world.step(1/60f, 6, 2); //60fps y los otros son weas que la documentacion menciona dejar asi pero igual se pueden cambiar

        /*Test movimiento de camara, hay que poner algunos otros parametros como el inicio de la camara y el final
        Si no se hace aca entonces que se haga con el mismo jugador*/
        camaraGame.position.y = avionJugador.b2body.getPosition().y+2; //esto arregla la camara al parecer

        camaraGame.update(); //update de la camarita
        renderer.setView(camaraGame); //solo renderisa lo que ve la camara
    }

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
