package com.android.callmemaybe.UI;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;

import com.android.callmemaybe.UI.databinding.ActivityMainBinding;
import com.android.callmemaybe.gistService.GistService;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.helpers.PhoneNumberHelper;
import com.android.callmemaybe.helpers.SharedPreferencesHelper;

import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment mostSearchedFragment  = new DeafultMostSearchedFragment();
    private Fragment favsFragment = new MyFavoritesFragment();
    private Fragment allContactsFragment = new AllContactsFragment();

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GistService.sendKill(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        ContactHelper contactHelper = new ContactHelper();
        contactHelper.setAllContactPref(this, ContactHelper.getAllContacts());
    }

    @Override
    protected void onStart() {
        super.onStart();
        GistService.sendStartup(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContactHelper helper = new ContactHelper();
        helper.updateContacts(this);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        toolbar = binding.activityMainToolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = binding.viewpager;
        setupViewPager(viewPager);

        tabLayout = binding.activityMainTabs;
        tabLayout.setupWithViewPager(viewPager);

        //setting listener to tabs
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                favsFragment = new MyFavoritesFragment();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Check that the activity is using the layout version with
        // the fragment_main FrameLayout
        if (binding.fragmentMain != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            DeafultMostSearchedFragment mostSearchedFragment = new DeafultMostSearchedFragment(); ///99

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //mostSearchedFragment.setArguments(getIntent().getExtras());

            Log.d(LOG_TAG, "before starting transaction");

            // Add the fragment to the 'fragment_container' FrameLayout
            //Optional tag name for the fragment, to later retrieve the fragment with
            //FragmentManager.findFragmentByTag(String)
            //addToBackStack -  transaction will be remembered after it is committed,
            // and will reverse its operation when later popped off the stack
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_main, mostSearchedFragment, "MostSearchedFragmentTag")
                    .addToBackStack(null).commit();

            Log.d(LOG_TAG, "after starting transaction");
        }
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_help:
                // User chose the "help" action, open tutorial
                // or something...
                return true;

            case R.id.action_my_profile:
                //User chose the "my profile" item, open my_profile activity
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mostSearchedFragment, "Most Searched");
        adapter.addFragment(favsFragment, "My Favs");
        adapter.addFragment(allContactsFragment, "All Contacts");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
