package com.learningjava.stephenlee.spaceshooter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Created by StephenLee on 4/5/16.
 */

public class SpaceView extends SurfaceView implements SurfaceHolder.Callback
{
    //ll
    private static final String Name = SpaceView.class.getSimpleName();

    private SpaceShooter spaceShooter;
    private Alien[][] aliens;
    //testing with ic_launcher first
//    Bitmap alienBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    Bitmap alienBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.donald);
    private int countDeadAliens = 0;

    private GameLoopThread gameLoopThread;
    //current bullet
    Bullet curr_bullet;

    //initializing arraylist for bulletList
    ArrayList<Bullet> bulletList = new ArrayList<Bullet>();

    //bullet
    private Bullet b;



    private static int HEIGHT,WIDTH;
    private int row  , column, paddingX, paddingY;
    private float velocityX, velocityY;
    //for mystery ship
    private MysteryShip mysteryShip;

    private int counter = 0;

    //to store the random number that was generated from rand
    //int mysteryshipflag;
    //to check if the mysteryship is out of the page(destroyed)
    //true: yes, destroyed
    //false: not destroyed

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

        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hilary);

        spaceShooter = new SpaceShooter(myBitmap, WIDTH, HEIGHT );
        // initialize the first bullet and add to bulletList
        Bullet bullet_tmp = new Bullet(spaceShooter.getX(), spaceShooter.getY());
        curr_bullet = bullet_tmp;
        bulletList.add(curr_bullet);
        //initialize the first mysteryship and add to mysteryshipList
        Bitmap mshipBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.mysteryship);
        mysteryShip = new MysteryShip(mshipBitmap, 10,10);
//        curr_ms = ms_tmp;
//        msList.add(curr_ms);
       // mysteryShip = new MysteryShip(10,10);
        /*CREATE ALIEN ARMY*/
        row = 4;
        column = 5;
        velocityX = 20f;
        velocityY = 50f;
        paddingX = 200;
        paddingY = 150;

        aliens = new Alien[row][column];


        alienArmy(alienBitmap, WIDTH, HEIGHT,row, column, velocityX, velocityY, paddingX, paddingY);    //Make first array of aliens


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
//                Log.d(Name, "Touch Coords: x=" + event.getX() + ",y=" + event.getY());
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
            if(event.getY() > 0){
                curr_bullet.setBulletReleased();
            }
        }//EOF if UP





        return true;
    }


    public void draw(Canvas c) {


        int i, j;
        c.drawColor(Color.BLACK);

        if(spaceShooter.getVisibility()) {
            spaceShooter.draw(c);

            //if bullet is released, get a reload a new bullet
            if(curr_bullet.isBulletReleased()){
                Bullet bullet_tmp = new Bullet(spaceShooter.getX(), spaceShooter.getY());

                //set that as current bullet
                curr_bullet = bullet_tmp;
                bulletList.add(curr_bullet);
            }//EOF currbullet

            for (i = 0; i < row; ++i) {
                for (j = 0; j < column; ++j) {
                    if(aliens[i][j].getVisibility()) {
                        aliens[i][j].draw(c);
                        aliens[i][j].update();

                        float alien_X = aliens[i][j].getPositionX();
                        float alien_Y = aliens[i][j].getPositionY();
                        float shooter_X = spaceShooter.getX();
                        float shooter_Y = spaceShooter.getY();
                        float difference_X = Math.abs(alien_X - shooter_X); //Difference X between alien and shooter
                        float difference_Y = Math.abs(alien_Y - shooter_Y); //Difference Y between alien and shooter

                        if(difference_X < 70  && difference_Y < 100)
                        {

                            spaceShooter.setVisibility(false);
//                                Log.d(Name, "Collsiton detected" + countDeadAliens);

                        }
//                    Log.d(Name, "Y coordinate: " + aliens[i][j].getPositionY());
//                    Log.d(Name, "HEIGHT: " + HEIGHT);
                        if(aliens[i][j].getPositionY() >= HEIGHT - 100)
                        {

                            Log.d(Name, "Get lower in screen: " + countDeadAliens);
                            countDeadAliens++;  //Increments the dead aliens + 1.

                            aliens[i][j].setVisibility(false);


                        }//EOF If aliens
                    }
                    //If the last alien was invisible. All the aliens will be visible

                }
            } //EOF For loops
            //Looping through all the bullets in bulletList
            for(int a=0; a < bulletList.size(); a++){

                //changing y pos of bullet if bullet is released
                if(bulletList.get(a).isBulletReleased()){
                    bulletList.get(a).moveBullet();


                    for (i = 0; i < row; ++i) {
                        for (j = 0; j < column; ++j) {
                            if(aliens[i][j].getVisibility() == true)
                            {
                                float bullet_X = bulletList.get(a).getX();
                                float bullet_Y = bulletList.get(a).getY();
                                float alien_X =  aliens[i][j].getPositionX();
                                float alien_Y =aliens[i][j].getPositionY();

                                float difference_X = Math.abs(alien_X - bullet_X);
                                float difference_Y = Math.abs(alien_Y - bullet_Y);


                                if(difference_X < 50  && difference_Y < 50){
                                    //Set the collision detection rect

//                                Log.d(Name, "Collsiton detected" + countDeadAliens);
                                    aliens[i][j].setVisibility(false);
                                    countDeadAliens++;
                                }
                            }

                        }
                    }


                }
                // else the bullet should follow the spaceShooter
                else {
                    bulletList.get(a).setX(spaceShooter.getX());
                    bulletList.get(a).setY(spaceShooter.getY());


                }

                //draw the bullet
                bulletList.get(a).draw(c);
                //remove the bullets that are out of the screen
                if(bulletList.get(a).getY() < 0){
                    bulletList.remove(a);
                    a--;
                } //fi

            } //rof
            //end of bullets
        }   //EOF SpaceShooter visible

        if(counter == 10) {
            Random rand = new Random();
            int mysteryshipflag = rand.nextInt(50); //generate number from 0 to 49
            System.out.println("what's the random generated number" + mysteryshipflag);

            if (mysteryshipflag < 20) {

                if (mysteryShip.getisMoving()) {
                } else {

                    mysteryShip.setisMoving(true);

                }
            }
            counter = 0;
        }
        counter ++;
        //drawing ms ships
        if(mysteryShip.getisMoving()){
            mysteryShip.moveShip();
            mysteryShip.draw(c);
        }
        if(mysteryShip.getX() >= c.getWidth()){
            mysteryShip.setisMoving(false);
            mysteryShip.setX(10);
        }

        //If all aliens are dead, do this.
        if(countDeadAliens == row * column)
        {
            velocityX = velocityX + 10;
            velocityY = velocityY + 10;
            alienArmy(alienBitmap, WIDTH, HEIGHT,row, column, velocityX, velocityY, paddingX, paddingY);
            countDeadAliens = 0;    //Initialize to 0. Restart
        }

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
                aliens[i][j].setPosition(getWidth() / 4 + paddingX * i, getHeight() / 5 + paddingY * j);     //Rectangular form of army of aliens with
                //paddings
                aliens[i][j].setVelocity(velocityX, velocityY);   //Set velocity
                aliens[i][j].setVisibility(true);


//                aliens[i][j].setRect((alienBitmap.getWidth()),(alienBitmap.getHeight()));  //Set the collision rect
            }
        }



    }//EOF alienArmy


} //EOF CLASS
