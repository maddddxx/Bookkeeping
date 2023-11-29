package com.hui.tally;

import android.app.Application;

import com.hui.tally.db.DBManager;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.initDB(getApplicationContext());
    }
}
