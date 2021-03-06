package com.mygdx.game.Builder;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.Sprites.ITEM;
import com.mygdx.game.Sprites.Items;
import com.mygdx.game.Sprites.Walls;

public class WorldBuilder {
    public WorldBuilder(World world, TiledMap map, Array<Items> BoxList, Array<Walls> WallsList, Array<ITEM> ItemList){
        //Render the items
        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            //Host se random item type
            if (Main.roomname.equals(Main.playerName)) {
                int tmp = BoxList.size;
                BoxList.add( new Items(world,map,rect,ItemList,-1,tmp));
            }
            else{
                BoxList.add( new Items(world,map,rect,ItemList,0,0));

            }

        }
        //Render the walls
        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            WallsList.add(new Walls(world,map,rect));
        }
    }
}
