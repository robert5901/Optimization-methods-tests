package com.example.diplom;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diplom.adapters.ResultsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Results extends Fragment {
    private View v;
    private Admin_activity smb;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<Students> students = new ArrayList<>();

    public Results(Admin_activity smb) {
        this.smb = smb;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.results, container, false);
        init(v);

        myRef.child("Users").child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                students = new ArrayList<>();
                List<String> keys = new ArrayList<>();

                for(final DataSnapshot templateSnapshot : dataSnapshot.getChildren()){
                    keys.add(templateSnapshot.getKey());

                    students.add(templateSnapshot.getValue(Students.class));
                }
                ResultsAdapter adapter = new ResultsAdapter(smb, students);
                RecyclerView recyclerView = v.findViewById(R.id.recyclerViewForResults);
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
    }
}
