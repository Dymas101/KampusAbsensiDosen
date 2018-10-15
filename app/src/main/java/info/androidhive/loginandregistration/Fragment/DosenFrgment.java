package info.androidhive.loginandregistration.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import info.androidhive.loginandregistration.TabActivity;
import info.androidhive.loginandregistration.TabUserActivity;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.adapter.Adapter;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.data.Data;
import info.androidhive.loginandregistration.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;

import static info.androidhive.loginandregistration.R.id.textView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DosenFrgment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //Toolbar toolbar;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList;// = new ArrayList<Data>();
    Adapter adapter;
    //int success;
    //AlertDialog.Builder dialog;
    //LayoutInflater inflater;
    //View dialogView;
    //EditText txt_id, txt_name, txt_status;
    //String id, name, status;

    //private SQLiteHandler db;

    // Session Manager Class
    //SessionManager session;

    private static final String TAG = DosenFrgment.class.getSimpleName();

    private static String url_select = Server.URL_2 + "select.php";
    //private static String url_masuk = Server.URL_2 + "masuk.php";
    //private static String url_tmasuk = Server.URL_2 + "masukt.php";

    public static final String TAG_ID_DOSEN = "id_dosen";
    public static final String TAG_NAME = "name";
    public static final String TAG_STATUS = "status";
    public static final String TAG_JAM = "jam";

    //String tag_json_obj = "json_obj_req";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.matkul_list, container, false);
        //toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Session class instance
        //session = new SessionManager(getActivity().getApplicationContext());

        // SqLite database handler
        //db = new SQLiteHandler(getActivity().getApplicationContext());

        // menghubungkan variablel pada layout dan pada java
        swipe = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        list = (ListView) v.findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        itemList = new ArrayList<>();
        adapter = new Adapter(getActivity(), itemList);
        list.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           //itemList.clear();
                           //adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        return v;
    }

    @Override
    public void onRefresh() {
        //itemList.clear();
        //adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setId_dosen(obj.getString(TAG_ID_DOSEN));
                        item.setName(obj.getString(TAG_NAME));
                        item.setAlamat(obj.getString(TAG_STATUS));
                        item.setJam(obj.getString(TAG_JAM));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                Toast.makeText(getActivity(), "Swipe down to recieve data", Toast.LENGTH_LONG).show();
            }
        });

        jArr.setShouldCache(false);
        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

}
