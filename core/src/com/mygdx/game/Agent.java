package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Agent extends ObjectOnMap {
    //Display
    boolean setBoom =false;

    Texture texture;

    ArrayList<Boom> boomList = new ArrayList<Boom>();
    public Agent(){
        x = 200;
        y = 50;
        texture = new Texture("Agent.png");


    }
    public void handleInput(Controller controller) {
        if (controller.isRightPressed() && x <= rangeX[1]  )
            x+=10;
        else if (controller.isLeftPressed() && x >= rangeX[0])
            x-=10;
        else if(controller.isDownPressed() && y >= rangeY[0])
            y-=10;
        else if(controller.isUpPressed() && y <= rangeY[1])
            y+=10;
        else if(controller.isSetBoomPressed()){
            setBoom = true;
            boomList.add(new Boom());
            boomList.get(boomList.size()-1).Plant(this.x, this.y);
            setBoom = false;
        }
    }
    public void draw(){
        for (int i=0; i<boomList.size();i++){
            if (boomList.get(i).time>0)
                boomList.get(i).draw();
            else boomList.remove(i);
        }
        Main.batch.draw(texture,x,y,size,size);

    }
}
