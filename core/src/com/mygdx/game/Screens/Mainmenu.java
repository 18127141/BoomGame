package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;

public class Mainmenu implements Screen {
    private Main game;
    Texture background;
    Viewport viewport;
    Stage stage;
    Skin skin;
    public OrthographicCamera cam;
    public Mainmenu(Main game){
        this.game = game;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        background = GameManager.getAssetManager().get("title_background-little.png", Texture.class);
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        //=================================///////////

        Table Bg = new Table();
        Image bg = new Image(background);
        Bg.setFillParent(true);

        Bg.add(bg).size(Main.WIDTH,Main.HEIGHT) ;

        int t = 50;
        Label play = new Label("Play",skin,"title-plain");

        play.setAlignment(Align.center);
        play.setPosition(Main.WIDTH/2-play.getWidth()/2,200-t);

        Label option = new Label("Option",skin,"title-plain");

        option.setAlignment(Align.center);
        option.setPosition(Main.WIDTH/2-option.getWidth()/2,150-t);
        Label credit = new Label("Credit",skin,"title-plain");

        credit.setAlignment(Align.center);
        credit.setPosition(Main.WIDTH/2-credit.getWidth()/2,100-t);
        Label Exit = new Label("Exit",skin,"title-plain");

        Exit.setAlignment(Align.center);
        Exit.setPosition(Main.WIDTH/2-Exit.getWidth()/2,50-t);
        //===========///


        stage.addActor(Bg);
        stage.addActor(play);
        stage.addActor(option);
        stage.addActor(credit);
        stage.addActor(Exit);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

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

    }
}
