package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;

public class Items extends Object{
    private boolean isDestroy=false;
    private boolean isDead = false;
    private Animation TimeOver ;

    public Items(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        Sprite sprite = new Sprite((Texture) GameManager.getAssetManager().get("Pack/objects.png"));
        Array<TextureRegion> frame = new Array<>();
        for (int i=0;i<6;i++){
            frame.add(new TextureRegion(sprite.getTexture(),i*20+3,20,20,20));


        }
        TimeOver = new Animation(0.3f,frame);
        sprite.setBounds(0,0,20/Main.PPM,20/Main.PPM);


    }
    public void Destroy(World world,int x,int y){
        super.Destroy(world,x,y);
        isDestroy =true;

    }
    public void update(float dt){
        if (isDestroy){

        }
    }
}

