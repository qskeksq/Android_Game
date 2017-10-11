package com.example.administrator.game.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.example.administrator.game.R;

import java.util.Random;

/**
 * 중심축을 중심으로 Bitmap 을 회전시킬 수 있다.
 */
public class Rose extends View {

    Bitmap[] roses = new Bitmap[4];
    Bitmap rose;
    int cx, cy;
    int w, h;
    int DIRECTION = 0;

    public Rose(Context context) {
        super(context);

    }

    public void init(){
        DIRECTION = new Random().nextInt(4);

        cx = getWidth()/2;
        cy = getHeight()/2;

        roses[0] = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rose_1);
        roses[1] = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rose_2);
        roses[2] = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rose_3);
        roses[3] = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rose_4);

        rose = roses[DIRECTION];
        switch (DIRECTION){
            case 0:
                w = rose.getWidth()/2;
                h = rose.getHeight();
                break;
            case 1:
                w = 0;
                h = rose.getHeight()/2;
                break;
            case 2:
                w = rose.getWidth()/2;
                h = 0;
                break;
            case 3:
                w = rose.getWidth();
                h = rose.getHeight()/2;
                break;
        }
    }

    /**
     * rotate 한 후 그리면 canvas 가 돌아간 상태에서 그려지는 것이다.
     * 따라서 특정 각도로 돌려서 그리고 싶으면 먼저 돌리고 그 후 그려야 한다.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(roses[DIRECTION], 10, 10, null);
        init();
        for (int i = 0; i < 18; i++) {
            canvas.rotate(20, cx, cy);
            canvas.drawBitmap(rose, cx-w, cy-h, null);
        }
    }
}
