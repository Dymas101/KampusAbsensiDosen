package info.androidhive.loginandregistration.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.TabActivity;
import info.androidhive.loginandregistration.TabUserActivity;
import info.androidhive.loginandregistration.util.Server;

/**
 * Created by Dymas Gemilang on 1/19/2018.
 */

public class FirstActivity extends AppCompatActivity {

    public final static String TAG_NID = "nid";
    public final static String TAG_ID_DOSEN = "id_dosen";

    ImageView icon;
    Animation fade;
    Animation rotare;

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id_dosen, nid;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_dosen = sharedpreferences.getString(TAG_ID_DOSEN, null);
        nid = sharedpreferences.getString(TAG_NID, null);

        icon = (ImageView) findViewById(R.id.icon);
        fade = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        rotare = AnimationUtils.loadAnimation(this,R.anim.rotare);
        icon.setAnimation(fade);
        icon.setAnimation(rotare);

        //PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //cal.set(Calendar.HOUR_OF_DAY, 07);
        //cal.set(Calendar.MINUTE, 30);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60 * 20, alarmIntent);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                notificationIntent.addCategory("android.intent.category.DEFAULT");

                PendingIntent pendingIntent = PendingIntent.getService(FirstActivity.this, 0, notificationIntent, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 07);
                cal.set(Calendar.MINUTE, 30);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours

                if (session) {
                    FirebaseMessaging.getInstance().subscribeToTopic("Dosen");
                    Intent intent = new Intent(FirstActivity.this, TabUserActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    FirebaseMessaging.getInstance().subscribeToTopic("Mahasiswa");
                    Intent intent = new Intent(FirstActivity.this, TabActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
