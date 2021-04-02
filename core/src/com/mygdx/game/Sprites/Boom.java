package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Screens.MyScreen;

public class Boom extends Sprite {

    public World world;
    public Body b2body;
    private Animation CountDown ;
    private Animation TimeOver ;
    public int Time = 150;
    private int Power = 10;
    float stateTimer;
    public FixtureDef fdef;
    private boolean isDestroy = false;
    public Boom(World world, Vector2 position,int direction, int Power ){
        super(GameManager.getAssetManager().get("Pack/PlayerandBoom.pack", TextureAtlas.class).findRegion("Bomb"));
        stateTimer=0;
        this.world = world;
        this.Power = Power;
        //==============|Create the box2d body|==========
        BodyDef bdef = new BodyDef();
        float x = position.x;
        float y = position.y;
        //System.out.println(x +  " " + y );
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
        //System.out.println(x +  " " + y );
        x = (float) (((int)(position.x*100))/20)/5 + (float)0.1;
        y =  (float) (((int)(position.y*100))/20)/5 + (float)0.1;
        //System.out.println(x +  " " + y );

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
        //=================|Run DOWN|

        Array<TextureRegion> frame = new Array<>();
        for (int i=12;i<15;i++){
            frame.add(new TextureRegion(getTexture(),i*20+3,11,20,20));


        }
        frame.add(new TextureRegion(getTexture(),13*20+3,11,20,20));

        CountDown = new Animation(0.3f,frame);
        setBounds(0,0,20/Main.PPM,20/Main.PPM);

        frame.clear();
        for (int i=15;i<22;i++){
            frame.add(new TextureRegion(getTexture(),i*20+3,11,20,20));


        }
        TimeOver = new Animation(0.2f,frame);




    }
    public void update(float dt) {

        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));


    }
    public TextureRegion getFrame(float dt){



        stateTimer=stateTimer+dt;
        TextureRegion region ;
        if (!isDestroy){
            region = (TextureRegion) CountDown.getKeyFrame(stateTimer,true);
        }
        else{
            region = (TextureRegion) TimeOver.getKeyFrame(stateTimer,false);
        }
        return region;
    }

    public void  Destroy(Player player,  Array<Items> BoxList, Array<Walls> WallList){
        isDestroy=true;
        stateTimer=0;
        if (CheckDead(player.b2body.getPosition(), b2body.getPosition(),Power,WallList))
            player.Dead();
        world.destroyBody(b2body);
        for (int i=0; i<BoxList.size;i++){
            final Items Temp = BoxList.get(i);

            if (CheckDead(Temp.body.getPosition(),b2body.getPosition(),Power,WallList))
            {
//                Temp.Destroy(world,(int)(Temp.body.getPosition().x*Main.PPM)/10,(int)(Temp.body.getPosition().y*Main.PPM)/10);
                Temp.Destroy(world,Temp.body.getPosition().x,Temp.body.getPosition().y);
                //BoxList.removeIndex(i);
                //i--;


            }
        }
    }
    private boolean CheckDead(Vector2 Point1, Vector2 Point2, float Power, Array<Walls> WallList){
        double Distance = Math.sqrt(Math.pow((Point1.x  - Point2.x), 2) + Math.pow((Point1.y  - Point2.y), 2));

        if (Distance < Power/Main.PPM )
        {
//            System.out.println((int)(Point2.x*100/20) + " "+ (int)(Point1.x*100/20) );
//            System.out.println((int)(Point2.y*100/20) +" "+(int)(Point1.y*100/20));
//
//            System.out.println("Distance: " + Distance);
//            System.out.println(Power/Main.PPM);
//            System.out.println(Point1.x + " "+ Point1.y);
            if ((int)(Point2.x*100/20) == (int)(Point1.x*100/20) )
            {

                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                    if ( (int)(Temp.x*100/20) == (int)(Point2.x*100/20)){
                       double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                       if(Distance >= Distance2){
                           return false;
                       }
                    }
                }
                return true;
            }else if ((int)(Point2.y*100/20) ==(int)(Point1.y*100/20)){
                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                    if ((int)(Point2.y*100/20) ==(int)(Temp.y*100/20)){
                        double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                        if(Distance >= Distance2){
                            return false;
                        }
                    }
                }
                return true;
            }
            else return false;
        }
        else return false;
    }

}
