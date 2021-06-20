package com.example.mediaappmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.ims.ImsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView textViewTitle, textViewCurrent, textViewTotal;
    ImageButton imageButtonPlay, imageButtonStop, imageButtonNext, imageButtonPrevious, imageButtonRepeat;
    SeekBar seekBarSong;
    ImageView imageViewDisc;

    ArrayList<Song> songs;

    int position = 0;

    MediaPlayer mediaPlayer;

    Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();
        AddSongs();

        // load animation
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        // Khoi tao
        InitMediaPlayer();

        // Bat su kien tren Button
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imageButtonPlay.setImageResource(R.drawable.play);
                }else{
                    mediaPlayer.start();
                    imageButtonPlay.setImageResource(R.drawable.pause);
                }
                SetTimeTotal();
                UpdateTimeSong();
                imageViewDisc.startAnimation(animation);
            }
        });

        imageButtonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isLooping()){
                    mediaPlayer.setLooping(false);
                    imageButtonRepeat.setImageResource(R.drawable.repeat);
                    Toast.makeText(MainActivity.this, "Stop looping", Toast.LENGTH_SHORT).show();
                }else{
                    mediaPlayer.setLooping(true);
                    imageButtonRepeat.setImageResource(R.drawable.repeat2);
                    Toast.makeText(MainActivity.this, "Looping", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                imageButtonPlay.setImageResource(R.drawable.play);

                // Khoi tao lai bai hat
                InitMediaPlayer();
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position ++;
                if(position == songs.size()) position = 0;
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                InitMediaPlayer();
                mediaPlayer.start();
                imageButtonPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
                imageViewDisc.startAnimation(animation);
            }
        });

        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position --;
                if(position < 0) position = songs.size() - 1;
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                InitMediaPlayer();
                mediaPlayer.start();
                imageButtonPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
                imageViewDisc.startAnimation(animation);
            }
        });

        seekBarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


    }

    private void UpdateTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

                // update time
                textViewCurrent.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));

                // update seekbar
                seekBarSong.setProgress(mediaPlayer.getCurrentPosition());

                // kiem tra thoi gian bai hat
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position ++;
                        if(position == songs.size()) position = 0;
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        InitMediaPlayer();
                        mediaPlayer.start();
                        imageButtonPlay.setImageResource(R.drawable.pause);
                        SetTimeTotal();
                        UpdateTimeSong();
                    }
                });

                handler.postDelayed(this, 1000);
            }
        }, 100);
    }

    private void SetTimeTotal(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        textViewTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBarSong.setMax(mediaPlayer.getDuration());
    }

    private void InitMediaPlayer() {
        textViewTitle.setText(songs.get(position).getName());
        mediaPlayer = MediaPlayer.create(MainActivity.this, songs.get(position).getFile());
    }

    private void AddSongs() {
        songs = new ArrayList<>();
        songs.add(new Song("Chandelier", R.raw.chandelier));
        songs.add(new Song("Labyrinth", R.raw.labyrith));
        songs.add(new Song("Numb", R.raw.numb));
        songs.add(new Song("On the floor", R.raw.onthefloor));
        songs.add(new Song("Redezvous", R.raw.redezvous));
        songs.add(new Song("Save me", R.raw.saveme));
        songs.add(new Song("Starboy", R.raw.starboy));
        songs.add(new Song("Unstoppable", R.raw.unstoppable));
    }

    private void Mapping(){
        textViewCurrent     = (TextView) findViewById(R.id.textViewTimeCurrent);
        textViewTotal       = (TextView) findViewById(R.id.textViewTimeTotal);
        textViewTitle       = (TextView) findViewById(R.id.textViewTitle);

        seekBarSong         = (SeekBar) findViewById(R.id.seekBarSong);

        imageViewDisc       =  (ImageView) findViewById(R.id.imageViewCD);

        imageButtonNext     = (ImageButton) findViewById(R.id.buttonNext);
        imageButtonPlay     = (ImageButton) findViewById(R.id.buttonPlay);
        imageButtonPrevious = (ImageButton) findViewById(R.id.buttonPrevious);
        imageButtonStop     = (ImageButton) findViewById(R.id.buttonStop);
        imageButtonRepeat   = (ImageButton) findViewById(R.id.buttonRepeat);
    }
}