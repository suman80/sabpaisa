package in.sabpaisa.droid.sabpaisa.Interfaces;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Model.ContactList;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.PersonalSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;

/**
 * Created by abc on 19-06-2017.
 */

/*public interface OnFragmentInteractionListener {
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists);
}**/


public interface OnFragmentInteractionListener {
    public void onFragmentSetFeeds(ArrayList<FeedData> feedData);
    public void onFragmentSetGroups(ArrayList<GroupListData> groupData);
    public void onFragmentSetMembers(ArrayList<Member_GetterSetter> memberData);
    public void onFragmentSetClients(ArrayList<PersonalSpaceModel> clientData);
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists);
    public void onFragmentSetMembersSpace(ArrayList<MemberSpaceModel> memberSpaceData);
}
