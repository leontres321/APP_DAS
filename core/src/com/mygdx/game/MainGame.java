package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture Fondo;
    private Texture Bullet;
    private Texture enemigoVertical;

    private Texture sprites;
    private TextureRegion[] AvionJugador = new TextureRegion[3];
    private TextureRegion[] ExplosionJugador = new TextureRegion[7];
    private TextureRegion[] ExplosionEnemigoVertical = new TextureRegion[6];
    //Ver animacion de las balas

    private float Alto; //Height
    private float Ancho; //width
    private Random random = new Random(); //Posicion de los aviones, se supone
    private int r; //Quitar esto ya que solo sirve para el random

    private Vector2 AvionPos = new Vector2(0, 0);

    private float tiempo = 0f;
    private float tiempoEnemigo = 0f;
    private float balaE = 0f;
    private int animacionAvion = 0; //Revisar si se puede eliminar alguna
    private int ayuda = 0;
    private boolean vivo = true;

    private ArrayList<Balas> BalasManager = new ArrayList<Balas>();
    private ArrayList<BalasEnemigos> BalasEManager = new ArrayList<BalasEnemigos>();
    private ArrayList<EnemigoVertical> EnemigoManager = new ArrayList<EnemigoVertical>();

    @Override
    public void create() {
        Ancho = Gdx.graphics.getWidth();
        Alto = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        Fondo = new Texture("Fondo2.png"); //Hacerlo potencia de 2
        Bullet = new Texture("bullet.png");
        enemigoVertical = new Texture("Enemigo1.png");

        sprites = new Texture("Sprites.png"); //todo men
        //Sprites de AvionJugador

        int i;
        int aux = 0;
        for (i = 0; i < 3; i++){
            AvionJugador[i] = new TextureRegion(sprites, aux , 396, 64, 64);
            aux += 66;
        }

        aux = 0;
        for (i = 0; i < 7; i++){
            ExplosionJugador[i] = new TextureRegion(sprites, aux, 297, 64,64);
            aux += 66;
        }

        aux = 66;
        for (i = 0; i < 6; i++){
            ExplosionEnemigoVertical[i] = new TextureRegion(sprites, aux, 165, 32, 32);
            aux += 33;
        }

        AvionPos = new Vector2(Ancho / 2 - AvionJugador[0].getRegionWidth() / 2, 20);
    }

    @Override
    public void render() {
        if (vivo){
            update(); //Hace que el avion se mueva y revisa si se presiona el boton menu
        }

        //Tiempos para las balas y salida de enemigos
        tiempo += Gdx.graphics.getDeltaTime();
        tiempoEnemigo += Gdx.graphics.getDeltaTime();
        balaE += Gdx.graphics.getDeltaTime(); // aun no se usa

        if (tiempo >= 0.1f){
            if (vivo){
                Balas nueva = new Balas(AvionPos, new Vector2(0,20));
                animacionAvion = (animacionAvion + 1 )%3;
                BalasManager.add(nueva);
            }
            ayuda = (ayuda + 1) %7;
            tiempo = 0f;
        }

        if (tiempoEnemigo >= 1f){ //Los aviones salen cada 2 segundos
            r = random.nextInt((int)(Ancho-32)); //Cambiar esto a lo de parametros instantaneos
            EnemigoVertical nuevo = new EnemigoVertical(new Vector2(r,Alto+32),new Vector2(0,15));
            EnemigoManager.add(nuevo);
            tiempoEnemigo = 0f;
        }

        //Limpiado de pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Dibujar en la pantalla
        batch.begin(); //Batch dibuja ensima por lo que el orden si importa
        batch.draw(Fondo, 0, 0,Ancho,Alto);  //La x se debe ir moviendo
        if (vivo){
            batch.draw(AvionJugador[animacionAvion], AvionPos.x, AvionPos.y); //X,Y del avion asi que la x es estatica
        }
        else{
            batch.draw(ExplosionJugador[ayuda], AvionPos.x, AvionPos.y);
        }
        moverEnemigo();
        colisionVertical();
        colisionJugador();
        //Otros tipos de colision aca
        balasJ();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        Fondo.dispose();
        Bullet.dispose();
        enemigoVertical.dispose();

        sprites.dispose(); //Este deberia ser el unico dispose

    }

    private void update() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (Gdx.input.getX() <= Ancho / 2 && AvionPos.x >= 0 - AvionJugador[0].getRegionWidth() / 3) {
                AvionPos.x -= 10;
            }
            //Aqui tiene que ir lo del menú
            /* Revisar si aca va de 0,0 izq inf o 0,0 izq superior
            if (Gdx.input.getX() >= Ancho - AnchoBoton && Gdx.input.getY() >= - AltoBoton){
            }*/
            if (Gdx.input.getX() > Ancho / 2 && AvionPos.x <= Ancho - 4 * AvionJugador[0].getRegionWidth() / 5) {
                AvionPos.x += 10;
            }
        }
    }

    private void balasJ(){
        int contador = 0;
        while (contador < BalasManager.size()){
            Balas actual = BalasManager.get(contador);
            actual.updateBala();

            if (actual.BalaPos.y < Alto){
                batch.draw(Bullet, actual.BalaPos.x, actual.BalaPos.y);
            }
            else {
                BalasManager.remove(contador);
                if (BalasManager.size() > 0){
                    contador--;
                }
            }
            contador++;
        }
    }

    private void moverEnemigo() {
        int contador = 0;
        while (contador < EnemigoManager.size()){
            EnemigoVertical actual = EnemigoManager.get(contador);
            actual.updateEnemigo();

            if (actual.posEnemigo.y > 0){
                batch.draw(enemigoVertical, actual.posEnemigo.x, actual.posEnemigo.y);
            }
            else{
                EnemigoManager.remove(contador);
                if (EnemigoManager.size() > 0){
                    contador--;
                }
            }
            contador++;
        }
    }

    private void colisionVertical(){
        for(int i =0;i<BalasManager.size();i++) {
            BalasManager.get(i);
            for (int j=0;j<EnemigoManager.size();j++) {
                if ((EnemigoManager.get(j).posEnemigo.x - BalasManager.get(i).getAncho() + 1) <= BalasManager.get(i).BalaPos.x && (EnemigoManager.get(j).posEnemigo.x + EnemigoManager.get(j).getAncho()+ BalasManager.get(i).getAncho() - 1) >= BalasManager.get(i).BalaPos.x) {
                    if(EnemigoManager.get(j).posEnemigo.y<=BalasManager.get(i).BalaPos.y) {
                        EnemigoManager.remove(j);
                        BalasManager.remove(i);
                    }
                }

            }
        }
    }

    private void colisionJugador(){
        for (int i = 0; i < EnemigoManager.size(); i++){

                if ((EnemigoManager.get(i).posEnemigo.x >= AvionPos.x) && (EnemigoManager.get(i).posEnemigo.x <= AvionPos.x + 64) && (EnemigoManager.get(i).posEnemigo.y <= AvionPos.y + 64)  && (EnemigoManager.get(i).posEnemigo.y >= AvionPos.y)){
                    vivo = false;
                }
        }

    }
}