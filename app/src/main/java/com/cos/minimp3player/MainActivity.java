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
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 전역변수 선언
    private ListView lvMP3;
    private Button btnPlay, btnStop;
    private TextView tvMP3;
    private ProgressBar pbMP3;

    private ArrayList<String> mp3List;
    private String selectedMP3, fileName, extName;
    private String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/";
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Mini MP3 Player");

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        // 리스트뷰에 출력할 ArrayList<String> 형 변수를 생성함
        mp3List = new ArrayList<String>();

        File[] listFiles = new File(mp3Path).listFiles();
        // listFiles 에 들어 있는 파일 또는 폴더를 하나씩 file 변수에 넣고 for 문을 실행함
        for (File file : listFiles) {
            // file 변수에서 파일 이름과 확장명을 추출함
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            // 확장명이 .mp3라면 준비한 mp3List에 추가함
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
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        tvMP3 = findViewById(R.id.tvMP3);
        pbMP3 = findViewById(R.id.pbMP3);
    }

    public void initData() {
        // 리스트뷰에 mp3List 배열의 내용을 출력함
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);
        lvMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvMP3.setAdapter(adapter);
        lvMP3.setItemChecked(0, true);
    }

    public void initLr(){
        // MP3 파일 목록이 출력된 리스트뷰의 각 항목을 클릭할 때마다 파일 이름이 selectedMP3 변수에 저장됨
        lvMP3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMP3 = mp3List.get(i);
            }
        });
        selectedMP3 = mp3List.get(0);

        // 듣기를 클릭했을 때 동작하는 부분
        btnPlay.setOnClickListener(v -> {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(mp3Path + selectedMP3);
                mediaPlayer.prepare();
                mediaPlayer.start();
                btnPlay.setClickable(false);
                btnStop.setClickable(true);
                tvMP3.setText("실행중인 음악 : " + selectedMP3);
                pbMP3.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 중지를 클릭했을 때 동작하는 부분
        btnStop.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.reset();
            btnPlay.setClickable(true);
            btnStop.setClickable(false);
            tvMP3.setText("실행중인 음악 : ");
            pbMP3.setVisibility(View.INVISIBLE);
        });

        // MediaPlayer 가 시작되지 않은 상태에서 <중지>를 클릭했을 때 발생하는 오류를 방지하기 위함
        btnStop.setClickable(false);
    }
}