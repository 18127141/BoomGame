package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.*;
import com.mygdx.game.Builder.WorldBuilder;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Sprites.Items;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.Walls;

public class MyScreen implements Screen {
    private Main game;
    Player player;
    private TextureAtlas atlas;

    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    public MyScreen(Main game){
        this.game = game;
        /////==============================================Create Box2d WORld==================================
        world = new World(new Vector2(),true);
        b2dr = new Box2DDebugRenderer();
        //Box2d World
        new WorldBuilder(world,game.mapp);

        //======================================================================

        player = new Player(world);


    }
    
    public void update(float dt){
        player.handleInput(game.controller);
        world.step(1/60f,6,2);
        game.cam.update();
        player.update(dt);
        game.renderer.setView(game.cam);
    }
    @Override
    public void show() {

    }

    @Override

    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        //=====|MAP RENDERER|===========
        game.batch.setProjectionMatrix(game.controller.cam.combined);

        game.renderer.render();
        //====|Box2d render|
        b2dr.render(world,game.cam.combined);

        game.batch.setProjectionMatrix(game.cam.combined);

        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
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
        world.dispose();
        b2dr.dispose();
    }
}
