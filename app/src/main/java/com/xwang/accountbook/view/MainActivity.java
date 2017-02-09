package com.xwang.accountbook.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xwang.accountbook.R;
import com.xwang.common.Tips;
import com.xwang.common.activity.BaseActivity;
import com.xwang.common.adapter.BaseListAdapter;
import com.xwang.common.adapter.CommonBaseAdapter;
import com.xwang.common.adapter.CommonViewHolder;
import com.xwang.common.utils.PrefUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by xwangly on 2017/2/8.
 */

public class MainActivity extends BaseActivity {
    @Bind(R.id.drawer_list)
    protected ListView listView;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;
    @Bind(R.id.title)
    protected TextView tvTitle;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        ChoreographerHelper.getInstance(this).start();
        initToolbarAndDrawer();
        mTintManager.setStatusBarTintResource(R.color.colorPrimary);

        listView.setAdapter(new MenuAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeDrawer();

//                switch (item.id) {
//                    case R.id.menu_search :
//                        Tips.showToast("Coming soon...");
//                        break;
//
//                    case R.id.menu_all_people:
//                        AllPeopleActivity.start(mActivity);
//                        break;
//
//                    case R.id.menu_recent_news :
//                        RecentPostListActivity.start(mActivity);
//                        break;
//
//                    default:break;
//                }
            }
        });
//
//        getSupportFragmentManager().beginTransaction().add(R.id.container,
//                PeopleListFragment.newInstance()).commit();

        //第一次启动，会打开抽屉菜单
//        Observable.fromCallable(new Callable<Void>() {
//            @Override
//            public Void call() throws Exception {
//                if ((boolean) PrefUtils.getValue(MainActivity.this, PrefUtils.KEY_FIRST_ENTER, true)) {
//                    openDrawer();
//                    PrefUtils.setValue(MainActivity.this, PrefUtils.KEY_FIRST_ENTER, false);
//                }
//                return null;
//            }
//        })
//                .delay(1500, TimeUnit.MILLISECONDS)
//                .subscribe();
//        NewsDetailActivity.startActivity(this, "http://music.163.com/m/topic/194001?type=android");
    }

    private void initToolbarAndDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        tvTitle.setText("长江");
//        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.inflateMenu(R.menu.main);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Tips.showToast("Coming soon...");
                        break;
                    case R.id.action_about:
                        Tips.showToast("About me");
                        break;
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    closeDrawer();
                } else {
                    openDrawer();
                }
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        closeDrawer();
        // Otherwise, it may return to the previous fragment stack
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // Lastly, it will rely on the system behavior for back
            super.onBackPressed();
        }
    }

    public boolean closeDrawer() {
        // If the drawer is open, back will close it
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return true;
        }
        return false;
    }

    private boolean openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }


    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    class MenuAdapter extends BaseListAdapter{
        private final int[] IDS = {R.drawable.ic_search_black_18dp,R.drawable.ic_supervisor_account_black_18dp,R.drawable.ic_view_list_black_18dp};
        private final int[] TITLES = {R.string.searching,R.string.all_people,R.string.recent_news};

        public MenuAdapter(Context context) {
            super(context, R.layout.layout_menu_item);
        }


        @Override
        protected void convert(int position, CommonViewHolder helper) {
            helper.setImageResource(R.id.iv_menu_icon, IDS[position]);
            helper.setText(R.id.tv_menu_item, getString(TITLES[position]));
        }

        @Override
        public int getCount() {
            return IDS.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
    }
}
