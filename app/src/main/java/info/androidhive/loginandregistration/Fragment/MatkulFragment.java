package info.androidhive.loginandregistration.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import info.androidhive.loginandregistration.adapter.AdapterMatkul;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.data.DataMatkul;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import info.androidhive.loginandregistration.R;

/**
 * Created by Dymas Gemilang on 8/28/2017.
 */

public class MatkulFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataMatkul> itemList;
    AdapterMatkul adapter;

    private static final String TAG = MatkulFragment.class.getSimpleName();

    private static String url_select 	 = Server.URL_2 + "select_matkul.php?";
    private static String url_select_jadwal = Server.URL_JadwalDosen;

    public static final String TAG_ID              = "id";
    public static final String TAG_NAMA_MATKUL     = "nama_matkul";
    public static final String TAG_SEMESTER        = "semester";
    public static final String TAG_ID_DOSEN        = "id_dosen";
    public static final String TAG_RUANG           = "ruang";
    public static final String TAG_JAM             = "jam";
    public static final String TAG_STATUS          = "status";
    public static final String TAG_NAME            = "name";
    public static final String TAG_CATATAN         = "catatan";

    String tag_json_obj = "json_obj_req";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.matkul_list, container, false);
        //toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // menghubungkan variablel pada layout dan pada java
        swipe = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        list = (ListView) v.findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        itemList = new ArrayList<>();
        adapter = new AdapterMatkul(getActivity(), itemList);
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
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select + "hari=" + dayOfTheWeek, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataMatkul item = new DataMatkul();

                        item.setId(obj.getString(TAG_ID));
                        item.setNama_matkul(obj.getString(TAG_NAMA_MATKUL));
                        item.setSemester(obj.getString(TAG_SEMESTER));
                        item.setName(obj.getString(TAG_NAME));
                        item.setRuang(obj.getString(TAG_RUANG));
                        item.setJam(obj.getString(TAG_JAM));
                        item.setStatus(obj.getString(TAG_STATUS));
                        item.setCatatan(obj.getString(TAG_CATATAN));

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

        // menambah request ke request queue
        jArr.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jArr);
    }
}
