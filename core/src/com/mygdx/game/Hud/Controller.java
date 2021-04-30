package com.mygdx.game.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;

/**
 * Created by brentaureli on 10/23/15.
 */
public class Controller {
    public static Image l, bottom;
    Viewport viewport;
    public Stage stage;
    public boolean upPressed, downPressed, leftPressed, rightPressed, setBoomPressed, pausePressed;
    public OrthographicCamera cam;
    Image Joy;
    Image BG;
    int countdown = 0;
    Vector2 Ox;
    JoyStick joyStick;
    Texture texture;
    //number 0 to 9
    Array<TextureRegion> number;
    Image Power, Speed, Life, Boom;

    public Controller() {
        joyStick = new JoyStick();
        Ox = new Vector2(1, 0);
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        texture = GameManager.getAssetManager().get("Pack/items.png", Texture.class);
        //
        number = new Array<>();
        //56 25 10 16
        number.add((new TextureRegion(texture, 56, 25, 10, 16)));
        for (int i = 0; i < 9; i++) {
            number.add((new TextureRegion(texture, 66 + i * 11, 25, 10, 16)));

        }
        //default
        Power = new Image(number.get(1));
        Speed = new Image(number.get(8));
        Life = new Image(number.get(1));
        Boom = new Image(number.get(1));
        Power.setPosition(43, Main.HEIGHT - 10 - 126);
        Boom.setPosition(43, Main.HEIGHT - 10 - 162);
        Speed.setPosition(43, Main.HEIGHT - 10 - (162 + 36));
        Life.setPosition(43, Main.HEIGHT - 10 - (162 + 36 * 2));
        //
        Gdx.input.setInputProcessor(stage);

        Table leftControls = new Table();


        leftControls.left().bottom();


        int w = 20;
        int h = 20;

        //BG = new Image(new Texture("flatDark25.png"));
        BG = new Image(new TextureRegion(new Texture("joystick.png"), 0, 0, 124, 124));
        BG.setSize(80, 80);
        BG.setPosition((float) 20, (float) 12.5);
        //BG.setColor(0,0,0,0.5f);
        Joy = new Image(new TextureRegion(new Texture("joystick.png"), 130, 26, 75, 75));
        Joy.setSize(50, 50);
        Joy.setPosition(BG.getX() + BG.getWidth() / 2 - Joy.getWidth() / 2, BG.getY() + BG.getHeight() / 2 - Joy.getHeight() / 2);


        Image setBoomImg = new Image(new Texture("flatDark25.png"));
        setBoomImg.setSize(w + 35, h + 35);

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
        setBoomImg.setColor(0, 0, 0, 0);
        Right.add(setBoomImg).size(setBoomImg.getWidth(), setBoomImg.getHeight());


        //============================

        stage.addActor(Left);
        stage.addActor(rightControls);
        stage.addActor(Right);

        Image pause = new Image(new Texture("Box.png"));
        pause.setSize(60, 60);
        pause.setPosition(221, 1);
        pause.setColor(0, 0, 0, 0);
        pause.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pausePressed = true;
            }
        });
        stage.addActor(pause);
        stage.addActor(Boom);
        stage.addActor(Speed);
        stage.addActor(Power);
        stage.addActor(Life);

        stage.addActor(BG);
        stage.addActor(Joy);
        //stage.addActor(leftControls);
