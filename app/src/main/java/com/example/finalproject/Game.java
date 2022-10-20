package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private static final int m_maxNumberOfMultiply = 2;
    private int m_correctAnswer;
    private int m_currentScore;
    private int m_currentLevel;
    private int m_lives;
    private Random m_rand;

    private TextView m_question;
    private TextView m_score;
    private Button m_answerA;
    private Button m_answerB;
    private Button m_answerC;

    private Queue<NodeData> m_xQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        m_lives = 3;
        m_currentScore = 0;
        m_currentLevel = 1;

        m_question = (TextView)findViewById(R.id.question);
        m_score = (TextView)findViewById(R.id.gameScore);
        m_answerA = (Button)findViewById(R.id.answerA);
        m_answerB = (Button)findViewById(R.id.answerB);
        m_answerC = (Button)findViewById(R.id.answerC);
        m_answerA.setOnClickListener(this::answerOnClick);
        m_answerB.setOnClickListener(this::answerOnClick);
        m_answerC.setOnClickListener(this::answerOnClick);

        setQuestion();
    }

    private void setQuestion() {
        int range = 100;
        NodeData node = null;
        NodeData head = null;
        m_xQueue = new LinkedList<>();
        m_currentLevel = 1;

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
    }

    private int getRandomNumber(int range) {
        return m_rand.nextInt(range);
    }

    private char getOperator(int numberOfMultiply) {
        return m_operators.charAt(m_rand.nextInt(numberOfMultiply < m_maxNumberOfMultiply ? m_operatorLength : m_operatorLength - 1));
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
        setQuestion();
    }

    private void updateScore(int answer) {
        if (isAnswerCorrect(answer)) {
            m_currentScore++;
        }
        m_score.setText(Integer.toString(m_currentScore));
    }

    private boolean isAnswerCorrect(int answer) {
        return answer == m_correctAnswer;
    }

    private int getRandomSeed() {
        LocalDateTime time = LocalDateTime.now();
        return m_currentLevel + m_currentScore + time.getSecond() * time.getMinute();
    }
}