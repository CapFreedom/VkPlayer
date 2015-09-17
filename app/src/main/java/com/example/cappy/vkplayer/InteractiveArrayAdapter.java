package com.example.cappy.vkplayer;

/**
 * Created by Cappy on 18.09.2015.
 */

import com.vk.sdk.api.model.VKApiAudio;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class InteractiveArrayAdapter extends ArrayAdapter<VKApiAudio> {

    private final List<VKApiAudio> list;
    private final Activity context;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    public InteractiveArrayAdapter(Activity context, List<VKApiAudio> list) {
        super(context, R.layout.audio_row, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected ImageButton download;
        protected ImageButton play;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.audio_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.audio_label);
            viewHolder.play = (ImageButton) view.findViewById(R.id.audio_play);
            viewHolder.download = (ImageButton) view.findViewById(R.id.audio_dl);
            viewHolder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(list.get(position).url.replace("https", "http"));
                        mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer player) {
                            player.start();
                        }

                    });
                }
            });
            view.setTag(viewHolder);
            viewHolder.play.setTag(list.get(position));
            viewHolder.download.setTag(list.get(position));
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.text.setText(list.get(position).artist + " - " + list.get(position).title);
            return view;
        } else {
            return convertView;
        }
    }
}
