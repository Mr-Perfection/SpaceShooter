package com.learningjava.stephenlee.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by StephenLee on 4/5/16.
 */
public class SpaceView extends SurfaceView implements SurfaceHolder.Callback
{
    Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK); //Create a black background
        Rect dst = new Rect();
        dst.set(10, 30, 20, 40);//Set window to place image from (10,30) to (20, 40)
        canvas.drawBitmap(myBitmap, null, dst, null);

    }

    public SpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }
}
