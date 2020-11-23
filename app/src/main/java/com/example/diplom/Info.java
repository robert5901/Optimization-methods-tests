package com.example.diplom;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Info extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button buttonStart;
    private String testName, time, questionsNumber;
    private TextView nameOfTest;
    private TextView testTime;
    private TextView numberOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        testName = Variables.tempTest.nameOfTest;
        time = String.valueOf(Variables.tempTest.TestTime);
        questionsNumber = String.valueOf(Variables.tempTest.questionList.size());

        nameOfTest.setText(testName);
        testTime.setText(time);
        numberOfQuestions.setText(questionsNumber);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Info.this, TestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void init(){
        numberOfQuestions = findViewById(R.id.questionNumber);
        testTime = findViewById(R.id.timeTextView);
        nameOfTest = findViewById(R.id.textViewTestName);
        buttonStart = findViewById(R.id.start);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public void onBackPressed(){
        Intent intent = new Intent(Info.this, TestSelection.class);
        startActivity(intent);
    }
}