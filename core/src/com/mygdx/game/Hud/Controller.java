package com.mygdx.game.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;

/**
 * Created by brentaureli on 10/23/15.
 */
public class Controller {
    public static Image l,bottom;
    Viewport viewport;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed, setBoomPressed;
    public OrthographicCamera cam;

    public Controller(){
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);


        Gdx.input.setInputProcessor(stage);

        Table leftControls = new Table();


        leftControls.left().bottom();


        int w=20;
        int h=20;
        Image setBoomImg = new Image(new Texture("flatDark25.png"));
        setBoomImg.setSize(w+35, h+35);

        setBoomImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                setBoomPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setBoomPressed = false;
            }
        });

        Image upImg = new Image(new Texture("flatDark25.png"));
        upImg.setSize(w, h);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image downImg = new Image(new Texture("flatDark26.png"));
        downImg.setSize(w, h);
        downImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("flatDark24.png"));
        rightImg.setSize(w, h);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = new Image(new Texture("flatDark23.png"));
        leftImg.setSize(w, h);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });


        leftControls.add();
        leftControls.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        leftControls.add();
        leftControls.row().pad(2, 2, 2, 2);



        leftControls.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        leftControls.add();
        leftControls.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());

        leftControls.row();
        leftControls.add();
        leftControls.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        leftControls.add();
        leftControls.row().padBottom(5);

//================================================
        Table Left = new Table();
        Left.setFillParent(true);
        Left.left();


        l = new Image(new Texture("Left.png"));
        Left.add(l);
        //==================================

        Table rightControls = new Table();
        rightControls.setFillParent(true);
        rightControls.bottom();
        rightControls.padLeft(l.getWidth());


        bottom = new Image(new Texture("bottom.png"));
        rightControls.add(bottom);
        //============================================
        Table Right = new Table();
        Right.setFillParent(true);

        Right.bottom().right();
        Right.padRight(7);
        Right.padBottom(5);
        setBoomImg.setColor(0,0,0,0);
        Right.add(setBoomImg).size(setBoomImg.getWidth(), setBoomImg.getHeight());


        //============================

        stage.addActor(Left);
        stage.addActor(rightControls);
        stage.addActor(leftControls);
        stage.addActor(Right);


    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }
    public boolean isSetBoomPressed()
    {
        return setBoomPressed;
    }
    public boolean isDownPressed() {

        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }
    public boolean isPlanted() {
        return setBoomPressed;
    }
    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}
