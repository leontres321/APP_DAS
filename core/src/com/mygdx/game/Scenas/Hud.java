package com.mygdx.game.Scenas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainGame;


public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport; //Esto es para que el hud sea independiente de todo

    private Integer Score;

    Label ScoreLabel; //cositas para poner en la pantalla, ver si tendra mas vidas

    public Hud(SpriteBatch sb){
        Score = 0;
        viewport = new FitViewport(MainGame.V_Width, MainGame.V_Height, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top(); // pa ponerlo arriba
        table.setFillParent(true);

        ScoreLabel = new Label(String.format("%06d", Score), new Label.LabelStyle(new BitmapFont(), Color.WHITE)); //El score solo tendra 6 digitos y es en blanco
        table.add(ScoreLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    public void masPuntaje(int puntos){
        Score += puntos;
        ScoreLabel.setText(String.format("%06d", Score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
