package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Arrays;

public class HighScores extends AppCompatActivity {

    private SharedPreferences m_preferences;
    private static final int m_normalMode = 0;
    private static final int m_speedMode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        m_preferences = getSharedPreferences("highScores", MODE_PRIVATE);

        Button back = (Button)findViewById(R.id.highScoreBack);
        back.setOnClickListener(this::backOnClick);
        setTabHost();
    }

    private void backOnClick(View view) {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupScoreList(m_normalMode);
        setupScoreList(m_speedMode);
    }

    private void setupScoreList(int mode) {
        int id = 0;
        switch (mode) {
            case m_normalMode:
                id = R.id.highScoreNormalList;
                break;
            case m_speedMode:
                id = R.id.highScoreSpeedList;
                break;
        }
        ListView listView = (ListView)findViewById(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getHighScoreList(mode));
        listView.setAdapter(adapter);
    }

    private ArrayList<String> getHighScoreList(int mode) {
        String highScores = m_preferences.getString("scores_" + mode, "");
        ArrayList<String> result = new ArrayList<>();
        if (!highScores.isEmpty()) {
            result = new ArrayList<>(Arrays.asList(highScores.split(";")));
        }
        return result;
    }

    private void setTabHost() {
        TabHost tabhost = findViewById(R.id.tabHost);
        tabhost.setup();
        TabHost.TabSpec spec = tabhost.newTabSpec("Normal");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Normal");
        tabhost.addTab(spec);
        spec = tabhost.newTabSpec("Speed");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Speed");
        tabhost.addTab(spec);
    }
}