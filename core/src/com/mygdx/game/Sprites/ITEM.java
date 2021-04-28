package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Screens.MyScreen;

public class ITEM  {
    public boolean isDestroy = false;
    private Animation TimeOver;
    private float x, y;
    private Sprite sprite;
    private float stateTimer = 0;
    public int Time = 50;
    private String name;
    public int typeItem  ;
    private int value;
    public World world;
    public TiledMap map;
    public TiledMapTile tile;
    public Rectangle bounds;
    public Body body;
    BodyDef bdef ;
    FixtureDef fdef;
    Array<TextureRegion> frame;
    protected Fixture fixture;
    public ITEM(World world, TiledMap map, Rectangle bounds,double x,double y,long type) {

        this.world = world;
        this.map = map;
        this.bounds = bounds;
        // HAM RANDOM VAT PHAM
        typeItem = (int) type;

        GenerateValue();

        bdef = new BodyDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9f/ Main.PPM);
        fdef = new FixtureDef();
        //Render the items
        bdef.type = BodyDef.BodyType.StaticBody;
        //if (type==-1)
            bdef.position.set((float)(bounds.getX()+bounds.getWidth()/2)/ Main.PPM,(float)(bounds.getY()+bounds.getHeight()/2)/Main.PPM);
//        else{
//            bdef.position.set((float)x,(float)y);
//            typeItem = (int) type;
//
//        }

        body = world.createBody(bdef);
        fdef.shape = shape;
        fdef.filter.categoryBits = MyScreen.ITEMS ;
        fdef.filter.maskBits = (short) (MyScreen.Wall);
        body.createFixture(fdef);

        //Animation cu~
        sprite = new Sprite((Texture) GameManager.getAssetManager().get("Pack/items.png"));
        sprite.setBounds(body.getPosition().x-10/Main.PPM, body.getPosition().y-10/Main.PPM,20 / Main.PPM, 20 / Main.PPM);
        frame = new Array<>();
        frame.add(new TextureRegion(sprite.getTexture(), 25, 3, 20, 20));
        frame.add(new TextureRegion(sprite.getTexture(), 3, 3, 20, 20));
        frame.add(new TextureRegion(sprite.getTexture(), 47, 3, 20, 20));
        frame.add(new TextureRegion(sprite.getTexture(), 47+22, 3, 20, 20));



        if (typeItem == 0) {
            this.Destroy(world,body.getPosition().x,body.getPosition().y);
        }
    }

    public void Destroy(World world,float x, float y){
        if (isDestroy == false){
            world.destroyBody(body);
            this.x = x;
            this.y = y;
            isDestroy = true;
        }


    }
    public void setCategoryFiler(short filterBit){
        Filter filter =  new Filter();
        filter.categoryBits = filterBit;

    }

    private void GenerateValue(){
        //1 Booom
        //2 Power
        //3 Speed
        //4 Life
        //typeItem = (int)(Math.random()*5);
        if (typeItem==1 || typeItem==3 || typeItem==4){
            value=1;
        }else if(typeItem==2){
            value=20;
        }
    }
    public void GetEffect(Player player){

        Vector2 Point1 = player.b2body.getPosition();
        Vector2 Point2 = this.body.getPosition();
        double Distance = Math.sqrt(Math.pow((Point1.x  - Point2.x), 2) + Math.pow((Point1.y  - Point2.y), 2));
        if (Distance*100 <20){
            player.EarnItem(this.typeItem,this.value);
            this.Destroy(world,body.getPosition().x,body.getPosition().y);
        }

    }
    public void GetEffectOther(Player player){

        Vector2 Point1 = new Vector2((float)player.x,(float)player.y);

        Vector2 Point2 = this.body.getPosition();
        double Distance = Math.sqrt(Math.pow((Point1.x  - Point2.x), 2) + Math.pow((Point1.y  - Point2.y), 2));
        if (Distance*100 <20){
            player.EarnItem(this.typeItem,this.value);
            this.Destroy(world,body.getPosition().x,body.getPosition().y);
        }

    }

    public void update(float dt) {
        if (!isDestroy) {
            sprite.setRegion(getFrame(dt));
        }
    }
    private TextureRegion getFrame(float dt) {
        return frame.get(typeItem-1);
    }
    public void draw(SpriteBatch batch) {
        if (!isDestroy) {
            sprite.draw(batch);
        }

    }
}

