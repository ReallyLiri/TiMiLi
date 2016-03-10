package com.android.callmemaybe.UI;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactAdapter;
import com.android.callmemaybe.UI.data.ContactFilter;
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
    private MenuItem mSearchAction;
    private ListView cListView;
    private boolean isSearchOpened = false;
    private EditText edtSearch;
    private Set<Contact> filteredSet;
    private StringBuilder word;
    private String SEARCH_STRING = "SEARCH_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchActivityBinding binding = DataBindingUtil.setContentView(
                this, R.layout.search_activity);
        final Context context = getApplicationContext();
        searchBar = binding.searchActivityToolbar;
        setSupportActionBar(searchBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addContact = binding.searchActivityAddContactBtn;
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonAction.addContactToPhone(context);
            }
        });

        cListView = binding.searchList;
        filteredSet = ContactHelper.getAllContacts();
        String word = getIntent().getStringExtra(SEARCH_STRING);
        Log.d("SearchString", "word = " + word);
        if (word == "-1") {
            defaultSearchFragment();
        } else {
            edtSearch.setText(word);
            doSearch();
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.search:
                handleMenuSearch();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(false); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

            //add the search icon in the action bar
            //mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSearch = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });


            edtSearch.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            //mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));

            isSearchOpened = true;
        }
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    private void doSearch() {
        String searching = edtSearch.getText().toString();
        if(searching.length() < word.length()) { //the user deleted a letter
            filteredSet = ContactHelper.getAllContacts();
        }

        Contact[] filtered = ContactFilter.filterContacts(searching,
                filteredSet.toArray(new Contact[0]));
        filtered = ContactSort.sortContacts(ContactSortOrderType.name_A_To_Z, filtered);
        filteredSet = new HashSet<>(Arrays.asList(filtered));
        word = new StringBuilder(searching);

        ContactAdapter contactAdapter = new ContactAdapter(getApplicationContext(), filtered,
                word.toString());
        cListView.setAdapter(contactAdapter);
    }

    public void defaultSearchFragment() {
        Contact[] list = ContactSort.sortContacts(ContactSortOrderType.mostSearchedToLeastSearched,
                filteredSet);
        ContactAdapter contactAdapter = new ContactAdapter(getApplicationContext(), list, "-1");
        cListView.setAdapter(contactAdapter);
    }

}
