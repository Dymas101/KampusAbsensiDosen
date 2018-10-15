package info.androidhive.loginandregistration.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class DetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    FeedImageView news_image;
    SwipeRefreshLayout swipe;
    TextView news_heading, news_description, news_date;
    String id_news;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private static String url_select = Server.URL_Detail;

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String TAG_NID = "nid";
    public static final String TAG_NEWS_HEADING = "news_heading";
    public static final String TAG_NEWS_DESCRIPTION = "news_description";
    public static final String TAG_NEWS_IMAGE = "news_image";
    public static final String TAG_DATE = "news_date";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_row_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("Detail News");

        news_heading = (TextView) findViewById(R.id.heading);
        news_description = (TextView) findViewById(R.id.description);
        news_image = (FeedImageView) findViewById(R.id.thumbnail);
        news_date = (TextView) findViewById(R.id.date);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_1);

        id_news = getIntent().getStringExtra("Clicked");
        //Toast.makeText(DetailActivity.this, id_news, Toast.LENGTH_SHORT).show();

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           callDetailNews(id_news);
                       }
                   }
        );

    }

    @Override
    public void onRefresh() {
        callDetailNews(id_news);
    }

    private void callDetailNews(final String nid) {
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

                    String Heading      = obj.getString(TAG_NEWS_HEADING);
                    String Description  = obj.getString(TAG_NEWS_DESCRIPTION);
                    String Image        = obj.getString(TAG_NEWS_IMAGE);
                    String Date         = obj.getString(TAG_DATE);

                    news_heading.setText(Heading);
                    news_description.setText(Description);
                    news_date.setText(Date);

                    if (obj.getString(TAG_NEWS_IMAGE) != "") {
                        news_image.setImageUrl(Image, imageLoader);
                    }

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
                params.put("nid", nid);

                return params;
            }
        };

        // menambah request ke request queue
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

}
