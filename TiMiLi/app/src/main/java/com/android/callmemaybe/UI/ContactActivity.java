package com.android.callmemaybe.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);
        Toolbar myContactToolbar = (Toolbar) findViewById(R.id.contact_detail_toolbar);
        setSupportActionBar(myContactToolbar);

        if (savedInstanceState == null) {
            // Create the contact detail fragment and add it to the activity
            // using a fragment transaction.
            ContactActivityFragment contactActivityFragment = new ContactActivityFragment();

            Bundle arguments = new Bundle();
            //needs to find out what to put in bundle

            ContactActivityFragment fragment = new ContactActivityFragment();

            /**
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.., fragment)
                    .commit();
             **/
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_detail_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                // we need to find the contact,
                // change the isFavorite field and update the symbol and the tables
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
