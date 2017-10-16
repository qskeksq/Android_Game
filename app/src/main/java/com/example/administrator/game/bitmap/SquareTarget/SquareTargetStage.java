package com.example.administrator.game.bitmap.SquareTarget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.game.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-09-30.
 */

public class SquareTargetStage extends View {

    Bitmap background, square, hole;
    int cx, cy;
    int tw, th;
    int bx, by;

    int total, point;
    Rect[] rects = new Rect[3];


    List<Bullet> bulletList = new ArrayList<>();

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
        square = Bitmap.createScaledBitmap(square, 280, 280, true);
        hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
        tw = square.getWidth()/2;
        th = square.getHeight()/2;
        bx = hole.getWidth()/2;
        by = hole.getHeight()/2;
        rects[0] = new Rect();
        rects[1] = new Rect();
        rects[2] = new Rect();
        rects[0].set(cx-40, cy-40, cx+40, cy+40);
        rects[1].set(cx-90, cy-90, cx+90, cy+90);
        rects[2].set(cx-140, cy-140, cx+140, cy+140);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        // 배경을 먼저 그리면 안 됨
       canvas.drawBitmap(background, 0,0, null);

        canvas.drawText("포인트 : "+total , 10, 30, paint);
        canvas.drawText("총 점수 : "+point, 200, 30, paint);

        canvas.drawBitmap(square, cx-tw, cy-th, null);
        for(Bullet bullet : bulletList){
            canvas.drawBitmap(hole, bullet.x-bx, bullet.y-by, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Bullet bullet = new Bullet();
                int x = (int)event.getX();
                int y = (int)event.getY();
                bullet.x = x;
                bullet.y = y;
                if(rects[0].contains(x,y)){
                    bullet.point = 7;
                    total += 7;
                    point ++;
                } else if(rects[1].contains(x,y)){
                    bullet.point = 5;
                    total += 5;
                    point++;
                } else if(rects[2].contains(x,y)){
                    bullet.point = 3;
                    total += 3;
                    point++;
                }
                bulletList.add(bullet);
                break;
        }
        invalidate();
        return true;
    }
}
