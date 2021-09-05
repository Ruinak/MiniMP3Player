package com.cos.minimp3player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 전역변수 선언
    private ListView lvMP3;
    private Button btnPlay, btnStop;
    private TextView tvMP3;
    private ProgressBar pbMP3;

    private ArrayList<String> mp3List;
    private String selectedMP3;
    private String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/";
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Mini MP3 Player");

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        mp3List = new ArrayList<String>();

        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;
        for (File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if(extName.equals((String) "mp3")) {
                mp3List.add(fileName);
            }
        }

        init();
        initData();
        initLr();
    }

    public void init(){
        lvMP3 = findViewById(R.id.lvMP3);
    }

    public void initData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);
        lvMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvMP3.setAdapter(adapter);
        lvMP3.setItemChecked(0, true);
    }

    public void initLr(){
        lvMP3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMP3 = mp3List.get(i);
            }
        });
        selectedMP3 = mp3List.get(0);
    }
}