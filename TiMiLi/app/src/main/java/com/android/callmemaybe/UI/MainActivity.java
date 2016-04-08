package com.android.callmemaybe.UI;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactFilter;
import com.android.callmemaybe.UI.data.ContactFilterType;
import com.android.callmemaybe.UI.databinding.ActivityMainBinding;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.IOnLatestGistUpdatedListener;
import com.android.callmemaybe.contracts.IOnLatestStatusUpdatedListener;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.gistService.GistService;
import com.android.callmemaybe.helpers.ContactHelper;

import com.android.callmemaybe.helpers.SharedPreferencesHelper;
import com.android.callmemaybe.notificationService.NotificationService;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private ImageButton searchText;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static boolean showOnlyActiveItemCheck;

    private Fragment mostSearchedFragment  = new DeafultMostSearchedFragment();
    private Fragment favsFragment = new MyFavoritesFragment();
    private Fragment allContactsFragment = new AllContactsFragment();

    private ICloudServer mCloudServer;
    private ContactHelper mContactHelper;

    private final String SHOW_ONLY_ACTIVE = "SHOW_ONLY_ACTIVE";

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // GistService.sendKill(this);
        Log.d("OnStop", "we have got: " + showOnlyActiveItemCheck);
        mContactHelper.setAllContactPref(this, ContactHelper.getAllContacts());
        SharedPreferencesHelper helper = new SharedPreferencesHelper();
        helper.putShowOnlyActive(MainActivity.this, SHOW_ONLY_ACTIVE, showOnlyActiveItemCheck);
        mCloudServer.UnRegisterAll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GistService.sendStartup(this);
        NotificationService.NotifyOfTrackedListChanged(this, ContactHelper.getMyContact(this).getContactStatus().trackedUsers);

        mCloudServer = new FireBaseCloudServer(this);
        for (final Contact contact: ContactHelper.getAllContacts()) {
            mCloudServer.RegisterForUserGistData(contact.getPhoneNumber(), new IOnLatestGistUpdatedListener() {
                @Override
                public void latestGistUpdated(UserGist latestGist) {
                    Log.e("FireBase.GIST", "Got gist for '" + latestGist.userId + "'");
                    contact.setContactGist(latestGist);
                    if (showOnlyActiveItemCheck) {
                        MainActivity.refreshAllData();
                    }
                }
            });
            mCloudServer.RegisterForUserStatusData(contact.getPhoneNumber(), new IOnLatestStatusUpdatedListener() {
                @Override
                public void latestStatusUpdated(UserStatus latestStatus) {
                    Log.e("FireBase.STATUS", "Got status for '" + latestStatus.phoneNum + "'");
                    contact.setContactStatus(latestStatus);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesHelper helper = new SharedPreferencesHelper();
        showOnlyActiveItemCheck = helper.getShowOnlyActive(MainActivity.this, SHOW_ONLY_ACTIVE);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchText = binding.activityMainSearchText;
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSearchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                goToSearchActivity.putExtra("SEARCH_STRING", "-1");
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

        if (savedInstanceState == null) {
            DeafultMostSearchedFragment mostSearchedFragment = new DeafultMostSearchedFragment(); ///99

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_main, mostSearchedFragment, "MostSearchedFragmentTag")
                    .addToBackStack(null).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        final MenuItem showOnlyActive = menu.findItem(R.id.action_show_only_active);
        showOnlyActive.setChecked(showOnlyActiveItemCheck);
        showOnlyActive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!showOnlyActiveItemCheck);
                showOnlyActiveItemCheck = !showOnlyActiveItemCheck;
                MainActivity.refreshAllData();
                return false;
            }
        });

        final MenuItem surpriseMe = menu.findItem(R.id.surprise_me);
        surpriseMe.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int rand;
                Context context = getApplicationContext();
                Contact contact;
                Contact[] aval = ContactFilter.filterContacts(ContactFilterType.available, context);
                Contact[] favsAndAv = ContactFilter.filterContacts(ContactFilterType.favorites, context, aval);
                if (favsAndAv.length == 0) {
                    if (aval.length == 0) {
                        Toast toast = new Toast(context);
                        toast.setText("All your friends are busy");
                        toast.setDuration(toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    } else {
                        rand = new Random().nextInt(aval.length);
                        contact = aval[rand];
                    }
                } else {
                    rand = new Random().nextInt(favsAndAv.length);
                    contact = favsAndAv[rand];
                }
                ContactActivity.StartContactActivity(context, contact.getPhoneNumber(), "MainActivity");

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_my_profile:
                Intent goToMyProfile = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(goToMyProfile);
                return true;

            case android.R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mostSearchedFragment, getString(R.string.most_searched_tab));
        adapter.addFragment(favsFragment, getString(R.string.fav_tab));
        adapter.addFragment(allContactsFragment, getString(R.string.all_contacts_tab));
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
            if (fragment instanceof TabsFragment) {
                fragments.add((TabsFragment) fragment);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private static List<TabsFragment> fragments = new ArrayList<>();
    public static void refreshAllData() {
        for (TabsFragment fragment : fragments) {
            fragment.RefreshData();
        }
    }
}
