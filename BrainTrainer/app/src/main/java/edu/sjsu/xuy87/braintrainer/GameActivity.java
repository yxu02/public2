package edu.sjsu.xuy87.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    List<Integer> arr = Arrays.asList(new Integer[10]);
    private int corrRow;
    private TextView answer_TV;
    int correctNum = 0;
    int total = 0;
    private TextView score;
    private TextView question;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView timer;
    Random rand = new Random(17);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        question = findViewById(R.id.questionTV);
        timer = findViewById(R.id.timerTV);
        score = findViewById(R.id.scoreTV);
        answer_TV = findViewById(R.id.answerTV);
        button0 = findViewById(R.id.gridButton0);
        button1 = findViewById(R.id.gridButton1);
        button2 = findViewById(R.id.gridButton2);
        button3 = findViewById(R.id.gridButton3);
        answer_TV.setText("Go on!");

        resetQuestion();

    }

    private void resetQuestion() {

        int a = rand.nextInt(11);
        int b = rand.nextInt(11);

        question.setText(String.valueOf(a) + " + " + String.valueOf(b));
        int answer = a+b;

        corrRow = rand.nextInt(4);

        for(int i = 0; i < 4; i++){
            if (i != corrRow){
                int wrongAnswer = rand.nextInt(21);
                while(wrongAnswer == answer){
                    wrongAnswer = rand.nextInt(21);
                }
                arr.set(i, wrongAnswer);
            } else{
                arr.set(i, answer);
            }
        }
        button0.setText(String.valueOf(arr.get(0)));
        button1.setText(String.valueOf(arr.get(1)));
        button2.setText(String.valueOf(arr.get(2)));
        button3.setText(String.valueOf(arr.get(3)));
    }

    public void calculate(View view) {
        Log.i("Tag: ", String.valueOf(view.getTag()));

        if (String.valueOf(corrRow).equals(view.getTag().toString())) {
            answer_TV.setText("Correct! Next...");
            correctNum++;
        }
        else {
            answer_TV.setText("Wrong! Next...");
        }
        total++;
        score.setText(String.valueOf(correctNum) + " / " + String.valueOf(total));
        resetQuestion();
    }

    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText(String.valueOf(millisUntilFinished/1000) +"s");
        }

        @Override
        public void onFinish() {
            double finalscore = (double) correctNum/total;
            if (finalscore >= 0.8)
                answer_TV.setText("Great job!");
            else if (finalscore >= 0.6)
                answer_TV.setText("Pass!");
            else
                answer_TV.setText("Failed!");

            button0.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);

        }
    }.start();
}
