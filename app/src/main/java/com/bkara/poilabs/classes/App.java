package com.bkara.poilabs.classes;

import android.app.Application;
import android.content.Context;

/**
 * Created by bkara on 29/7/2020.
 *
 *
 *
 */

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
    }
}
