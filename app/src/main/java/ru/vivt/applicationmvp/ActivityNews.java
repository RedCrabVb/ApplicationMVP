package ru.vivt.applicationmvp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ActivityNews extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);

        Bundle b = getIntent().getExtras();
        String header = "None";
        if (b != null) {
            header = getIntent().getExtras().getString("header");
        }

        TextView textHeader = findViewById(R.id.textViewHeader);
        textHeader.setText(header);
    }
}
