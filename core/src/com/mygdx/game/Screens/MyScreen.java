package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.mygdx.game.Builder.WorldBuilder;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Sprites.Boom;
import com.mygdx.game.Sprites.Items;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.Walls;

public class MyScreen implements Screen {
    int c = 0;
    Stage stage;
    Skin skin;
    private Main game;
    Player player;
    ChildEventListener listener;

    private TextureAtlas atlas;
    public Array<Boom> BoomList;
    public Array<Items> BoxList;
    public Array<Walls> WallList;
    public Array<Player> PlayerList;
    public static short PLAYER, ITEMS, BOOM, Wall;
    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    public static String mapName = "Forest";
    float deltatime = 0;
    //Room


    //=========================Tiled Map/////=====================

    public TmxMapLoader mapLoader;
    public TiledMap mapp;
    public OrthogonalTiledMapRenderer renderer;
    //music
    private Music music;

    public MyScreen(Main game,String map) {
        this.game = game;
        mapName=map;
        mapLoader = new TmxMapLoader();
        setMap(mapName);
        //=================        AddplayertoRoom====================

        //==================

        PLAYER = 2;
        ITEMS = 4;
        BOOM = 8;
        Wall = 16;
        //important
        game.controller = new Controller();
        stage = game.controller.stage;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        //
        BoomList = new Array<Boom>();
        BoxList = new Array<Items>();
        WallList = new Array<Walls>();
        PlayerList = new Array<Player>();
        /////==============================================Create Box2d WORld==================================
        world = new World(new Vector2(), true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);
        //Box2d World
        new WorldBuilder(world, mapp, BoxList, WallList);
        //======================================================================
        initPlayer();
        music = GameManager.getAssetManager().get("music/"+mapName+".mp3",Music.class);
        music.setLooping(true);
        music.play();


    }

