package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
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
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mygdx.game.Builder.WorldBuilder;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Sprites.Boom;
import com.mygdx.game.Sprites.ITEM;
import com.mygdx.game.Sprites.Items;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.Walls;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.mygdx.game.Sprites.Walls;
import javax.rmi.CORBA.Tie;
public class MyScreen implements Screen {
    int c = 0;
    Stage stage;
    Skin skin;
    private Main game;
    Player player;
    ChildEventListener listener, item_listener;
    boolean gameOver = false;
    int playercount = 0;
    private TextureAtlas atlas;
    public Array<Boom> BoomList;
    public Array<Items> BoxList;
    public Array<Walls> WallList;
    public Array<Player> PlayerList;
    public Array<ITEM> ItemList;
    public static short PLAYER, ITEMS, BOOM, Wall;
    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    public static String mapName = "Forest";

    Array<LocalTime> Time; // Create a date object
    int runTimeDown = 100;
    int lvHard = 1;
    float deltatime = 0;
    //Room


    //=========================Tiled Map/////=====================

    public TmxMapLoader mapLoader;
    public TiledMap mapp;
    public OrthogonalTiledMapRenderer renderer;
    //music
    private Music music;

    public MyScreen(Main game, String map) {
        this.game = game;
        mapName = map;
        mapLoader = new TmxMapLoader();
        setMap(mapName);
        // THU  HEP BAN DO

        Time = new Array<>();
        Time.add(LocalTime.now());
        for (int i = 1; i <= 24; i++) {
            //Thay bien trong PlusSeconds de tang thoi gian thu  hep
            Time.add(Time.get(0).plusSeconds(30 * i));
        }

        //=================        AddplayertoRoom====================

        //==================

        PLAYER = 1;
        ITEMS = 2;
        BOOM = 4;
        Wall = 8;
        //important
        game.controller = new Controller();
        stage = game.controller.stage;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        //
        BoomList = new Array<Boom>();
        BoxList = new Array<Items>();
        WallList = new Array<Walls>();
        PlayerList = new Array<Player>();
        ItemList = new Array<ITEM>();
        /////==============================================Create Box2d WORld==================================
        world = new World(new Vector2(), true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);
        //Box2d World
        new WorldBuilder(world, mapp, BoxList, WallList, ItemList);
        //======================================================================
        getItemsFromDataBase();
        initPlayer();
        music = GameManager.getAssetManager().get("music/" + mapName + ".mp3", Music.class);
        music.setLooping(true);
        if (game.checkMusic) {
            music.setVolume(game.musicPosition / 100);
        } else {
            music.setVolume(0);
        }
        music.play();


    }

