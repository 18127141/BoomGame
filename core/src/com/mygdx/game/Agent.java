package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Agent extends ObjectOnMap {
    //Display
    boolean setBoom =false;

    Texture texture;
    Boom boom;
    public Agent(){
        x = 200;
        y = 50;
        texture = new Texture("Agent.png");
        boom = new Boom();

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
            boom.Plant(this.x, this.y);
        }
    }
    public void draw(){
        boom.draw();
        Main.batch.draw(texture,x,y,size,size);

    }
}
