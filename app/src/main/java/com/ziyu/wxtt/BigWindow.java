package com.ziyu.wxtt;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mengjiyong on 2018/1/14.
 */

public class BigWindow extends LinearLayout {

    public WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    private Context mContext;
    private int downX;
    private int downY;
    private int lastX;
    private int lastY;
    private int TouchSlop;
    private int mViewWidth;
    private int mViewHeight;
    private float mCenterOfScreenX;
    private float mCenterOfScreenY;
    private PositionView posView;

    private float fromX;
    private float fromY;
    private float toX;
    private float toY;

    public BigWindow(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BigWindow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BigWindow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
    }


    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_big_view, this);
        TouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        //设置大窗口属性信息
        lp.type = WindowManager.LayoutParams.TYPE_PHONE;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        lp.format = PixelFormat.TRANSPARENT;
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        lp.x = 0;
        lp.y = 0;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView tvDis = findViewById(R.id.tv_distance);
        posView = findViewById(R.id.positionView);


        findViewById(R.id.btn_from).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setPosition(0);
            }
        });

        findViewById(R.id.btn_go).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setPosition(1);

                Log.i("BigView","["+fromX+","+fromY+"]"+"to"+"["+toX+","+toY+"]");

                int distance=(int)Utils.point2Distance(fromX,fromY,toX,toY);
                tvDis.setText(distance+"");

//                fromX=toX;
//                fromY=toY;
            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyWindowManager.getInstance(mContext).removeBigWindow();
                MyWindowManager.getInstance(mContext).createSmallWindow();
            }
        });


    }

    private void setPosition(int type) {
        //根据悬浮view的位置，计算出红点相对于屏幕的位置
        mCenterOfScreenX = lp.x + posView.getCenterX();
        mCenterOfScreenY = lp.y + posView.getCenterY();
        if (type == 0) {
            fromX = mCenterOfScreenX;
            fromY = mCenterOfScreenY;

        } else {
            toX = mCenterOfScreenX;
            toY = mCenterOfScreenY;
        }

        Log.i("BigView", "lpXY=[" + lp.x + "," + lp.y + "]");
        Log.i("BigView", "centerXY=[" + mCenterOfScreenX + "," + mCenterOfScreenY + "]");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();

                lastX = downX;
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int moveY = (int) event.getRawY();

                int dx = moveX - lastX;
                int dy = moveY - lastY;

                lp.x += dx;
                lp.y += dy;

                //边界判断start
                if (lp.x <= 0) {
                    lp.x = 0;
                }

                if (lp.y <= 0) {
                    lp.y = 0;
                }

                if ((lp.x + mViewWidth) >= Utils.screenWidth(mContext)) {
                    lp.x = Utils.screenWidth(mContext) - mViewWidth;
                }

                if ((lp.y + mViewHeight) >= Utils.screenHeight(mContext)) {
                    lp.y = Utils.screenHeight(mContext) - mViewHeight;
                }
                //边界判断end

                MyWindowManager.getInstance(mContext).updateBigWindow();


                lastX = moveX;
                lastY = moveY;

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;

        }
        return true;
    }
}
