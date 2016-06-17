package com.codepath.simpletodo.di;

import com.codepath.simpletodo.activities.EditItemActivity;
import com.codepath.simpletodo.activities.MainActivity;
import com.codepath.simpletodo.di.modules.ApplicationModule;
import com.codepath.simpletodo.di.modules.DatabaseModule;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class})
public interface ApplicationComponent {

    void inject(ToDoItemPersistenceService toDoItemPersistenceService);

//    void inject(MainActivity mainActivity);

//    void inject(EditItemActivity editItemActivity);
}