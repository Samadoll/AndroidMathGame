package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

class NodeData {
    public String val;
    public NodeData next;
    public NodeData prev;

    public NodeData(String value, NodeData prev) {
        val = value;
        this.prev = prev;
    }
}

public class Game extends AppCompatActivity {

    private static final String m_operators = "+-*";
    private static final int m_operatorLength = 3;
    private static final int m_maxNumberOfMultiply = 0;
    private static final int m_defaultCountDown = 10;
    private static final int m_countDownIncrement = 2;
    private int m_correctAnswer;
    private int m_currentScore;
    private int m_currentLevel;
    private int m_currentLives;
    private int m_currentMaxCountDown;
    private Random m_rand;
    private boolean m_countDownMode = false;
    private CountDownTimer m_timer;

    private TextView m_question;
    private TextView m_score;
    private TextView m_level;
    private TextView m_lives;
    private TextView m_gameTimer;
    private Button m_answerA;
    private Button m_answerB;
    private Button m_answerC;

    private Queue<NodeData> m_xQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        m_currentLives = 3;
        m_currentScore = 0;
        m_currentLevel = 1;
        m_currentMaxCountDown = m_defaultCountDown;

        m_question = (TextView)findViewById(R.id.question);
        m_score = (TextView)findViewById(R.id.gameScore);
        m_level = (TextView)findViewById(R.id.gameLevel);
        m_lives = (TextView)findViewById(R.id.gameLives);
        m_gameTimer = (TextView)findViewById(R.id.gameTimer);
        m_answerA = (Button)findViewById(R.id.answerA);
        m_answerB = (Button)findViewById(R.id.answerB);
        m_answerC = (Button)findViewById(R.id.answerC);
        Button m_back = (Button) findViewById(R.id.gameMenu);
        m_answerA.setOnClickListener(this::answerOnClick);
        m_answerB.setOnClickListener(this::answerOnClick);
        m_answerC.setOnClickListener(this::answerOnClick);
        m_back.setOnClickListener(this::backOnClick);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            m_countDownMode = extras.getBoolean("gameMode", false);
        }
        if (m_countDownMode) {
            m_timer = getCountDown(m_defaultCountDown);
        } else {
            findViewById(R.id.gameTimerText).setVisibility(View.INVISIBLE);
            m_gameTimer.setVisibility(View.INVISIBLE);
        }

        setQuestion();
    }

    private CountDownTimer getCountDown(long countDown) {
        return new CountDownTimer(countDown * 1000, 1) {
            @Override
            public void onTick(long time) {
                m_gameTimer.setText(Double.toString(time / 1000.0));
            }
            @Override
            public void onFinish() {
                m_currentLives--;
                if (m_currentLives == 0) endGame();
                m_lives.setText(Integer.toString(m_currentLives));
                setQuestion();
            }
        };
    }


    private void setQuestion() {
        if (m_countDownMode) m_timer.cancel();
        int range = 100;
        NodeData node = null;
        NodeData head = null;
        m_xQueue = new LinkedList<>();

        m_rand = new Random(getRandomSeed());

        StringBuilder equation = new StringBuilder();
        int numberOfMultiple = 0;

        for (int i = 0; i < m_currentLevel; i++) {
            int digit = getRandomNumber(range);
            char operator = getOperator(numberOfMultiple);
            equation.append(digit);
            equation.append(" ");
            equation.append(operator);
            equation.append(" ");
            if (node == null) {
                node = new NodeData(Integer.toString(digit), null);
                head = node;
            } else {
                node.next = new NodeData(Integer.toString(digit), node);
                node = node.next;
            }
            node.next = new NodeData(Character.toString(operator), node);
            node = node.next;
            if (operator == '*') {
                m_xQueue.add(node);
                numberOfMultiple++;
            }
        }
        int digit = getRandomNumber(range);
        equation.append(digit);
        if (node != null) {
            node.next = new NodeData(Integer.toString(digit), node);
        }

        rebuildQueue();
        assert head != null;
        m_correctAnswer = getCorrectAnswer(head);
        setViews(equation.toString());

        if (m_countDownMode) {
            m_currentMaxCountDown = m_defaultCountDown + m_countDownIncrement * (m_currentLevel - 1);
            m_timer = getCountDown(m_currentMaxCountDown);
            m_timer.start();
        }
    }

    private int getRandomNumber(int range) {
        return m_rand.nextInt(range);
    }

    private char getOperator(int numberOfMultiply) {
        int ind = m_rand.nextInt(numberOfMultiply < m_maxNumberOfMultiply && m_currentLevel >= 5 ? m_operatorLength : m_operatorLength - 1);
        return m_operators.charAt(ind);
    }

    private int getCorrectAnswer(NodeData node) {
        assert node != null;
        int val = Integer.parseInt(node.val);
        if (node.next == null) return val;
        String operator = node.next.val;
        NodeData next = node.next.next;
        int nextVal = Integer.parseInt(next.val);
        val = operator.equals("+") ? val + nextVal : val - nextVal;
        next.val = Integer.toString(val);
        return getCorrectAnswer(next);
    }

    private void rebuildQueue() {
        while (m_xQueue.size() > 0) {
            NodeData node = m_xQueue.poll();
            assert node != null;
            NodeData prev = node.prev;
            NodeData next = node.next;
            prev.val = Integer.toString(Integer.parseInt(prev.val) * Integer.parseInt(next.val));
            prev.next = next.next;
            if(next.next != null) next.next.prev = prev;
        }
    }

    private void setViews(String question) {
        m_question.setText(question);
        List<Integer> answers = new ArrayList<>();
        List<Integer> wrongAnswers = new ArrayList<>();
        answers.add(m_correctAnswer);
        for (int i = -5; i <= 5 && i != 0; i++) {
            wrongAnswers.add(m_correctAnswer + i);
        }
        Random rand = new Random(getRandomSeed());
        Collections.shuffle(wrongAnswers, rand);
        answers.add(wrongAnswers.get(0));
        answers.add(wrongAnswers.get(1));
        Collections.shuffle(answers, rand);
        m_answerA.setText(answers.get(0).toString());
        m_answerB.setText(answers.get(1).toString());
        m_answerC.setText(answers.get(2).toString());
    }

    private void backOnClick(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void answerOnClick(View view) {
        int inputAnswer = 0;
        switch (view.getId()) {
            case R.id.answerA:
                inputAnswer = Integer.parseInt(m_answerA.getText().toString().trim());
                break;
            case R.id.answerB:
                inputAnswer = Integer.parseInt(m_answerB.getText().toString().trim());
                break;
            case R.id.answerC:
                inputAnswer = Integer.parseInt(m_answerC.getText().toString().trim());
                break;
        }
        updateScore(inputAnswer);
        updateLevel();
        if (m_currentLives == 0) endGame();
        else setQuestion();
    }

    private void updateScore(int answer) {
        if (isAnswerCorrect(answer)) {
            m_currentScore++;
        } else {
            m_currentLives--;
            m_lives.setText(Integer.toString(m_currentLives));
        }
        m_score.setText(Integer.toString(m_currentScore));
    }

    private void updateLevel() {
        m_currentLevel = m_currentScore / 5 + 1;
        m_level.setText(Integer.toString(m_currentLevel));
    }

    private void endGame() {
        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("qScore", Integer.toString(m_currentScore));
        finish();
        startActivity(intent);
    }

    private boolean isAnswerCorrect(int answer) {
        return answer == m_correctAnswer;
    }

    private int getRandomSeed() {
        LocalDateTime time = LocalDateTime.now();
        return m_currentLevel + m_currentScore + time.getSecond() * time.getMinute();
    }
}