package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Firebase.firebase;
import com.mygdx.game.Hud.Controller;
import com.mygdx.game.ResourceManager.GameManager;
import com.mygdx.game.Screens.Mainmenu;

import java.util.ArrayList;


public class Main extends Game {
	public  static float PPM=100;
	public static float WIDTH =480;
	public static float HEIGHT =320;
	public static int[] posx={20,400,20,400};
	public static int[] poxy={20,20,240,240};

	public static SpriteBatch batch;
	public OrthographicCamera cam;
	public Viewport viewport;
	public Texture texture;
	public Controller controller;
	public String playerName="";
	//fireBase instance
	public firebase db;
	public ArrayList<String> TakenName;
	public String roomname="Test";



	//====================|GameManager|/////////
	public GameManager manager;

	@Override
	public void create() {
		TakenName = new ArrayList<>();
		db = new firebase(this);
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
		if (!playerName.equals("")){
			db.deletePlayer(playerName);

		}

	}
}