package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Screens.MyScreen;

import org.w3c.dom.css.Rect;

import java.sql.Time;
import java.util.ArrayList;

public class Boom extends Sprite {

    public World world;
    public Body b2body;
    private Animation CountDown ;
    private Animation TimeOver ;
    public int Time = 200;
    private int Power = 10;
    public FixtureDef fdef;
    public Boom(World world, Vector2 position,int direction, int Power  ){
        super(GameManager.getAssetManager().get("Pack/PlayerandBoom.pack", TextureAtlas.class).findRegion("Bomb"));
        this.world = world;
        this.Power = Power;
        //==============|Create the box2d body|==========
        BodyDef bdef = new BodyDef();
        float x = position.x;
        float y = position.y;
        System.out.println(x +  " " + y );
        switch (direction){
            case 0://Down
                y -=20/Main.PPM;
                break;
            case 1: // Left
                x-=20/Main.PPM;
                break;
            case 2: //Right
                x+=20/Main.PPM;
                break;
            case 3://Up
                y+=20/Main.PPM;
                break;
        }

        System.out.println(x +  " " + y );
        x = (float) (((int)(position.x*100))/20)/5 + (float)0.1;
        y =  (float) (((int)(position.y*100))/20)/5 + (float)0.1;
        System.out.println(x +  " " + y );

        bdef.position.set(x, y)  ;

        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9f/ Main.PPM);

        fdef.shape= shape;
        fdef.filter.categoryBits =MyScreen.BOOM ;
        fdef.filter.maskBits = MyScreen.ITEMS ;
        b2body.createFixture(fdef);
        b2body.setLinearDamping(10f);





    }
    public void  Destroy(Player player, Array<Items> BoxList){
        if (CheckDead(player.b2body.getPosition(), b2body.getPosition(),Power))
            player.Dead();
        world.destroyBody(b2body);
        for (int i=0; i<BoxList.size;i++){
            Items Temp = BoxList.get(i);
            if (CheckDead(Temp.body.getPosition(),b2body.getPosition(),Power))
            {
                Temp.Destroy(world);
                BoxList.removeIndex(i);
                i--;
            }
        }
    }
    private boolean CheckDead(Vector2 Point1, Vector2 Point2, float Power){
        double Distance = Math.sqrt(Math.pow((Point1.x  - Point2.x), 2) + Math.pow((Point1.y  - Point2.y), 2));

        if (Distance < Power/Main.PPM )
        {
            System.out.println((int)(Point2.x*100/20) + " "+ (int)(Point1.x*100/20) );
            System.out.println((int)(Point2.y*100/20) +" "+(int)(Point1.y*100/20));
            if ((int)(Point2.x*100/20) == (int)(Point1.x*100/20) || (int)(Point2.y*100/20) ==(int)(Point1.y*100/20) )
                return true;
            else return false;
        }
        else return false;
    }
}
