package com.codepath.simpletodo.di.modules;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.repos.SimpleToDoDatabase;
import com.yahoo.squidb.data.AbstractModel;
import com.yahoo.squidb.data.SquidDatabase;
import com.yahoo.squidb.data.UriNotifier;
import com.yahoo.squidb.sql.SqlTable;
import com.yahoo.squidb.sql.Table;

import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @NonNull
    @Singleton
    public SimpleToDoDatabase provideShopTalkDatabase(@NonNull final Context context) {
        final SimpleToDoDatabase database = new SimpleToDoDatabase(context);

        // Setting up a UriNotifier will sent ContentObserver notifications to Uris on table writes
        registerSimpleDataChangedNotifier(database, ToDoItem.TABLE, ToDoItem.CONTENT_URI);

        return database;
    }

    private void registerSimpleDataChangedNotifier(final SquidDatabase database, final Table table, final Uri uri) {
        database.registerDataChangedNotifier(new UriNotifier(table) {
            @Override
            protected boolean accumulateNotificationObjects(final Set<Uri> uris, final SqlTable<?> table, final SquidDatabase database,
                                                            final DBOperation operation, final AbstractModel modelValues, final long rowId) {
                return uris.add(uri);
            }
        });
    }
}
