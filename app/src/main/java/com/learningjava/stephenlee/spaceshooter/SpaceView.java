package com.learningjava.stephenlee.spaceshooter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by StephenLee on 4/5/16.
 */

public class SpaceView extends SurfaceView implements SurfaceHolder.Callback
{

    private static final String Name = SpaceView.class.getSimpleName();

    protected SpaceShooter spaceShooter;
    private GameLoopThread gameLoopThread;

	//current bullet
	Bullet curr_bullet;

	//initializing arraylist for bulletList
	ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	
    //bullet
    private Bullet b;

    //press down for detecting player
    private static Boolean FristBullet;


    public SpaceView(Context context)
    {
        super(context);

        //Add callback to the surfaceholder to intercept the events
        // Notify the SurfaceHolder that you’d like to receive
        // SurfaceHolder callbacks .
        getHolder().addCallback(this);



        //Get device height and width
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);
        int HEIGHT = displaymetrics.heightPixels;
        int WIDTH = displaymetrics.widthPixels;

        //get the bitmap and create spaceshooter
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        spaceShooter = new SpaceShooter(myBitmap, WIDTH/2, HEIGHT*2/3 );
        System.out.println("in super context what player'sx? " +WIDTH/2);
       System.out.println("in super context what player'sy? " +HEIGHT*2/3 );

    System.out.println("in super context what bullet'sy? " +(spaceShooter.getY()-10));

	// initialize the first bullet and add to bulletList
	Bullet bullet_tmp = new Bullet(spaceShooter.getX(), spaceShooter.getY());
	curr_bullet = bullet_tmp;
	bulletList.add(curr_bullet);

    // make the SpaceView focusable so it can handle events
        setFocusable(true);
    } //eof spaceview


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // Launch animator thread .
//        spaceShooter = new SpaceShooter(getWidth(), getHeight());
        /*Set thread to running*/

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

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //IF TOUCH WAS INITIATED
                //Delegate event handling to the spaceshooter
                spaceShooter.handleActionDown((int) event.getX(), (int) event.getY());
//                System.out.println("pass through action down for player");

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
                    spaceShooter.setX((int) event.getX());


                    //DO NOT SET the y axis since we will be moving the spaceshooter left and right horizontally only.
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
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        c.drawColor(Color.BLACK);
        System.out.println("draw spaceshooter first");
        spaceShooter.draw(c);
        System.out.println("after drawing spaceshooter");

		//if bullet is released, geet a reload a new bullet
		if(curr_bullet.isBulletReleased()){
			Bullet bullet_tmp = new Bullet(spaceShooter.getX(), spaceShooter.getY());
		
			//set that as current bullet
			curr_bullet = bullet_tmp;
			bulletList.add(curr_bullet);
		}
		//Looping through all the bullets in bulletList
	    for(int i=0; i < bulletList.size(); i++){
		
	    	//changing y pos of bullet if bullet is released
	    	if(bulletList.get(i).isBulletReleased()){
	    		bulletList.get(i).moveBullet();
	    	}
	    	// else the bullet should follow the spaceShooter
		    else{
			    bulletList.get(i).setX(spaceShooter.getX());
			    bulletList.get(i).setY(spaceShooter.getY());
		    }

    		//draw the bullet
	    	bulletList.get(i).draw(c);
		    //remove the bullets that are out of the screen
		    if(bulletList.get(i).getY() < 0){
			    bulletList.remove(i);
		    } //fi
	    } //rof
   } //eof draw

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