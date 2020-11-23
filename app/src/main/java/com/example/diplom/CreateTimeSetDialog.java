package com.example.diplom;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class CreateTimeSetDialog extends DialogFragment {

    private Admin_activity testListTab;
    private TextView startTime, endTime, open, close;
    private Calendar dateAndTime = Calendar.getInstance();
    private Integer min, hour, flag ;
    private Button save;
    private String summStart, summEnd;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public interface onClickListener {void onClick(Record record);}
    public void setOnClickListener (CreateTimeSetDialog.onClickListener listener) {mListener = listener;}
    private CreateTimeSetDialog.onClickListener mListener;

    public CreateTimeSetDialog(Admin_activity smb) {
        this.testListTab = smb;
    }

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

            if (flag == 1){
                startTime.setText(time);
                summStart = time;
            }else{
                endTime.setText(time);
                summEnd = time;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.set_time_for_test, container, false);

        startTime = view.findViewById(R.id.startTimeTextView);
        endTime = view.findViewById(R.id.endTimeTextView);
        open = view.findViewById(R.id.openText);
        close = view.findViewById(R.id.closeText);
        save = view.findViewById(R.id.save_btn);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true)
                        .show();
                flag = 1;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true)
                        .show();
                flag = 2;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestTime testTime = new TestTime();
                testTime.startTime = summStart;
                testTime.endTime = summEnd;

                myRef.child("TestTime").setValue(testTime);

                Intent intent = new Intent(getContext(), Admin_activity.class);
                startActivity(intent);
            }
        });
    }
}
