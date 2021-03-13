package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Map {
    //Display
    Viewport viewport;
    Stage stage;
    OrthographicCamera cam;
    Texture box;
    int size = 50;
    int Area [][];
    public Map(){

        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, Main.batch);
        box = new Texture("Box.png");

        Area = new int[10][10];

        for (int i =0 ; i< 10 ; i++){
            for (int j =0 ; j< 10 ; j++){
                Area[i][j] = 1;
            }
        }

    }
    public void draw(){

        for (int i =0; i< 10; i++ ){
            Main.batch.draw(box,150,  i*50,size,size);
            Main.batch.draw(box,150 + 50*10,  i*50,size,size);
            Main.batch.draw(box,150 + i *50,0,size,size);
            Main.batch.draw(box,150 + i *50 ,50*9 ,size,size);
        }


    }
}
