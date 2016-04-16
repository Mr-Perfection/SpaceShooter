package com.learningjava.stephenlee.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by StephenLee on 4/12/16.
 */
public class MysteryShip
{
    //fields
    private int x_ms;
    private int y_ms;
    private boolean dd = false;
    private boolean isMoving = false;

    //constructor: part of methods
    public MysteryShip(int screenWidth, int screenHeight) {

        x_ms = screenWidth;
        y_ms = screenHeight;
    }

    //methods

    // move mystery ship across, for now
    public void moveShip() {
        x_ms = x_ms +50;
    }


    public int getX() {
        return x_ms;
    }
    public void setX(int screenX) {
        x_ms = screenX;
    }
    public int getY() {
        return y_ms;
    }
    public void setY(int screenY) {
        y_ms = screenY;
    }
    public boolean getisMoving(){ return isMoving;}
    public void setisMoving(boolean _isMoving){isMoving = _isMoving;}
    //isShipDestroyed and setShipDestroyed
//    public int isShipDestroyed(){ return sd;}
//    public void setShipDestroyed(int setsd) {
//        sd = setsd;
//    }
    //mysteryship is destroyed
    public boolean Destroyed(){ return dd;}
    public void setDestroyed(){dd = true;}
    public void setDDestroyed(){dd = false;}

    //update mystery ships


    //drawing mystery ship
    public void draw(Canvas canvas) {
//        System.out.println("drawing ms");
        Paint p = new Paint();
        p.setColor(Color.BLUE);

        canvas.drawCircle(x_ms, y_ms, 10, p);
//        System.out.println("x_ms: " + x_ms);
//        System.out.println("y_ms: " +y_ms);
    } //eof draw



}
