package info.androidhive.loginandregistration;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.android.volley.Request;

/**
 * Created by Dymas Gemilang on 3/7/2018.
 */

public class MySingleton {

    private static MySingleton mInstance;
    private static Context mcontext;
    RequestQueue requestQueue;

    private MySingleton(Context context){

        mcontext = context;

    }

    private RequestQueue getRequestQueue(){

        if(requestQueue==null){

            requestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());

        }
        return requestQueue;

    }

    public static synchronized MySingleton getmInstance(Context context){

        if (mInstance==null){

            mInstance = new MySingleton(context);

        }
        return mInstance;

    }

    public<T> void addToRequestque(Request<T> request){

        getRequestQueue().add(request);

    }


}
