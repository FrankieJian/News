package com.itheima.news01;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.itheima.news01.fragment.MainFragment01;
import com.itheima.news01.fragment.MainFragment02;
import com.itheima.news01.fragment.MainFragment03;
import com.itheima.news01.fragment.MainFragment04;
import com.itheima.news01.fragment.MainFragment05;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.rg_01);
        initViewPager();
        initNavigationView();
        initToolbar();
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();     // 同步drawerLayout和toolbar的状态
    }

    // 使用toolBar代替ActionBar,并设置相应属性
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);     //使用toolbar代替ActionBar

//        toolbar.setLogo(R.drawable.logo);
        toolbar.setTitle("News实训");

//        toolbar.setSubtitle("这是子标题");
        toolbar.setTitleTextColor(Color.WHITE);
//        显示标题栏左上角返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setSubtitleTextColor(Color.YELLOW);

        //导航图标显示
//        toolbar.setNavigationIcon(R.drawable.btn_back);

        //点击toolbar导航栏左上角的图标后，退出当前界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //=======================Toolbar右上角弹出菜单（begin）============================
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_option, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_01) {
            showToast("item 01");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //=======================Toolbar右上角弹出菜单（end）==============================

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        // 侧滑菜单点击监听
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // 点击侧滑菜单item时，通过DrawerLayout关闭侧滑菜单
                        showToast("" + item.getTitle());
                        drawerLayout.closeDrawers();

                        return false;
                    }
        });
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment01());
        fragments.add(new MainFragment02());
        fragments.add(new MainFragment03());
        fragments.add(new MainFragment04());
        fragments.add(new MainFragment05());

        viewPager.setAdapter(new FragmentPagerAdapter(
                getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @Override
    public void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override           //点击单选是，调用此方法
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_01:               //新闻
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_02:               //视听
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_03:               //阅读
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_04:               //发现
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.rb_05:               //设置
                        viewPager.setCurrentItem(4);
                        break;
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:               //新闻
                        viewPager.setCurrentItem(0);
                        radioGroup.check(R.id.rb_01);
                        break;
                    case 1:               //视听
                        viewPager.setCurrentItem(1);
                        radioGroup.check(R.id.rb_02);
                        break;
                    case 2:               //阅读
                        viewPager.setCurrentItem(2);
                        radioGroup.check(R.id.rb_03);
                        break;
                    case 3:               //发现
                        viewPager.setCurrentItem(3);
                        radioGroup.check(R.id.rb_04);
                        break;
                    case 4:               //设置
                        viewPager.setCurrentItem(4);
                        radioGroup.check(R.id.rb_05);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
