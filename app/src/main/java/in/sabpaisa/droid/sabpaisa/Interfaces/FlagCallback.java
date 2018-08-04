package in.sabpaisa.droid.sabpaisa.Interfaces;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.GroupListData;

public interface FlagCallback {
    public void onSharedFragmentSetFeeds(ArrayList<String> feedData);
    public void onSharedFragmentSetGroups(ArrayList<String> groupData);
}
