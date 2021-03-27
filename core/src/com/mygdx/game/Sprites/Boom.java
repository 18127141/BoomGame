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

    public Boom(World world, Vector2 position, int Power  ){
        super(GameManager.getAssetManager().get("Pack/PlayerandBoom.pack", TextureAtlas.class).findRegion("Bomb"));
        this.world = world;
        this.Power = Power;
        //==============|Create the box2d body|==========
        BodyDef bdef = new BodyDef();
        bdef.position.set(position)  ;
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9f/ Main.PPM);

        fdef.shape= shape;
        b2body.createFixture(fdef);
        b2body.setLinearDamping(10f);


    }
    public void  Destroy(Player player){
        if (Math.sqrt(Math.pow((player.b2body.getPosition().x - b2body.getPosition().x), 2) + Math.pow((player.b2body.getPosition().y - b2body.getPosition().y), 2)) < Power/5)
            player.Dead();
        world.destroyBody(b2body);
    }

}
