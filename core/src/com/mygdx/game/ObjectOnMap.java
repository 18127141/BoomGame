package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ObjectOnMap extends Thread {
    //Display
    Viewport viewport;
    Stage stage;
    OrthographicCamera cam;
    //Atribute
    int size = 50;
    int rangeX[] = {200,200+50*8};
    int rangeY[] = {50,50*8};
    int x ;
    int y ;

    public ObjectOnMap(){
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, cam);
        stage = new Stage(viewport, Main.batch);
        x = 200;
        y = 50;
    }
    }

