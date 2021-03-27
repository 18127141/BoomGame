package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class CountDown extends ObjectOnMap{
    int time =5;
    Body Boom ;
    World world;
    public CountDown(int time, Body boom, World world){
        this.time = time;
        this.world = world;
        this.Boom = boom;
        this.run();
    }
    public void run() {
        try {
            while(time >=0) {
                time -= 1;
                Thread.sleep(1000);
            }
            world.destroyBody(Boom);
        }catch (Exception e){

        }

    }
}
