package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.mygdx.game.ResourceManager.GameManager;

public class Lobby implements Screen {
    Main game;
    public long quanity = 0;
    public Array<String> player;

    public Stage stage;
    Skin skin;
    Viewport viewport;
    boolean available = true;
    boolean start = false;
    public OrthographicCamera cam;
    ChildEventListener listener;
    List players_list;
    Table scrollTable;
    String map = "Forest";
    TextField message_field;
    Table mapTable;
    int mapIndex = 0;

    public Lobby(final Main game) {
        this.game = game;
        player = new Array<>();

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);
        players_list = new List<String>(skin, "dimmed");

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
        join_btn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //Only host can start the game
                if (game.playerName.equals(game.roomname)) {
                    game.db.AddRoomStatus(game.roomname, true);
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
        title.setSize(Main.WIDTH / 4, 50);
        stage.addActor(title);
        //=====//

        final ScrollPane scroller = new ScrollPane(players_list, skin);
        scroller.setPosition(0, 100);
        scroller.setSize(20 + Main.WIDTH / 4, 150);
        this.stage.addActor(scroller);


        int aa = 0;
        Label title1 = new Label("Chat Room", skin, "subtitle");
        title1.setAlignment(Align.center);

        title1.setPosition(scroller.getX() + scroller.getWidth() + 25 - aa, 250);
        title1.setSize(Main.WIDTH / 4, 50);
        stage.addActor(title1);

        scrollTable = new Table();
        scrollTable.left().top();

        //
        final ScrollPane scroller1 = new ScrollPane(scrollTable, skin);

        scroller1.setPosition(title1.getX() - 20 - aa, 100);
        scroller1.setSize(title.getWidth() + 40, 150);
        this.stage.addActor(scroller1);
        message_field = new TextField("", skin);
        message_field.setMessageText("Message");
        message_field.setColor(0.2f, 0.4f, 0.3f, 1f);
        message_field.getStyle().fontColor = Color.WHITE;

        message_field.setSize(scroller1.getWidth() - 10 - 40, 20);
        message_field.setPosition(scroller1.getX() + 6, scroller1.getY() - 15);
        stage.addActor(message_field);

        TextButton Send = new TextButton("Send", skin, "default");
        Send.setPosition(message_field.getX() + message_field.getWidth() - aa, message_field.getY());
        Send.setSize(40, 20);
        Send.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                game.db.sendMsg(game.roomname, game.playerName, message_field.getText());
                message_field.setText("");
            }
        });
        stage.addActor(Send);

        Label title2 = new Label("Select Map", skin, "subtitle");
        title2.setAlignment(Align.center);
        title2.setPosition(scroller1.getX() + scroller1.getWidth() + 25, 250);
        title2.setSize(Main.WIDTH / 4, 50);
        stage.addActor(title2);
        //mapTable = new Table();
        MapTable();



        Button left = new Button(skin, "left");
        left.setPosition(title2.getX(), message_field.getY() - 5);
        left.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.playerName.equals(game.roomname)) {
                    mapIndex--;
                    if (mapIndex < 0) {
                        mapIndex = Main.Maps.length - 1;
                    }
                    game.db.AddMap(game.roomname, Main.Maps[mapIndex]);
                }
            }
        });
        stage.addActor(left);

        Button right = new Button(skin, "right");
        right.setPosition(title2.getX() + title2.getWidth() - 20, message_field.getY() - 5);
        right.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.playerName.equals(game.roomname)) {
                    mapIndex++;
                    if (mapIndex >= Main.Maps.length) {
                        mapIndex = 0;
                    }
                    game.db.AddMap(game.roomname, Main.Maps[mapIndex]);
                }
            }
        });
        stage.addActor(right);


    }

    public void MapTable() {

        mapTable = new Table();
        mapTable.setPosition(311, 110);
        mapTable.setSize(160, 120);
        Image minimap = new Image((Texture) GameManager.getAssetManager().get("map/minimap/" + map + ".png"));


        mapTable.add(minimap);
        if (mapTable!=null)
            stage.addActor(mapTable);

    }

    public void StartGame() {
        start = true;
        game.setScreen(new MyScreen(game,map));

    }

    public void getRoomsize(String Room) {
        listener = game.db.db.child("rooms/" + Room).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getName().equals("_RoomMap")) {
                    map = (String) dataSnapshot.getValue();
                    MapTable();
                } else if (!dataSnapshot.getName().equals("_RoomStatus")) {
                    player.add(dataSnapshot.getName());
                    players_list.setItems(player);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getName().equals("_RoomMap")) {
                    map = (String) dataSnapshot.getValue();
                    MapTable();

                }
                if (dataSnapshot.getName().equals("_RoomStatus") && !game.playerName.equals(game.roomname)) {
                    start = true;
                }
                if (!dataSnapshot.getName().equals("_RoomMap") && !dataSnapshot.getName().equals("_RoomStatus")) {
                    String msg = (String) dataSnapshot.child("message").getValue();
                    if (msg!=null && !msg.equals("")) {
                        scrollTable.add(new Label("<" + dataSnapshot.getName() + ">: " + msg, skin)).top().left();
                        scrollTable.row();
                    }

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getName().equals(game.roomname)) {
                    available = false;

                } else if (!dataSnapshot.getName().equals("_RoomMap") && !dataSnapshot.getName().equals("_RoomStatus")) {
                    int index = 0;
                    //player.removeValue(dataSnapshot.getName(),true);
                    for (int i = 0; i < player.size; i++) {
                        if (player.get(i).equals(dataSnapshot.getName())) break;
                        index++;
                    }
                    if (!player.isEmpty())
                        player.removeIndex(index);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!available) {
            game.roomname = "Test";
            game.setScreen(new MainHall(game));
        }
        if (start) {
            StartGame();
        }
        stage.draw();
        stage.act();

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
        if (!start) {
            if (game.roomname.equals(game.playerName)) {
                game.db.db.child("rooms/" + game.roomname).setValue(null);
            } else {
                game.db.db.child("rooms/" + game.roomname + "/" + game.playerName).setValue(null);

            }
        }
    }
}
