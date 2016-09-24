package com.ysy.pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ysy.sourcer_slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class GestureLock extends View {

    private Point[][] points =  new Point[3][3];
    private ArrayList<Point> pointList = new ArrayList<Point>();
    private ArrayList<Integer> passList = new ArrayList<Integer>();
    private boolean inited = false;
    private boolean isDraw = false;

    private Bitmap bitmapPointError;
    private Bitmap bitmapPointNoraml;
    private Bitmap bitmapPointPress;

    private onDrawFinishedListener listener;

    float mouseX, mouseY;

    private float bitmapR;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint pressPaint = new Paint();
    Paint errorPaint = new Paint();


    public GestureLock(Context context) {
        super(context);
    }

    public GestureLock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureLock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!inited) {
            init();
        }

        drawPoints(canvas);

        if (pointList.size() > 0) {
            Point a = pointList.get(0);
            for (int i = 1; i < pointList.size(); i++) {
                Point b = pointList.get(i);
                drawLine(canvas, a, b);
                a = b;
            }
            if (isDraw) { // 画完最后一个点后，还保证能继续绘制额外活动的直线
                drawLine(canvas, a, new Point(mouseX, mouseY));
            }
        }

    }

    private void drawLine(Canvas canvas, Point a, Point b) {
        if (a.state == Point.STATE_PRESS) {
            canvas.drawLine(a.x, a.y, b.x, b.y, pressPaint);
        }
        else if (a.state == Point.STATE_ERROR) {
            canvas.drawLine(a.x, a.y, b.x, b.y, errorPaint);
        }
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].state == Point.STATE_NORMAL) {
                    // Normal
                    canvas.drawBitmap(bitmapPointNoraml, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
                }
                else if (points[i][j].state == Point.STATE_PRESS) {
                    // Press
                    canvas.drawBitmap(bitmapPointPress, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
                }
                else if (points[i][j].state == Point.STATE_ERROR) {
                    // Error
                    canvas.drawBitmap(bitmapPointError, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
                }
            }
        }
    }

    private void init() {

        pressPaint.setColor(Color.YELLOW);
        pressPaint.setStrokeWidth(5);
        errorPaint.setColor(Color.RED);
        errorPaint.setStrokeWidth(5);

        bitmapPointError = BitmapFactory.decodeResource(getResources(), R.drawable.error);
        bitmapPointNoraml = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        bitmapPointPress = BitmapFactory.decodeResource(getResources(), R.drawable.press);

        bitmapR = bitmapPointError.getHeight() / 2;

        int width = getWidth();
        int height = getHeight();
        int offset = Math.abs(width - height) / 2;
        int offsetX, offsetY;
        int space;

        if(width > height) {
            space = height / 4;
            offsetX = offset;
            offsetY = 0;
        }
        else {
            space = width / 4;
            offsetY = offset;
            offsetX = 0;
        }

        for (int j = 0; j < 3; j++) { // 利用循环来快速绘制点
            for (int i = 0; i < 3; i++) {
                points[i][j] = new Point(offsetX + space * (i + 1), offsetY + space * (j + 1));
            }
        }

        inited = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mouseX = event.getX();
        mouseY = event.getY();
        int[] ij;
        int i,j;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                resetPoints();
                ij = getSelectedPoint();
                if (ij != null) {
                    isDraw = true;
                    i = ij[0];
                    j = ij[1];
                    points[i][j].state = Point.STATE_PRESS;
                    pointList.add(points[i][j]);
                    passList.add(i * 3 + j);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDraw) {
                    ij = getSelectedPoint();
                    if (ij != null) {
                        i = ij[0];
                        j = ij[1];
                        if (!pointList.contains(points[i][j])) {
                            points[i][j].state = Point.STATE_PRESS;
                            pointList.add(points[i][j]);
                            passList.add(i * 3 + j);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                boolean valid = false;
                isDraw = false;
                if (listener != null && isDraw == false) {
                    valid = listener.OnDrawFinished(passList);
                }
                if ( valid == false ) { // 绘制点序若错误，则点阵呈现错误状态
                // if ( !valid )同样
                    for (Point p : pointList) {
                        p.state = Point.STATE_ERROR;
                    }
                }
                break;
        }
        this.postInvalidate(); // 刷新屏幕
        return true;
    }

    private int[] getSelectedPoint() {
        Point pMouse = new Point(mouseX, mouseY);

        for (int i = 0; i < points.length; i++) {
            for (int j =0; j < points[i].length; j++) {

                if (points[i][j].distance(pMouse) < bitmapR) {
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return  result;
                }
            }
        }
        return null;
    }

    public void resetPoints() {

        pointList.clear();
        passList.clear();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                points[i][j].state = Point.STATE_NORMAL;
            }
        }
        this.postInvalidate(); // 重绘

    }

    public interface onDrawFinishedListener {

        boolean OnDrawFinished(List<Integer> passList); // 绘制图案是否正确的判定

    }

    public void setOnDrawFinishedListener(onDrawFinishedListener listener) {

        this.listener = listener;

    }

}
