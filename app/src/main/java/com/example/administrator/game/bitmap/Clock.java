package com.example.administrator.game.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.administrator.game.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017-09-30.
 */
public class Clock extends View {

    Bitmap dial, secPin, minPin, hourPin;
    int cx, cy;
    int hour, min, sec;
    float rSec, rMin, rHour;

    public Clock(Context context) {
        super(context);
        init();
    }

    public void init(){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        cx = width/2;
        cy = (height-100)/2;

        dial = BitmapFactory.decodeResource(getResources(), R.drawable.dial);
        secPin = BitmapFactory.decodeResource(getResources(), R.drawable.pin_1);
        minPin = BitmapFactory.decodeResource(getResources(), R.drawable.pin_2);
        hourPin = BitmapFactory.decodeResource(getResources(), R.drawable.pin_3);

        timeHandler.sendEmptyMessage(0);
    }

    /**
     * canvas 를 rotate 하는 것이기 때문에 먼저 돌리고 그 후 그려야 한다.
     * 또한 각 시, 분, 침은 따로 각도를 계산했기 때문에 canvas 를 되돌려 줘야 원하는 각도가 그려진다.
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calcTime();
        // 시계
        canvas.drawBitmap(dial, cx-dial.getWidth()/2, cy-dial.getHeight()/2, null);
        // 시침
        canvas.rotate(rHour, cx, cy);
        canvas.drawBitmap(hourPin, cx-hourPin.getWidth()/2, cy-hourPin.getHeight()+20, null);
        // 분침
        canvas.rotate(rMin-rHour, cx, cy);
        canvas.drawBitmap(minPin, cx-minPin.getWidth()/2, cy-minPin.getHeight()+20, null);
        // 초침
        canvas.rotate(rSec-rMin, cx, cy);
        canvas.drawBitmap(secPin, cx-secPin.getWidth()/2, cy-secPin.getHeight()+20, null);
        canvas.rotate(-rSec, cx, cy);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void calcTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        // 타임존이 맞지 않아서 그랬던 것이로군.
        TimeZone utc = TimeZone.getTimeZone("Asia/Seoul");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(utc);
        calendar.setTime(date);
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);
        sec = calendar.get(Calendar.SECOND);

        Log.e("시간", hour+":"+min+":"+sec);

        rSec = 6*sec;
        rMin =  rSec/60 + 6*min;    // 아주 미세하게라도 초 단위로 움직임을 60으로 나눠서 더해준다.
        rHour = rMin/12 + 30*hour;
    }

    Handler timeHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            timeHandler.sendEmptyMessageDelayed(0, 500);
        }
    };

}
