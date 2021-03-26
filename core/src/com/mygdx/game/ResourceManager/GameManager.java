package com.mygdx.game.ResourceManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class GameManager implements Disposable {
    private static AssetManager assetManager;

    public GameManager() {
        assetManager= new AssetManager();

        //load Agent
        assetManager.load("Pack/PlayerandBoom.pack", TextureAtlas.class);

        assetManager.finishLoading();

    }
    public static AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void dispose() {

    }
}
