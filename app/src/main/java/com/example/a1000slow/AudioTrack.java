package com.example.a1000slow;

import android.media.MediaPlayer;

import java.util.ArrayList;

public class AudioTrack {
    private static AudioTrack instance;
    private int track;
    private int currentWords;
    private int previousWords;
    private int delay;
    ArrayList<String> filenames;

    private AudioTrack() {
        this.filenames = new ArrayList<>();
        this.track = 0;
        this.currentWords = 0;
        this.previousWords = 0;
        this.delay = 0;
    }

    public static AudioTrack getInstance() {
        if (instance == null) {
            instance = new AudioTrack();
        }
        return instance;
    }

    public void addFilenames(String filename){
        this.filenames.add(filename);
    }

    private void checkRange(){
        if(this.track >= this.filenames.size())
            this.track = 0;
        if(this.track<0)
            this.track = this.filenames.size() - 2;
    }

    public String getFilename(){
        String filename = this.filenames.get(track);
        this.setCurrentWordsAndPreviousWords();
        this.track++;
        this.checkRange();
        return filename;
    }

    public void setTrack(int track){
        this.track = track;
    }

    private void setCurrentWordsAndPreviousWords(){
        this.previousWords = this.currentWords;
        this.currentWords = this.track/2;
    }

    public void nextTrack(){
        if(this.track%2!=0) {
            this.track = this.track + 1;
        }
        this.checkRange();
    }

    public void previousTrack(){
        if(this.track%2==0)
            this.track = this.track - 4;
        else
            this.track = this.track - 3;
        this.checkRange();

    }

    public int getCurrentWords() {
        return this.currentWords;
    }

    public int getPreviousWords() {
        return this.previousWords;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void removeFilenames(){
        this.filenames.removeAll(this.filenames);
    }
}
