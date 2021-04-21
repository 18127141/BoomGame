package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
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
import com.mygdx.game.Screens.MyScreen;

public class Items extends Object {
    public boolean isDestroy = false;
    private boolean isDead = false;
    private Animation TimeOver;
    private float x, y;
    private Sprite sprite;
    private float stateTimer = 0;
    public int Time = 50;
    private String name;
    private Rectangle bounds;
    private TiledMap map;
    private World world;
    private Array<ITEM> ItemList;
    public Items(World world, TiledMap map, Rectangle bounds, Array<ITEM> ItemList) {
        super(world, map, bounds);
        this.bounds = bounds;
        this.world=world;
        this.map=map;
        this.ItemList=ItemList;
        sprite = new Sprite((Texture) GameManager.getAssetManager().get("Pack/objects.png"));
        Array<TextureRegion> frame = new Array<>();
        if (MyScreen.mapName.equals("Forest")) {
            frame.add(new TextureRegion(sprite.getTexture(), 0 * 20, 20, 20, 20));
        } else if (MyScreen.mapName.equals("Temple")) {
            frame.add(new TextureRegion(sprite.getTexture(), 0 * 20, 40, 20, 20));
        } else if (MyScreen.mapName.equals("Cave")) {
            frame.add(new TextureRegion(sprite.getTexture(), 0 * 20, 100, 20, 20));
        } else if (MyScreen.mapName.equals("Sewer")) {
            frame.add(new TextureRegion(sprite.getTexture(), 0 * 20, 140, 20, 20));

        }


        for (int i = 1; i < 6; i++) {
            if (MyScreen.mapName.equals("Forest"))
                frame.add(new TextureRegion(sprite.getTexture(), i * 20, 20, 20, 20));
            else if (MyScreen.mapName.equals("Temple") || MyScreen.mapName.equals("Cave"))
                frame.add(new TextureRegion(sprite.getTexture(), i * 20, 40, 20, 20));
            else if (MyScreen.mapName.equals("Sewer")) {
                frame.add(new TextureRegion(sprite.getTexture(), i * 20, 60, 20, 20));

            }


        }
        TimeOver = new Animation(0.2f, frame);


    }

    public void Destroy(World world, float x, float y) {
        if (isDestroy == false){
            ITEM item =new ITEM(this.world,this.map,this.bounds);
            this.ItemList.add(item);
            super.Destroy(world, x, y);
            this.x = x;
            this.y = y;
            isDestroy = true;
            sprite.setBounds(this.x - 10 / Main.PPM, this.y - 10 / Main.PPM, 20 / Main.PPM, 20 / Main.PPM);
        }
    }

    public void update(float dt) {
        if (isDestroy) {
            sprite.setRegion(getFrame(dt));
        }
    }

    private TextureRegion getFrame(float dt) {
        stateTimer = stateTimer + dt;
        return (TextureRegion) TimeOver.getKeyFrame(stateTimer, false);
    }

    public void draw(SpriteBatch batch) {
        if (isDestroy) {
            sprite.draw(batch);

        }
    }
}

