package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {
	public static SpriteBatch batch;
	OrthographicCamera cam;
	Viewport viewport;
	Texture texture;
	Controller controller;
	Agent agent;
	Map map;
	@Override
	public void create() {
		cam = new OrthographicCamera();
		viewport = new FitViewport(800, 480 , cam);
		batch = new SpriteBatch();
		controller = new Controller();
		agent = new Agent();
		map = new Map();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
		controller.resize(width, height);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(1,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		agent.handleInput(controller);


		batch.begin();

		map.draw();
		agent.draw();
		batch.end();

		controller.draw();

	}
}