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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.mygdx.game.Main;

import java.util.HashMap;

public class MainHall implements Screen {
    private Main game;
    public long quanity = 0;
    public Array<String> player;
    public Stage stage;
    Skin skin;
    Viewport viewport;
    public OrthographicCamera cam;
    Table scrollTable;
    public HashMap<String, Integer> rooms;
    ChildEventListener listener;


    public MainHall(final Main game) {
        this.game = game;
        rooms = new HashMap<>();

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);


        //
        Button createRoom = new TextButton("CREATE A ROOM", skin, "round");
        createRoom.setPosition(Main.WIDTH / 2 - createRoom.getMinWidth() / 2, 10);
        Button Back = new TextButton("Back", skin, "round");
        Back.setPosition(0, 10);
        Back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Mainmenu(game));
            }
        });
        createRoom.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //game.db.AddRoomStatus(game.playerName, false);
                game.db.AddRoomStatus(game.playerName, 0);

                game.db.AddMap(game.playerName,"Forest");
                game.db.addPlayertoRoom(game.playerName, game.playerName, Main.posx[0], Main.poxy[0]);

                //add room status
                //
                game.roomname = game.playerName;
                game.setScreen(new Lobby(game));

            }
        });
        stage.addActor(Back);
        stage.addActor(createRoom);
        Label title = new Label("List Rooms", skin, "title");
        title.setAlignment(Align.center);
        title.setPosition(0, 250);
        title.setSize(Main.WIDTH, 70);
        stage.addActor(title);

        //Scroll
        scrollTable = new Table();

        final ScrollPane scroller = new ScrollPane(scrollTable);
        scroller.setPosition(Main.WIDTH / 2 - scroller.getWidth() / 2, 100);

        this.stage.addActor(scroller);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something important here, asynchronously to the rendering thread
                //final Result result = createResult();
                // post a Runnable to the rendering thread that processes the result
                GetRoom();

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

    public void GetRoom() {
        listener = game.db.db.child("rooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("_RoomStatus").getValue()!=null && (long)dataSnapshot.child("_RoomStatus").getValue() == 0) {
                    final String tmp = dataSnapshot.getName();
                    rooms.put(tmp, Integer.valueOf((int) dataSnapshot.getChildrenCount()));
                    Label text = new Label(tmp, skin, "title-plain");
                    text.setAlignment(Align.center);
//                  text.setWrap(true);
                    text.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                            game.db.addPlayertoRoom(tmp, game.playerName, Main.posx[rooms.get(tmp) - 2], Main.poxy[rooms.get(tmp) - 2]);
                            game.roomname = tmp;
                            game.setScreen(new Lobby(game));

                        }
                    });


                    scrollTable.add(text);
                    scrollTable.row();


                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("_RoomStatus").getValue()!=null && (long)dataSnapshot.child("_RoomStatus").getValue() == 1){
                    //room has started
                    int i = 0;
                    String key="";
                    for (String j : rooms.keySet()) {
                        key=j;
                        if (j.equals(dataSnapshot.getName())) break;
                        i++;
                    }
                    if (scrollTable.getChildren().size!=0 && i < scrollTable.getChildren().size){
                        scrollTable.removeActorAt(i, true);
                        rooms.remove(key);
                    }
                }
                else{
                    rooms.put(dataSnapshot.getName(), Integer.valueOf((int) dataSnapshot.getChildrenCount()));

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int i = 0;
                String key="";
                for (String j : rooms.keySet()) {
                    key=j;
                    if (j.equals(dataSnapshot.getName())) break;
                    i++;
                }
                if (scrollTable.getChildren().size!=0){
                    scrollTable.removeActorAt(i, true);
                    rooms.remove(key);
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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        game.db.db.child("rooms").removeEventListener(listener);
    }
}
