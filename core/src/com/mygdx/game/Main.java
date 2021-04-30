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
import com.mygdx.game.Screens.MainHall;
import com.mygdx.game.Screens.Mainmenu;

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends Game {
	public static String []Maps ={"Forest","Temple","Cave","Sewer"};
	public  static float PPM=100;
	public static float WIDTH =480;
	public static float HEIGHT =320;
	public static int[] posx={20,400,20,400};
	public static int[] poxy={20,20,240,240};

	public static SpriteBatch batch;
	public OrthographicCamera cam;
	public Viewport viewport;
	public Texture texture;
	public static Controller controller;
	public static String playerName="";
	//fireBase instance
	public static firebase db;
	public ArrayList<String> TakenName;
	public static String roomname="Test";

	//Music and sound
	public static boolean checkMusic=true;
	public static float musicPosition = 100;
	public static boolean checkSound = true;
	public static float soundPosition = 100;



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
		//setScreen(new MainHall(this));
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