    public void getPlayerfromDataBase() {

        listener = game.db.db.child("rooms/" + game.roomname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getName().equals("_RoomStatus") &&!dataSnapshot.getName().equals("_RoomMap")) {
                    if (!dataSnapshot.getName().equals(game.playerName)) {

                        PlayerList.add(new Player(world, BoomList, (double) dataSnapshot.child("x").getValue(), (double) dataSnapshot.child("y").getValue(), dataSnapshot.getName(), false));

                    } else {
                        player = new Player(world, BoomList, (double) dataSnapshot.child("x").getValue(), (double) dataSnapshot.child("y").getValue(), dataSnapshot.getName(), true);

                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getName().equals("_RoomStatus") &&!dataSnapshot.getName().equals("_RoomMap")) {
                    if (!dataSnapshot.getName().equals(game.playerName)) {
                        //get the right Player to update
                        int index = 0;
                        for (int i = 0; i < PlayerList.size; i++) {
                            Player p = PlayerList.get(i);
                            if (p.name.equals(dataSnapshot.getName())) break;
                            index++;
                        }

                        if (dataSnapshot.child("State").getValue().equals("Run")) {
                            PlayerList.get(index).state = Player.State.Run;
                        } else {
                            PlayerList.get(index).state = Player.State.Stand;

                        }
                        if (dataSnapshot.child("prevState").getValue().equals("Stand")) {
                            PlayerList.get(index).previous = Player.State.Stand;
                        } else {
                            PlayerList.get(index).previous = Player.State.Run;
                        }

//                    PlayerList.get(index).state= (Player.State) dataSnapshot.child("State").getValue();
//                    PlayerList.get(index).previous= (Player.State) dataSnapshot.child("prevState").getValue();
                        double x, y;
                        x = (double) dataSnapshot.child("x").getValue();
                        y = (double) dataSnapshot.child("y").getValue();
                        PlayerList.get(index).x = x;
                        PlayerList.get(index).y = y;
                        long direction;
                        direction = (long) dataSnapshot.child("direction").getValue();
                        PlayerList.get(index).direction = direction;
                        if (dataSnapshot.child("action").getValue().equals("Planted")) {
                            BoomList.add(new Boom(world, new Vector2((float) x, (float) y), (int) direction, PlayerList.get(index).Power));
                        }


                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.getName().equals("_RoomStatus") &&!dataSnapshot.getName().equals("_RoomMap") ) {
                    for (int i=0;i<PlayerList.size;i++){
                        Player temp= PlayerList.get(i);
                        if (temp.name.equals(dataSnapshot.getName())){
                            PlayerList.removeIndex(i);
                            break;
                        }
                    }

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void initPlayer() {
        //player = new Player(world,BoomList,Main.posx[c],Main.poxy[c],game.playerName,true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something important here, asynchronously to the rendering thread
                //final Result result = createResult();
                // post a Runnable to the rendering thread that processes the result
                getPlayerfromDataBase();

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {

                        // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                        //results.add(result);
                    }
                });
            }
        }).start();

    }

    public void setMap(String map) {
        mapp = mapLoader.load("map/" + mapName + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(mapp, 1 / Main.PPM);

    }

    public void update(float dt) {



        deltatime = dt;
        for (int i = 0; i < PlayerList.size; i++) {
            Player temp = PlayerList.get(i);
            temp.updateother(dt);
        }

        if (player != null)
            player.handleInput(game.controller, game);
        //PlayerList.get(index).handleInput(game.controller);

        world.step(1 / 60f, 6, 2);

        game.cam.update();
        if (player != null) {
            player.update(dt);
            //update the firebase
            updatefirebase();
        }
        //check paused
        if (game.controller.ispausePressed()){
            game.controller.pausePressed=false;

            showDialog("You wish to Exit?");
            //paused

        }




        renderer.setView(game.cam);
    }
    public void showDialog(String s){
        final Dialog a = new Dialog("Options",skin);
        a.setWidth(300);
        a.setHeight(250);
        a.setPosition(Main.WIDTH/2-a.getWidth()/2,Main.HEIGHT/2-a.getHeight()/2);

        Label label = new Label(s,skin,"optional");

//        a.getContentTable().add(label).center().expand();

        TextButton btn = new TextButton("Back", skin, "round");

        a.getButtonTable().add(btn).size(100,50);
        btn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                a.remove();

            }
        });

        TextButton btn1 = new TextButton("Exit", skin, "round");
        btn1.setWidth(100);

        a.getButtonTable().add(btn1).size(100,50);;
        btn1.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                a.remove();
                game.setScreen(new Mainmenu(game));

            }
        });
        //Music button
        final Button music_btn = new Button(skin,"music");
        music_btn.setChecked(game.checkMusic);
        a.getContentTable().add(music_btn).left();
        music_btn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.checkMusic) {
                    game.checkMusic=false;
                    music.setVolume(0);
                }
                else {
                    game.checkMusic=true;
                    music.setVolume(game.musicPosition/100);

                }


            }
        });


        final Slider slider = new Slider(0,100,0.1f,false,skin);
        a.getContentTable().add(slider).right();
        slider.setValue(game.musicPosition);
        slider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.musicPosition = slider.getValue();
                if (game.checkMusic){
                    music.setVolume(slider.getValue()/100);

                }

            }
        });
        //Sound button
        a.getContentTable().row();
        final Button sound_btn = new Button(skin,"sound");
        sound_btn.setChecked(game.checkSound);
        a.getContentTable().add(sound_btn).left();
        sound_btn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.checkSound) {
                    game.checkSound=false;

                }
                else {
                    game.checkSound=true;

                }


            }
        });


        final Slider slider1 = new Slider(0,100,0.1f,false,skin);
        a.getContentTable().add(slider1).right();
        slider1.setValue(game.soundPosition);
        slider1.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.soundPosition = slider1.getValue();


            }
        });

        ///===========
        stage.addActor(a);

    }

    public void updatefirebase() {

        String a, b;
        if (player.state == Player.State.Stand) a = "Stand";
        else a = "Run";
        if (player.previous == Player.State.Run) b = "Run";
        else b = "Stand";
        game.db.SetPlayerXY(game.roomname, game.playerName, player.b2body.getPosition().x, player.b2body.getPosition().y, "Action", a, b, (int) player.direction);

    }

    public void BoomCountDown(float delta) {

        for (int i = 0; i < BoomList.size; i++) {
            Boom Temp = BoomList.get(i);
            Temp.Time -= 1;
            Temp.update(delta);

            if (Temp.Time == 50) {
                Temp.Destroy(player, BoxList, WallList);
            }

            if (Temp.Time == 0) {
                BoomList.removeValue(Temp, true);
                i--;
            }
            if (Temp.Time == 150) {
                Temp.fdef.filter.maskBits = MyScreen.PLAYER;
                Temp.b2body.createFixture(Temp.fdef);
            }

        }
        for (int i = 0; i < BoxList.size; i++) {
            Items Temp = BoxList.get(i);
            if (Temp.isDestroy) {
                Temp.Time -= 1;

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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //=====|MAP RENDERER|===========
        game.batch.setProjectionMatrix(game.controller.cam.combined);

        renderer.render();
        //====|Box2d render|
        b2dr.render(world, game.cam.combined);

        game.batch.setProjectionMatrix(game.cam.combined);

        game.batch.begin();
        for (int i = 0; i < BoomList.size; i++) {
            Boom item = BoomList.get(i);
            item.draw(game.batch);
        }
        for (int i = 0; i < BoxList.size; i++) {
            Items item = BoxList.get(i);
            item.draw(game.batch);
        }
        for (int i = 0; i < PlayerList.size; i++) {
            Player temp = PlayerList.get(i);
            temp.draw(game.batch);
        }
        if (player != null)
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
        game.db.db.child("rooms/" + game.roomname).removeEventListener(listener);
        music.dispose();
        world.dispose();
        b2dr.dispose();
        mapp.dispose();
        renderer.dispose();
        game.db.deletePlayerfromRoom(game.roomname, game.playerName);
        if (PlayerList.size ==0){
//            game.db.db.child("rooms/" + game.roomname).child("_RoomStatus").setValue(null);
//            game.db.db.child("rooms/" + game.roomname).child("_RoomMap").setValue(null);
            game.db.db.child("rooms/" + game.roomname).setValue(null);
        }
    }

}
