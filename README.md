# Andorid Game

## Basics
- 장미 그리기 : 좌표값
- 달리는 토끼 : 충돌 처리
- 시계 : 각도값 계산
- 오뚜기 : 반응형 각도값 계산

![](https://github.com/qskeksq/Game/blob/master/pic/AC_%5B20171012-211458%5D.gif)
![](https://github.com/qskeksq/Game/blob/master/pic/AC_%5B20171011-221134%5D.gif)
![](https://github.com/qskeksq/Game/blob/master/pic/AC_%5B20171011-221502%5D.gif)
![](https://github.com/qskeksq/Game/blob/master/pic/AC_%5B20171012-214523%5D.gif)
![](https://github.com/qskeksq/Game/blob/master/pic/AC_%5B20171016-205226%5D.gif)
#### 장미 그리기
- 원하는 좌표값에 비트맵을 그릴 수 있다.
- Canvas rotate 원리 이해
```java
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
```
```java
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawBitmap(roses[DIRECTION], 10, 10, null);
    init();
    for (int i = 0; i < 18; i++) {
        canvas.rotate(20, cx, cy);
        canvas.drawBitmap(rose, cx-w, cy-h, null);
    }
}
```

#### 달리는 토끼
- handler self 호출
- 현재 설정된 방향에 따라 상, 하, 좌, 우 움직임 처리
- 충돌시 방향 전환, 속도 Random 값으로 전환
- 터치시 위치 설정, 지속적인 애니메이션 설정, postInvalidate 이해

```java
Handler rabbitHandler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        invalidate();
        rabbitHandler.sendEmptyMessageDelayed(0, 10);
    }
};
```

```java
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
```

#### 시계
- 시, 분, 초 연동 각도값 계산
- 시침, 분침, 초침 그려주고 rotate 한 만큼 빼 준 후 다시 rotate
- handler self 호출

```java
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
```
```java
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
```


#### 오뚜기
- 터치시 초기값 설정
- hanlder self 호출로 각도값 자동 증감
- 원하는 값을 중심으로 회전
```java
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
```
```java
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
```

```java
 RotateToy();
canvas.drawBitmap(background, 0, 0, null);
canvas.drawBitmap(shadow, cx-sw-20, cy+rh, null);

canvas.rotate(ang, cx, cy+rh-130);
canvas.drawBitmap(rolyPoly, cx-rw, cy-rh+130, null);
canvas.rotate(ang, cx, cy+rh-130);
```

#### 사각 Target 다트

- 겹쳐진 상태의 사각형 좌표 찾아내기

```java
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
```

#### 원형 Target 다트
- atan()을 이용하여 원의 각도 구하기
- Math.pow() 이용 원의 포함 관계 구하기
- Target을 12각도로 나눠 다트의 각도와 비교, 점수 구하기 

```java
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
```