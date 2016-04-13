package com.learningjava.stephenlee.spaceshooter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by StephenLee on 4/9/16.
 */
public class Alien {

    private static final String Name = SpaceView.class.getSimpleName();
    int isAlienDestroyed = 0;   //1: yes    0:no
    private int width, height;  //Screen size
    private Bitmap bitmap; // the actual bitmap
    private float x,y;   // the X,Y coordinate
    private int count;
    private int cFlag = 0;  //0: goes down, 1: goes up

    float vx,vy;   // velocity x y
    private boolean isVisible = true; //invisibility flag (when bullet hits the enemy, it becomes invisible)
    //Alien bitmap
    public Alien(Bitmap _bitmap, int _width, int _height)
    {
        bitmap = _bitmap;
        width = _width;
        height = _height;

//        x = _width / 5;
//        y = _height / 6;

    }//EOF Alien

    void setPosition(int _x, int _y)
    {
        x = _x;
        y = _y;

        Log.d(Name, "POSITION:" + x + "   " + y);


    }


    //Get position of alien
    float getPositionX(){return x;}
    float getPositionY(){return y;}



    void setVelocity(float velocity_x, float velocity_y)
    {
        vx = velocity_x;
        vy = velocity_y;
        Log.d(Name, "VELOCITY IS" + vx + "   " + vy);
    }

    public void setInvisible(boolean _isVisible)
    {
        isVisible = _isVisible; //set it to false when hit by bullet

    }

    public Boolean getVisibility()
    {
        return isVisible; //set it to false when hit by bullet

    }

    //Draw the alien
    void draw(Canvas canvas) {


        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);

    }




    void update()
    {
        if(cFlag == 0)
        {
            if (count < 20)
            {
                count++;
                y = y + 2f;
            }
            if (count == 20)
                cFlag = 1;  //now the alien moves down
        }
        else if(cFlag == 1)
        {
            if(count > 0 )
            {
                count--;
                y= y - 2f;
            }

            if (count == 0)
                cFlag = 0; //now the alien moves up
        }


        x=x+vx;

        if (x>width&& vx>0)
        {
            vx = -vx;   //Reverse direction


        }
        if (x<0 && vx<0)
        {
            vx=-vx; //Reverse direction

        }

        if(x >= width || x <= 0)
        {
            y=y+vy; //move down a bit.
        }

        //If it reaches the bottom screen, destroy the alien.
        if(y >= height - 50)
            isVisible = false;
        else
            isVisible = true;



    }



}
