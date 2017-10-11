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

import java.util.Random;

/**
 * 1. 비트맵 그리기
 * 2. 화면 크기의 이해
 */
public class Rabbit extends View {

    int width, height;  // 화면 전체
    int cx, cy;         // 캔버스 중앙
    int rw, rh;         // 토끼의 길이/2
    int x, y;           // 토끼의 중심 좌표
    int mx = 15, my = 15;         // 움직이는 거리
    Bitmap[] rabbit = new Bitmap[2];
    int n, counter;     // 토끼 셀 애니메이션

    boolean isDown = false;

    int X_DIRECTION = 0;
    int Y_DIRECTION = 0;
    final int UP = 0;
    final int DOWN = 1;
    final int RIGHT = 0;
    final int LEFT = 1;

    public Rabbit(Context context) {
        super(context);
        init(context);

    }

    public void init(Context context){
        // 화면 전체 가로 세로 길이(Pixel, 해상도)
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        // 캔버스 중앙
        cx = width/2;
        cy = height/2;

        // drawable 에서 Bitmap 을 그려낸다
        rabbit[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit_1);
        rabbit[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit_2);
        rw = rabbit[0].getWidth()/2;
        rh = rabbit[0].getHeight()/2;

        // 시작 지점을 중심으로 설정
        x = cx;
        y = cy;

        // 시작
        rabbitHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Random random = new Random();

        if(isDown){
            counter++;
            n = (counter % 10) / 5;
            Log.e("눌림", "======================");
        } else {
            switch (X_DIRECTION) {
                case RIGHT:
                    x += mx;
                    break;
                case LEFT:
                    x -= mx;
                    break;
            }
            switch (Y_DIRECTION) {
                case UP:
                    y -= my;
                    break;
                case DOWN:
                    y += my;
                    break;
            }
            // 왼쪽 벽과 충돌
            if (x < rw) {
                X_DIRECTION = RIGHT;
                mx = random.nextInt(10) + 20;
            }

            // 오른쪽 벽 충돌
            if (x > width - rw) {
                X_DIRECTION = LEFT;
                mx = random.nextInt(10) + 20;
            }

            // 천정과 충돌
            if (y < rh) {
                Y_DIRECTION = DOWN;
                my = random.nextInt(10) + 20;
            }

            // 바닥과 충돌
            if (y > height - rh) {
                Y_DIRECTION = UP;
                my = random.nextInt(10) + 20;
            }
            counter++;
            n = (counter % 20) / 10;
        }
        canvas.drawBitmap(rabbit[n], x - rw, y - rh, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
                isDown = true;
                x = (int) event.getX();
                y = (int) event.getY();
                Log.e("좌표 확인", x+":"+y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isDown = false;
                break;
        }
        postInvalidate();
        return true;
    }

    Handler rabbitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            rabbitHandler.sendEmptyMessageDelayed(0, 10);
        }
    };

}
