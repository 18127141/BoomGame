package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;

public class Items extends Object{
    public boolean isDestroy=false;
    private boolean isDead = false;
    private Animation TimeOver ;
    private float x,y;
    private Sprite sprite;
    private float stateTimer=0;
    public int Time =50;
    public Items(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        sprite = new Sprite((Texture) GameManager.getAssetManager().get("Pack/objects.png"));
        Array<TextureRegion> frame = new Array<>();
        for (int i=0;i<6;i++){
            frame.add(new TextureRegion(sprite.getTexture(),i*20,20,20,20));


        }
        TimeOver = new Animation(0.2f,frame);


    }
    public void Destroy(World world,float x,float y){
        super.Destroy(world,x,y);

        this.x = x;
        this.y =y;
        isDestroy =true;
        sprite.setBounds(this.x-10/Main.PPM,this.y-10/Main.PPM,20/Main.PPM,20/Main.PPM);


    }
    public void update(float dt){
        if (isDestroy){
            sprite.setRegion(getFrame(dt));

        }
    }

    private TextureRegion getFrame(float dt) {
        stateTimer=stateTimer+dt;
        return (TextureRegion) TimeOver.getKeyFrame(stateTimer,false);
    }
    public void draw(SpriteBatch batch){
        if (isDestroy){
            sprite.draw(batch);

        }
    }
}

