package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyScreen implements Screen {
    private Main game;

    MyScreen(Main game){
        this.game = game;
    }
    public void update(){
        game.cam.update();

        game.renderer.setView(game.cam);
    }
    @Override
    public void show() {

    }

    @Override

    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.agent.handleInput(game.controller);


//        game.batch.begin();
//
//        game.map.draw();
//        game.agent.draw();
//        game.batch.end();
        //=====|MAP RENDERER|===========
        game.batch.setProjectionMatrix(game.controller.cam.combined);

        game.renderer.render();

        game.controller.draw();


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
