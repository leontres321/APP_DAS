package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Pantallas.PlayScreen;

public class MainGame extends Game {
    public static final int V_Width = 600;
    public static final int V_Height = 500; //Tamaños virtuales de la pantalla, es test esto asi que se puede cambiar
    public static final float PPM = 100; //100 pixeles por metro
	public SpriteBatch batch; //public para poder ser utilizado por todas las pantallas

	public AssetManager manager;

	@Override
	public void create () {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
        manager = new AssetManager();
        //manager.load("Sonido/efectos/snd_explosion1.wav", Music.class);
//        manager.load("Sonido/efectos/snd_explosion1.wav", Sound.class);
  //      manager.load("Sonido/efectos/snd_explosion2.wav", Sound.class);
    //    manager.finishLoading();
	}

	@Override
	public void render () {
		super.render();//render a la pantalla activa
        manager.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        manager.dispose();
	}
}
