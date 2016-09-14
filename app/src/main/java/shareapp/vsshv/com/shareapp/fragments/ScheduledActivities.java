package shareapp.vsshv.com.shareapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shareapp.vsshv.com.shareapp.R;

/**
 * Created by PC414506 on 31/08/16.
 */

public class ScheduledActivities extends Fragment{

    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.twitterbg,
            R.drawable.uber_badge,
            R.drawable.gmailbg
    };

    public static ScheduledActivities newInstance() {
        ScheduledActivities f = new ScheduledActivities();

        return (f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduled_activities, null);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new TwitterFragment();
                case 1 : return new UberFragment();
                case 2 : return new GmailFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return tabIcons.length;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Twitter";
                case 1 :
                    return "Uber";
                case 2 :
                    return "Gmail";
            }
            return null;
        }
    }
}
