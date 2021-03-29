package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class Player extends Sprite {
    //
    //0 is Down
    //1 is Left
    //2 is Right
    //3 is Up

    int direction =0;
    public enum State{Run,Stand};
    private State state;
    private State previous;
    public World world;
    public Body b2body;
    public Array<Boom> BoomList ;
    private int boomCount =1;
    private int speedCount =10;
    private int rangeCount = 1;
    boolean ALIVE = true;
    final int MaxBoom = 5;
    final int MaxSpeed = 5;
    final int MaxRange = 5;
    int Power =  5;
    final int TIME_PREPARE = 100;
    private Array<TextureRegion> stand;

    private Animation RunDown;
    private Animation RunUp;
    private Animation RunLeft;
    private Animation RunRight;
    private Array<Animation> Run;
    private float stateTimer;
    //1s nut an se nhan 4 lan thong tin PressTrue nen bien nay de delay so lan dat bom 1s lai
    int TimePlanted = 0;
    //DEM THOI GIAN CO BOM MOI
    int PrepareTime = TIME_PREPARE;
    //SO BOM CO THE DAT
    int  AvaiableBoom = MaxBoom;

    public Player(World world,Array<Boom> BoomList){
        super(GameManager.getAssetManager().get("Pack/PlayerandBoom.pack", TextureAtlas.class).findRegion("playerAnimation"));
        this.BoomList = BoomList;
        this.world = world;

        //==============|Create the box2d body|==========
        BodyDef bdef = new BodyDef();
        bdef.position.set(20/ Main.PPM,20/ Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9f/ Main.PPM);

        fdef.shape= shape;
        b2body.createFixture(fdef);
        //Ham nay se lam cho vat dung im khi khong chiu tac dung luc

        b2body.setLinearDamping(10f);

        //===========================|GET Player Graphics|===============
        state=State.Stand;
        previous= State.Stand;
        stateTimer=0;
        //Standing
        stand = new Array<TextureRegion>();
        for (int i=0;i<4;i++){
            stand.add( new TextureRegion(getTexture(),3*i*20,0,20,30));

        }


        //=================|Run DOWN|
        Array<TextureRegion> frame = new Array<>();
        for (int i=1;i<3;i++){
            frame.add(new TextureRegion(getTexture(),i*20,0,20,30));
            frame.add(stand.get(0));

        }
        RunDown = new Animation(0.1f,frame);
        frame.clear();

        //======================Run LEFT
        for (int i=4;i<6;i++){
            frame.add(new TextureRegion(getTexture(),i*20,0,20,30));
            frame.add(stand.get(1));

        }
        RunLeft = new Animation(0.1f,frame);
        frame.clear();

        //===============Run RIGHT
        for (int i=7;i<9;i++){
            frame.add(new TextureRegion(getTexture(),i*20,0,20,30));
            frame.add(stand.get(2));
        }
        RunRight = new Animation(0.1f,frame);
        frame.clear();

        //=======Run UP
        for (int i=10;i<12;i++){
            frame.add(new TextureRegion(getTexture(),i*20,0,20,30));
            frame.add(stand.get(3));

        }
        RunUp = new Animation(0.1f,frame);
        frame.clear();
        Run = new Array<>();
        Run.add(RunDown);
        Run.add(RunLeft);
        Run.add(RunRight);
        Run.add(RunUp);

        setBounds(0,0,20/Main.PPM,30/Main.PPM);
        setRegion(stand.get(0));


    }
    public void update(float dt){
        if (ALIVE == false)
            return;
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
    public TextureRegion getFrame(float dt){
        State currentState = getState();
        updateDirection();
        TextureRegion region;
        switch (currentState){
            case Stand:
                region = stand.get(direction);
                break;
            case Run:
                region = (TextureRegion) Run.get(direction).getKeyFrame(stateTimer,true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentState);
        }
        stateTimer=currentState==previous?stateTimer+dt:0;
        previous=currentState;
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
    public void handleInput(Controller controller){
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
            if (TimePlanted == 0  && AvaiableBoom >0 && ALIVE != false)
            {
                Boom Temp = new Boom(this.world, this.b2body.getPosition(),Power);
                BoomList.add(Temp);
                System.out.println("HEHE");
                TimePlanted=10;
                AvaiableBoom--;
            }
        }
    }
    public void Dead(){
        ALIVE = false;
    }
}
