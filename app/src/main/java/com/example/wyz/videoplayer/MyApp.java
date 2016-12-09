package com.example.wyz.videoplayer;

import android.app.Application;
import android.content.Context;

/**
 * Created by Wyz on 2016/12/8.
 */
public class MyApp extends Application {

    private static Context sAppContext;


    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext=MyApp.this;
    }

    public static Context getAppContext() {
        return sAppContext;
    }
}
