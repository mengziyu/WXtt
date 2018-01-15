package com.ziyu.wxtt;


import android.content.Context;

import java.io.File;

/**
 * Created by mengjiyong on 2018/1/13.
 */

public class Utils {

    public static float point2Distance(float x1,float y1,float x2,float y2){
        float ii=(x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
        float result=(float) Math.sqrt(ii);
        return result;
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isRoot() {

        boolean root = false;

        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }

        } catch (Exception e) {
        }

        return root;
    }

    public static int screenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2dx(Context context,int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
