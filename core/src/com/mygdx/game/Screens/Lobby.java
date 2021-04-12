package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.mygdx.game.Main;

public class Lobby implements Screen {
    Main game;
    public long quanity=0;
    public Array<String> player;

    public Stage stage;
    Skin skin;  Viewport viewport;
    boolean available = true;
    boolean start = false;
    public OrthographicCamera cam;
    ChildEventListener listener;
    List players_list;
    Table scrollTable;
    String map;
    public Lobby(final Main game)   {
        this.game = game;
        player = new Array<>();

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);
        players_list = new List<String>(skin,"dimmed");

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something important here, asynchronously to the rendering thread
                //final Result result = createResult();
                // post a Runnable to the rendering thread that processes the result
                getRoomsize(game.roomname);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                        //results.add(result);
                    }
                });
            }
        }).start();

        final TextButton join_btn = new TextButton("Start", skin, "round");

        join_btn.setPosition(Main.WIDTH / 2 - join_btn.getMinWidth() / 2, 10);
        join_btn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //Only host can start the game
                if (game.playerName.equals(game.roomname)){
                    game.db.AddRoomStatus(game.roomname,true);
                    StartGame();
                }

            }
        });
        Button Back = new TextButton("Back", skin, "round");
        Back.setPosition(0, 10);
        Back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainHall(game));
            }
        });
        stage.addActor(Back);
        stage.addActor(join_btn);

        Label title = new Label("Game Lobby", skin, "subtitle");
        title.setAlignment(Align.center);
        title.setPosition(10, 250);
        title.setSize(Main.WIDTH/4, 50);
        stage.addActor(title);
        //=====//

        final ScrollPane scroller = new ScrollPane(players_list,skin);
        scroller.setPosition(0, 100);
        scroller.setSize(20+Main.WIDTH/4,150);
        this.stage.addActor(scroller);



        Label title1 = new Label("Chat Room", skin, "subtitle");
        title1.setAlignment(Align.center);

        title1.setPosition(scroller.getX()+scroller.getWidth()+25, 250);
        title1.setSize(Main.WIDTH/4, 50);
        stage.addActor(title1);

        scrollTable = new Table();
        scrollTable.left().top();
        scrollTable.add(new Label("HEHE",skin));
        scrollTable.row();
        scrollTable.add(new Label("HEHE",skin));
        scrollTable.row();
        scrollTable.add(new Label("HEHE",skin));
        scrollTable.row();

        //
        final ScrollPane scroller1 = new ScrollPane(scrollTable,skin);
        scroller1.setPosition(title1.getX()-20, 100);
        scroller1.setSize(title.getWidth()+40,150);
        this.stage.addActor(scroller1);
        TextField message_field = new TextField("", skin);
        message_field.setMessageText("Message");
        message_field.setColor(0.2f, 0.4f, 0.3f, 1f);
        message_field.getStyle().fontColor = Color.WHITE;

        message_field.setSize(scroller1.getWidth()-10-40,20);
        message_field.setPosition(scroller1.getX()+6,scroller1.getY()-15);
        stage.addActor(message_field);

        TextButton Send = new TextButton("Send", skin, "default");
        Send.setPosition(message_field.getX()+message_field.getWidth(), message_field.getY());
        Send.setSize(40,20);
        Send.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
        stage.addActor(Send);
//
//        Label title2 = new Label("Select map", skin, "title");
//        title.setAlignment(Align.center);
//        title.setPosition(0, 250);
//        title.setSize(Main.WIDTH/4, 70);
//        stage.addActor(title2);

//        players_list.setItems(player);


    }
    public void StartGame(){
        start = true;
        game.setScreen(new MyScreen(game));

    }
    public void getRoomsize(String Room){
        listener=game.db.db.child("rooms/"+Room).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getName().equals("_RoomMap")){
                    map=(String)dataSnapshot.getValue();
                }
                else if (!dataSnapshot.getName().equals("_RoomStatus")){
                    player.add(dataSnapshot.getName());
                    players_list.setItems(player);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getName().equals("_RoomStatus") && !game.playerName.equals(game.roomname) )
                {
                    start = true;
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getName().equals(game.roomname)){
                    available=false;

                }
                else{
                    player.removeValue(dataSnapshot.getName(),true);
                    players_list.setItems(player);
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
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!available){
            game.roomname="Test";
            game.setScreen(new MainHall(game));
        }
        if (start){
            StartGame();
        }
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
        dispose();
    }

    @Override
    public void dispose() {
        game.db.db.child("rooms/"+game.roomname).removeEventListener(listener);
        if (!start){
            if (game.roomname.equals(game.playerName)){
                game.db.db.child("rooms/"+game.roomname).setValue(null);
            }
            else{
                game.db.db.child("rooms/"+game.roomname+"/"+game.playerName).setValue(null);

            }
        }
    }
}
