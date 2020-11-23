package com.example.diplom.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diplom.Admin_activity;
import com.example.diplom.R;
import com.example.diplom.Students;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private Admin_activity context;
    private List<Students> students;


    public ResultsAdapter(Admin_activity context, List<Students> students) {
        this.students = students;
        this.context = context;

    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2, parent, false);
        return new ResultsAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ViewHolder holder, final int position) {

        holder.fio.setText(students.get(position).Fio);
        holder.points.setText(String.valueOf(students.get(position).CorrectAnswers));

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fio, points;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            fio = view.findViewById(R.id.fio);
            points = view.findViewById(R.id.points);
            cardView = view.findViewById(R.id.cardView2);
    }
}
}
