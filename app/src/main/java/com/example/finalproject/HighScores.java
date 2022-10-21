package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class HighScores extends AppCompatActivity {

    ArrayAdapter<String> adapter;

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

    @Override
    protected void onStart() {
        super.onStart();
        ListView listView = (ListView)findViewById(R.id.highScoreList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getHighScoreList());
        listView.setAdapter(adapter);
    }

    private ArrayList<String> getHighScoreList() {
        SharedPreferences preferences = getSharedPreferences("highScores", MODE_PRIVATE);
        String highScores = preferences.getString("scores", "");
        ArrayList<String> result = new ArrayList<>();
        if (!highScores.isEmpty()) {
            result = new ArrayList<>(Arrays.asList(highScores.split(";")));
        }
        return result;
    }
}