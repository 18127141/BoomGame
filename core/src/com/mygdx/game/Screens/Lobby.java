package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    public Lobby(final Main game)   {
        this.game = game;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);

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

        final TextButton join_btn = new TextButton("Join", skin, "round");

        join_btn.setPosition(100,100);
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
        stage.addActor(join_btn);
    }
    public void StartGame(){
        start = true;
        game.setScreen(new MyScreen(game));

    }
    public void getRoomsize(String Room){
        game.db.db.child("rooms/"+Room).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
