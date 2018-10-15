package info.androidhive.loginandregistration.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.MySingleton;
import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.TabUserActivity;
import info.androidhive.loginandregistration.util.Server;

/**
 * Created by Dymas Gemilang on 3/7/2018.
 */

public class SecondActivity extends AppCompatActivity {

    private static String url_token = Server.URL_2 + "fcm_token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url_token,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
                        finish();
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {

         @Override
            protected Map<String, String> getParams() throws AuthFailureError {
             Map<String, String> params = new HashMap<String, String>();
             params.put("fcm_token", token);

             return super.getParams();
         }
        };

        stringRequest.setShouldCache(false);
        MySingleton.getmInstance(SecondActivity.this).addToRequestque(stringRequest);
    }
}
