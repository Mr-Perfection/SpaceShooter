package com.learningjava.stephenlee.spaceshooter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Elisa on 4/8/2016.
 */
public class Bullet {

    public float x;
    public float y;

    private boolean touched;
    private boolean released = false;


    public Bullet(int screenWidth, int screenHeight) {

        x = screenWidth;
        y = screenHeight;


    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float screenX) {
        x = screenX;
    }

    public void setY(float screenY) {
        y = screenY;
    }


    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean _touched) {
        touched = _touched;
    }

     // function to move bullet up
    public void moveBullet() {
        y = y - 90;
    }


    public boolean isBulletReleased() {
        return released;
    }

    public void setBulletReleased() {
        released = true;
    }

    //draw bullet
    public void draw(Canvas canvas) {
        System.out.println("in b.draw");
        Paint p = new Paint();
        p.setColor(Color.RED);

        canvas.drawCircle(x, y, 10, p);
    } //eof draw

    //click
    public void handleActionDown(int bulletX, int bulletY) {

        if (bulletX >= x && bulletX <= x) {
            if (bulletY >= y && bulletY <= y) {

                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {

            setTouched(false);

        }
    } //eof handleactiondown
}