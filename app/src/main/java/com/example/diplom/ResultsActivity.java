package com.example.diplom;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

 public class ResultsActivity extends AppCompatActivity {
     TextView results;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        init();

        results.setText(String.valueOf(Variables.tempTest.correctAnswers));
    }

    private void init(){
        results = findViewById(R.id.totalPoints);
    }

     @Override
     public void onBackPressed() {
         Intent intent = new Intent(ResultsActivity.this, TestSelection.class);
         startActivity(intent);
     }
}
