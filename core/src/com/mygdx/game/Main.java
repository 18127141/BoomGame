package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.Screens.MyScreen;

public class Main extends Game {
	public  static float PPM=100;
	public static float WIDTH =480;
	public static float HEIGHT =320;
	public static SpriteBatch batch;
	public OrthographicCamera cam;
	public Viewport viewport;
	public Texture texture;
	public com.mygdx.game.Hud.Controller controller;
	public Agent agent;
	public Map map;

	//=========================Tiled Map/////=====================
	public TmxMapLoader mapLoader;
	public TiledMap mapp;
	public OrthogonalTiledMapRenderer renderer;
	@Override
	public void create() {
		//Scaling b2d

		//
		cam = new OrthographicCamera();
		viewport = new FitViewport(WIDTH/PPM, HEIGHT /PPM, cam);
		batch = new SpriteBatch();
		controller = new Controller();
		agent = new Agent();
		map = new Map();
		mapLoader = new TmxMapLoader();
		mapp = mapLoader.load("map/Forest.tmx");

		renderer = new OrthogonalTiledMapRenderer(mapp,1/PPM);
		cam.position.set((viewport.getWorldWidth()/2-controller.l.getWidth()/PPM),(viewport.getWorldHeight()/2-(controller.bottom.getHeight()*5/7/PPM))
				,0);

		cam.update();
		setScreen(new MyScreen(this));
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
		controller.resize(width, height);
	}


	@Override
	public void render() {
		super.render();

//		Gdx.gl.glClearColor(1,0,0,1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//		agent.handleInput(controller);
//
//
//		batch.begin();
//
//		map.draw();
//		agent.draw();
//		batch.end();
//
//		controller.draw();

	}
}