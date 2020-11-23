package com.example.diplom;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateQuestionDialog extends DialogFragment {
    private ChangeTest changeTest;
    private Button saveQuestion;
    private EditText question, answer, first, second, third, fourth;

    public interface onClickListener {void onClick(Record record);}
    public void setOnClickListener (onClickListener listener) {mListener = listener;}
    private onClickListener mListener;

    public CreateQuestionDialog(ChangeTest smb) {
        this.changeTest = smb;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.test_creating, container, false);

        saveQuestion = view.findViewById(R.id.save_test);
        question = view.findViewById(R.id.question);
        answer = view.findViewById(R.id.answer);
        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);
        third = view.findViewById(R.id.third);
        fourth = view.findViewById(R.id.fourth);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Record record = new Record();
                record.question = question.getText().toString();
                record.answer = Integer.parseInt(answer.getText().toString());
                record.firstVariant = first.getText().toString();
                record.secondVariant = second.getText().toString();
                record.thirdVariant = third.getText().toString();
                record.fourthVariant = fourth.getText().toString();
                mListener.onClick(record);
                dismiss();
            }
        });
    }
}
