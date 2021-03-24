package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.mygdx.game.Sprites.Player;

public class MyScreen implements Screen {
    private Main game;
    Player player;

    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    public MyScreen(Main game){

        this.game = game;

        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //Render the items
        for (MapObject object: game.mapp.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/Main.PPM,(rect.getY()+rect.getHeight()/2)/Main.PPM);


            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/Main.PPM,rect.getHeight()/2/Main.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        //Render the walls
        for (MapObject object: game.mapp.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/Main.PPM,(rect.getY()+rect.getHeight()/2)/Main.PPM);


            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/Main.PPM,rect.getHeight()/2/Main.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //
        player = new Player(world);
    }
    public void update(){
        player.handleInput(game.controller);
        world.step(1/60f,6,2);
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
        //====|Box2d render|
        b2dr.render(world,game.cam.combined);

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
