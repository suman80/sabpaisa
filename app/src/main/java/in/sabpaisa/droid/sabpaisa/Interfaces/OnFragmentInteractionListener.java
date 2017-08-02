package in.sabpaisa.droid.sabpaisa.Interfaces;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Model.ContactList;

/**
 * Created by abc on 19-06-2017.
 */

/*public interface OnFragmentInteractionListener {
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists);
}**/


public interface OnFragmentInteractionListener {
    public void onFragmentSetFeeds(ArrayList<FeedData> feedData);
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists);
    public void onFragmentSetGroups(ArrayList<GroupListData> groupData);
}
