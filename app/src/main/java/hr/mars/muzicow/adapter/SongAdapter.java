package hr.mars.muzicow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hr.mars.muzicow.R;
import hr.mars.muzicow.models.Song;

/**
 * Created by Emil on 20.1.2016..
 */
public class SongAdapter extends ArrayAdapter {

    List<Song> songs;
    TextView eventItems;
    View customView;

    public SongAdapter(Context context, List<Song> songs) {
        super(context, R.layout.songs_list_item_design, songs);
        this.songs = songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            customView = layoutInflater.inflate(R.layout.songs_list_item_design, parent, false);

            eventItems = (TextView) customView.findViewById(R.id.songItems);

            eventItems.setText(songs.get(position).getName());
            switch (songs.get(position).getStatus()) {
                case "1":
                    eventItems.setTextColor(Color.parseColor("#009688"));
                    break;
                case "2":
                    eventItems.setTextColor(Color.parseColor("#F44336"));
                    break;
                case "0":
                    eventItems.setTextColor(Color.parseColor("#673AB7"));
                    break;
            }
        } catch (Exception e) {
        }
        return customView;
    }
}
