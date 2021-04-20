package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Sprites.Player;

public class LoadingScreen implements Screen {
    private Main game;
    Texture loadingBarBg;
    Texture loadingBarProcess;
    TextureRegion loadingbarStart;
    TextureRegion loadingbarMid;
    TextureRegion loadingbarEnd;

    public LoadingScreen(Main game){
        this.game = game;
        loadingBarProcess = GameManager.getAssetManager().get("loadingbar.png", Texture.class);
        loadingBarBg = GameManager.getAssetManager().get("loadingbarbg.png",Texture.class);

        loadingbarStart = new TextureRegion(loadingBarProcess,0,0,20, loadingBarProcess.getHeight());
        loadingbarMid = new TextureRegion(loadingBarProcess,20,0,460, loadingBarProcess.getHeight());
        loadingbarEnd = new TextureRegion(loadingBarProcess,480,0,20, loadingBarProcess.getHeight());

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();


        float initialPosX=Main.WIDTH/2;
        float initialPosY=Main.HEIGHT/2;
        game.batch.draw(loadingBarBg,initialPosX,initialPosY);
        game.batch.draw(loadingbarStart,initialPosX,         initialPosY);
        game.batch.draw(loadingbarMid,   initialPosX+loadingbarStart.getRegionWidth(),    initialPosY,loadingbarMid.getRegionWidth()*game.manager.getAssetManager().getProgress(), loadingbarMid.getRegionHeight());
        game.batch.draw(loadingbarEnd,initialPosX+loadingbarStart.getRegionWidth()+loadingbarMid.getRegionWidth()*GameManager.getAssetManager().getProgress(),
                initialPosY);

        game.batch.end();
        if (GameManager.getAssetManager().update())
        {


            game.setScreen(new Mainmenu(game));

        }

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
