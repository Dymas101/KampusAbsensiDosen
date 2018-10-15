package info.androidhive.loginandregistration.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.TabActivity;
import info.androidhive.loginandregistration.TabActivityBerita;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

/**
 * Created by Dymas Gemilang on 10/17/2017.
 */

public class MenuLoginFragment extends Fragment{

    Intent intent;
    ListView listview;
    String e[] = {
            "Logout", "About Us"
    };

    //private SQLiteHandler db;
    //private SessionManager session;

    public final static String TAG_NID = "nid";
    public final static String TAG_ID_DOSEN = "id_dosen";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id_dosen, nid;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_main, container, false);

        listview = (ListView) v.findViewById(R.id.list);

        sharedpreferences = this.getActivity().getSharedPreferences(LoginAppActivity.my_shared_preferences, Context.MODE_PRIVATE);

        // SqLite database handler
        //db = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        //session = new SessionManager(getActivity().getApplicationContext());

    //    // Check if user is already logged in or not
    //    if (!session.isLoggedIn()) {
    //        // User is already logged in. Take him to main activity
    //        Intent intent = new Intent(getActivity(), TabActivity.class);
    //        startActivity(intent);
    //        getActivity().finish();
    //    }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1, e
        );

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    logoutUser();
                } else if (position == 1){
                    intent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    private void logoutUser() {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LoginAppActivity.session_status, false);
        editor.putString(TAG_ID_DOSEN, null);
        editor.putString(TAG_NID, null);
        editor.commit();

        intent = new Intent(this.getActivity(), TabActivity.class);
        startActivity(intent);
    }
}
