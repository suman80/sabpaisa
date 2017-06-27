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

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 14-06-2017.
 */

public class InstitutionFragment extends Fragment {

    View rootView;
    RecyclerView recyclerViewInstitutions;
    InstitutionAdapter institutionAdapter;
    ArrayList<Institution> institutions;

    public InstitutionFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_institutions, container, false);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);
        institutions = new ArrayList<>();
        Institution institution = new Institution("COA", "New Delhi");
        institutions.add(institution);
        institutions.add(new Institution("COA", "New Delhi"));
        institutions.add(new Institution("COA", "New Delhi"));
        institutions.add(new Institution("COA", "New Delhi"));
        institutions.add(new Institution("COA", "New Delhi"));
        institutionAdapter = new InstitutionAdapter(institutions);
        recyclerViewInstitutions.setAdapter(institutionAdapter);
        return rootView;
    }
}
