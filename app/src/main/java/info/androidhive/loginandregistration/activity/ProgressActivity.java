package info.androidhive.loginandregistration.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.loginandregistration.R;

/**
 * Created by Dymas Gemilang on 3/16/2018.
 */

public class ProgressActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.progress_activity, container, false);

        return v;
    }
}
