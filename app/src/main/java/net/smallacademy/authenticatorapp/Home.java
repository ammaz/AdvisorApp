package net.smallacademy.authenticatorapp;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        FirebaseUser fuser=fauth.getCurrentUser();
        if(fuser!=null){

            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

}
