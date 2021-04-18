package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
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

import java.time.format.DateTimeFormatter;

public class Boom extends Sprite {

    public World world;
    public Body b2body;
    private Animation CountDown ;
    private Animation TimeOver ;
    public int Time = 150;
    private int Power = 5;
    float stateTimer;
    public FixtureDef fdef;
    private boolean isDestroy = false;
    Array<Items>   DetroyList;
    int Left,Right,Up,Down;
    //
    Explosion explosion;

    //
    public Boom(World world, Vector2 position,int direction, int Power ){
        super(GameManager.getAssetManager().get("Pack/PlayerandBoom.pack", TextureAtlas.class).findRegion("Bomb"));
        stateTimer=0;
        this.world = world;
        this.Power = Power;
        Left = Right =Up= Down = Power/20-1;
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

        //



    }
    public void update(float dt) {
        if (Time >40){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
        }
        else{
            explosion.update(dt);
        }


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
        GameManager.getAssetManager().get("sounds/Explosion.ogg", Sound.class).play();

        isDestroy=true;
        stateTimer=0;
        Array<Items> MaybeDetroy = new Array();
        world.destroyBody(b2body);
        for (int i=0; i<BoxList.size;i++){
            final Items Temp = BoxList.get(i);
            if (CheckDead(Temp.body.getPosition(),b2body.getPosition(),Power,WallList) )
            {
                MaybeDetroy.add(Temp);
            }
        }
        DetroyList = new Array();
        for (int i=0; i <MaybeDetroy.size;i++){
            Items Temp = MaybeDetroy.get(i);
            if (CheckCollision(Temp.body.getPosition(),b2body.getPosition(),Power,MaybeDetroy)){
                DetroyList.add(Temp);
            }
        }
        if (CheckDead(player.b2body.getPosition(), b2body.getPosition(),Power,WallList) ==true && CheckDeadBox(player.b2body.getPosition(), b2body.getPosition(),Power,MaybeDetroy) ==true)
            player.Dead();
        for(int i=0; i<WallList.size;i++){
            if (WallList.get(i).body.getPosition().x > b2body.getPosition().x && (int)((WallList.get(i).body.getPosition().y*100+5)/20) == (int)((b2body.getPosition().y*100+5)/20) && Distance(WallList.get(i).body.getPosition(),b2body.getPosition())< Right+1   )
                {
                    this.Right =Distance(WallList.get(i).body.getPosition(),b2body.getPosition()) -1;
                    System.out.println("R "+ Distance(WallList.get(i).body.getPosition(),b2body.getPosition()));
                }
            if (WallList.get(i).body.getPosition().x < b2body.getPosition().x && (int)((WallList.get(i).body.getPosition().y*100+5)/20) == (int)((b2body.getPosition().y*100+5)/20) && Distance(WallList.get(i).body.getPosition(),b2body.getPosition())< Left+1 )
                {
                    this.Left =Distance(WallList.get(i).body.getPosition(),b2body.getPosition()) -1;
                    System.out.println("L "+ Distance(WallList.get(i).body.getPosition(),b2body.getPosition()));
                }
            if (WallList.get(i).body.getPosition().y > b2body.getPosition().y && (int)((WallList.get(i).body.getPosition().x*100+5)/20) == (int)((b2body.getPosition().x*100+5)/20)  && Distance(WallList.get(i).body.getPosition(),b2body.getPosition())< Up+1  )
            {
                this.Up =Distance(WallList.get(i).body.getPosition(),b2body.getPosition()) -1;
                System.out.println("U "+Distance(WallList.get(i).body.getPosition(),b2body.getPosition()));
            }
            if (WallList.get(i).body.getPosition().y < b2body.getPosition().y && (int)((WallList.get(i).body.getPosition().x*100+5)/20) == (int)((b2body.getPosition().x*100+5)/20)  && Distance(WallList.get(i).body.getPosition(),b2body.getPosition())< Down+1 )
            {
                this.Down =Distance(WallList.get(i).body.getPosition(),b2body.getPosition())-1;
                System.out.println("D "+Distance(WallList.get(i).body.getPosition(),b2body.getPosition()));
            }
            System.out.print(i);
            System.out.println(" Distance "+ Distance(WallList.get(i).body.getPosition(),b2body.getPosition()) + " coordinatex " + WallList.get(i).body.getPosition().x + " " + WallList.get(i).body.getPosition().y);

        }

        for (int i =0;i<DetroyList.size;i++){
            if (DetroyList.get(i).body.getPosition().x > b2body.getPosition().x && (int)(DetroyList.get(i).body.getPosition().y*100/20) == (int)(b2body.getPosition().y*100/20)   )
                this.Right =Distance(DetroyList.get(i).body.getPosition(),b2body.getPosition());
            else if (DetroyList.get(i).body.getPosition().x < b2body.getPosition().x && (int)(DetroyList.get(i).body.getPosition().y*100/20) == (int)(b2body.getPosition().y*100/20) )
                this.Left =Distance(DetroyList.get(i).body.getPosition(),b2body.getPosition());
            else if (DetroyList.get(i).body.getPosition().y > b2body.getPosition().y && (int)(DetroyList.get(i).body.getPosition().x*100/20) == (int)(b2body.getPosition().x*100/20)  )
                this.Up =Distance(DetroyList.get(i).body.getPosition(),b2body.getPosition());
            else if (DetroyList.get(i).body.getPosition().y < b2body.getPosition().y && (int)(DetroyList.get(i).body.getPosition().x*100/20) == (int)(b2body.getPosition().x*100/20) )
                this.Down =Distance(DetroyList.get(i).body.getPosition(),b2body.getPosition());
            DetroyList.get(i).Destroy(world,DetroyList.get(i).body.getPosition().x,DetroyList.get(i).body.getPosition().y);
        }
        if ((int)((this.b2body.getPosition().x*100+5)/20) - Left <=0 )
            Left =(int)((this.b2body.getPosition().x*100+5)/20) -1;
        if ((int)((this.b2body.getPosition().y*100+5)/20) - Down <=0 )
            Down =(int)((this.b2body.getPosition().y*100+5)/20) -1 ;
        if ((int)((this.b2body.getPosition().x*100+5)/20) + Right >=19 )
            Right =20-(int)((this.b2body.getPosition().x*100+5)/20) -1 ;
        if ((int)((this.b2body.getPosition().y*100+5)/20) + Up >=11 )
            Up =12-(int)((this.b2body.getPosition().y*100+5)/20) -1;
        if (Right<0)  Right=0;
        if (Left<0) Left=0;
        if (Down < 0)  Down =0;
        if (Up < 0) Up=0;
        explosion = new Explosion(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2,Left,Right,Up, Down);


     }
     int Distance (Vector2 P1, Vector2 P2){

        return (int)(((Math.sqrt(Math.pow((P1.x  - P2.x), 2) + Math.pow((P1.y  - P2.y), 2)))*100 +5)/20);
     }
    private boolean CheckDead(Vector2 Point1, Vector2 Point2, float Power, Array<Walls> WallList){
        double Distance = Math.sqrt(Math.pow((Point1.x  - Point2.x), 2) + Math.pow((Point1.y  - Point2.y), 2));
        if (Math.ceil(Distance*100) < Power )
        {
            if ((int)((Point2.x*100+5)/20) == (int)((Point1.x*100+5)/20) )
            {

                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                    if ( (int)((Temp.x*100+5)/20) == (int)((Point2.x*100+5)/20)){
                        double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                        if((int)(Distance*1000) > (int)(Distance2*1000)   && ( (Point2.y< Temp.y && Point1.y > Temp.y) || (Point2.y> Temp.y && Point1.y < Temp.y) )){
                            return false;
                        }
                    }
                }
                System.out.print("TRUE1");
                return true;

            }else if ((int)((Point2.y*100+5)/20) ==(int)((Point1.y*100+5)/20)  ){
                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                    if ((int)((Point2.y*100+5)/20) ==(int)((Temp.y*100+5)/20)){
                        double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                        if((int)(Distance*1000) > (int)(Distance2*1000)   &&  ( (Point2.x< Temp.x && Point1.x > Temp.x) || (Point2.x> Temp.x && Point1.x < Temp.x) )){
                            return false;
                        }
                    }
                }
                System.out.print("TRUE1");
                return true;
            }
            else return false;
        }
        else return false;
    }
    private boolean CheckDeadBox(Vector2 Point1, Vector2 Point2, float Power, Array<Items> WallList){
        double Distance = Math.sqrt(Math.pow((Point1.x  - Point2.x), 2) + Math.pow((Point1.y  - Point2.y), 2));

        if (Math.ceil(Distance*100) <= Power )
        {
            System.out.print("Start");
            if( (int)((Point2.x*100+5)/20) == (int)((Point1.x*100+5)/20) ) {
                System.out.print("TRUE2");
                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                        if ( (int)((Temp.x*100+5)/20) == (int)((Point2.x*100+5)/20) && ( (Point2.y< Temp.y && Point1.y > Temp.y) || (Point2.y> Temp.y && Point1.y < Temp.y) )){
                            double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                            if((int)(Distance*1000) > (int)(Distance2*1000)  ){
                                return false;
                            }
                        }

                }
                System.out.print("TRUE2");
                return true;
            }else if ((int)((Point2.y*100+5)/20) ==(int)((Point1.y*100+5)/20)){
                System.out.print("TRUE2");
                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                        if ((int)((Point2.y*100+5)/20) ==(int)((Temp.y*100+5)/20) &&  ( (Point2.x< Temp.x && Point1.x > Temp.x) || (Point2.x> Temp.x && Point1.x < Temp.x) )){
                            double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                            if((int)(Distance*1000) > (int)(Distance2*1000) ){
                                return false;
                            }
                        }
                }
                System.out.print("TRUE2");
                return true;
            }
            else return false;
        }
        else return false;
    }

    private boolean CheckCollision(Vector2 Point1, Vector2 Point2, float Power, Array<Items> WallList){

        double Distance = Math.sqrt(Math.pow((Point1.x  - Point2.x), 2) + Math.pow((Point1.y  - Point2.y), 2));

        if (Math.ceil(Distance*100) <= Power )
        {
            if ((int)((Point2.x*100+5)/20)  == (int)((Point1.x*100+5)/20)  )
            {
                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                    if (!Point1.epsilonEquals(Temp) ){
                        if ( (int)((Temp.x*100+5)/20)   == (int)((Point2.x*100+5)/20)   && ( (Point2.y< Temp.y && Point1.y > Temp.y) || (Point2.y> Temp.y && Point1.y < Temp.y) )){
                            double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                            if((int)(Distance*1000) > (int)(Distance2*1000)  ){
                                return false;
                            }
                        }
                    }
                }
                return true;
            }else if ((int)((Point2.y*100+5)/20)   ==(int)((Point1.y*100+5)/20)  ){
                for (int i=0; i<WallList.size;i++){
                    Vector2 Temp = WallList.get(i).body.getPosition();
                    if (!Point1.epsilonEquals(Temp) ){
                        if ((int)((Point2.y*100+5)/20)   ==(int)((Temp.y*100+5)/20)    &&  ( (Point2.x< Temp.x && Point1.x > Temp.x) || (Point2.x> Temp.x && Point1.x < Temp.x) )){
                            double Distance2 = Math.sqrt(Math.pow((Temp.x  - Point2.x), 2) + Math.pow((Temp.y  - Point2.y), 2));
                            if((int)(Distance*1000) > (int)(Distance2*1000)    ){
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
            else return false;
        }
        else return false;
    }
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (Time<=40){
            explosion.draw(batch);
        }

    }
}
