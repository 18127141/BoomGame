package com.mygdx.game.Firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mygdx.game.Main;

public class firebase {
    public Firebase db;
    boolean check;
    private Main game;
    public long quanity;
    public firebase(Main game){
        this.game=game;
        db = new Firebase("https://boomonline-bf8ff-default-rtdb.firebaseio.com");
        check=false;
        init();




    }

    public void init(){
        db.getRef().child("players").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                game.TakenName.add(dataSnapshot.getName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
    public boolean CheckPlayer(String name){

        for (String s: game.TakenName){
            if (s.equals(name)) return true;
        }
        return false;
    }
    public void setPlayername(String s){
        db.getRef().child("players").child(s).setValue("");
    }
    public void deletePlayer(String s){
        db.getRef().child("players").child(s).setValue(null);

    }
    public void addPlayertoRoom(String room,String player,float x,float y){
        cordinate Temp=new cordinate(x,y,"","Stand","Stand",0);
        db.getRef().child("rooms/"+room+"/"+player).setValue(Temp);
    }
    public void deletePlayerfromRoom(String room,String player){
        db.getRef().child("rooms/"+room+"/"+player).setValue(null);

    }
    public void SetPlayerXY(String room,String player,float x,float y,String Action,String a,String b,int direction){
        cordinate Temp=new cordinate(x,y,Action,a,b,direction);
        db.getRef().child("rooms/"+room+"/"+player).setValue(Temp);
    }
    public void AddBoom(String room,String player){
        db.getRef().child("rooms/"+room+"/"+player).child("action").setValue("Planted");
    }
    public void AddRoomStatus(String room,boolean s){
        if (!s){
            db.getRef().child("rooms/"+room).child("_RoomStatus").setValue(false);
        }
        else{
            db.getRef().child("rooms/"+room).child("_RoomStatus").setValue(null);

        }
    }


    class cordinate{
        public float x,y;
        public String action;
        public cordinate(){};
        public String State;
        public String prevState;
        public int direction;
        public cordinate(float x,float y,String action,String state,String prevState,int direction){
            this.x=x;
            this.y=y;
            this.action=action;
            this.State=state;
            this.prevState=prevState;
            this.direction=direction;
        }
    }
}
