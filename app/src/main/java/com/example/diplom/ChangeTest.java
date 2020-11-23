package com.example.diplom;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diplom.adapters.QuestionListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeTest extends AppCompatActivity  {
    private Record test;
    Button addQuestion, save;
    private Record questionList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> variants = new ArrayList<>();
    private ChangeTest smb;
    private Test testObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list);

        smb = this;
        init();

        adapter();


        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateQuestionDialog dialog = new CreateQuestionDialog(smb);
                dialog.setOnClickListener(new CreateQuestionDialog.onClickListener() {
                    @Override
                    public void onClick(Record record) {
                        Variables.tempTest.questionList.add(record);
                    }
                });
                dialog.show(getSupportFragmentManager(), "TAG");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> test = new HashMap<>();
                test.put(Variables.key, Variables.tempTest);

                myRef.child("Tests").updateChildren(test);

                Intent intent = new Intent(ChangeTest.this, Admin_activity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });
    }

    private void adapter(){
        final QuestionListAdapter adapter = new QuestionListAdapter (smb, Variables.tempTest.questionList);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewForQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(smb, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new QuestionListAdapter.onClickListener() {
            @Override
            public void onClick(final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(smb);
                builder.setTitle("Внимание!")
                        .setMessage("Вы точно хотите удалить вопрос?")
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Variables.tempTest.questionList.remove(position);
                                adapter.notifyDataSetChanged();

                                // Закрываем окно
                                dialog.cancel();
                            }

                        }).show();
                builder.create();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(ChangeTest.this, Admin_activity.class);
        startActivity(intent);
    }

    private void init(){
        save = findViewById(R.id.saveChanges);
        addQuestion = findViewById(R.id.addQuestionButton);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
}
