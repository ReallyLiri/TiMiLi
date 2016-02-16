package com.android.callmemaybe.UI.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 16/02/2016.
 */
public class ContactSort {

    public static Comparator<Contact> COMPARE_BY_NAME_A_TO_Z = new Comparator<Contact>() {
        public int compare(Contact one, Contact other) {
            return one.getUserName().compareTo(other.getUserName());
        }
    };

    public static Comparator<Contact> COMPARE_BY_NAME_Z_TO_A = new Comparator<Contact>() {
        public int compare(Contact one, Contact other) {
            return other.getUserName().compareTo(one.getUserName());
        }
    };

    public static Comparator<Contact> COMPARE_BY_MOST_TO_LEAST_SEARCHED = new Comparator<Contact>() {
        public int compare(Contact one, Contact other) {
            return Integer.compare(one.getSearchesCounter(), other.getSearchesCounter());
        }
    };

    public static Contact[] sortContacts(ContactSortOrderType sortType, Set<Contact> contacts){
        List<Contact> contacts1 = new ArrayList<>(contacts);
        switch (sortType){
            case name_A_To_Z:
                Collections.sort(contacts1, COMPARE_BY_NAME_A_TO_Z);
                break;
            case name_Z_To_A:
                Collections.sort(contacts1, COMPARE_BY_NAME_Z_TO_A);
                break;
            case mostSearchedToLeastSearched:
                Collections.sort(contacts1, COMPARE_BY_MOST_TO_LEAST_SEARCHED);
                break;
        }
    return contacts1.toArray(new Contact[0]);
    }
}
