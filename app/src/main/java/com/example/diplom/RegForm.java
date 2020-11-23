package com.example.diplom;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegForm extends AppCompatActivity {
    private EditText reg_fio_edit, reg_email_edit, reg_pass_edit, reg_confirm_edit;
    private Button reg_reg_btn;
    private RadioButton stud_rbtn, teacher_rbtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        reg_fio_edit = findViewById(R.id.reg_name_edit);
        reg_email_edit = findViewById(R.id.reg_email_edit);
        reg_pass_edit = findViewById(R.id.reg_pass_edit);
        reg_confirm_edit = findViewById(R.id.reg_confirm_edit);
        reg_reg_btn = findViewById(R.id.registr_btn);
        stud_rbtn = findViewById(R.id.radioButton);
        teacher_rbtn = findViewById(R.id.radioButton2);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users");

        reg_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fio = reg_fio_edit.getText().toString();
                String role = "";
                final String email = reg_email_edit.getText().toString().trim();
                final String password = reg_pass_edit.getText().toString().trim();

                String conf_pass = reg_confirm_edit.getText().toString().trim();

                if (TextUtils.isEmpty(fio)) {
                    Toast.makeText(RegForm.this, "Пожалуйста, введите ФИО.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegForm.this, "Пожалуйста, введите E-mail.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegForm.this, "Пожалуйста, введите пароль.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(conf_pass)) {
                    Toast.makeText(RegForm.this, "Пожалуйста, повторите пароль.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(RegForm.this, "Пароль должен состоять из не менее 6 символов.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stud_rbtn.isChecked()) {
                    role = "Студент";
                } else {
                    if (teacher_rbtn.isChecked()) {
                        role = "Преподаватель";
                    } else {
                        Toast.makeText(RegForm.this, "Выберите роль.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (password.equals(conf_pass)) {
                    final String finalRole = role;
                    if (finalRole.equals("Студент")) {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegForm.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Users information = new Users(fio, email, finalRole, 0);
                                            myRef.child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(RegForm.this, "Регистрация прошла успешно.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegForm.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            Toast.makeText(RegForm.this, "Регистрация провалилась. Очень жаль.", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegForm.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Users information = new Users(fio, email, finalRole, 0);
                                            myRef.child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(RegForm.this, "Регистрация прошла успешно.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegForm.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            Toast.makeText(RegForm.this, "Регистрация провалилась. Очень жаль.", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                }else{
                    Toast.makeText(RegForm.this, "Пароли не совпадают.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
