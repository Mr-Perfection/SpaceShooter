package com.learningjava.stephenlee.spaceshooter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    //Get the name of this class
    /*final means it cannot be modified and is final value*/
    private static final String Name = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //No title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Set to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new SpaceView(this));
        Log.d(Name, "SpaceView added!");


    } //EOF OnCreate

    @Override
    protected void onDestroy() {
        Log.d(Name, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(Name, "Stopping...");
        super.onStop();
    }




}





