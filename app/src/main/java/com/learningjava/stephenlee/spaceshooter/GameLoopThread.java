package com.learningjava.stephenlee.spaceshooter;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by StephenLee on 4/5/16.
 */
public class GameLoopThread extends Thread {

    private static final String Name = GameLoopThread.class.getSimpleName();

    /*Initializations*/;

    // The actual view that handles inputs and draws to the surface
    private SpaceView spaceView;
    // Surface holder that can access the physical surface
    private SurfaceHolder surfaceHolder;


    // flag to hold game state
    private boolean running;
    //SET running flag
    public void setRunning(boolean _running) {
        running = _running;
    }
    //SET surfaceHolder and spaceView
    public GameLoopThread(SpaceView _spaceView, SurfaceHolder _surfaceHolder) {
        super();

        spaceView = _spaceView;
        surfaceHolder = _surfaceHolder;
    }


    @Override
    public void run() {
        Canvas canvas;
        Log.d(Name, "Starting game loop");

        // Main game loop.
        while (running) {
            canvas = null;
//You might want to do game specific processing in a method you call here

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    spaceView.onDraw(canvas);
                }
            } catch (Exception e) {} finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
// Set the frame rate by setting this delay
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
// Thread was interrupted while sleeping.
                return;
            }
        }
    }
}

