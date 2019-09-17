package com.usoft.pedidos.Vista;

import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {

    private static Context appcontext;

    @Override
    public void onCreate() {
        super.onCreate();
        appcontext = getApplicationContext();
    }

    public static Context getContext(){
        return  appcontext;
    }

}
