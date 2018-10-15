package info.androidhive.loginandregistration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.TabActivityBerita;
import info.androidhive.loginandregistration.TabUserActivity;

/**
 * Created by Dymas Gemilang on 10/17/2017.
 */

public class MenuBeritaFragment extends Fragment{

    Intent intent;
    ListView listview;
    String e[] = {
            "Absen Dosen", "Login"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_main, container, false);

        listview = (ListView) v.findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1, e
        );

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    intent = new Intent(getActivity(), FirstActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(getActivity(), LoginAppActivity.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }
}
