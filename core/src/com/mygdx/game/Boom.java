package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Boom extends ObjectOnMap{
    Texture texture;
    boolean OnTime = false;

    public Boom(){
        texture = new Texture("badlogic.jpg");
    }
    public void Plant(int X, int Y){
        OnTime = true;
        x  = X;
        y = Y;
    };
    public void draw(){
        if(this.OnTime==true){
            Main.batch.draw(texture,x,y,size,size);
        }

    }
}
