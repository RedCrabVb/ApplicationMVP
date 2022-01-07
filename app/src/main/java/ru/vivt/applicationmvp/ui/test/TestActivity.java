package ru.vivt.applicationmvp.ui.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Question;
import ru.vivt.applicationmvp.ui.repository.QuestionAdapter;
import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.TestAdapter;
import ru.vivt.applicationmvp.ui.test.test.TestBlankFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_test);

        FragmentManager manager = getSupportFragmentManager();
        TestBlankFragment fragment = (TestBlankFragment) manager.findFragmentById(R.id.nav_host_fragment_activity_test);
        fragment.onCreate(null);
    }
}
