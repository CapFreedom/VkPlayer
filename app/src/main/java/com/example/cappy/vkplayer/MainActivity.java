package com.example.cappy.vkplayer;

import android.app.Application;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.vk.sdk.VKScope;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VkAudioArray;
import com.vk.sdk.util.VKUtil;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    ListView listView;
    List<VKApiAudio> audioList = new ArrayList<>();
    MediaPlayer mediaPlayer;

    VKParameters requestParams = new VKParameters(VKUtil.mapFrom("count", "100"));

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.VIDEO,
            VKScope.AUDIO,
            VKScope.NOHTTPS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button_audio);

        // находим список
        ListView lvMain = (ListView) findViewById(R.id.main_list);

        // создаем адаптер
        final ArrayAdapter<VKApiAudio> adapter = new InteractiveArrayAdapter(this, audioList);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                VKRequest currentRequest = VKApi.audio().get(requestParams);
                currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Log.d("VkDemoApp", "onComplete " + response);
                        VkAudioArray audioArray = (VkAudioArray) response.parsedModel;
                        audioList.clear();
                        audioList.addAll(audioArray);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Log.d("VkDemoApp","error: " +  error.errorMessage);
                    }
                });

            }
        });
    }
}
