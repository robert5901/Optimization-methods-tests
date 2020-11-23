package com.example.diplom;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestActivity extends AppCompatActivity {
    TextView questionText, counter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private RadioGroup radioGroup;
    private RadioButton first, second, third, fourth;
    private Integer tempAnswer = 0, correctAnswers = 0, points = 0, item = 0, time, randNum = 0, count = 1, flag = 0;
    private Button next, exitFromTest, saveAndComplete;
    private TestActivity smb;
    private List<Integer> randNumber = new ArrayList<>();
    private String questionNum;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        flag=0;
        smb = this;
        init();
        time = Variables.tempTest.TestTime * 60000;
        timer(time);

        for (int i = 0; i < Variables.tempTest.questionList.size(); i++) { // 5 вопросов
            randNumber.add(i);
        }
        Collections.shuffle(randNumber);
        randNum = randNumber.get(0);

        questionNum = "1. " + Variables.tempTest.questionList.get(randNum).question;

        questionText.setText(questionNum);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.firstRadioButton:
                        tempAnswer = 1;
                        break;
                    case R.id.secondRadioButton:
                        tempAnswer = 2;
                        break;
                    case R.id.thirdRadioButton:
                        tempAnswer = 3;
                        break;
                    case R.id.fourthRadioButton:
                        tempAnswer = 4;
                        break;
                    default:
                        tempAnswer = 0;
                        break;
                }
            }
        });

        first.setText(Variables.tempTest.questionList.get(randNum).firstVariant);
        second.setText(Variables.tempTest.questionList.get(randNum).secondVariant);
        third.setText(Variables.tempTest.questionList.get(randNum).thirdVariant);
        fourth.setText(Variables.tempTest.questionList.get(randNum).fourthVariant);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Variables.tempTest.questionList.get(randNum).answer) == tempAnswer){
                    correctAnswers++;
                    points = (correctAnswers*100)/Variables.tempTest.questionList.size();
                }

                if (tempAnswer != 0){
                    if ((Variables.tempTest.questionList.size() - 1) == item ){
                        Variables.tempTest.correctAnswers = points;

                        next.setEnabled(false);
                    }else {
                        item++; count++;

                        for (int i = 0; i < randNumber.size(); i++) {
                            if (randNumber.get(i) == randNum){
                                randNumber.remove(i); //удаляем из списка случайных чисел использованое число
                            }
                        }
                        randNum = randNumber.get(0);  //присваеваем след случайное число

                        questionNum = count + ". " + Variables.tempTest.questionList.get(randNum).question;

                        questionText.setText(questionNum);

                        first.setText(Variables.tempTest.questionList.get(randNum).firstVariant);
                        second.setText(Variables.tempTest.questionList.get(randNum).secondVariant);
                        third.setText(Variables.tempTest.questionList.get(randNum).thirdVariant);
                        fourth.setText(Variables.tempTest.questionList.get(randNum).fourthVariant);

                        first.setChecked(false);
                        second.setChecked(false);
                        third.setChecked(false);
                        fourth.setChecked(false);

                        tempAnswer = 0;
                    }
                }else{
                    Toast.makeText(TestActivity.this, "Выберите вариант отвевта!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exitFromTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(smb);
                builder.setTitle("Внимание!")
                        .setMessage("Вы точно хотите выйти из теста?")
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(TestActivity.this, Info.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                // Закрываем окно
                                dialog.cancel();
                            }
                        }).show();
                builder.create();
            }
        });

        saveAndComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(smb);
                builder.setTitle("Внимание!")
                        .setMessage("Вы точно хотите сохранить результаты теста?")
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Закончить тест", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setCorrectAnswers();

                                Intent intent = new Intent(TestActivity.this, ResultsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                finish();
                                // Закрываем окно
                                dialog.cancel();
                            }
                        }).show();
                builder.create();
            }
        });
    }

    private void setCorrectAnswers(){
        Map<String, Object> test = new HashMap<>();
        test.put("CorrectAnswers", Variables.tempTest.correctAnswers);

        myRef.child("Users").child("Students").child(Variables.uid).updateChildren(test);
    }

    private void timer(final Integer time){
        final Integer[] hour = new Integer[1];
        final Integer[] min = new Integer[1];
        final Integer[] sec = new Integer[1];

        hour[0] = time/60000%60;
        min[0] = time/60000 - 60;
        sec[0] = 0;

        new CountDownTimer(time, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                hour[0] = (int) TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1));
                int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));

                String timeLeftFormatted = String.format(Locale.getDefault(), "Осталось: %02d часов %02d минут %02d секунд",hour[0], minutes, seconds);
                counter.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(TestActivity.this, ResultsActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onStop(){
        super.onStop();
        flag = 1;
    }

    public void onResume() {
        super.onResume();
        if (flag == 1){
            Intent intent = new Intent(TestActivity.this, TestSelection.class);
            startActivity(intent);
        }
    }

    private void init(){
        questionText = findViewById(R.id.questionText);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        radioGroup = findViewById(R.id.radioGroupTest);
        first = findViewById(R.id.firstRadioButton);
        second = findViewById(R.id.secondRadioButton);
        third = findViewById(R.id.thirdRadioButton);
        fourth = findViewById(R.id.fourthRadioButton);
        next = findViewById(R.id.next);
        counter = findViewById(R.id.textViewCounter);
        exitFromTest = findViewById(R.id.exit);
        saveAndComplete = findViewById(R.id.saveResults);
    }
}
