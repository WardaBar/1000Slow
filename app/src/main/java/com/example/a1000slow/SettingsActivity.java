package com.example.a1000slow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    EditText delayValue;
    RadioButton wordsTranslatorButton;
    RadioButton wordsNietranslatorButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        sharedpreferences = getSharedPreferences(MainActivity.myPreference, Context.MODE_PRIVATE);

        int delay = 1000;

        delayValue = (EditText) findViewById(R.id.delayValue);
        if(sharedpreferences.contains(MainActivity.delay)){
            delay = sharedpreferences.getInt(MainActivity.delay, 1000);
        }
        delayValue.setText(String.valueOf(delay));

        int chooseWords = 0;

        if(sharedpreferences.contains(MainActivity.chooseWords)){
            chooseWords = sharedpreferences.getInt(MainActivity.chooseWords, 0);
        }

        wordsTranslatorButton = (RadioButton) findViewById(R.id.wordsTranslatorButton);
        wordsNietranslatorButton = (RadioButton) findViewById(R.id.wordsNietranslatorButton);

        if(chooseWords == 0){
            wordsTranslatorButton.setChecked(true);
        }
        else{
            wordsNietranslatorButton.setChecked(true);
        }
    }

    public void cancleSettings(View view){
        AudioTrack.getInstance().setTrack(0);
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();
    }

    public void saveSettings(View view){
        SharedPreferences.Editor editor = sharedpreferences.edit();

        int chosenWords = 0;
        if(wordsTranslatorButton.isChecked()){
            chosenWords = 0;
        }else if(wordsNietranslatorButton.isChecked()){
            chosenWords = 1;
        }

        editor.putInt(MainActivity.chooseWords, chosenWords);

        int delay = 1000;
        delay = Integer.parseInt(delayValue.getText().toString());

        editor.putInt(MainActivity.delay, delay);
        AudioTrack.getInstance().setDelay(delay);
        AudioTrack.getInstance().setTrack(0);

        editor.apply();

        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();
    }
}