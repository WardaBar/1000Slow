package com.example.a1000slow;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ImageButton btnPlay;
    RecyclerView recyclerView;
    Handler handler;
    SharedPreferences sharedpreferences;
    ArrayList<Word> words;
    public static final String myPreference = "mypref";
    public static final String delay = "delayKey";
    public static final String chooseWords = "chooseWordsKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mediaPlayer = new MediaPlayer();
        btnPlay = (ImageButton) findViewById(R.id.playPauseButton);

        sharedpreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        setRecyclerView();

        AudioTrack audioTrack = AudioTrack.getInstance();
        audioTrack.setTrack(0);
        if(sharedpreferences.contains(delay)){
            audioTrack.setDelay(sharedpreferences.getInt(delay, 1000));
        }else{
            audioTrack.setDelay(1000);
        }

        handler = new Handler();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                handler.postDelayed(runnablePlay, AudioTrack.getInstance().getDelay());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void playStopButton(View view) {
        if(mediaPlayer.isPlaying() || handler.hasCallbacks(runnablePlay)){
            handler.removeCallbacks(runnablePlay);
            mediaPlayer.stop();
            btnPlay.setImageResource(R.drawable.ic_media_play);
        }else{
            AudioTrack audioTrack = AudioTrack.getInstance();
            int track = audioTrack.getCurrentWords() * 2;
            audioTrack.setTrack(track);
            play();
            btnPlay.setImageResource(R.drawable.ic_media_stop);
        }
    }

    public void nextButton(View view) {
        AudioTrack audioTrack = AudioTrack.getInstance();
        audioTrack.nextTrack();
        changeColor();
        play();
    }

    public void previousButton(View view) {
        AudioTrack audioTrack = AudioTrack.getInstance();
        audioTrack.previousTrack();
        changeColor();
        play();
    }

    public void showCurrent(View view) {
        AudioTrack audioTrack = AudioTrack.getInstance();
        recyclerView.scrollToPosition(audioTrack.getCurrentWords());
    }

    public void goToSettings(View view){
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        mediaPlayer.stop();
        mediaPlayer.release();
        handler.removeCallbacks(runnablePlay);
        AudioTrack.getInstance().removeFilenames();
        AudioTrack.getInstance().setTrack(0);
        finish();
    }

    public void play(){
        AudioTrack audioTrack = AudioTrack.getInstance();

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        try { mediaPlayer.setDataSource(getApplicationContext(),
                Uri.parse(audioTrack.getFilename())); }
        catch (Exception e) {
            Log.e("Filename", e.toString());
        }
        try {
            mediaPlayer.prepare();
        } catch (Exception e) {
            Log.e("Filename", e.toString());
        }
        mediaPlayer.start();
        btnPlay.setImageResource(R.drawable.ic_media_stop);
        changeColor();
    }

    private void changeColor(){
        AudioTrack audioTrack = AudioTrack.getInstance();
        WordAdapter adapter = (WordAdapter) recyclerView.getAdapter();
        adapter.notifyItemChanged(audioTrack.getPreviousWords());
        adapter.notifyItemChanged(audioTrack.getCurrentWords());
    }

    private void setRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.wordList);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AudioTrack audioTrack = AudioTrack.getInstance();

        words = new ArrayList<>();

        int chosenWords = 0;
        if(sharedpreferences.contains(chooseWords)){
            chosenWords = sharedpreferences.getInt(chooseWords, 0);
        }
        Log.e("chosenWords", String.valueOf(chosenWords));

        Resources res = getResources();

        String[] engWord;
        String[] engFilenames;
        String[] plWord;
        String[] plFilenames;

        if(chosenWords == 0){
            engWord = res.getStringArray(R.array.translator_english_words);
            engFilenames = res.getStringArray(R.array.translator_english_filenames);
            plWord = res.getStringArray(R.array.translator_polish_words);
            plFilenames = res.getStringArray(R.array.translator_polish_filenames);
        }
        else {
            engWord = res.getStringArray(R.array.nietranslator_english_words);
            engFilenames = res.getStringArray(R.array.nietranslator_english_filenames);
            plWord = res.getStringArray(R.array.nietranslator_polish_words);
            plFilenames = res.getStringArray(R.array.nietranslator_polish_filenames);
        }

        int i=0;
        for(String s: engWord){
            words.add(new Word(s, engFilenames[i], plWord[i], plFilenames[i]));
            i++;
        }

        recyclerView.setAdapter(new WordAdapter(words, recyclerView));

        for (Word word: words){
            String filename = "android.resource://" + this.getPackageName() + "/raw/" + word.getEnWordVoiceFileName();
            Log.e("Filename", filename);
            audioTrack.addFilenames(filename);
            filename = "android.resource://" + this.getPackageName() + "/raw/" + word.getPlWordVoiceFileName();
            audioTrack.addFilenames(filename);
        }
    }

    public void randomize(View view){
        AudioTrack audioTrack = AudioTrack.getInstance();
        audioTrack.removeFilenames();
        audioTrack.setTrack(0);

        Random random = new Random();
        int size = words.size();

        for(int i = 0; i < size; i++){
            int r = random.nextInt(size);
            Collections.swap(words, i, r);
        }

        recyclerView.setAdapter(new WordAdapter(words, recyclerView));

        for (Word word: words){
            String filename = "android.resource://" + this.getPackageName() + "/raw/" + word.getEnWordVoiceFileName();
            Log.e("Filename", filename);
            audioTrack.addFilenames(filename);
            filename = "android.resource://" + this.getPackageName() + "/raw/" + word.getPlWordVoiceFileName();
            audioTrack.addFilenames(filename);
        }

    }

    Runnable runnablePlay = new Runnable() {
        @Override
        public void run() {
            play();
        }
    };

}