package com.example.administrator.game.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.game.R;

/**
 * Created by Administrator on 2017-09-30.
 */

public class RolyPoly extends View {

    Bitmap background, shadow, rolyPoly;
    int cx, cy, rw, rh, sw, sh;
    int bx, by;

    int ang, dir;   // 현재 각도, 회전 방향
    int an1, an2;   // 좌우 한계점

    public RolyPoly(Context context) {
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
        shadow = BitmapFactory.decodeResource(getResources(), R.drawable.shadow);
        rolyPoly = BitmapFactory.decodeResource(getResources(), R.drawable.toy);

        rw = rolyPoly.getWidth()/2;
        rh = rolyPoly.getHeight()/2;

        sw = shadow.getWidth()/2;
        sh = shadow.getHeight()/2;

        bx = cx-rw;
        by = cy-rh+130;

        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RotateToy();
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(shadow, cx-sw-20, cy+rh, null);

        canvas.rotate(ang, cx, cy+rh-130);
        canvas.drawBitmap(rolyPoly, cx-rw, cy-rh+130, null);
        canvas.rotate(ang, cx, cy+rh-130);
        Log.e("호출","========");
    }


    private void RotateToy() {
        // 각도가 dir 만큼 계속 움직이다가
        ang = ang + dir;
        // 한계 각도에 다다르면
        if(ang >= an1 || ang <= an2){
            // 한계 각도를 줄여주고
            an1--;
            an2++;
            // 방향을 바꿔준다
            dir = -dir;
            //
            ang += dir;
        }
    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            an1 = 15;
            an2 = -15;
            if(dir == 0){
                dir = -1;
            }
        }
        return true;
    }

}

