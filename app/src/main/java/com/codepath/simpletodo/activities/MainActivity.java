package com.codepath.simpletodo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.fragments.AllToDoListFragment;
import com.codepath.simpletodo.fragments.BaseTodoListFragment;
import com.codepath.simpletodo.fragments.HighPriorityToDoListFragment;
import com.codepath.simpletodo.fragments.HomeToDoListFragment;
import com.codepath.simpletodo.fragments.OverdueToDoListFragment;
import com.codepath.simpletodo.models.ToDoItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BaseTodoListFragment.ToDoActionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int INDEX_HOME = 0;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nv_drawer)
    NavigationView mDrawer;

    private ActionBarDrawerToggle mDrawerToggle;

    public static Intent createIntent(final Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        setSupportActionBar(mToolbar);
        setupDrawerContent();

        showHome();
    }

    private void showHome() {
        // simulate clicking which shows home
        selectDrawerItem(mDrawer.getMenu().getItem(INDEX_HOME));
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent() {
        mDrawer.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(final MenuItem menuItem) {
                    selectDrawerItem(menuItem);
                    return true;
                }
            });
    }

    public void selectDrawerItem(final MenuItem menuItem) {
        Fragment fragment;
        switch(menuItem.getItemId()) {
            case R.id.nav_home: {
                fragment = HomeToDoListFragment.createInstance();
                break;
            }
            case R.id.nav_all: {
                fragment = AllToDoListFragment.createInstance();
                break;
            }
            case R.id.nav_high: {
                fragment = HighPriorityToDoListFragment.createInstance();
                break;
            }
            case R.id.nav_overdue: {
                fragment = OverdueToDoListFragment.createInstance();
                break;
            }
            default:
                Log.w(TAG, "Unknown menu item: " + menuItem.getTitle());
                fragment = HomeToDoListFragment.createInstance();
        }

        // Insert the fragment by replacing any existing fragment
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onAddNewToDo() {
        navigateToEditItemActivity(null);
    }

    @Override
    public void onEditToDo(final ToDoItem toDoItem) {
        navigateToEditItemActivity(toDoItem);
    }

    private void navigateToEditItemActivity(final ToDoItem toDoItem) {
        final Intent intent = EditItemActivity.createIntent(this, toDoItem);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        startActivity(intent, options.toBundle());
    }
}