package com.example.diplom;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestCreating extends AppCompatActivity {
    private EditText question, answer, first, second, third, fourth, testName;
    private TextView testNameText, timeText, timeForPass;
    private Button saveTest, nextQuestion;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Integer numberOfQuestion = 0;
    private int flag = 1;
    private List<Record> questionList = new ArrayList<>();
    private Test testObject;
    private String nameOfTest, testTime;
    private Calendar dateAndTime = Calendar.getInstance();
    private Integer min, hour, summ;
    private Integer flag2 = 0;

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time;
            hour = hourOfDay;
            min = minute;
            if (min < 10){
                time = hour.toString() + ":0" + min.toString();
            }else{
                time = hour.toString() + ":" + min.toString();
            }
            timeForPass.setText(time); // 1:25
            summ = hour*60 + min;
        }
    };

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_creating);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        timeForPass.setPaintFlags(timeForPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);// подчеркивание текста

        testObject = new Test();
        testName.setVisibility(View.VISIBLE);
        timeForPass.setVisibility(View.VISIBLE);
        timeText.setVisibility(View.VISIBLE);
        saveTest.setVisibility(View.VISIBLE);
        testNameText.setVisibility(View.VISIBLE);
        nextQuestion.setVisibility(View.VISIBLE);

        timeForPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(TestCreating.this, t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkingEditText();
                if (flag2 == 1) return;

                if (timeForPass.getText().toString().equals("")){
                    timeForPass.setError("Введите значение!");
                    flag2 = 1;
                }else {
                    flag2 = 0;
                }

                nameOfTest = testName.getText().toString();
                testTime = timeForPass.getText().toString();
                final String questionText = question.getText().toString();
                final String answerText = answer.getText().toString();
                final String firstVariant = first.getText().toString();
                final String secondVariant = second.getText().toString();
                final String thirdVariant = third.getText().toString();
                final String fourthVariant = fourth.getText().toString();

                testObject.nameOfTest = nameOfTest;

                if (flag == 1) {
                    testName.setEnabled(false);
                }

                numberOfQuestion++;

                String questionNumber = "Вопрос " + numberOfQuestion;

                checkingEditText();
                if (flag2 == 1) return;

                Record record = new Record();
                record.question = question.getText().toString();
                record.answer = Integer.parseInt(answer.getText().toString());
                record.firstVariant = first.getText().toString();
                record.secondVariant = second.getText().toString();
                record.thirdVariant = third.getText().toString();
                record.fourthVariant = fourth.getText().toString();
                questionList.add(record);
                clearAllEditText();
            }
        });

        saveTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testObject.questionList = questionList;

                Test test = new Test();
                test.nameOfTest = nameOfTest;
                test.TestTime = Integer.parseInt(String.valueOf(summ));
                test.questionList = questionList;

                myRef.child("Tests").push().setValue(test);

                Intent intent = new Intent(TestCreating.this, Admin_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void init(){
        testName = findViewById(R.id.testNameEditText);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        testNameText = findViewById(R.id.testNameTextView);
        timeForPass = findViewById(R.id.ViewTextTime);
        timeText = findViewById(R.id.timeTextView);
        saveTest = (MaterialButton) findViewById(R.id.save_test);
        nextQuestion = (MaterialButton) findViewById(R.id.next_question);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private void clearAllEditText(){
        question.setText(null);
        answer.setText(null);
        first.setText(null);
        second.setText(null);
        third.setText(null);
        fourth.setText(null);
    }

    private void checkingEditText(){
        List<EditText> list = new ArrayList();

        list.add(testName);
        list.add(question);
        list.add(answer);
        list.add(first);
        list.add(second);
        list.add(third);
        list.add(fourth);

        for(int i=0; i<list.size(); i++){
            if (list.get(i).getText().toString().equals("")){
                list.get(i).setError("Введите значение!");
                flag2 = 1;
            }else{
                flag2 = 0;
            }
        }
    }
}
