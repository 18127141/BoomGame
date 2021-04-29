package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;

public class VictoryScreen implements Screen {
    private Main game;
    Viewport viewport;
    Stage stage;
    Skin skin;
    public OrthographicCamera cam;
    private Music music;
    Texture texture;
    Image gameover,Press;
    boolean win =true;
    public VictoryScreen(final Main game,String s){
        this.game = game;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);
        texture = GameManager.getAssetManager().get("Pack/items.png", Texture.class);
        gameover = new Image(new TextureRegion(texture,3,82,212,34));
        Press = new Image (new TextureRegion(texture,218,97,122,20));
        gameover.setPosition(Main.WIDTH/2-gameover.getWidth()/2,Main.HEIGHT/2);
        stage.addActor(gameover);
        Label text = new Label(s,skin,"title-plain");
        text.setPosition(Main.WIDTH/2-text.getWidth()/2,gameover.getY()-gameover.getHeight());
        stage.addActor(text);
        Press.setPosition(Main.WIDTH/2-Press.getWidth()/2,text.getY()-text.getHeight());
        if (s.equals("You Win"))
            music = GameManager.getAssetManager(). get("music/Victory.mp3",Music.class);
        else {
            win = false;
            music = GameManager.getAssetManager(). get("music/Lose.mp3",Music.class);

        }
        music.setLooping(false);
        if (game.checkMusic) {
            music.setVolume(game.musicPosition / 100);
        } else {
            music.setVolume(0);
        }
        music.play();
        Press.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//               game.setScreen(new MainHall(game));
                dispose();
            }
        });
        //218 97 122 20
    }
    @Override
    public void show() {

    }
    //3 83 212 34
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!music.isPlaying()){
            stage.addActor(Press);
        }
        stage.act();
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

        game.db.db.child("rooms/" + game.roomname).setValue(null, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                        game.setScreen(new MainHall(game));
                    }
                });
            }
        });
    }
}
