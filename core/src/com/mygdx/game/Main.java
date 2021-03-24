package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.WildcardType;

public class Main extends Game {
	public static int WIDTH =480;
	public static int HEIGHT =320;
	public static SpriteBatch batch;
	OrthographicCamera cam;
	Viewport viewport;
	Texture texture;
	Controller controller;
	Agent agent;
	Map map;


	TmxMapLoader mapLoader;
	TiledMap mapp;
	OrthogonalTiledMapRenderer renderer;
	@Override
	public void create() {

		cam = new OrthographicCamera();
		viewport = new FitViewport(WIDTH, HEIGHT , cam);
		batch = new SpriteBatch();
		controller = new Controller();
		agent = new Agent();
		map = new Map();
		mapLoader = new TmxMapLoader();
		mapp = mapLoader.load("map/Forest.tmx");

		renderer = new OrthogonalTiledMapRenderer(mapp);
		cam.position.set(viewport.getWorldWidth()/2-controller.l.getWidth(),viewport.getWorldHeight()/2-(controller.bottom.getHeight()*5/7)
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