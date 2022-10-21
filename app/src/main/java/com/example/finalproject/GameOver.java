package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private EditText m_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Bundle extras = getIntent().getExtras();

        m_player = (EditText)findViewById(R.id.gameOverEdit);
        Button playAgain = (Button)findViewById(R.id.gameOverPlayAgain);
        Button mainMenu = (Button)findViewById(R.id.gameOverMain);
        playAgain.setOnClickListener(this::playAgain);
        mainMenu.setOnClickListener(this::backToMain);

        if (extras == null) return;
        String qScore = extras.getString("qScore");
        TextView gameScore = findViewById(R.id.gameOverScore);
        gameScore.setText(qScore);
    }

    private void playAgain(View view) {
        saveScore();
        finish();
        startActivity(new Intent(this, Game.class));
    }

    private void backToMain(View view) {
        saveScore();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void saveScore() {

    }
}