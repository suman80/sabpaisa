package in.sabpaisa.droid.sabpaisa;

import com.android.volley.VolleyError;

public interface SabpaisaPg {
    void Success(String PGresponse);
    void failure(VolleyError error);

}
