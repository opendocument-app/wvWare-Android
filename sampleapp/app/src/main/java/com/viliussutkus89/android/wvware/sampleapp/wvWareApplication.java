package com.viliussutkus89.android.wvware.sampleapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

public class wvWareApplication extends Application implements Configuration.Provider {
    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setDefaultProcessName(getPackageName())
                .build();
    }
}
