package com.codepath.simpletodo.di;

import com.codepath.simpletodo.di.modules.ApplicationModule;
import com.codepath.simpletodo.di.modules.DatabaseModule;
import com.codepath.simpletodo.repos.ToDoItemDAO;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class})
public interface ApplicationComponent {

    ToDoItemDAO getToDoItemDao();
}