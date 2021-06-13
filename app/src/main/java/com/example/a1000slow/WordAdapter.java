package com.example.a1000slow;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter{
    private ArrayList<Word> wordList = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView enWord;
        public TextView plWord;

        public MyViewHolder(View pItem) {
            super(pItem);
            enWord = (TextView) pItem.findViewById(R.id.english_word);
            plWord = (TextView) pItem.findViewById(R.id.polish_word);
        }
    }

    public WordAdapter(ArrayList<Word> wordList, RecyclerView mRecyclerView){
        this.wordList = wordList;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.word_list_item, viewGroup, false);
        Log.e("Final int onCreate", String.valueOf(i));

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        Word word = wordList.get(i);
        Log.e("Final int onBind", String.valueOf(i));
        ((MyViewHolder) viewHolder).enWord.setText(word.getEnWord());
        ((MyViewHolder) viewHolder).plWord.setText(word.getPlWord());
        AudioTrack audioTrack = AudioTrack.getInstance();
        Log.e("onBind ", String.valueOf(audioTrack.getCurrentWords()));
        if(i == audioTrack.getCurrentWords()){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#488E8B8B"));
        }else{
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioTrack audioTrack = AudioTrack.getInstance();
                audioTrack.setTrack(i*2);
                try {
                    ((MainActivity) v.getContext()).play();
                }catch (Exception e){
                    Log.e("Filename", e.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }
}
