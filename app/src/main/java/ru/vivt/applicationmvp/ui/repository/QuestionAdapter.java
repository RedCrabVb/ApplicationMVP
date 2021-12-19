package ru.vivt.applicationmvp.ui.repository;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ru.vivt.applicationmvp.R;

public class QuestionAdapter extends ArrayAdapter<Question>  {
    private Context mContext;
    private int mResource;

    public QuestionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Question> testsArrayList) {
        super(context, resource, testsArrayList);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView textViewTitle = convertView.findViewById(R.id.textViewHeader_listQuestion);
        EditText editText = convertView.findViewById(R.id.editTextTextAnswer);
        CheckBox checkBox = convertView.findViewById(R.id.checkBoxIsComlit);
        Button send = convertView.findViewById(R.id.buttonSend);

        checkBox.setEnabled(false);


        Question question = getItem(position);
        textViewTitle.setText(question.getText());
        send.setOnClickListener(v -> {
            checkBox.setChecked(editText.getText().toString().equals(question.getAnswer()));
        });

        return convertView;
    }
}
