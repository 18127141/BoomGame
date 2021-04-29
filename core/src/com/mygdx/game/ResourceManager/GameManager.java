package com.mygdx.game.ResourceManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Main;

public class GameManager implements Disposable {
    private static AssetManager assetManager;

    public GameManager() {
        assetManager= new AssetManager();

        //load Agent
        assetManager.load("Pack/PlayerandBoom.pack", TextureAtlas.class);

        //Load Texture
        assetManager.load("loadingbarbg.png", Texture.class);
        assetManager.load("loadingbar.png", Texture.class);
        assetManager.load("Mainmenu.png", Texture.class);
        assetManager.load("bottom.png", Texture.class);
        assetManager.load("title_background-little.png", Texture.class);
        assetManager.load("Pack/objects.png", Texture.class);
        assetManager.load("Pack/items.png", Texture.class);
        assetManager.load("Pack/Boom.png", Texture.class);
        assetManager.load("Pack/DarkPlayer.png", Texture.class);

        //Loadminimap
//        assetManager.load("map/minimap/Temple.png",Texture.class);
//        assetManager.load("map/minimap/Forest.png",Texture.class);
//        assetManager.load("map/minimap/Cave.png",Texture.class);
        for (int i=0;i< Main.Maps.length;i++){
            assetManager.load("map/minimap/"+Main.Maps[i]+".png",Texture.class);
        }
        //load map bgm
        for (int i=0;i< Main.Maps.length;i++){
            assetManager.load("music/"+Main.Maps[i]+".mp3",Music.class);
        }
        //load victory music
        assetManager.load("music/Victory.mp3",Music.class);
        assetManager.load("music/Lose.mp3",Music.class);


        //load Sound
        assetManager.load("sounds/PlaceBomb.ogg", Sound.class);
        assetManager.load("sounds/Explosion.ogg",Sound.class);

        assetManager.finishLoading();

    }
    public static AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void dispose() {

    }
}
