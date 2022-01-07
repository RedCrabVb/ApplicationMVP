package ru.vivt.applicationmvp.ui.test.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentTestBlankBinding;
import ru.vivt.applicationmvp.ui.repository.Question;

public class TestBlankFragment extends Fragment {
    private Gson gson = new Gson();

    private FragmentTestBlankBinding binding;

    private Question[] questions;
    private int currentPositionQuestion = 0;

    private void loadTestCase(EditText questionText, EditText comment, TextView countQuestion) {
        Question question = questions[currentPositionQuestion++];
        questionText.setText(question.getText());
        comment.setText(question.getComment());
        countQuestion.setText(String.format("%d/%d", currentPositionQuestion, questions.length));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTestBlankBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        savedInstanceState = getActivity().getIntent().getExtras();
        if (savedInstanceState != null) {
            questions = gson.fromJson(savedInstanceState.get("questions").toString(), Question[].class);

            EditText questionText = binding.textQuestion;
            EditText comment = binding.textComment;
            TextView countQuestion = binding.countQuestion;
            Button buttonNextQuestion = binding.buttonNextQuestion;

            loadTestCase(questionText, comment, countQuestion);

            buttonNextQuestion.setOnClickListener(v -> {
                if (currentPositionQuestion + 1 > questions.length) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_test, new TestResultFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    loadTestCase(questionText, comment, countQuestion);
                }
            });
        }

        return root;
    }

}
