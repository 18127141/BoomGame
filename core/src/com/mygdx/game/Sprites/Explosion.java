package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ResourceManager.GameManager;

public class Explosion {
    private Array<Sprite> texture;
    private Sprite sprite;
    private Animation middle,left_tail,right_tail,up_tail,down_tail,body_x,body_y;
    float x,y;
    int l,r,u,d;
    float stateTimer=0;
    public Explosion(float x,float y,int left,int right,int up,int down){
        this.x=x;
        this.y=y;
        this.l=left;
        this.r=right;
        this.u=up;
        this.d=down;
        texture = new Array<>();
        sprite = new Sprite((Texture) GameManager.getAssetManager().get("Pack/Boom.png"));
        Array<TextureRegion> frame = new Array<>();
        float FP=1.2f;
        frame.add(new TextureRegion(sprite.getTexture(),1,1,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),21,1,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),45,1,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),67,1,20,20));

        frame.reverse();
        up_tail=new Animation(FP,frame);
        frame.clear();

        frame.add(new TextureRegion(sprite.getTexture(),1,21,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),21,21,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),45,21,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),67,21,20,20));

        frame.reverse();

        down_tail=new Animation(FP,frame);
        frame.clear();

        for (int i=0;i<4;i++){

            frame.add(new TextureRegion(sprite.getTexture(),87+i*20,1,20,20));



        }
        frame.reverse();

        body_x = new Animation(FP,frame);
        frame.clear();

        for (int i=0;i<4;i++){

            frame.add(new TextureRegion(sprite.getTexture(),1+i*22,43,20,20));



        }
        frame.reverse();

        middle = new Animation(FP,frame);
        frame.clear();
        //
        frame.add(new TextureRegion(sprite.getTexture(),86+1,43,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),107+1,43,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),129+1,43,20,20));
        frame.add(new TextureRegion(sprite.getTexture(),151+1,43,20,20));
//
//        86 106
//        107 127
//        129
//        151
        frame.reverse();

        left_tail= new Animation(FP,frame);
        frame.clear();
        for (int i=0;i<4;i++){

            frame.add(new TextureRegion(sprite.getTexture(),1+i*20,103,20,20));


        }
        //frame.add(new TextureRegion(sprite.getTexture(),1+3*20,103,20,20));
        frame.reverse();

        right_tail = new Animation(FP,frame);
        frame.clear();
        for (int i=0;i<4;i++){

            frame.add(new TextureRegion(sprite.getTexture(),82+i*20,103,20,20));


        }
        //103 61
        frame.reverse();

        body_y = new Animation(FP,frame);
        initalize();


    }
    public void initalize(){
        //middle
        texture.add(new Sprite((Texture) GameManager.getAssetManager().get("Pack/Boom.png")));
        texture.get(texture.size-1).setBounds(x,y,(float)0.2,(float)0.2);
        texture.get(texture.size-1).setPosition(x,y);
        //left
        for (int i=0;i<l;i++){
            texture.add(new Sprite((Texture) GameManager.getAssetManager().get("Pack/Boom.png")));
            texture.get(texture.size-1).setBounds(x-((i+1)*(float)0.2),y,(float)0.2,(float)0.2);
            texture.get(texture.size-1).setPosition(x-((i+1)*(float)0.2),y);
        }
        //right
        for (int i=0;i<r;i++){
            texture.add(new Sprite((Texture) GameManager.getAssetManager().get("Pack/Boom.png")));
            texture.get(texture.size-1).setBounds(x+((i+1)*(float)0.2),y,(float)0.2,(float)0.2);
            texture.get(texture.size-1).setPosition(x+((i+1)*(float)0.2),y);
        }
        //up
        for (int i=0;i<u;i++){
            texture.add(new Sprite((Texture) GameManager.getAssetManager().get("Pack/Boom.png")));
            texture.get(texture.size-1).setBounds(x,y+((i+1)*(float)0.2),(float)0.2,(float)0.2);
            texture.get(texture.size-1).setPosition(x,y+((i+1)*(float)0.2));
        }
        //down
        for (int i=0;i<d;i++){
            texture.add(new Sprite((Texture) GameManager.getAssetManager().get("Pack/Boom.png")));
            texture.get(texture.size-1).setBounds(x,y-((i+1)*(float)0.2),(float)0.2,(float)0.2);
            texture.get(texture.size-1).setPosition(x,y-((i+1)*(float)0.2));
        }

    }
    public void update(float dt) {
        for (int i=0;i<texture.size;i++){
            Sprite tmp = texture.get(i);
            tmp.setRegion(getFrame(dt,i));

        }

    }
    public TextureRegion getFrame(float dt,int i){
        stateTimer=stateTimer+dt;
        TextureRegion region = new TextureRegion() ;



        if (i==0){
            region = (TextureRegion) middle.getKeyFrame(stateTimer,false);
        }
        else if (l!=0 && i<l+1){
            //check tail
            if ( i==l){
                region = (TextureRegion)left_tail.getKeyFrame(stateTimer,false);

            }
            else{
                region = (TextureRegion)body_x.getKeyFrame(stateTimer,false);

            }

        }
        else if (r!=0 && i<l+1+r){
            if (i==l+r){
                region = (TextureRegion)right_tail.getKeyFrame(stateTimer,false);

            }
            else{
                region = (TextureRegion)body_x.getKeyFrame(stateTimer,false);

            }

        }
        else if (u!=0 && i<l+r+u+1){
            if (i==l+r+u){
                region = (TextureRegion)up_tail.getKeyFrame(stateTimer,false);

            }
            else{
                region = (TextureRegion)body_y.getKeyFrame(stateTimer,false);

            }

        }
        else if (d!=0 && i<l+r+u+d+1){
            if (i==l+r+u+d){
                region = (TextureRegion)down_tail.getKeyFrame(stateTimer,false);

            }
            else {
                region = (TextureRegion)body_y.getKeyFrame(stateTimer,false);

            }

        }
        return region;
    }
    public void draw(Batch batch){
        for (int i=0;i<texture.size;i++){
            Sprite temp = texture.get(i);
            temp.draw(batch);
        }
    }



}
