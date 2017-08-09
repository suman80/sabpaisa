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

import in.sabpaisa.droid.sabpaisa.Adapter.AccountIFSCAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SavedUPIAdapter;
import in.sabpaisa.droid.sabpaisa.Model.Bank;
import in.sabpaisa.droid.sabpaisa.Model.SavedUPI;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 16-06-2017.
 */

public class AccountIFSCFragment extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    AccountIFSCAdapter adapter;
    ArrayList<Bank> banks;

    public AccountIFSCFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account_ifsc, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_ac_ifsc);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerView.setLayoutManager(llm);
        banks = new ArrayList<>();
        loadArrayList();
        adapter = new AccountIFSCAdapter(getContext(),banks);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void loadArrayList() {
        banks.add(new Bank("Allahabad Bank",getResources().getDrawable(R.drawable.allahabad_bank_logo_only),"ALLA"));
//        banks.add(new Bank("Canara Bank",getResources().getDrawable(R.drawable.test_bank_logo),"CNRB"));
        banks.add(new Bank("Axis Bank",getResources().getDrawable(R.drawable.axis_bank_logo_only),"UTIB"));
        banks.add(new Bank("Corporation Bank",getResources().getDrawable(R.drawable.corporation_bank_logo_only),"CORP"));
        banks.add(new Bank("DCB Bank",getResources().getDrawable(R.drawable.dcb_bank_logo_only),"DCBL"));
        banks.add(new Bank("HDFC Bank",getResources().getDrawable(R.drawable.hdfc_bank_logo_only),"HDFC"));
        banks.add(new Bank("ICICI Bank",getResources().getDrawable(R.drawable.icici_bank_logo_only),"ICIC"));
    }
}
