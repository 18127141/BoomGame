package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Builder.WorldBuilder;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Main;
import com.mygdx.game.Sprites.Boom;
import com.mygdx.game.Sprites.Items;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.Walls;

public class MyScreen implements Screen {
    private Main game;
    Player player;
    private TextureAtlas atlas;
    public Array<Boom> BoomList;
    public Array<Items> BoxList;
    public Array<Walls> WallList;
    public static short PLAYER, ITEMS, BOOM;
    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private String mapName="Forest";

    //=========================Tiled Map/////=====================

    public TmxMapLoader mapLoader;
    public TiledMap mapp;
    public OrthogonalTiledMapRenderer renderer;
    public MyScreen(Main game){

        mapLoader = new TmxMapLoader();
        setMap(mapName);

        //mapp = mapLoader.load("map/Temple.tmx");

        renderer = new OrthogonalTiledMapRenderer(mapp,1/Main.PPM);

        PLAYER = 2;
        ITEMS = 4;
        BOOM = 8;
        this.game = game;
        game.controller = new Controller();
        BoomList = new Array<Boom>();
        BoxList = new Array<Items>();
        WallList = new Array<Walls>();
        /////==============================================Create Box2d WORld==================================
        world = new World(new Vector2(),true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);
        //Box2d World
        new WorldBuilder(world,mapp,BoxList,WallList);
        //======================================================================
        player = new Player(world,BoomList);


    }
    public void setMap(String map){
        mapp = mapLoader.load("map/"+mapName+".tmx");
    }

    public void update(float dt){
        player.handleInput(game.controller);
        world.step(1/60f,6,2);
        game.cam.update();

        player.update(dt);


        renderer.setView(game.cam);
    }
    public void BoomCountDown(float delta){

        for (int i=0; i< BoomList.size;i++){
            Boom Temp = BoomList.get(i);
            Temp.Time-=1;
            Temp.update(delta);

            if (Temp.Time == 50) {
                Temp.Destroy(player, BoxList, WallList);
            }

            if (Temp.Time == 0) {
                BoomList.removeValue(Temp,true);
                i--;
            }
            if (Temp.Time == 150) {
                Temp.fdef.filter.maskBits = MyScreen.PLAYER;
                Temp.b2body.createFixture(Temp.fdef);
            }

        }
        for (int i=0; i< BoxList.size;i++){
            Items Temp = BoxList.get(i);
            if (Temp.isDestroy) {
                Temp.Time-=1;

            }

            Temp.update(delta);



            if (Temp.Time == 0) {
                BoxList.removeIndex(i);
                i--;
            }


        }
    }
    @Override
    public void show() {

    }

    @Override

    public void render(float delta) {
        update(delta);
        BoomCountDown(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        //=====|MAP RENDERER|===========
        game.batch.setProjectionMatrix(game.controller.cam.combined);

        renderer.render();
        //====|Box2d render|
        b2dr.render(world,game.cam.combined);

        game.batch.setProjectionMatrix(game.cam.combined);

        game.batch.begin();
        for (Boom boom: BoomList ){
            boom.draw(game.batch);
        }
        for (Items item: BoxList){

            item.draw(game.batch);
        }
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
        mapp.dispose();
        renderer.dispose();
    }
}
