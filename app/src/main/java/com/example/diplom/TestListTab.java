package com.example.diplom;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class TestListTab extends Fragment {
    private Button buttonCreateTest, setTime;
    private View v;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<Test> tests = new ArrayList<>();
    private Admin_activity smb;

    public TestListTab(Admin_activity smb) {
        this.smb = smb;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.test_list_tab, container, false);
        init(v);
        buttonCreateTest.setVisibility(View.VISIBLE);
        setTime.setVisibility(View.VISIBLE);

        buttonCreateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestCreating.class));
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTimeSetDialog dialog = new CreateTimeSetDialog(smb);
                dialog.show(getChildFragmentManager(), "TAG");
            }
        });

        myRef.child("Tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests = new ArrayList<>();
                List<String> keys = new ArrayList<>();

                for(final DataSnapshot templateSnapshot : dataSnapshot.getChildren()){
                    keys.add(templateSnapshot.getKey());

                        tests.add(templateSnapshot.getValue(Test.class));
                }

                TestListAdapter adapter = new TestListAdapter(smb, tests, keys, null);
                RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(smb, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return v;
    }

    private void init(View v){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        buttonCreateTest = v.findViewById(R.id.createTestButton);
        setTime = v.findViewById(R.id.setTimeForTest);
    }
}