//        Skin touchpadskin = new Skin();
//
//        touchpadskin.add("touchBackground",new TextureRegion(new Texture("joystick.png"),0,0,124,124));
//        touchpadskin.add("touchKnob",new TextureRegion(new Texture("joystick.png"),130,26,75,75));
//        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
//        Drawable touchBackGround = touchpadskin.getDrawable("touchBackground");
//        Drawable touchKnob = touchpadskin.getDrawable("touchKnob");
//
//
//        touchpadStyle.background=touchBackGround;
//        touchpadStyle.knob=touchKnob;
//
//        Touchpad joystick = new Touchpad(6,touchpadStyle);
//
//        stage.addActor(joystick);


    }

    //        Power.setPosition(43,Main.HEIGHT   -10 -126);
    //        Boom.setPosition( 43 ,Main.HEIGHT  -10   - 162);
    //        Speed.setPosition(43 , Main.HEIGHT -10  -(162+36));
    //        Life.setPosition( 43 ,Main.HEIGHT  -10   -(162+36*2));
    //        //
    public void updateStat(String s, int value) {
        System.out.println(s + " " + value);
        if (s.equals("Speed")) {

//            System.out.println("speed");

            Speed.remove();
            Speed = new Image(number.get(value));
            Speed.setPosition(43, Main.HEIGHT - 10 - (162 + 36));

            stage.addActor(Speed);
        } else if (s.equals("Power")) {
//            System.out.println("POwer");

            Power.remove();
            Power = new Image(number.get(value));
            Power.setPosition(43, Main.HEIGHT - 10 - 126);
            stage.addActor(Power);


        } else if (s.equals("Life")) {
//            System.out.println("life");

            Life.remove();
            Life = new Image(number.get(value));
            Life.setPosition(43, Main.HEIGHT - 10 - (162 + 36 * 2));
            stage.addActor(Life);


        } else if (s.equals("Boom")) {
//            System.out.println("Boom");

            Boom.remove();
            Boom = new Image(number.get(value));
            Boom.setPosition(43, Main.HEIGHT - 10 - 162);
            stage.addActor(Boom);
        }



    }

    public void Joy() {
//        joyStick.Joy(this);
        float scale = Gdx.graphics.getHeight() / Main.HEIGHT;
        float deltaX = -(Gdx.graphics.getWidth() - Main.WIDTH * scale) / 2;

        upPressed = false;
        leftPressed = false;
        rightPressed = false;
        downPressed = false;
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

//            System.out.println( "WH:" + Gdx.graphics.getWidth() + " "+ Gdx.graphics.getHeight());
//            System.out.println("BF: " + Gdx.input.getX() + " "+ (Gdx.graphics.getHeight()-Gdx.input.getY()) );
            float X = Gdx.input.getX() + deltaX - 40 * scale;
            float Y = Gdx.graphics.getHeight() - Gdx.input.getY() - 40 * scale;
//            System.out.println( X + " " + Y + " " );

            if (X < Gdx.graphics.getWidth() / 2 + deltaX) {
                if (X < 0)
                    X = 0;
                else if (X > Gdx.graphics.getWidth() - Joy.getWidth())
                    X = Gdx.graphics.getWidth() - Joy.getWidth();
                if (Y < 0)
                    Y = 0;
                else if (Y > Gdx.graphics.getHeight() - Joy.getHeight())
                    Y = Gdx.graphics.getHeight() - Joy.getHeight();
                if (Y > 60)
                    Y = 60;
                if (X > 60)
                    X = 60;
                Vector2 Cur = new Vector2(X - 30, Y - 30);
                if (countdown <= 0) {

                    countdown = 1;

                    float Deg = Cur.angleDeg(Ox);
                    if ((Deg <= 45) || Deg > 315)
                        rightPressed = true;
                    if (Deg > 45 && Deg <= 135)
                        upPressed = true;
                    if ((Deg > 135 && Deg <= 225))
                        leftPressed = true;
                    if ((Deg > 225 && Deg <= 315))
                        downPressed = true;
                }

                //System.out.println( X + " " + Y + " " +    Cur.angleDeg(Ox));
                Joy.setPosition(X, Y);

            }

        } else {
            float aa = 0.5f;
            Joy.setPosition(BG.getX() + BG.getWidth() / 2 - Joy.getWidth() / 2 + aa, BG.getY() + BG.getHeight() / 2 - Joy.getHeight() / 2 + aa);
        }

    }

    public void draw() {
        countdown--;
        stage.draw();
        stage.act();
        //joyStick.draw();

    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isSetBoomPressed() {
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

    public boolean ispausePressed() {
        return pausePressed;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);

    }
}
