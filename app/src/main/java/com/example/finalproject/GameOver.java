package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class GameOver extends AppCompatActivity {

    private final String m_defaultPlayer = "Anonymous";
    private EditText m_player;
    private String m_gameScore;

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
        m_gameScore = extras.getString("qScore");
        TextView gameScore = findViewById(R.id.gameOverScore);
        gameScore.setText(m_gameScore);
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
        SharedPreferences preferences = getSharedPreferences("highScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        ArrayList<String> scoresAsString = getHighScoreList(preferences);
        String player = m_player.getText().toString();
        if (player.isEmpty()) player = m_defaultPlayer;
        String playerScore = player + " : " + m_gameScore;

        if (!scoresAsString.isEmpty()) {
            scoresAsString.add(playerScore);
            scoresAsString.sort((s1, s2) -> {
                String[] l1 = s1.split(":");
                String[] l2 = s2.split(":");
                int i1 = Integer.parseInt(l1[1].trim());
                int i2 = Integer.parseInt(l2[1].trim());
                return Integer.compare(i2, i1);
            });
            if (scoresAsString.size() > 10) scoresAsString.remove(10);
        } else {
            scoresAsString.add(playerScore);
        }
        String result = String.join(";", scoresAsString);
        editor.putString("scores", result);
        editor.apply();
    }

    private ArrayList<String> getHighScoreList(SharedPreferences preferences) {
        String highScores = preferences.getString("scores", "");
        ArrayList<String> result = new ArrayList<>();
        if (!highScores.isEmpty()) {
            result = new ArrayList<>(Arrays.asList(highScores.split(";")));
        }
        return result;
    }
}