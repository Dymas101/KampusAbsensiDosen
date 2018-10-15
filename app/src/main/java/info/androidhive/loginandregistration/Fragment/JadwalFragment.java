package info.androidhive.loginandregistration.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import info.androidhive.loginandregistration.activity.DetailActivity;
import info.androidhive.loginandregistration.activity.EditJadwalActivity;
import info.androidhive.loginandregistration.activity.LoginAppActivity;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.adapter.AdapterMatkul;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.data.DataMatkul;
import info.androidhive.loginandregistration.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.androidhive.loginandregistration.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    Button btMatul;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataMatkul> itemList;
    AdapterMatkul adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id, txt_catatan;
    String id, catatan, nid;
    SharedPreferences sharedpreferences;

    private SQLiteHandler db;
    private SessionManager session;

    private static final String TAG = JadwalFragment.class.getSimpleName();

    private static String url_select = Server.URL_2 + "select_jadwal_";
    private static String url_select_jadwal = Server.URL_JadwalDosen;
    private static String url_status_masuk = Server.URL_2 + "masuk_jadwal.php";
    private static String url_status_nmasuk = Server.URL_2 + "masuk_jadwal_t.php";
    private static String url_edit 	 = Server.URL_2 + "edit_catatan.php";
    private static String url_insert 	 = Server.URL_2 + "insert_catatan.php";
    private static String url_update 	 = Server.URL_2 + "update_catatan.php";

    public static final String TAG_ID = "id";
    public static final String TAG_NAMA_MATKUL = "nama_matkul";
    public static final String TAG_SEMESTER = "semester";
    public static final String TAG_ID_DOSEN = "id_dosen";
    public static final String TAG_RUANG = "ruang";
    public static final String TAG_JAM = "jam";
    public static final String TAG_STATUS = "status";
    public static final String TAG_CATATAN = "catatan";
    public static final String TAG_NAME = "name";
    public static final String TAG_NID = "nid";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";


    public JadwalFragment() {
        // Required empty public constructor
    }


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
/*
        //listview click ke edit activity
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String id_jadwal = itemList.get(position).getId();

                Intent intent = new Intent(getActivity(), EditJadwalActivity.class);
                intent.putExtra("Terklik", id_jadwal);
                startActivity(intent);
            }
        });
*/
        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx = itemList.get(position).getId();

                final CharSequence[] dialogitem = {"masuk", "tidak masuk", "buat catatan"};
                dialog = new AlertDialog.Builder(JadwalFragment.this.getContext());
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                masuk(idx);
                                break;
                            case 1:
                                nmasuk(idx);
                                break;
                            case 2:
                                edit(idx);
                                break;
                        }
                    }
                }).show();

                return false;
            }
        });

        return v;
    }

    @Override
    public void onRefresh() {
        //itemList.clear();
        //adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk mengosongi edittext pada form
    private void kosong(){
        txt_id.setText(null);
        txt_catatan.setText(null);
    }

    // untuk menampilkan dialog from kontak
    private void DialogForm(String id_matkulx, String catatanx, String button) {
        dialog = new AlertDialog.Builder(JadwalFragment.this.getContext());
        inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_catatan, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_kabar);
        dialog.setTitle("Buat Catatan");

        txt_id      = (EditText) dialogView.findViewById(R.id.txt_id);
        txt_catatan = (EditText) dialogView.findViewById(R.id.txt_catatan);

        if (!id_matkulx.isEmpty()){
            txt_id.setText(id_matkulx);
            txt_catatan.setText(catatanx);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id             = txt_id.getText().toString();
                catatan        = txt_catatan.getText().toString();

                simpan_update();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // SqLite database handler
        //db = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        //session = new SessionManager(getActivity().getApplicationContext());

        // Fetching user details from SQLite
        //HashMap<String, String> user = db.getUserDetails();

        //String nid = user.get("nid");

        SharedPreferences nid = getContext().getSharedPreferences(LoginAppActivity.my_shared_preferences, Context.MODE_PRIVATE);

        String nomor = nid.getString(TAG_NID, null);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select_jadwal + "nid=" + nomor + "&hari=" + dayOfTheWeek, new Response.Listener<JSONArray>() {
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

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        url = url_update;

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Update", jObj.toString());

                        callVolley();
                        kosong();

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("catatan", catatan);

                return params;
            }

        };
        strReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk mengubah alamat menjadi masuk
    private void masuk(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_status_masuk, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("masuk", jObj.toString());

                        callVolley();

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };
        strReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk mengubah alamat menjadi tidak masuk
    private void nmasuk(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_status_nmasuk, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("masuk", jObj.toString());

                        callVolley();

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        strReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get edit data catatan
    private void edit(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String id_matkulx      = jObj.getString(TAG_ID);
                        String catatanx    = jObj.getString(TAG_CATATAN);

                        DialogForm(id_matkulx, catatanx, "UPDATE");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };
        strReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