    public void getItemsFromDataBase() {
        item_listener = game.db.db.child("rooms/" + game.roomname + "/Items").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if (!game.playerName.equals(game.roomname)) {

                            long type = (long) dataSnapshot.getValue();

                            int index = Integer.valueOf(dataSnapshot.getName());
                            BoxList.get(index).typeItem = type;
                            //ItemList.add(tmp);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
                }
        );
    }

    //

    //
    public void getPlayerfromDataBase() {

        listener = game.db.db.child("rooms/" + game.roomname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getName().equals("_RoomStatus") && !dataSnapshot.getName().equals("_RoomMap") && !dataSnapshot.getName().equals("Items")) {
                    if (!dataSnapshot.getName().equals(game.playerName)) {
                        playercount++;
                        PlayerList.add(new Player(world, BoomList, (double) dataSnapshot.child("x").getValue(), (double) dataSnapshot.child("y").getValue(), dataSnapshot.getName(), false));

                    } else {
                        playercount++;

                        player = new Player(world, BoomList, (double) dataSnapshot.child("x").getValue(), (double) dataSnapshot.child("y").getValue(), dataSnapshot.getName(), true);

                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getName().equals("_RoomStatus")){
                    if ((long)dataSnapshot.getValue()==2){
                        //Game over
                        if (player!=null ){
                            if ( player.ALIVE){
                                System.out.println("YOU WIN");
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                                        game.setScreen(new VictoryScreen(game,"You Win"));
                                    }
                                });
                            }
                            else{
                                System.out.println("YOU LOSE");
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                                        game.setScreen(new VictoryScreen(game,"You lose"));
                                    }
                                });


                            }

                        }

                    }
                }
                else if (!dataSnapshot.getName().equals("_RoomStatus") && !dataSnapshot.getName().equals("_RoomMap") && !dataSnapshot.getName().equals("Items")) {
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
                        } else if (dataSnapshot.child("State").getValue().equals("Stand")) {
                            PlayerList.get(index).state = Player.State.Stand;

                        } else {
                            PlayerList.get(index).state = Player.State.Dead;
                            PlayerList.get(index).stateTimer = 0;

                        }
                        if (dataSnapshot.child("prevState").getValue().equals("Stand")) {
                            PlayerList.get(index).previous = Player.State.Stand;
                        } else if (dataSnapshot.child("prevState").getValue().equals("Run")) {
                            PlayerList.get(index).previous = Player.State.Run;
                        } else {
                            PlayerList.get(index).previous = Player.State.Dead;
                            PlayerList.get(index).stateTimer = 0;


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
                            BoomList.add(new Boom(world, new Vector2((float) x, (float) y), (int) direction, PlayerList.get(index).Power, false));
                        } else if (dataSnapshot.child("action").getValue().equals("ReallyDead")) {
                            PlayerList.get(index).ReallyDead = true;

                        }


                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.getName().equals("_RoomStatus") && !dataSnapshot.getName().equals("_RoomMap") && !dataSnapshot.getName().equals("Items")) {
                    for (int i = 0; i < PlayerList.size; i++) {
                        Player temp = PlayerList.get(i);
                        if (temp.name.equals(dataSnapshot.getName())) {
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
    public void CheckGameOver(){
        if (playercount <2 ) return;
        if (player!= null && !player.ALIVE) return;
        for (int i = 0; i < PlayerList.size; i++) {
            Player temp = PlayerList.get(i);
            if (!temp.ReallyDead) return;
            //ham nay se k return neu player con song va cac ng choi khac chet het
        }
        game.db.AddRoomStatus(Main.roomname,2);

    }
    public void update(float dt) {
        CheckGameOver();
        deltatime = dt;
        for (int i = 0; i < PlayerList.size; i++) {
            Player temp = PlayerList.get(i);
            temp.updateother(dt);

        }

        if (player != null) {
            player.handleInput(game.controller, game);
            if (player.ReallyDead) {
                if (c == 0) {
                    game.db.setDead(game.roomname, game.playerName);

                    c = 1;
                }
            }
        }
        //PlayerList.get(index).handleInput(game.controller);

        world.step(1 / 60f, 6, 2);

        game.cam.update();
        if (player != null) {
            player.update(dt);
            //update the firebase
            updatefirebase();
        }
        //check paused
        if (game.controller.ispausePressed()) {
            game.controller.pausePressed = false;

            showDialog("You wish to Exit?");
            //paused

        }


        renderer.setView(game.cam);
    }

    public void showDialog(String s) {
        final Dialog a = new Dialog("Options", skin);
        a.setWidth(300);
        a.setHeight(250);
        a.setPosition(Main.WIDTH / 2 - a.getWidth() / 2, Main.HEIGHT / 2 - a.getHeight() / 2);

        Label label = new Label(s, skin, "optional");

//        a.getContentTable().add(label).center().expand();

        TextButton btn = new TextButton("Back", skin, "round");

        a.getButtonTable().add(btn).size(100, 50);
        btn.addListener(new InputListener() {
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

        a.getButtonTable().add(btn1).size(100, 50);
        ;
        btn1.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                a.remove();
                showDialogConfirm("Are you sure to exit?");

            }
        });
        //Music button
        final Button music_btn = new Button(skin, "music");
        music_btn.setChecked(game.checkMusic);
        a.getContentTable().add(music_btn).left();
        music_btn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.checkMusic) {
                    game.checkMusic = false;
                    music.setVolume(0);
                } else {
                    game.checkMusic = true;
                    music.setVolume(game.musicPosition / 100);

                }


            }
        });


        final Slider slider = new Slider(0, 100, 0.1f, false, skin);
        a.getContentTable().add(slider).right();
        slider.setValue(game.musicPosition);
        slider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.musicPosition = slider.getValue();
                if (game.checkMusic) {
                    music.setVolume(slider.getValue() / 100);

                }

            }
        });
        //Sound button
        a.getContentTable().row();
        final Button sound_btn = new Button(skin, "sound");
        sound_btn.setChecked(game.checkSound);
        a.getContentTable().add(sound_btn).left();
        sound_btn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.checkSound) {
                    game.checkSound = false;

                } else {
                    game.checkSound = true;

                }


            }
        });


        final Slider slider1 = new Slider(0, 100, 0.1f, false, skin);
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

    public void showDialogConfirm(String s) {
        final Dialog a = new Dialog("Confirm", skin);
        a.setWidth(250);
        a.setHeight(200);
        a.setPosition(Main.WIDTH / 2 - a.getWidth() / 2, Main.HEIGHT / 2 - a.getHeight() / 2);

        Label label = new Label(s, skin, "optional");

        a.getContentTable().add(label).center().expand();
        TextButton btn = new TextButton("Back", skin, "round");
        a.getButtonTable().add(btn);
        btn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                a.remove();

            }
        });
        TextButton btn1 = new TextButton("Yes", skin, "round");
        a.getButtonTable().add(btn1);
        btn1.addListener(new InputListener() {
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
        stage.addActor(a);

    }

    public void updatefirebase() {

        String a, b;
        if (player.state == Player.State.Stand) a = "Stand";
        else a = "Run";
        if (player.previous == Player.State.Run) b = "Run";
        else b = "Stand";
        if (player.state == Player.State.Dead) {
            a = "Dead";
            b = "Dead";
        }
        game.db.SetPlayerXY(game.roomname, game.playerName, player.b2body.getPosition().x, player.b2body.getPosition().y, "Action", a, b, (int) player.direction);

    }

    public void TimeDown() {

        if (runTimeDown > 0) {
            runTimeDown--;
        }
        if (Time.size - 1 >= lvHard) {
            if (LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(Time.get(lvHard).format((DateTimeFormatter.ofPattern("HH:mm:ss")))) && runTimeDown == 0) {

                runTimeDown = 100;
                //lvHard++;
                switch (mapName){
                    case "Forest":{
                        for (int i = 1; i < 5; i++) {
                            Vector2 tempV = new Vector2((float) 0.3, (float) (0.4 * i + 0.2));

                            Boom Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) 3.8, (float) (0.4 * i + 0.2));
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        for (int i = 1; i < 9; i++) {
                            Vector2 tempV = new Vector2((float) (0.4 * i + 0.2), (float) 0.2);
                            Boom Temp = new Boom(world, tempV, 1, 20 *(int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) (0.4 * i + 0.2), (float) 2.2);
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        break;
                    }
                    case "Temple":{
                        for (int i = 1; i < 5; i++) {
                            Vector2 tempV = new Vector2((float) 0.3, (float) (0.4 * i + 0.2));

                            Boom Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) 3.8, (float) (0.4 * i + 0.2));
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        for (int i = 1; i < 9; i++) {
                            Vector2 tempV = new Vector2((float) (0.4 * i + 0.2), (float) 0.2);
                            Boom Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) (0.4 * i + 0.2), (float) 2.2);
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        break;
                    }
                    case "Cave":{
                        for (int i = 1; i < 5; i++) {
                            Vector2 tempV = new Vector2((float) 0.3, (float) (0.4 * i + 0.2));

                            Boom Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) 3.8, (float) (0.4 * i + 0.2));
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        for (int i = 1; i < 9; i++) {
                            Vector2 tempV = new Vector2((float) (0.4 * i + 0.2), (float) 0.2);
                            Boom Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) (0.4 * i + 0.2), (float) 2.2);
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        break;
                    }
                    case "Sewer":{
                        for (int i = 1; i < 5; i++) {
                            Vector2 tempV = new Vector2((float) 0.3, (float) (0.4 * i + 0.2));

                            Boom Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) 3.8, (float) (0.4 * i + 0.2));
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        for (int i = 1; i < 9; i++) {
                            Vector2 tempV = new Vector2((float) (0.4 * i + 0.2), (float) 0.2);
                            Boom Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                            tempV = new Vector2((float) (0.4 * i + 0.2), (float) 2.2);
                            Temp = new Boom(world, tempV, 1, 20 * (int)(lvHard/2+1), true);
                            BoomList.add(Temp);
                        }
                        break;
                    }
                    default:
                        break;

                }
                lvHard++;




            }
        }


    }

    public void BoomCountDown(float delta) {
        try {
            for (int i = 0; i < BoomList.size; i++) {
                Boom Temp = BoomList.get(i);
                Temp.Time -= 1;
                Temp.update(delta);

                if (Temp.Time == 50) {
                    Temp.Destroy(player, BoxList, WallList, BoomList, ItemList);
                }

                if (Temp.Time == 0) {
                    BoomList.removeValue(Temp, true);
                    i--;
                }
                int Distance=  (int)(((Math.sqrt(Math.pow((Temp.b2body.getPosition().x  - player.b2body.getPosition().x), 2) + Math.pow((Temp.b2body.getPosition().y  - player.b2body.getPosition().y), 2)))*100 +5)/20);
                if (Distance>=1) {
                    Temp.fdef.filter.categoryBits =MyScreen.BOOM ;
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
        } catch (Exception e) {
            System.out.print("BoomList");
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
        game.controller.Joy();
        TimeDown();

        for (int i = 0; i < ItemList.size; i++) {
            ITEM item = ItemList.get(i);
            if (item.isDestroy == true) {
                ItemList.removeIndex(i);
                i--;
            } else {
                item.update(delta);
                item.draw(game.batch);
                item.GetEffect(this.player);
                for (int j = 0; j < PlayerList.size; j++) {
                    Player temp = PlayerList.get(j);
                    item.GetEffectOther(temp);
                }


            }

        }
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


            if (!temp.ReallyDead)
                temp.draw(game.batch);


        }
        if (player != null && !player.ReallyDead)
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
        game.db.db.child("rooms/" + game.roomname + "/Items").removeEventListener(item_listener);
        music.dispose();
        world.dispose();
        b2dr.dispose();
        mapp.dispose();
        renderer.dispose();
        if (gameOver) {

        } else {
            game.db.deletePlayerfromRoom(game.roomname, game.playerName);

            if (PlayerList.size == 0) {
//            game.db.db.child("rooms/" + game.roomname).child("_RoomStatus").setValue(null);
//            game.db.db.child("rooms/" + game.roomname).child("_RoomMap").setValue(null);
                game.db.db.child("rooms/" + game.roomname).setValue(null);
            }
        }


    }

}
