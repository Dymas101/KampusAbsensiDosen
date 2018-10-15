package info.androidhive.loginandregistration.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.FeedImageView;
import info.androidhive.loginandregistration.util.Server;

/**
 * Created by Dymas Gemilang on 4/2/2018.
 */

public class EditJadwalActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipe;
    TextView id, nama_matkul, semester, id_dosen, ruang, jam, status, catatan;
    String id_jadwal;
    Button bmasuk, tmasuk, ucatatan;
    int success;

    private static String url_select = Server.URL_D_Jadwal;

    private static String url_status_masuk = Server.URL_2 + "masuk_jadwal.php";

    private static final String TAG = EditJadwalActivity.class.getSimpleName();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_row_matkul_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("Detail News");

        nama_matkul = (TextView) findViewById(R.id.heading);
        semester = (TextView) findViewById(R.id.semester);
        id_dosen = (TextView) findViewById(R.id.id);
        ruang = (TextView) findViewById(R.id.ruang);
        jam = (TextView) findViewById(R.id.jam);
        status = (TextView) findViewById(R.id.masuk);
        catatan = (TextView) findViewById(R.id.catatan);

        bmasuk = (Button) findViewById(R.id.b_masuk);
        tmasuk = (Button) findViewById(R.id.t_masuk);
        ucatatan = (Button) findViewById(R.id.u_catatan);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_2);

        id_jadwal = getIntent().getStringExtra("Terklik");
        //Toast.makeText(EditJadwalActivity.this, id_jadwal, Toast.LENGTH_SHORT).show();

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           callDetailNews(id_jadwal);
                       }
                   }
        );

        bmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masuk(id_jadwal);
            }
        });

    }

    @Override
    public void onRefresh() {
        callDetailNews(id_jadwal);
    }

    private void callDetailNews(final String id) {
        swipe.setRefreshing(true);
        //String id_news = getIntent().getStringExtra("Clicked");
        //Toast.makeText(DetailActivity.this, id_news, Toast.LENGTH_SHORT).show();

        // membuat request JSON
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe.setRefreshing(false);

                try {
                    JSONObject obj = new JSONObject(response);

                    String Matkul = obj.getString(TAG_NAMA_MATKUL);
                    String Semester = obj.getString(TAG_SEMESTER);
                    String Dosen = obj.getString(TAG_ID_DOSEN);
                    String Ruang = obj.getString(TAG_RUANG);
                    String Jam = obj.getString(TAG_JAM);
                    String Status = obj.getString(TAG_STATUS);
                    String Catatan = obj.getString(TAG_CATATAN);

                    nama_matkul.setText(Matkul);
                    semester.setText(Semester);
                    id_dosen.setText(Dosen);
                    ruang.setText(Ruang);
                    jam.setText(Jam);
                    status.setText(Status);
                    catatan.setText(Catatan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }
        };

        // menambah request ke request queue
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }


    // fungsi untuk mengubah alamat menjadi masuk
    private void masuk(final String id) {
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

                        callDetailNews(id_jadwal);

                        Toast.makeText(EditJadwalActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(EditJadwalActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(EditJadwalActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

    }
}
