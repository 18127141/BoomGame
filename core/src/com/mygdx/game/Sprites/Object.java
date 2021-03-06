package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.MyScreen;


public abstract class Object  {
    public World world;
    public TiledMap map;
    public TiledMapTile tile;
    public Rectangle bounds;
    public Body body;
    BodyDef bdef ;
    FixtureDef fdef;
    protected Fixture fixture;
    public Object(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        fdef = new FixtureDef();

        //Render the items
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((float)(bounds.getX()+bounds.getWidth()/2)/ Main.PPM,(float)(bounds.getY()+bounds.getHeight()/2)/Main.PPM);


        body = world.createBody(bdef);
        shape.setAsBox((float)bounds.getWidth()/2/Main.PPM,(float)bounds.getHeight()/2/Main.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = MyScreen.Wall ;
        fdef.filter.maskBits = MyScreen.PLAYER ;
        body.createFixture(fdef);

    }
    public void Destroy(World world,float x, float y){
        int xx=(int)(Math.ceil(x*10));
        int yy=(int)(Math.ceil(y*10));


        TiledMapTileLayer layer=(TiledMapTileLayer) map.getLayers().get(1);
        layer.getCell(xx,yy).setTile(null);
        layer.getCell(xx-1,yy).setTile(null);
        layer.getCell(xx,yy-1).setTile(null);
        layer.getCell(xx-1,yy-1).setTile(null);
        world.destroyBody(body);

    }
    public void setCategoryFiler(short filterBit){
        Filter filter =  new Filter();
        filter.categoryBits = filterBit;

    }
}
