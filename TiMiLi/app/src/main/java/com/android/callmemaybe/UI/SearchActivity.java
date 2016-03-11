package com.android.callmemaybe.UI;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactAdapter;
import com.android.callmemaybe.UI.data.ContactFilter;
import com.android.callmemaybe.UI.data.ContactFilterType;
import com.android.callmemaybe.UI.data.ContactSort;
import com.android.callmemaybe.UI.data.ContactSortOrderType;
import com.android.callmemaybe.UI.databinding.SearchActivityBinding;
import com.android.callmemaybe.helpers.ButtonAction;
import com.android.callmemaybe.helpers.ContactHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ana on 04/03/2016.
 */
public class SearchActivity extends AppCompatActivity {
    private Toolbar searchBar;
    private ListView cListView;

    private String lastWordSearches;

    private Contact[] filteredContacts;
    private String[] contactsDescriptions;
    private ArrayAdapter<String> adapter;

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_activity_add_contact_btn:
                ButtonAction.addContactToPhone(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.search_activity);
        final Context context = getApplicationContext();

        final String getsearchWordFromIntent = getIntent().getStringExtra("SEARCH_STRING");
        Log.d("Intent Search: ", getsearchWordFromIntent);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //contact list
        cListView = binding.searchList;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        cListView.setAdapter(adapter);
        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = filteredContacts[position];
                Contact selected = filteredContacts[position];
                if (lastWordSearches == null){
                    lastWordSearches = "-1";
                }
                ContactActivity.StartContactActivity(SearchActivity.this, selected.getPhoneNumber()
                        , "SearchActivity", lastWordSearches);
            }
        });

        this.filteredContacts = ContactFilter.filterContacts(ContactFilterType.allValidContacts,
                SearchActivity.this);
        updateFilteredContacts();

        //edit text
        final EditText searchEditText = binding.searchEditText;
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String newWord;
                if (getsearchWordFromIntent != null && !getsearchWordFromIntent.equals("-1")){
                    newWord = getsearchWordFromIntent;
                    searchEditText.setText(newWord);
                }
                else {
                    newWord = searchEditText.getText().toString();
                }
                Log.d("Intent Search: ", "new word = " + newWord);
                lastWordSearches = newWord;
                updateFilteredContacts(newWord);
                return false;
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    private void updateFilteredContacts() {
        filteredContacts = ContactSort.sortContacts(ContactSortOrderType.mostSearchedToLeastSearched,
                filteredContacts);
        contactsDescriptions = new String[filteredContacts.length];
        for (int i = 0; i < filteredContacts.length; i++) {
            contactsDescriptions[i] = filteredContacts[i].getUserName() + " (" + filteredContacts[i].getPhoneNumber() + ")";
        }
        adapter.clear();
        adapter.addAll(contactsDescriptions);
        adapter.notifyDataSetChanged();
    }

    private void updateFilteredContacts(String word){
        filteredContacts = ContactFilter.filterContacts(word, SearchActivity.this);
        updateFilteredContacts();
    }
}
