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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.activity.DetailActivity;
import info.androidhive.loginandregistration.adapter.AdapterBerita;
import info.androidhive.loginandregistration.adapter.AdapterMatkul;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.data.DataBerita;
import info.androidhive.loginandregistration.data.DataMatkul;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.util.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeritaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    Button btMatul;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataBerita> itemList;
    AdapterBerita adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id;
    String id;

    private SQLiteHandler db;
    private SessionManager session;

    private static final String TAG = BeritaFragment.class.getSimpleName();

    private static String url_select = Server.URL_Berita;

    public static final String TAG_NID = "nid";
    public static final String TAG_NEWS_HEADING = "news_heading";
    public static final String TAG_NEWS_DESCRIPTION = "news_description";
    public static final String TAG_NEWS_IMAGE = "news_image";
    public static final String TAG_DATE = "news_date";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

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
        adapter = new AdapterBerita(getActivity(), itemList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long nid) {
                // TODO Auto-generated method stub
                String nid_news = itemList.get(position).getNid();


                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("Clicked", nid_news);
                startActivity(intent);
            }
        });

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        return v;
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String email = user.get("email");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        // membuat request JSON
        final JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataBerita item = new DataBerita();

                        item.setNid(obj.getString(TAG_NID));
                        item.setNews_heading(obj.getString(TAG_NEWS_HEADING));
                        item.setNews_description(obj.getString(TAG_NEWS_DESCRIPTION));
                        item.setNews_image(obj.getString(TAG_NEWS_IMAGE));
                        item.setNews_date(obj.getString(TAG_DATE));

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
            }

        });

        // menambah request ke request queue
        jArr.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jArr);
    }
}
