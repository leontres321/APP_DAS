package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Pantallas.PlayScreen;

public class MainGame extends Game {
	//Pantalla virtual
    public static final int V_Width = 600;
    public static final int V_Height = 500;
    public static final float PPM = 100; //100 pixeles por metro

    //Box2D bit de colision
    public static final short NOTHING_BIT = 0;
    public static final short AVION_BIT = 1;
    public static final short KAMIKAZE_BIT = 2;
    public static final short BALA_BIT = 4;
    public static final short MURALLA_BIT = 8;
    public static final short DESTRUIDO_BIT = 16;


	public AssetManager manager;

    public SpriteBatch batch; //public para poder ser utilizado por todas las pantallas

	@Override
	public void create () {
        batch = new SpriteBatch();
        /*
        manager = new AssetManager();
        manager.load("Sonido/efectos/snd_explosion1.wav", Sound.class);
        manager.load("Sonido/efectos/snd_explosion2.wav", Sound.class);
        manager.finishLoading();
        */
        setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();//render a la pantalla activa
	}
	
	@Override
	public void dispose () {
        super.dispose();
		batch.dispose();
        //manager.dispose();
	}
}
