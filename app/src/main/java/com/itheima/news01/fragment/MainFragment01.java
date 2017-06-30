package com.itheima.news01.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.itheima.news01.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g572-528r on 2017/6/27.
 */

public class MainFragment01 extends BaseFragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_01;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) super.mRootView.findViewById(R.id.view_pager_02);
        tabLayout = (TabLayout) super.mRootView.findViewById(R.id.tab_layout);

        initViewPager();
    }

    private void initViewPager() {
        final String[] titles = new String[] {
                "头条", "社会", "科技", "财经", "体育", "汽车"
        };

        final String[] channelId = new String[] {
                "T1348647909107",
                "T1348648037603",
                "T1348649580692",
                "T1348648756099",
                "T1348649079062",
                "T1348654060988",
        };

        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            NewsFragment fragment = new NewsFragment();
            fragment.setChannelId(channelId[i]);
            fragments.add(fragment);
        }

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        //关联ViewPager和TabLayout显示
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        initViewPager();
    }
}
