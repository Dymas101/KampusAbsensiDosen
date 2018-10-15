package info.androidhive.loginandregistration.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.data.DataBerita;
import info.androidhive.loginandregistration.helper.FeedImageView;

/**
 * Created by Kuncoro on 26/03/2016.
 */
public class AdapterDetail extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataBerita> items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AdapterDetail(Activity activity, List<DataBerita> items) {
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
            convertView = inflater.inflate(R.layout.list_row_berita, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView gambar = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        DataBerita data = items.get(position);

        TextView news_heading = (TextView) convertView.findViewById(R.id.heading);
        TextView news_description = (TextView) convertView.findViewById(R.id.description);
        TextView news_date = (TextView) convertView.findViewById(R.id.date);

        news_heading.setText(data.getNews_heading());
        news_description.setText(data.getNews_description());
        news_date.setText(data.getNews_date());
        gambar.setImageUrl(data.getNews_image(), imageLoader);

        return convertView;
    }

}