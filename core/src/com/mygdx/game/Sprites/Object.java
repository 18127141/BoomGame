package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.MyScreen;


public abstract class Object {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    public Object(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        //Render the items
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((float)(bounds.getX()+bounds.getWidth()/2)/ Main.PPM,(float)(bounds.getY()+bounds.getHeight()/2)/Main.PPM);


        body = world.createBody(bdef);
        shape.setAsBox((float)bounds.getWidth()/2/Main.PPM,(float)bounds.getHeight()/2/Main.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = MyScreen.ITEMS ;
        fdef.filter.maskBits = MyScreen.PLAYER ;
        body.createFixture(fdef);
    }
    void Destroy(World world){
        world.destroyBody(body);
    }
}
