package com.example.administrator.game.bitmap.RoundTarget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.game.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-10-16.
 */

public class RoundTargetStage extends View {

    Bitmap background, roundStage, dart;
    int cx, cy;
    int sx, sy;
    int dx, dy;
    List<Dart> dartList = new ArrayList<>();

    int[] targetRadius = {40, 80, 180};
    int[] aScore = {10, 6, 12, 4, 15, 8, 10, 6, 12, 4, 15, 8, 10};
    int score;
    int total;

    public RoundTargetStage(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        cx = width/2;
        cy = height/2;

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
        background = Bitmap.createScaledBitmap(background, width, height, true);

        roundStage = BitmapFactory.decodeResource(context.getResources(), R.drawable.target_2);
//        roundStage = Bitmap.createScaledBitmap(roundStage, 180, 180, true);
        sx = roundStage.getWidth()/2;
        sy = roundStage.getHeight()/2;

        dart = BitmapFactory.decodeResource(context.getResources(), R.drawable.dart);
        dx = dart.getWidth()/2;
        dy = dart.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(roundStage, cx-sx, cy-sy, null);
        canvas.drawText("점수 = "+score, 10, 30, paint);
        canvas.drawText("총점 = "+total, 10, 200, paint);
        for(Dart d : dartList){
            canvas.drawBitmap(dart, d.x, d.y-dy, null);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                calc((int)event.getX(), (int)event.getY());
                break;
        }
        invalidate();
        return false;
    }

    private void calc(int x, int y){

        // 점수를 몇 배 할 것인가
        int n = 3;
        score = 0;
        // 먼저 삼각형의 x,y 변을 구한 후, 라디안 값을 각도 값으로 바꿔주고, 눈에 보이는 좌표와
        // 컴퓨터의 좌표가 다르기 때문에 90도를 빼줘서 위아래를 바꿔준다
        double degree = Math.atan2(cx-x, cy-y)*180/Math.PI - 90;
        if(degree < 0){
            degree += 360;
        }
        for(int i=0; i<targetRadius.length; i++){
            // 다트가 떨어진 원이 타켓 원 안에 있는지 3번 돌면서 확인. 가장 바깥 안쪽 원부터 돌기 때문에 바깥 원으로 갈 때마가 n값을 뺴준다
            if(Math.pow(cx-x, 2) <= Math.pow(targetRadius[i], 2)){
                dartList.add(new Dart(x, y));
                // 점수 계산
                for (int j = 0; j < 12; j++) {
                    int k = 30*j + 15;
                    if(degree < k){
                        score++;
                        total += aScore[j]*n;
                        break;
                    }
                }
                n--;
                if(score > 0){
                    break;
                }
            }
        }
    }
}
