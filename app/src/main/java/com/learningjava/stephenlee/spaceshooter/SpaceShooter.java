package com.learningjava.stephenlee.spaceshooter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by StephenLee on 4/5/16.
 */
public class SpaceShooter
{

    private int width, height;  //Screen size
    private Bitmap bitmap; // the actual bitmap
    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private boolean touched; // if spaceshooter is touched/picked up

    /*Set SpaceShooter (bitmap, x, y)*/
    public SpaceShooter(Bitmap _bitmap, int _width, int _height)
    {
        bitmap = _bitmap;
        width = _width;
        height = _height;
        x = _width / 2;
        y = _height * 3/4;

    }


    //Get bitmap
    public Bitmap getBitmap()
    {
        return bitmap;
    }

    //Set bitmap
    public void setBitmap(Bitmap _bitmap)
    {
        bitmap = _bitmap;
    }
    //Get x axis
    public int getX() {

        return x;

    }

    //Set x axis
    public void setX(int _x) {

        x = _x;

    }
    //Get y axis
    public int getY() {

        return y;

    }
    //Set y axis
    public void setY(int _y) {

        y = _y;

    }
    //is touched
    public boolean isTouched(){
        return touched;
    }
    public void setTouched(boolean _touched)
    {
        touched = _touched;
    }

    void draw(Canvas canvas) {

        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);

    }

    public void handleActionDown(int eventX, int eventY) {

        if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {

            if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {

                // spaceshooter touched

                setTouched(true);

            } else {

                setTouched(false);

            }

        } else {

            setTouched(false);

        }



    }





}