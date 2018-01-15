package com.ziyu.wxtt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mengjiyong on 2018/1/13.
 */

public class PositionView extends View{

    private final static String TAG="PositionView";
    /**
     * 默认半径
     */
    private final static int DEFAULT_RADIUS=30;

    private int touchWidth=30+DEFAULT_RADIUS;
    private Paint mPaint;

    private int mViewWidth;
    private int mViewHeight;

    private int downX;
    private int downY;
    private int lastX;
    private int lastY;

    private Ball mBall1;
    private Ball mBall2;
    private Ball mBall3;
    private Ball mBall4;

    private Ball[] mBalls=new Ball[4];
    private Ball moveBall=null;


    private float centerX;
    private float centerY;
    public PositionView(Context context) {
        this(context,null);
    }

    public PositionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PositionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth=getMeasuredWidth();
        mViewHeight=getMeasuredHeight();
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }


    private void init(Context context) {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        //画左上右下四个圆圈
        mBall1=new Ball(DEFAULT_RADIUS*2,Utils.dp2dx(context,100),DEFAULT_RADIUS);
        mBall2=new Ball(Utils.dp2dx(context,90),DEFAULT_RADIUS*2,DEFAULT_RADIUS);
        mBall3=new Ball(Utils.dp2dx(context,180)-DEFAULT_RADIUS*2,Utils.dp2dx(context,100),DEFAULT_RADIUS);
        mBall4=new Ball(Utils.dp2dx(context,90),Utils.dp2dx(context,200)-DEFAULT_RADIUS*2,DEFAULT_RADIUS);

        mBalls[0]=mBall1;
        mBalls[1]=mBall2;
        mBalls[2]=mBall3;
        mBalls[3]=mBall4;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX= (int) event.getX();
                downY= (int) event.getY();
                
                lastX=downX;
                lastY=downY;
                Log.i(TAG,"downX="+downX+",downY="+downY);

                //若手指落在某个球的范围内，则该球可以滑动
                moveBall=null;
                for(int i=0;i<mBalls.length;i++){
                    if(Utils.point2Distance(
                            downX,
                            downY,
                            mBalls[i].getX(),
                            mBalls[i].getY())<=touchWidth){
                        moveBall=mBalls[i];
                        break;
                    }
                }

                if(moveBall!=null){
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if(moveBall!=null){

                    int moveX= (int) event.getX();
                    int moveY= (int) event.getY();

                    int dx=moveX-lastX;
                    int dy=moveY-lastY;
                    
                    int ballX=moveBall.getX()+dx;
                    int ballY=moveBall.getY()+dy;

                    //判断边界
                    if(ballX+DEFAULT_RADIUS<=mViewWidth
                            &&ballX-DEFAULT_RADIUS>=0
                            &&ballY-DEFAULT_RADIUS>=0
                            &&ballY+DEFAULT_RADIUS<=mViewHeight){

                        moveBall.setX(ballX);
                        moveBall.setY(ballY);

                        lastX=moveX;
                        lastY=moveY;

                        postInvalidate();

                    }
                    return true;

                }

            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas,mBall1.getX(),mBall1.getY(),mBall1.getRadius());
        drawCircle(canvas,mBall2.getX(),mBall2.getY(),mBall2.getRadius());
        drawCircle(canvas,mBall3.getX(),mBall3.getY(),mBall3.getRadius());
        drawCircle(canvas,mBall4.getX(),mBall4.getY(),mBall4.getRadius());

        drawLine(canvas);

        drawCenterPoint(canvas);
    }

    private void drawCircle(Canvas canvas,int centerX,int centerY,int rad){

        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(centerX,centerY,rad,mPaint);

    }

    private void drawLine(Canvas canvas){
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);

        canvas.drawLine(mBall1.getX(),mBall1.getY(),mBall2.getX(),mBall2.getY(),mPaint);
        canvas.drawLine(mBall2.getX(),mBall2.getY(),mBall3.getX(),mBall3.getY(),mPaint);
        canvas.drawLine(mBall3.getX(),mBall3.getY(),mBall4.getX(),mBall4.getY(),mPaint);
        canvas.drawLine(mBall4.getX(),mBall4.getY(),mBall1.getX(),mBall1.getY(),mPaint);

        canvas.drawLine(mBall1.getX(),mBall1.getY(),mBall3.getX(),mBall3.getY(),mPaint);
        canvas.drawLine(mBall2.getX(),mBall2.getY(),mBall4.getX(),mBall4.getY(),mPaint);
    }

    //求四个点的交叉点，用直线方程，ax+b=y
    private void drawCenterPoint(Canvas canvas){
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);

        float a,b,c,d;

        //以下要考虑直线垂直于坐标轴的情况
        if((mBall3.getY()-mBall1.getY())==0&&(mBall4.getX()-mBall2.getX())==0){
            centerX= (float) (mBall3.getX()-mBall1.getX())/2+DEFAULT_RADIUS*2;
            centerY= (float) (mBall4.getY()-mBall2.getY())/2+DEFAULT_RADIUS*2;

        }else if((mBall3.getY()-mBall1.getY())==0){
            centerY=mBall1.getY();

            c=(float) (mBall4.getY()-mBall2.getY())/(float)(mBall4.getX()-mBall2.getX());
            d=mBall2.getY()-c*mBall2.getX();

            centerX=(centerY-d)/c;


        }else if((mBall4.getX()-mBall2.getX())==0){
            centerX=mBall2.getX();

            a=(float) (mBall3.getY()-mBall1.getY())/(float)(mBall3.getX()-mBall1.getX());
            b=mBall1.getY()-a*mBall1.getX();

            centerY=a*centerX+b;
        }else {

            a=(float) (mBall3.getY()-mBall1.getY())/(float)(mBall3.getX()-mBall1.getX());
            b=mBall1.getY()-a*mBall1.getX();

            c=(float) (mBall4.getY()-mBall2.getY())/(float)(mBall4.getX()-mBall2.getX());
            d=mBall2.getY()-c*mBall2.getX();

            centerX=(b-d)/(c-a);
            centerY=a*centerX+b;
        }

        Log.i(TAG,"centerX="+centerX+",centerY="+centerY);

        canvas.drawCircle(centerX,centerY,5,mPaint);

    }


    private class Ball{
        private int x ;
        private int y;
        private int radius;
        public Ball(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

    }

}
