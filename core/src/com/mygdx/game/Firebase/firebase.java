package com.mygdx.game.Firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class firebase {
    Firebase db;
    public firebase(){
        db = new Firebase("https://boomonline-bf8ff-default-rtdb.firebaseio.com");
//        db.getRef().child("HOHO").setValue("?");
//        db.getRef().child("Test").setValue("Test");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    System.out.println(snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
