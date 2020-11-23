package com.example.diplom.adapters;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diplom.Admin_activity;
import com.example.diplom.ChangeTest;
import com.example.diplom.Info;
import com.example.diplom.R;
import com.example.diplom.Test;
import com.example.diplom.TestSelection;
import com.example.diplom.TestTime;
import com.example.diplom.Variables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {

    private List<Test> testArrayList;
    private Admin_activity context;
    private List<String> keys;
    private TestSelection testSelection;
    private TestTime testTime;
    private Calendar calendar = Calendar.getInstance();

    public TestListAdapter(Admin_activity context, List<Test> testArrayList, List<String> keys, TestTime testTime) {
        this.testArrayList = testArrayList;
        this.context = context;
        this.keys = keys;

    }

    public TestListAdapter(TestSelection smb, List<Test> tests, List<String> keys, TestTime testTime) {
        this.testArrayList = tests;
        this.testSelection = smb;
        this.keys = keys;
        this.testTime = testTime;
    }


    @NonNull
    @Override
    public TestListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestListAdapter.ViewHolder holder, final int position) {
        holder.password.setText(testArrayList.get(position).nameOfTest);

        if (context != null) {

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ChangeTest.class);
                    Variables.tempTest = testArrayList.get(position);
                    Variables.key = keys.get(position);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });


        }else{

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Date d = new Date();
                        String currentTime = ((d.getTime() / 1000 / 60 / 60) % 24)+3 + ":" + (d.getTime() / 1000 / 60) % 60;

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                        Date currentTimeDate = null;
                        Date start = null;
                        Date end = null;

                        try {
                             currentTimeDate = format.parse(currentTime);
                             start = format.parse(testTime.startTime);
                             end = format.parse(testTime.endTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (start != null && end != null) {
                            if (currentTimeDate.after(start) && currentTimeDate.before(end)) {
                                Intent intent = new Intent(testSelection, Info.class);
                                Variables.tempTest = testArrayList.get(position);
                                Variables.key = keys.get(position);
                                testSelection.startActivity(intent);
                            }
                        }
                    }

                });
        }
    }

    @Override
    public int getItemCount() {
            return testArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView password;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            password = view.findViewById(R.id.password_textView);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
