package com.example.diplom;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editPassword;
    EditText editLogin;
    Button buttonAuthorization;
    Button buttonRegistration;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listData;
    private FirebaseAuth auth;
    private FirebaseDatabase mDataBase;
    private DatabaseReference myRef;
    private RadioButton teach_rdbtn, sports_rdbtn;
    private RadioGroup rdGroup;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //для портретного режима
        auth = FirebaseAuth.getInstance();
        init();

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.teacher_rdbtn:
                        myRef = mDataBase.getReference().child("Users/Teachers");
                        break;
                    case R.id.sportsmen_rdbtn:
                        myRef = mDataBase.getReference().child("Users/Students");
                        break;
                    default:
                        break;
                }
            }
        });

        //проверка пороля и переход в следующую активити.
        buttonAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = editPassword.getText().toString();
                String email = editLogin.getText().toString();

                if (password.isEmpty()){
                    editPassword.setError("Введите пароль");
                    return;
                }

                if (email.isEmpty()){
                    editLogin.setError("Введите Email");
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            try {
                                                String fio = dataSnapshot.child("Fio").getValue().toString();
                                                String role = dataSnapshot.child("Role").getValue().toString().trim();
                                                if (role.equals("Преподаватель")) {
                                                    Intent intent = new Intent(MainActivity.this, Admin_activity.class);
                                                    intent.putExtra("Fio", fio);

                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                    closeActivity();
                                                } else {
                                                    Intent intent = new Intent(MainActivity.this, TestSelection.class);
                                                    intent.putExtra("Fio", fio);

                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                    closeActivity();
                                                }
                                            }
                                            catch (NullPointerException e){
                                                Toast.makeText(MainActivity.this,"Ошибка входа. Пользователь не найден.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    editLogin.getText().clear();
                                    editPassword.getText().clear();

                                } else {
                                    Toast.makeText(MainActivity.this,"Ошибка входа. Пользователь не найден.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    Variables.uid = uid;
                }
            }
        });

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegForm.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        auth=FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance();
        myRef = mDataBase.getReference().child("Users");
        buttonAuthorization = (MaterialButton) findViewById(R.id.auth_btn);
        buttonRegistration = (MaterialButton) findViewById(R.id.reg_btn);
        editPassword = findViewById(R.id.passwordEditText);
        editLogin = findViewById(R.id.loginEditText);
        listData = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listData);
        teach_rdbtn=findViewById(R.id.teacher_rdbtn);
        sports_rdbtn=findViewById(R.id.sportsmen_rdbtn);
        rdGroup=findViewById(R.id.radioGroup);
    }
    private void closeActivity() {
        this.finish();
    }
}