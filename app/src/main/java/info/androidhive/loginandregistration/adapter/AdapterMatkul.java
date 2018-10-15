package info.androidhive.loginandregistration.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.data.DataMatkul;

import java.util.List;

/**
 * Created by Kuncoro on 26/03/2016.
 */
public class AdapterMatkul extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataMatkul> items;

    public AdapterMatkul(Activity activity, List<DataMatkul> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_matkul, null);

        DataMatkul data = items.get(position);

        TextView id = (TextView) convertView.findViewById(R.id.id_matkul);
        TextView nama_matkul = (TextView) convertView.findViewById(R.id.heading);
        TextView semester = (TextView) convertView.findViewById(R.id.semester);
        TextView id_dosen = (TextView) convertView.findViewById(R.id.id);
        TextView ruang = (TextView) convertView.findViewById(R.id.ruang);
        TextView jam = (TextView) convertView.findViewById(R.id.jam);
        TextView status = (TextView) convertView.findViewById(R.id.masuk);
        TextView catatan = (TextView) convertView.findViewById(R.id.catatan);

        id.setText(data.getId());
        nama_matkul.setText(data.getNama_matkul());
        semester.setText(data.getSemester());
        id_dosen.setText(data.getName());
        ruang.setText(data.getRuang());
        jam.setText(data.getJam());
        status.setText(data.getStatus());
        catatan.setText(data.getCatatan());

        return convertView;
    }

}