package com.mygdx.game.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
public class JoyStick {
    Viewport viewport;
    public Stage stage;
    public boolean upPressed, downPressed, leftPressed, rightPressed, setBoomPressed,pausePressed;
    public OrthographicCamera cam;
    Image Joy ;
    Image Center;
    Vector2 Ox;
    public JoyStick(){
        Ox = new Vector2(16,0);
        cam = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        stage = new Stage(viewport, Main.batch);


        Gdx.input.setInputProcessor(stage);

        int w=20;
        int h=20;

        Joy = new Image(new Texture("flatDark25.png"));
        Joy.setSize(50,50);
        Joy.setPosition(20,20);
        Center = new Image(new Texture("flatDark25.png"));
        Center.setSize(1,1);
        Center.setPosition(16,16);
        stage.addActor(Joy);
        stage.addActor(Center);


    }
    public void Joy(Controller controller){

        upPressed=false;
        leftPressed=false;
        rightPressed=false;
        downPressed=false;
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){

            System.out.println( Gdx.input.getX() + " "+ Gdx.input.getY() );
            float X =Gdx.input.getX()-30;
            float Y =Gdx.graphics.getHeight() - Gdx.input.getY()-55;
            System.out.println( X + " " + Y + " " );

            if (X < Gdx.graphics.getWidth()/2){
                if (X<0 )
                    X=0;
                else if(X>Gdx.graphics.getWidth()-this.Joy.getWidth())
                    X = Gdx.graphics.getWidth()-this.Joy.getWidth();
                if (Y<0)
                    Y=0;
                else if(Y>Gdx.graphics.getHeight()-this.Joy.getHeight())
                    Y = Gdx.graphics.getHeight()-this.Joy.getHeight();
                if (Y >32)
                    Y = 32;
                if (X >32)
                    X=32;
                Vector2 Cur = new Vector2(X-Center.getX(),Y-Center.getY());
                float Deg = Cur.angleDeg(Ox);
                if ((Deg <=45) || Deg>=315)
                    rightPressed=true;
                else if (Deg>=45 && Deg <=135)
                    upPressed =true;
                else if ((Deg>=135 && Deg <=225))
                    leftPressed=true;
                else if ((Deg>=225 && Deg <=315) )
                    downPressed=true;
                System.out.println( X + " " + Y + " " +    Cur.angleDeg(Ox));
                this.Joy.setPosition(X,Y);
            }

        }else
            this.Joy.setPosition(16,16);
        controller.rightPressed= this.rightPressed;
        controller.upPressed = this.upPressed;
        controller.leftPressed=this.leftPressed;
        controller.downPressed=this.downPressed;
    }
    public void draw(){
        stage.draw();
    }
}
