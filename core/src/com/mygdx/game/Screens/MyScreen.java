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
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mygdx.game.Builder.WorldBuilder;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Main;
import com.mygdx.game.Sprites.Boom;
import com.mygdx.game.Sprites.Items;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.Walls;

public class MyScreen implements Screen {
    int c=0;
    private Main game;
    Player player;
    private TextureAtlas atlas;
    public Array<Boom> BoomList;
    public Array<Items> BoxList;
    public Array<Walls> WallList;
    public Array<Player> PlayerList;
    public static short PLAYER, ITEMS, BOOM , Wall;
    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    public static String mapName="Forest";
    float deltatime=0;
    //Room


    //=========================Tiled Map/////=====================

    public TmxMapLoader mapLoader;
    public TiledMap mapp;
    public OrthogonalTiledMapRenderer renderer;
    public MyScreen(Main game){
        this.game = game;

        mapLoader = new TmxMapLoader();
        setMap(mapName);

        //=================        AddplayertoRoom====================

        //==================

        PLAYER = 2;
        ITEMS = 4;
        BOOM = 8;
        Wall = 16;
        game.controller = new Controller();
        BoomList = new Array<Boom>();
        BoxList = new Array<Items>();
        WallList = new Array<Walls>();
        PlayerList = new Array<Player>();
        /////==============================================Create Box2d WORld==================================
        world = new World(new Vector2(),true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);
        //Box2d World
        new WorldBuilder(world,mapp,BoxList,WallList);
        //======================================================================
        initPlayer();


    }
    public void getPlayerfromDataBase(){

        game.db.db.child("rooms/"+game.roomname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (!dataSnapshot.getName().equals(game.playerName)){

                    PlayerList.add(new Player(world,BoomList,(double)dataSnapshot.child("x").getValue(),(double)dataSnapshot.child("y").getValue(),dataSnapshot.getName(),false));

                }
                else {
                    player = new Player(world,BoomList,(double)dataSnapshot.child("x").getValue(),(double)dataSnapshot.child("y").getValue(),dataSnapshot.getName(),true);

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getName().equals(game.playerName)){
                    //get the right Player to update
                    int index=0;
                    for (int i=0;i<PlayerList.size;i++){
                        Player p = PlayerList.get(i);
                        if (p.name.equals(dataSnapshot.getName())) break;
                        index++;
                    }
//                    for (DataSnapshot data: dataSnapshot.getChildren()){
//                        System.out.println("hehe "+data.getValue());
//                    }
                    if ( dataSnapshot.child("State").getValue().equals("Run")){
                        PlayerList.get(index).state= Player.State.Run;
                    }
                    else{
                        PlayerList.get(index).state= Player.State.Stand;

                    }
                    if (dataSnapshot.child("prevState").getValue().equals("Stand")){
                        PlayerList.get(index).previous= Player.State.Stand;
                    }else
                    {
                        PlayerList.get(index).previous= Player.State.Run;
                    }

//                    PlayerList.get(index).state= (Player.State) dataSnapshot.child("State").getValue();
//                    PlayerList.get(index).previous= (Player.State) dataSnapshot.child("prevState").getValue();
                    double x,y;
                    x=(double) dataSnapshot.child("x").getValue();
                    y=(double) dataSnapshot.child("y").getValue();
                    PlayerList.get(index).x=x;
                    PlayerList.get(index).y=y;
                    long direction;
                    direction=(long)dataSnapshot.child("direction").getValue();
                   PlayerList.get(index).direction=direction;
                    if ( dataSnapshot.child("action").getValue().equals("Planted")){
                        BoomList.add( new Boom(world, new Vector2((float)x,(float)y),(int)direction,PlayerList.get(index).Power));
                    }


                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void GetData(){
        game.db.db.child("rooms/"+game.roomname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void initPlayer(){
        player = new Player(world,BoomList,Main.posx[c],Main.poxy[c],game.playerName,true);
        getPlayerfromDataBase();
        GetData();
    }
    public void setMap(String map){
        mapp = mapLoader.load("map/"+mapName+".tmx");
        renderer = new OrthogonalTiledMapRenderer(mapp,1/Main.PPM);

    }

    public void update(float dt){
        deltatime=dt;
        for (int i=0;i<PlayerList.size;i++){
            Player temp=PlayerList.get(i);
            temp.updateother(dt);
        }
        player.handleInput(game.controller,game);
        //PlayerList.get(index).handleInput(game.controller);
        world.step(1/60f,6,2);
        game.cam.update();

        player.update(dt);
        //update the firebase
        updatefirebase();

        //


        renderer.setView(game.cam);
    }
    public void updatefirebase(){
        String a,b;
        if (player.state== Player.State.Stand) a="Stand";
        else a="Run";
        if (player.previous==Player.State.Run) b="Run";
        else b="Stand";
        game.db.SetPlayerXY(game.roomname,game.playerName,player.b2body.getPosition().x,player.b2body.getPosition().y,"Action",a,b,(int)player.direction);
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
        for (int i=0;i<BoomList.size;i++){
            Boom item = BoomList.get(i);
            item.draw(game.batch);
        }
        for (int i=0;i<BoxList.size;i++){
            Items item = BoxList.get(i);
            item.draw(game.batch);
        }
        for (int i=0;i<PlayerList.size;i++){
            Player temp=PlayerList.get(i);
            temp.draw(game.batch);
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
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        mapp.dispose();
        renderer.dispose();
        game.db.deletePlayerfromRoom("Test",game.playerName);
    }
}
