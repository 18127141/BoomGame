package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Main;

public class Player extends Sprite {
    public World world;
    public Body b2body;
    public Player(World world){
        this.world = world;

        //
        BodyDef bdef = new BodyDef();
        bdef.position.set(20/ Main.PPM,20/ Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9/ Main.PPM);

        fdef.shape= shape;
        b2body.createFixture(fdef);
    }
    public void handleInput(Controller controller){
        if (controller.isRightPressed()){
            this.b2body.applyLinearImpulse(new Vector2(0.1f,0),this.b2body.getWorldCenter(),true);
        }
        if (controller.isLeftPressed()){
            this.b2body.applyLinearImpulse(new Vector2(-0.1f,0),this.b2body.getWorldCenter(),true);
        }if (controller.isUpPressed()){
            this.b2body.applyLinearImpulse(new Vector2(0,0.1f),this.b2body.getWorldCenter(),true);
        }if (controller.isDownPressed()){
            this.b2body.applyLinearImpulse(new Vector2(0,-0.1f),this.b2body.getWorldCenter(),true);
        }
    }

}
