package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HighScores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        Button back = (Button)findViewById(R.id.highScoreBack);
        back.setOnClickListener(this::backOnClick);
    }

    private void backOnClick(View view) {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}