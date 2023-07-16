package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.*;
import java.io.*;

public class playsong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play,previous,next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
int position;
Thread updateSeek;
SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);

        textView=findViewById(R.id.textView);
        seekBar=findViewById(R.id.seekBar);
        play=findViewById(R.id.play);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);

        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songList");
        textContent=intent.getStringExtra("currentSong");
        textView.setText(textContent);
        position=intent.getIntExtra("position",0);
        Uri uri= Uri.parse(songs.get(position).toString());
      mediaPlayer=MediaPlayer.create(this,uri);
      mediaPlayer.start();

      seekBar.setMax(mediaPlayer.getDuration());


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek=new Thread(){
            public void run(){
              int currentposition = 0;
              try {
                  while (currentposition<mediaPlayer.getDuration()){
                      currentposition=mediaPlayer.getCurrentPosition();
                      seekBar.setProgress(currentposition);
                      sleep(800);
                  }
              }catch (Exception e){
                  e.printStackTrace();
              }
            }
        };
        updateSeek.start();


        play.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        previous.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position !=0){
                    position=position - 1 ;
                }else {
                    position=songs.size() - 1 ;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);

            }
        });

        next.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position !=songs.size() - 1){
                    position=position + 1 ;
                }else {
                    position=0 ;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);

            }
        });

    }
}