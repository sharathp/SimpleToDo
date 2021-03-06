package com.codepath.simpletodo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.codepath.simpletodo.di.ApplicationComponent;
import com.codepath.simpletodo.di.DaggerApplicationComponent;
import com.codepath.simpletodo.di.modules.ApplicationModule;
import com.codepath.simpletodo.utils.AlarmUtils;

public class SimpleToDoApplication extends Application {
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencyInjection();
        registerAlarms();
    }

    private void registerAlarms() {
        AlarmUtils.scheduleToDoNotificationAlarm(this);
    }

    private void initDependencyInjection() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    public static SimpleToDoApplication from(@NonNull final Context context) {
        return (SimpleToDoApplication) context.getApplicationContext();
    }
}