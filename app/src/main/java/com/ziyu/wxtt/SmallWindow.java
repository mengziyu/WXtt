package com.ziyu.wxtt;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mengjiyong on 2018/1/14.
 */

public class SmallWindow extends LinearLayout{


    public WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
    private Context mContext;
    public SmallWindow(Context context) {
        super(context);
        this.mContext=context;
        init(context);

    }

    public SmallWindow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallWindow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_small_view,this);

        //设置小窗口属性信息
        lp.type = WindowManager.LayoutParams.TYPE_PHONE;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        lp.format= PixelFormat.TRANSPARENT;
        //设置wrap_content
        lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM|Gravity.RIGHT;
        lp.y=200;

        TextView tvOpen=findViewById(R.id.tv_open);
        tvOpen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyWindowManager.getInstance(mContext).removeSmallWindow();
                MyWindowManager.getInstance(mContext).createBigWindow();
            }
        });
    }


}
