package com.learningjava.stephenlee.spaceshooter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by StephenLee on 4/5/16.
 */

public class SpaceView extends SurfaceView implements SurfaceHolder.Callback
{

    private static final String Name = SpaceView.class.getSimpleName();

    private SpaceShooter spaceShooter;
    private Alien alien;
    private GameLoopThread gameLoopThread;

    int HEIGHT,WIDTH;



    public SpaceView(Context context)
    {
        super(context);

        //Add callback to the surfaceholder to intercept the events
        // Notify the SurfaceHolder that you’d like to receive
        // SurfaceHolder callbacks .
        getHolder().addCallback(this);

        // make the SpaceView focusable so it can handle events
        setFocusable(true);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // Launch animator thread .
//        spaceShooter = new SpaceShooter(getWidth(), getHeight());
        /*Set thread to running*/
        WIDTH = getWidth();
        HEIGHT = getHeight();

        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        spaceShooter = new SpaceShooter(myBitmap, WIDTH, HEIGHT );
        alien = new Alien(myBitmap, getWidth(), getHeight() );  //Use get methods to get screen size.
        alien.setVelocity(10.0f, 35.0f); //Velociry for x and y.
        // create the game loop thread. Pass SurfaceHolder and this SurfaceView
        gameLoopThread = new GameLoopThread(this);
        gameLoopThread.start(); //Game is started


    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
        int height) {
            // TODO Auto-generated method stud
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        Log.d(Name, "Surface is getting destroyeeddd!");
        gameLoopThread.interrupt();
        Log.d(Name, "Thread shut down cleanly");

    }//EOF SurfaceDestroyed





    @Override
    public boolean onTouchEvent(MotionEvent event)
    {



        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //IF TOUCH WAS INITIATED
            //Delegate event handling to the spaceshooter
            spaceShooter.handleActionDown((int) event.getX(), (int) event.getY());

            // check if in the lower part of the screen we exit
            if (event.getY() > getHeight() - 50) {
                gameLoopThread.interrupt();//stop rendering the screen.
                ((Activity) getContext()).finish();

            } else {
                Log.d(Name, "Touch Coords: x=" + event.getX() + ",y=" + event.getY());
            }
        } //End of DOWN
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                //DRAGGGING
                // the gestures
                if (spaceShooter.isTouched()) {
                    // the spaceshooter was picked up and is being dragged
                    spaceShooter.setX((int)event.getX());

                    /*DO NOT SET the y axis since we will be moving the spaceshooter left and right horizontally only.*/
//                    spaceShooter.setY((int)event.getY());
                }

            } //EOF if MOVE

            if (event.getAction() == MotionEvent.ACTION_UP) {
                // touch was released
                if (spaceShooter.isTouched()) {
                    spaceShooter.setTouched(false);
                }
            }//EOF if UP



        return true;
    }


    public void draw(Canvas c) {

        c.drawColor(Color.BLACK);
        spaceShooter.draw(c);
        alien.draw(c);
        alien.update();
    }



//    @Override
//    protected void onDraw(Canvas canvas)
//
//    {
//
//        canvas.drawColor(Color.BLACK);
//        spaceShooter.draw(canvas);
//
//
//    }


} //EOF CLASS
