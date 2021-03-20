package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.game.Main;

public class AndroidLauncher extends AndroidApplication {
	FirebaseDatabase database;
	DatabaseReference Ref;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main(), config);
		database = FirebaseDatabase.getInstance("https://boomonline-bf8ff-default-rtdb.firebaseio.com");
		Ref= database.getReference("message");
		Ref.setValue("Test");
	}
}
