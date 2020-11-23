package com.example.diplom;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diplom.adapters.TestListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TestSelection extends AppCompatActivity {
    private TestSelection smb;
    private List<Test> tests = new ArrayList<>();
    private TestTime testTime = new TestTime();
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        smb = this;
        init();

        myRef.child("Tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests = new ArrayList<>();
                List<String> keys = new ArrayList<>();

                for(final DataSnapshot templateSnapshot : dataSnapshot.getChildren()){
                    keys.add(templateSnapshot.getKey());

                    tests.add(templateSnapshot.getValue(Test.class));
                }

                myRef.child("TestTime").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> g = new ArrayList<>();

                        for(final DataSnapshot templateSnapshot : dataSnapshot.getChildren()){
                            g.add(templateSnapshot.getValue(String.class));
                        }
                        testTime.endTime = g.get(0);
                        testTime.startTime = g.get(1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                TestListAdapter adapter = new TestListAdapter(smb, tests, keys, testTime);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(smb, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void init(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TestSelection.this, MainActivity.class);
        startActivity(intent);
    }
}
