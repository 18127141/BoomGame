package com.mygdx.game.Builder;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Sprites.Items;
import com.mygdx.game.Sprites.Walls;

public class WorldBuilder {
    public WorldBuilder(World world, TiledMap map){

        //Render the items
//        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject)object).getRectangle();
//
//            new Items(world,map,rect);
//        }
        //Render the walls
        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            new Walls(world,map,rect);
        }
    }
}