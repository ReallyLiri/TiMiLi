package com.android.callmemaybe.UI;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactAdapter;
import com.android.callmemaybe.UI.data.ContactFilter;
import com.android.callmemaybe.UI.data.ContactSort;
import com.android.callmemaybe.UI.data.ContactSortOrderType;
import com.android.callmemaybe.UI.databinding.SearchActivityBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchActivityBinding binding = DataBindingUtil.setContentView(
                this, R.layout.search_activity);
        searchBar = binding.searchActivityToolbar;
        setSupportActionBar(searchBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cListView = binding.searchList;
        filteredSet = ContactHelper.getAllContacts();
        defaultSearchFragment();
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
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

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

        ContactAdapter contactAdapter = new ContactAdapter(getApplicationContext(), filtered);
        cListView.setAdapter(contactAdapter);
    }

    public void defaultSearchFragment() {
        Contact[] list = ContactSort.sortContacts(ContactSortOrderType.mostSearchedToLeastSearched,
                filteredSet);
        ContactAdapter contactAdapter = new ContactAdapter(getApplicationContext(), list);
        cListView.setAdapter(contactAdapter);
    }

}