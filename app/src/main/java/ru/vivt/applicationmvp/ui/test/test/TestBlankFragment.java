package ru.vivt.applicationmvp.ui.test.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentTestBlankBinding;
import ru.vivt.applicationmvp.ui.repository.Question;
import ru.vivt.applicationmvp.ui.repository.ResultTest;
import ru.vivt.applicationmvp.ui.test.TestFragment;

public class TestBlankFragment extends Fragment {
    private final Gson gson = new Gson();

    private CodeScanner mCodeScanner;

    private FragmentTestBlankBinding binding;

    private Question[] questions;
    private int currentPositionQuestion = 0;
    private int wrongAnswer = 0;

    private void loadTestCase(TextView questionText, TextView comment, TextView countQuestion) {
        Question question = questions[currentPositionQuestion];
        questionText.setText(question.getText());
        comment.setText(question.getComment());
        countQuestion.setText(String.format("%d/%d", currentPositionQuestion, questions.length));
    }

    private boolean saveAnswer(TextView answerEditText) {
        boolean result = answerEditText.getText().toString().equals(questions[currentPositionQuestion].getAnswer());
        return result;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTestBlankBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button buttonNextQuestion = binding.buttonNextQuestion;
        TextView comment = binding.textComment;
        TextView answer = binding.textAnswer;
        TextView error = binding.textError;
        error.setTextColor(Color.RED);


        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        buttonNextQuestion.setEnabled(false);

        Bundle questionBundle = getActivity().getIntent().getExtras();
        if (questionBundle != null) {
            questions = gson.fromJson(questionBundle.get("questions").toString(), Question[].class);
            int idTest = questionBundle.getInt("idTest");
            long timeStart = System.currentTimeMillis();

            TextView questionText = binding.textQuestion;
            TextView countQuestion = binding.countQuestion;

            loadTestCase(questionText, comment, countQuestion);

            buttonNextQuestion.setOnClickListener(v -> {

                currentPositionQuestion++;
                if (currentPositionQuestion == questions.length) {
                    long timeEnd = System.currentTimeMillis();


                    ResultTest resultTest = new ResultTest(idTest, "" + (timeEnd - timeStart), wrongAnswer, questions.length, "right");
                    getActivity().getIntent().putExtra("resultTest", gson.toJson(resultTest));
                    replaceFragment(new TestResultFragment());
                } else {
                    loadTestCase(questionText, comment, countQuestion);
                }
                answer.setText("");
                answer.setTextColor(Color.BLACK);
                error.setVisibility(View.GONE);
                buttonNextQuestion.setEnabled(false);
            });
        }

        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) {
                CodeScannerView scannerView = binding.scannerView;
                mCodeScanner = new CodeScanner(getActivity(), scannerView);
                mCodeScanner.setDecodeCallback(result -> {
                    for (Question question : questions) {
                        if (question.getAnswer().hashCode() == Integer.parseInt(result.getText())) {
                            this.getActivity().runOnUiThread(() -> {
                                answer.setText(question.getAnswer());
                            });

                            this.getActivity().runOnUiThread(() -> {


                                        if (!saveAnswer(answer)) {
                                            wrongAnswer += 1;
                                            answer.setTextColor(Color.BLACK);
                                            error.setText("Ответ не верный");
                                            error.setVisibility(View.VISIBLE);
                                            buttonNextQuestion.setEnabled(false);
                                            vibrator.vibrate(1000);
                                        } else {
                                            answer.setTextColor(Color.GREEN);
                                            error.setText("");
                                            buttonNextQuestion.setEnabled(true);
                                        }
                                    }
                            );
                        }
                    }
                });
                scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
            } else {
                throw new RuntimeException();
            }
        } catch (Exception | Error e) {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error, разрешите приложению доступ к камере", Toast.LENGTH_SHORT).show());
        }

        return root;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


}
