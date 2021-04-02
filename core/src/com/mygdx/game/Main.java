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
import com.mygdx.game.Firebase.firebase;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Screens.Mainmenu;


public class Main extends Game {
	public  static float PPM=100;
	public static float WIDTH =480;
	public static float HEIGHT =320;
	public static SpriteBatch batch;
	public OrthographicCamera cam;
	public Viewport viewport;
	public Texture texture;
	public Controller controller;
	public firebase db;




	//====================|GameManager|/////////
	public GameManager manager;

	@Override
	public void create() {
		db = new firebase();
		manager = new GameManager();
		cam = new OrthographicCamera();
		viewport = new FitViewport(WIDTH/PPM, HEIGHT /PPM, cam);
		batch = new SpriteBatch();
		controller = new Controller();



		cam.position.set((viewport.getWorldWidth()/2-controller.l.getWidth()/PPM),(viewport.getWorldHeight()/2-(controller.bottom.getHeight()*5/7/PPM))
				,0);

		cam.update();

		//setScreen(new MyScreen(this));
		setScreen(new Mainmenu(this));
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

	}

	@Override
	public void dispose() {
		super.dispose();


	}
}