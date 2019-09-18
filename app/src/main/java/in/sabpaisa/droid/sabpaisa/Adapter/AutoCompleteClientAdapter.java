package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Model.FilterClientModel;
import in.sabpaisa.droid.sabpaisa.R;

public class AutoCompleteClientAdapter extends ArrayAdapter<FilterClientModel> {

    private List<FilterClientModel> clientListFull;

    Context context;

    public static String state,clientId,clientLogo,clientImage,clientName,stateId;

    public AutoCompleteClientAdapter(@NonNull Context context, @NonNull List<FilterClientModel> clientList) {
        super(context, 0 ,clientList);
        clientListFull = new ArrayList<>(clientList);
        this.context = context;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return clientFilter;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.auto_complete_text_view_custom , parent,false);
        }

        TextView clientAutoCompleteTextView_Text = convertView.findViewById(R.id.clientAutoCompleteTextView_Text);
        ImageView clientAutoCompleteTextView_Image = convertView.findViewById(R.id.clientAutoCompleteTextView_Image);

        FilterClientModel filterClientModel = getItem(position);

        if (filterClientModel != null){
            clientAutoCompleteTextView_Text.setText(filterClientModel.getOrganization_name());

            Glide.with(context)
                    .load(filterClientModel.getOrgLogo())
                    .error(R.drawable.image_not_found)
                    .into(clientAutoCompleteTextView_Image);

            /*Log.d("ACCA","state_"+filterClientModel.getOrgAddress());
            Log.d("ACCA","ID_"+filterClientModel.getOrganizationId());
            Log.d("ACCA","logo_"+filterClientModel.getOrgLogo());
            Log.d("ACCA","image_"+filterClientModel.getOrgWal());
            Log.d("ACCA","name_"+filterClientModel.getOrganization_name());
            Log.d("ACCA","stateId_"+filterClientModel.getStateId());

            state = filterClientModel.getOrgAddress();
            clientId = filterClientModel.getOrganizationId();
            clientLogo = filterClientModel.getOrgLogo();
            clientImage = filterClientModel.getOrgWal();
            clientName = filterClientModel.getOrganization_name();
            stateId = filterClientModel.getStateId();
*/

        }

        return convertView;
    }

    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<FilterClientModel> suggestions = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                suggestions.addAll(clientListFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (FilterClientModel item: clientListFull) {
                    if (item.getOrganization_name().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);

                        //Log.d("TESTTTTT :" ,item.getOrganization_name() +" "+item.getStateId() +" "+item.getOrgAddress() );
                    }
                }

            }

            results.values = suggestions;

            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            clear();
            addAll((List)filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((FilterClientModel)resultValue).getOrganization_name();
        }
    };


}
