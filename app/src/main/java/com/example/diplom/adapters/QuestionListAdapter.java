package com.example.diplom.adapters;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diplom.ChangeTest;
import com.example.diplom.R;
import com.example.diplom.Record;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {

    private ChangeTest context;
    private List<com.example.diplom.Record> questions;
    private Object Record;

    public interface onClickListener {void onClick(int position);}
    public void setOnClickListener (onClickListener listener) {mListener = listener;}
    private onClickListener mListener;


    public QuestionListAdapter(ChangeTest context, List<Record> questions)   {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new QuestionListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListAdapter.ViewHolder holder, final int position) {
        holder.deleteQuestion.setVisibility(View.VISIBLE);

        holder.deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });


        String text = "Вопрос " + (position+1) + "\n" + questions.get(position).question;

        holder.password.setText(text);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChangeTest.class);
                ArrayList<String> variants = new ArrayList<>();

                variants.add(questions.get(position).firstVariant);
                variants.add(questions.get(position).secondVariant);
                variants.add(questions.get(position).thirdVariant);
                variants.add(questions.get(position).fourthVariant);

                intent.putStringArrayListExtra("aye", variants);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView password;
        CardView cardView;
        MaterialButton deleteQuestion;

        public ViewHolder(View view) {
            super(view);
            password = view.findViewById(R.id.password_textView);
            cardView = view.findViewById(R.id.cardView);
            deleteQuestion = view.findViewById(R.id.deleteQuestionButton);
        }
    }
}
