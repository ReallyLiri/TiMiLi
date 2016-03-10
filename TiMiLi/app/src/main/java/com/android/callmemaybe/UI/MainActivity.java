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
import android.view.View;
import android.widget.Button;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.databinding.ActivityMainBinding;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.IOnLatestGistUpdatedListener;
import com.android.callmemaybe.contracts.IOnLatestStatusUpdatedListener;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.gistService.GistService;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.helpers.PhoneNumberHelper;
import com.android.callmemaybe.helpers.SharedPreferencesHelper;

import com.android.callmemaybe.server.FireBaseCloudServer;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private Button searchText;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment mostSearchedFragment  = new DeafultMostSearchedFragment();
    private Fragment favsFragment = new MyFavoritesFragment();
    private Fragment allContactsFragment = new AllContactsFragment();

    private ICloudServer mCloudServer;
    private ContactHelper mContactHelper;

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // GistService.sendKill(this);
        mContactHelper.setAllContactPref(this, ContactHelper.getAllContacts());

        mCloudServer.UnRegisterAll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GistService.sendStartup(this);

        mCloudServer = new FireBaseCloudServer(this);
        for (final Contact contact: ContactHelper.getAllContacts()) {
            mCloudServer.RegisterForUserGistData(contact.getPhoneNumber(), new IOnLatestGistUpdatedListener() {
                @Override
                public void latestGistUpdated(UserGist latestGist) {
                    contact.setContactGist(latestGist);
                }
            });
            mCloudServer.RegisterForUserStatusData(contact.getPhoneNumber(), new IOnLatestStatusUpdatedListener() {
                @Override
                public void latestStatusUpdated(UserStatus latestStatus) {
                    contact.setContactStatus(latestStatus);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContactHelper = new ContactHelper();
        mContactHelper.updateContacts(this);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        toolbar = binding.activityMainToolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchText = binding.activityMainSearchText;
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSearchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(goToSearchActivity);
            }
        });

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

            //Add the fragment to the 'fragment_container' FrameLayout
            //Optional tag name for the fragment, to later retrieve the fragment with
            //FragmentManager.findFragmentByTag(String)
            //addToBackStack -  transaction will be remembered after it is committed,
            //and will reverse its operation when later popped off the stack
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_main, mostSearchedFragment, "MostSearchedFragmentTag")
                    .addToBackStack(null).commit();

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
            case R.id.action_my_profile:
                Intent goToMyProfile = new Intent(getApplicationContext(), MyProfile_Activity.class);
                startActivity(goToMyProfile);
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
