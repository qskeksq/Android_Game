package com.example.administrator.game.bitmap.SquareTarget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.game.R;

/**
 * Created by Administrator on 2017-09-30.
 */

public class SquareTargetStage extends View {

    Bitmap background, square;
    int cx, cy;
    int tw, th;

    public SquareTargetStage(Context context) {
        super(context);
        init();
    }

    public void init(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        cx = width/2;
        cy = height/2;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        background = Bitmap.createScaledBitmap(background, width, height, true);
        square = BitmapFactory.decodeResource(getResources(), R.drawable.targer);
//        square = Bitmap.createScaledBitmap(square, 280, 280, true);

        tw = square.getWidth()/2;
        th = square.getHeight()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, 0,0, null);
        canvas.drawBitmap(square, cx-tw, cy-th, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }
}
