package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Boom extends ObjectOnMap{
    Texture texture;
    boolean OnTime = false;
    int time =5;
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
//            if (time == 5){
//                this.start();
//            }
            Main.batch.draw(texture,x,y,size,size);
        }
    }
    
    public void run() {
        try {
            while(time >=0) {
                time -= 1;
                Thread.sleep(1000);
            }
            OnTime = false;
        }catch (Exception e){

        }

    }
}
