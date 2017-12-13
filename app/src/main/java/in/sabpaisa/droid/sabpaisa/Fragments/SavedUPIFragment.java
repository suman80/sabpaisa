package in.sabpaisa.droid.sabpaisa.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.SavedUPIAdapter;
import in.sabpaisa.droid.sabpaisa.Model.SavedUPI;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 16-06-2017.
 */

public class SavedUPIFragment extends Fragment {
    View rootView;
    RecyclerView recyclerViewInstitutions;
    SavedUPIAdapter adapter;
    ArrayList<SavedUPI> savedUPIs;

    public SavedUPIFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_saved_upi, container, false);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_saved_upi);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);
        savedUPIs = new ArrayList<>();
        loadArrayList();
        adapter = new SavedUPIAdapter(getContext(),savedUPIs);
        recyclerViewInstitutions.setAdapter(adapter);
        return rootView;
    }

    private void loadArrayList() {
        /*savedUPIs.add(new SavedUPI("gaurav@upi"));
        savedUPIs.add(new SavedUPI("manish@upi"));
        savedUPIs.add(new SavedUPI("9818849948@upi"));
        savedUPIs.add(new SavedUPI("mukesh@icici"));
        savedUPIs.add(new SavedUPI("harpreet@upi"));
        savedUPIs.add(new SavedUPI("neeraj@upi"));
        savedUPIs.add(new SavedUPI("mukesh@upi"));
        savedUPIs.add(new SavedUPI("9458763183@icici"));
        savedUPIs.add(new SavedUPI("faltu@upi"));
        savedUPIs.add(new SavedUPI("adarsh@upi"));
        savedUPIs.add(new SavedUPI("vinayak@upi"));
        savedUPIs.add(new SavedUPI("vishal@icici"));*/

        savedUPIs.add(new SavedUPI("aditya@dcb"));

    }
}
