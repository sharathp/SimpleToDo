package com.codepath.simpletodo.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.codepath.simpletodo.SimpleToDoApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final SimpleToDoApplication application;

    public ApplicationModule(final SimpleToDoApplication application) {
        this.application = application;
    }

    @Singleton
    @NonNull
    @Provides
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @NonNull
    @Singleton
    SharedPreferences providePreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
