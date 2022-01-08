package ru.vivt.applicationmvp.ui.test.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.gson.Gson;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentTestBlankBinding;
import ru.vivt.applicationmvp.ui.repository.Question;

public class TestBlankFragment extends Fragment {
    private Gson gson = new Gson();

    private CodeScanner mCodeScanner;

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

        Bundle questionBundle = getActivity().getIntent().getExtras();
        if (questionBundle != null) {
            questions = gson.fromJson(questionBundle.get("questions").toString(), Question[].class);

            EditText questionText = binding.textQuestion;
            EditText comment = binding.textComment;
            TextView countQuestion = binding.countQuestion;
            Button buttonNextQuestion = binding.buttonNextQuestion;

            loadTestCase(questionText, comment, countQuestion);

            buttonNextQuestion.setOnClickListener(v -> {
                if (currentPositionQuestion + 1 > questions.length) {
                    replaceFragment(new TestResultFragment());
                } else {
                    loadTestCase(questionText, comment, countQuestion);
                }
            });
        }

        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA }, 100);
            }
            else {
                Toast.makeText(getActivity(), "Permission already granted", Toast.LENGTH_SHORT).show();
            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) {
                CodeScannerView scannerView = binding.scannerView;
                mCodeScanner = new CodeScanner(getActivity(), scannerView);
                mCodeScanner.setDecodeCallback(result -> getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show()));
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
