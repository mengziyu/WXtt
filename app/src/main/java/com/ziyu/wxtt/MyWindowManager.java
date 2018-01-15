package com.ziyu.wxtt;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by mengjiyong on 2018/1/14.
 */

public class MyWindowManager {
    private final static String TAG="MyWindowManager";

    private SmallWindow mSmallWindow;
    private BigWindow mBigWindow;

    private WindowManager mWindowManager;
    private static MyWindowManager mMyWindowManager;
    private Context mContext;
    public MyWindowManager(Context context){
        this.mContext=context;
        if(mWindowManager==null){
            mWindowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

    }


    public static MyWindowManager getInstance(Context context){

        if(mMyWindowManager==null){
            mMyWindowManager= new MyWindowManager(context);
        }

        return mMyWindowManager;
    }


    public void createSmallWindow(){
        if(mSmallWindow==null){
            mSmallWindow=new SmallWindow(mContext);
            mWindowManager.addView(mSmallWindow,mSmallWindow.lp);
        }
    }

    public void removeSmallWindow(){
        if(mSmallWindow!=null){
            mWindowManager.removeView(mSmallWindow);
            mSmallWindow=null;
        }
    }

    public void createBigWindow(){
        if(mBigWindow==null){
            mBigWindow=new BigWindow(mContext);
            mWindowManager.addView(mBigWindow,mBigWindow.lp);
        }
    }

    public void removeBigWindow(){
        if(mBigWindow!=null){
            mWindowManager.removeView(mBigWindow);
            mBigWindow=null;
        }
    }

    public void removeAllWindow(){
        removeSmallWindow();
        removeBigWindow();

    }

    public void updateBigWindow(){
        if(mBigWindow!=null){
            mWindowManager.updateViewLayout(mBigWindow,mBigWindow.lp);
        }
    }
}
