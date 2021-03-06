package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Screens.MyScreen;

public class Player extends Sprite {

    //
    //0 is Down
    //1 is Left
    //2 is Right
    //3 is Up
    public double x=0,y=0;
    public boolean main=true;
    public String name="";
    public long direction =0;
    public enum State{Run,Stand,Dead};
    public State state;
    public State previous;
    public World world;
    public Body b2body;
    public Array<Boom> BoomList ;
    private int boomCount =1;
    private int speedCount =-3;
    private int rangeCount = 1;
    public boolean ALIVE = true;

    public boolean ReallyDead = false;

    private int Life = 1;
    int MaxBoom = 3;
    int BoomMax = 9;
    final int MaxSpeed = 5;
    final int MaxRange = 100;
    public int Power =  40;
    final int TIME_PREPARE = 100;
    int Time_dead =100;
    private Array<TextureRegion> stand;

    private Animation RunDown;
    private Animation RunUp;
    private Animation RunLeft;
    private Animation RunRight;
    private Animation Dead;
    private Array<Animation> Run;
    public float stateTimer;
    //1s nut an se nhan 4 lan thong tin PressTrue nen bien nay de delay so lan dat bom 1s lai
    int TimePlanted = 0;
    //DEM THOI GIAN CO BOM MOI
    int PrepareTime = TIME_PREPARE;
    //SO BOM CO THE DAT
    int  AvaiableBoom = MaxBoom;

    //20 20
    //20 240
    //400 20
    //400 240
    public Player(World world,Array<Boom> BoomList,double x,double y,String name,boolean main){

        super(GameManager.getAssetManager().get("Pack/PlayerandBoom.pack", TextureAtlas.class).findRegion("playerAnimation"));
        //System.out.println(x+" "+y);
        this.BoomList = BoomList;
        this.world = world;
        this.name=name;
        this.main=main;
        this.x=x;
        this.y=y;
        //==============|Create the box2d body|==========
        if (main==true){
        BodyDef bdef = new BodyDef();
        bdef.position.set((float)x/ Main.PPM,(float)y/ Main.PPM);
        //bdef.position.set(x,y);

        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9f/ Main.PPM);

        fdef.shape= shape;
        fdef.filter.categoryBits = MyScreen.PLAYER ;
        fdef.filter.maskBits = (short) (MyScreen.Wall|MyScreen.BOOM);
        b2body.createFixture(fdef);

        //Ham nay se lam cho vat dung im khi khong chiu tac dung luc

        b2body.setLinearDamping(10f);
            Main.controller.updateStat("Boom",MaxBoom);
            Main.controller.updateStat("Power",(Power-20)/20);
            Main.controller.updateStat("Speed",speedCount+4);
            Main.controller.updateStat("Life",Life);
        }
        Texture black = GameManager.getAssetManager().get("Pack/DarkPlayer.png", Texture.class);
        //===========================|GET Player Graphics|===============
        state=State.Stand;
        previous= State.Stand;
        stateTimer=0;
        //Standing
        stand = new Array<TextureRegion>();
        for (int i=0;i<4;i++){
            if (main)
                stand.add( new TextureRegion(black,3*i*20+1,0,20,30));
            else
                stand.add( new TextureRegion(getTexture(),3*i*20+1,0,20,30));

        }



        Array<TextureRegion> frame = new Array<>();
        for (int i=1;i<3;i++){
            if (main)
                frame.add(new TextureRegion(black,i*20+1,0,20,30));
            else
                frame.add(new TextureRegion(getTexture(),i*20+1,0,20,30));
            frame.add(stand.get(0));

        }
        RunDown = new Animation(0.1f,frame);
        frame.clear();

        //======================Run LEFT
        for (int i=4;i<6;i++){
            if (main)
                frame.add(new TextureRegion(black,i*20+1,0,20,30));
            else
                frame.add(new TextureRegion(getTexture(),i*20+1,0,20,30));
            frame.add(stand.get(1));

        }
        //Array <TextureRegion>
        RunLeft = new Animation(0.1f,frame);
        frame.clear();

        //===============Run RIGHT
        for (int i=7;i<9;i++){
            if (main)
                frame.add(new TextureRegion(black,i*20+1,0,20,30));
            else
                frame.add(new TextureRegion(getTexture(),i*20+1,0,20,30));
            frame.add(stand.get(2));
        }
        RunRight = new Animation(0.1f,frame);
        frame.clear();

        //=======Run UP
        for (int i=10;i<12;i++){
            if (main)
                frame.add(new TextureRegion(black,i*20+1,0,20,30));
            else
                frame.add(new TextureRegion(getTexture(),i*20+1,0,20,30));
            frame.add(stand.get(3));

        }
        RunUp = new Animation(0.1f,frame);
        frame.clear();
        Run = new Array<>();
        Run.add(RunDown);
        Run.add(RunLeft);
        Run.add(RunRight);
        Run.add(RunUp);
        //dead
        frame.clear();
        for (int i=0;i<7;i++){
            if (main)
                frame.add(new TextureRegion(black,i*20+478,0,20,30));
            else
                frame.add(new TextureRegion(getTexture(),i*20+478,0,20,30));
            //478

        }
        Dead = new Animation(0.3f,frame);

