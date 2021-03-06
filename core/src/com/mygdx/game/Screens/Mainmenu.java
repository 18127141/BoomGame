package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Mainmenu implements Screen {

    private Main game;
    Texture background;
    Viewport viewport;
    Stage stage;
    Skin skin;
    Label option,credit,play,Exit;
    public OrthographicCamera cam;
    private Music music;
    public Mainmenu(final Main game){
        this.game = game;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        background = GameManager.getAssetManager().get("title_background-little.png", Texture.class);
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);


        //=================================///////////

        final Table Bg = new Table();
        Image bg = new Image(background);
        Bg.setFillParent(true);

        Bg.add(bg).size(Main.WIDTH,Main.HEIGHT) ;

        int t = 50;
        play = new Label("Play",skin,"title-plain");

        play.setAlignment(Align.center);
        play.setPosition(Main.WIDTH/2-play.getWidth()/2,200-t);

        option = new Label("Option",skin,"title-plain");

        option.setAlignment(Align.center);
        option.setPosition(Main.WIDTH/2-option.getWidth()/2,150-t);
        credit = new Label("Credit",skin,"title-plain");

        credit.setAlignment(Align.center);
        credit.setPosition(Main.WIDTH/2-credit.getWidth()/2,100-t);
        Exit = new Label("Exit",skin,"title-plain");

        Exit.setAlignment(Align.center);
        Exit.setPosition(Main.WIDTH/2-Exit.getWidth()/2,50-t);
        //===========///
        play.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.playerName.equals("")){
                    LoginScreen();
                }
                else{
                    game.setScreen(new MainHall(game));
                }

            }
        });
        Exit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                //System.exit(0);

            }
        });
        option.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                optionScreen()        ;
                //System.exit(0);

            }
        });
        stage.addActor(Bg);
        stage.addActor(play);
        stage.addActor(option);
        stage.addActor(credit);
        stage.addActor(Exit);
//        Touchpad touchpad = new Touchpad(5,skin);
//        touchpad.setSize(100,100);
//
//        stage.addActor(touchpad);

    }
    //
    public void showDialog(String s){
        final Dialog a = new Dialog("Warning",skin);
        a.setWidth(200);
        a.setHeight(150);
        a.setPosition(Main.WIDTH/2-a.getWidth()/2,Main.HEIGHT/2-a.getHeight()/2);

        Label label = new Label(s,skin,"optional");

        a.getContentTable().add(label).center().expand();
        TextButton btn = new TextButton("OK", skin, "round");
        a.getButtonTable().add(btn);
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
        stage.addActor(a);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    }

    @Override
    public void dispose() {

    }
    void LoginScreen(){
        play.remove();
        option.remove();
        credit.remove();
        Exit.remove();
        final TextField name_field = new TextField("",skin);
        name_field.setMessageText("Your Name");
        name_field.setColor(0.2f, 0.4f, 0.3f, 0.8f);
        name_field.getStyle().fontColor = Color.WHITE;
        name_field.setWidth(200);
        name_field.setHeight(50);

        name_field.setAlignment(Align.center);
        name_field.setPosition(Main.WIDTH/2-name_field.getWidth()/2,150);
        stage.addActor(name_field);
        final TextButton join_btn = new TextButton("Join", skin, "round");
        final TextButton login_cancel = new TextButton("Cancel", skin, "round");
        join_btn.setPosition(Main.WIDTH/2-name_field.getWidth()/2,100);
        login_cancel.setPosition(Main.WIDTH/2,100);
        join_btn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!name_field.getText().toString().equals("")){
                    if (!game.db.CheckPlayer(name_field.getText().toString())){
                        game.db.setPlayername(name_field.getText().toString());
                        game.playerName =  name_field.getText();


                        game.setScreen(new MainHall(game));
                    }
                    else
                    {
                        showDialog("Username is taken");
                    }
                }
                else{
                    showDialog("Please fill out the box");
                }

            }
        });
        login_cancel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                name_field.remove();
                join_btn.remove();
                login_cancel.remove();
                stage.addActor(play);
                stage.addActor(option);
                stage.addActor(credit);
                stage.addActor(Exit);

            }
        });
        stage.addActor(join_btn);
        stage.addActor(login_cancel);
    }
    void optionScreen(){
        play.remove();
        option.remove();
        credit.remove();
        Exit.remove();
        final Dialog a = new Dialog("Options",skin);
        a.getTitleLabel().setAlignment(Align.center);
        a.setSize(Main.WIDTH,Main.HEIGHT);
        a.setPosition(Main.WIDTH/2,Main.HEIGHT/2);


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
                }
                else {
                    game.checkMusic=true;

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
        TextButton btn = new TextButton("Back", skin, "round");
        a.getButtonTable().add(btn).center();
        btn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                a.remove();
                stage.addActor(play);
                stage.addActor(option);
                stage.addActor(credit);
                stage.addActor(Exit);
            }
        });

        stage.addActor(a);
    }
}
