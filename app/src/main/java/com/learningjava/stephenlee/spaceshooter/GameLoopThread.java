package com.learningjava.stephenlee.spaceshooter;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Created by StephenLee on 4/5/16.
 */
public class GameLoopThread extends Thread {

    private static final String Name = GameLoopThread.class.getSimpleName();

    /*Initializations*/;

    // The actual view that handles inputs and draws to the surface
    private SpaceView spaceView;
    // Surface holder that can access the physical surface

    //bullet
    private Bullet b;
//    // flag to hold game state
//    private boolean running;
//    //SET running flag
//    public void setRunning(boolean _running) {
//        running = _running;
//    }
    //SET surfaceHolder and spaceView
    public GameLoopThread(SpaceView _spaceView) {

        spaceView = _spaceView;
        
    }


    @Override
    public void run() {

        //Get spaceview surfaceholder
        SurfaceHolder surfaceHolder = spaceView.getHolder();



        Log.d(Name, "Starting game loop");

        // Main game loop.
        while ( !Thread.interrupted()) {

            Canvas canvas = surfaceHolder.lockCanvas(null);

            try {
//                System.out.println("intry");
                synchronized (surfaceHolder) {
                    if(spaceView.isShipDestroyed == true){
                        Random rand = new Random();
                        spaceView.mysteryshipflag = rand.nextInt(50); //generate number from 0 to 49
//                        System.out.println("what's the random generated number" +spaceView.mysteryshipflag);
                    }
                    spaceView.draw(canvas); //drawing frame
                    
                 
                }
            } catch (Exception e) {} finally {
                if (canvas != null) {
//                    System.out.println("inif");
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
// Set the frame rate by setting this delay
            try {

                Thread.sleep(1);
            } catch (InterruptedException e) {
// Thread was interrupted while sleeping.
//                System.out.println("b4return");
                return;

            }
        } //While
    }//Run
}

