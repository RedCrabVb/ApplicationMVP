package ru.vivt.applicationmvp.ui.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.ui.repository.Question;
import ru.vivt.applicationmvp.ui.repository.QuestionAdapter;
import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.TestAdapter;

public class TestActivity extends AppCompatActivity {
    private Gson gson = new Gson();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_test);


        Question[] question = gson.fromJson(getIntent().getExtras().get("questions").toString(), Question[].class);
        ListView listView = findViewById(R.id.listTestQuestion);
        listView.setAdapter(new QuestionAdapter(this, R.layout.list_test,
                new ArrayList(Arrays.asList(question))));
    }
}