        setBounds(0,0,20/Main.PPM,30/Main.PPM);
        setRegion(stand.get(0));


    }
    public void update(float dt){
        if (ALIVE == false){
            Time_dead-=1;

            if (Time_dead<=0){
                //animation chet ket thuc
                //kiem tra mang song

                    ReallyDead=true;





            }

        }
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/3);
        setRegion(getFrame(dt));


        //bug
        if (Math.abs(b2body.getLinearVelocity().x) <0.000001 || Math.abs(b2body.getLinearVelocity().y) <0.000001 ){
            b2body.setLinearVelocity(0,0);
        }
        //2 cai nay de tinh thoi gian  dat dc bom moi va thoi gian bom no

        if(TimePlanted >0) TimePlanted--;
        if(--PrepareTime==0) {
            if (MaxBoom > AvaiableBoom ) AvaiableBoom++;
            PrepareTime = TIME_PREPARE;

        }

    }
    public void updateother(float dt){
        setPosition((float)(x)-getWidth()/2,(float)(y)-getHeight()/3);

        setRegion(getFrame(dt));

    }
    public TextureRegion getFrame(float dt){
        if (main==true && ALIVE){
            State currentState = getState();
            state=currentState;
            updateDirection();
        }

        TextureRegion region;
        switch (state){
            case Stand:
                region = stand.get((int)direction);
                break;
            case Run:
                region = (TextureRegion) Run.get((int)direction).getKeyFrame(stateTimer,true);
                break;
            case Dead:
                region = (TextureRegion) Dead.getKeyFrame(stateTimer,false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
        stateTimer=state==previous?stateTimer+dt:0;
        previous=state;
        return region;
    }

    private void updateDirection() {
        if (b2body.getLinearVelocity().x>0){
            direction=2;
        }
        else if (b2body.getLinearVelocity().x<0){
            direction = 1;
        }
        else if (b2body.getLinearVelocity().y>0){
            direction=3;
        }
        else if (b2body.getLinearVelocity().y<0){
            direction=0;
        }
    }

    public State getState(){
        if( b2body.getLinearVelocity().x==0 && b2body.getLinearVelocity().y==0){
            return State.Stand;
        }
        else return State.Run;
    }
    public void handleInput(Controller controller,Main game){
        if (ALIVE== false) return;
        if (Math.abs(b2body.getLinearVelocity().x)<0.8+(0.5*speedCount)){
            b2body.setLinearVelocity(0,0);
        }

        if ((controller.isRightPressed() || Gdx.input.isKeyPressed(Input.Keys.D))&& this.b2body.getLinearVelocity().x<=2){
            this.b2body.setLinearVelocity(0,0);
            this.b2body.applyLinearImpulse(new Vector2(1f+(0.2f*speedCount),0),this.b2body.getWorldCenter(),true);
            direction = 2;

        }

        if ((controller.isLeftPressed() || Gdx.input.isKeyPressed(Input.Keys.A))&& this.b2body.getLinearVelocity().x >=-2){
            this.b2body.setLinearVelocity(0,0);

            this.b2body.applyLinearImpulse(new Vector2(-1f-(0.2f*speedCount),0),this.b2body.getWorldCenter(),true);
            direction=1;

        }
        if ((controller.isUpPressed() || Gdx.input.isKeyPressed(Input.Keys.W))&& this.b2body.getLinearVelocity().y <=2){
            this.b2body.setLinearVelocity(0,0);

            this.b2body.applyLinearImpulse(new Vector2(0,1f+(0.2f*speedCount)),this.b2body.getWorldCenter(),true);
            direction=3;
        }
        if ((controller.isDownPressed()|| Gdx.input.isKeyPressed(Input.Keys.S)) && this.b2body.getLinearVelocity().y >=-2){
            this.b2body.setLinearVelocity(0,0);

            this.b2body.applyLinearImpulse(new Vector2(0,-1f-(0.2f*speedCount)),this.b2body.getWorldCenter(),true);
            direction=0;
        }
        if (controller.isPlanted()){

            try{
            if (TimePlanted == 0  && AvaiableBoom >0 && ALIVE != false)
            {

                Boom Temp = new Boom(this.world, this.b2body.getPosition(),(int)this.direction,Power,true);
                boolean Add = true;
                for(int i =0; i< BoomList.size;i++){
                    if ((int)(BoomList.get(i).b2body.getPosition().x*100) == (int)(Temp.b2body.getPosition().x*100) && (int)(BoomList.get(i).b2body.getPosition().y*100) ==(int)(Temp.b2body.getPosition().y*100) )
                        Add = false;
                }
                if (Add) {
                    BoomList.add(Temp);
                    TimePlanted=10;
                    AvaiableBoom--;
                    //ADD boom
                    game.db.AddBoom(game.roomname,game.playerName);
                    if (game.checkSound){
                        GameManager.getAssetManager().get("sounds/PlaceBomb.ogg", Sound.class).play(game.soundPosition/100);
                    }

                }
                else {
                    world.destroyBody(Temp.b2body);

                }

            }
            }catch (Exception e){
                System.out.println("plant");
            }
        }
    }
    public void Dead(){

        if(--Life==0){
            ALIVE = false;
            state=State.Dead;
            previous=State.Dead;
            stateTimer=0;
        }
        if (main){
            Main.controller.updateStat("Life",Life);

        }

    }
    public void EarnItem(int type,int value){


        if (type == 1){
            MaxBoom+=value;

            if (MaxBoom>BoomMax){
                MaxBoom=BoomMax;
            }
            if (main)
                Main.controller.updateStat("Boom",MaxBoom);
        }
        else if(type==2){

            Power+=value;
            if (Power>MaxRange){
                Power=MaxRange;
            }
            if (main)
                Main.controller.updateStat("Power",(Power-20)/20);
        }
        else if(type==3){
            speedCount+=value;

            if (speedCount>MaxSpeed){
                speedCount=MaxSpeed;
            }
            if (main)

                Main.controller.updateStat("Speed",speedCount+4);

        }else if (type==4){

            Life+=1;

            if (main)

                Main.controller.updateStat("Life",Life);


        }

    }
}
