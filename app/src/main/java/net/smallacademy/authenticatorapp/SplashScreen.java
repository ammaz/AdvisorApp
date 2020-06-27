package net.smallacademy.authenticatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    //Variables
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo;

    private static int SPLASH_SCREEN = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks

        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView7);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth fauth=FirebaseAuth.getInstance();
                FirebaseUser fuser=fauth.getCurrentUser();
                if(fuser!=null){

                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{

                    Intent intent = new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);


    }
}
