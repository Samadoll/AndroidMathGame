package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button playButton = (Button)findViewById(R.id.mainPagePlay);
        playButton.setOnClickListener(this::playOnClick);
        Button highScoresButton = (Button)findViewById(R.id.mainPageHighScores);
        highScoresButton.setOnClickListener(this::highScoresOnClick);
        Button quitButton = (Button)findViewById(R.id.mainPageQuit);
        quitButton.setOnClickListener(this::quitOnClick);
    }

    private void playOnClick(View view) {
        Intent intent = new Intent(this, Game.class);
        finish();
        startActivity(intent);
    }

    private void highScoresOnClick(View view) {
        Intent intent = new Intent(this, HighScores.class);
        finish();
        startActivity(intent);
    }

    private void quitOnClick(View view) {
        finish();
        System.exit(0);
    }
}