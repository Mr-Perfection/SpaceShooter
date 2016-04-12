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

import java.util.ArrayList;


/**
 * Created by StephenLee on 4/5/16.
 */

public class SpaceView extends SurfaceView implements SurfaceHolder.Callback
{
//ll
    private static final String Name = SpaceView.class.getSimpleName();

    private SpaceShooter spaceShooter;
    private Alien[][] aliens;
//    private boolean aliensFlag = false; //True if aliens are ready to be drawn

    private GameLoopThread gameLoopThread;
    //current bullet
    Bullet curr_bullet;

    //initializing arraylist for bulletList
    ArrayList<Bullet> bulletList = new ArrayList<Bullet>();

    //bullet
    private Bullet b;

    //press down for detecting player
    private static Boolean FristBullet;

    int HEIGHT,WIDTH;
    private int row  , column, paddingX, paddingY;
    private float velocityX, velocityY;

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
        Bitmap alienBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.donald);
        spaceShooter = new SpaceShooter(myBitmap, WIDTH, HEIGHT );
    // initialize the first bullet and add to bulletList
        Bullet bullet_tmp = new Bullet(spaceShooter.getX(), spaceShooter.getY());
        curr_bullet = bullet_tmp;
        bulletList.add(curr_bullet);
        /*CREATE ALIEN ARMY*/
        row = 4;
        column = 5;
        velocityX = 20f;
        velocityY = 50f;
        paddingX = 200;
        paddingY = 150;

        aliens = new Alien[row][column];
//        aliens[1][1] =  new Alien(myBitmap, WIDTH, HEIGHT );

//        for (int i = 0; i < row; ++i)
//        {
//            for (int j = 0; j< column; ++j){
//                aliens[i][j] =  new Alien(myBitmap, WIDTH, HEIGHT );
//            }
//        }

        alienArmy(alienBitmap, WIDTH, HEIGHT,row, column, velocityX, velocityY, paddingX, paddingY);


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
                // release the bullet
                curr_bullet.setBulletReleased();
            }
        }//EOF if UP





        return true;
    }


    public void draw(Canvas c) {

        c.drawColor(Color.BLACK);
        spaceShooter.draw(c);

        int i, j;

        for (i = 0; i < row; ++i) {
            for (j = 0; j < column; ++j) {
                aliens[i][j].draw(c);
                aliens[i][j].update();
            }
        } //rof
        //if bullet is released, get a reload a new bullet
        if(curr_bullet.isBulletReleased()){
            Bullet bullet_tmp = new Bullet(spaceShooter.getX(), spaceShooter.getY());

            //set that as current bullet
            curr_bullet = bullet_tmp;
            bulletList.add(curr_bullet);
        }
        //Looping through all the bullets in bulletList
        for(int a=0; a < bulletList.size(); a++){

            //changing y pos of bullet if bullet is released
            if(bulletList.get(a).isBulletReleased()){
                bulletList.get(a).moveBullet();
            }
            // else the bullet should follow the spaceShooter
            else{
                bulletList.get(a).setX(spaceShooter.getX());
                bulletList.get(a).setY(spaceShooter.getY());
            }

            //draw the bullet
            bulletList.get(a).draw(c);
            //remove the bullets that are out of the screen
            if(bulletList.get(a).getY() < 0){
                bulletList.remove(a);
            } //fi
        } //rof

    } //eof draw




    void alienArmy(Bitmap myBitmap, int _width ,int _height,int _row, int _column, float velocityX, float velocityY, int paddingX, int paddingY)
    {
        int i, j;

        for (i = 0; i < _row; ++i)

        {
            for (j = 0; j < _column; ++j)
            {

                Log.d(Name, "Alien Army" +i + " " + j + "created!");
                aliens[i][j] =  new Alien(myBitmap,_width, _height );
                aliens[i][j].setPosition(getWidth() / 4 + paddingX * i, getHeight()/5 + paddingY*j);     //Rectangular form of army of aliens with
                //paddings
                aliens[i][j].setVelocity(velocityX,velocityY);   //Set velocity

            }
        }



    }//EOF alienArmy


} //EOF CLASS