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
        String body = "Text not found";
        if (b != null) {
            header = b.getString("header");
            body = b.getString("body");
        }

        TextView textViewHeader = findViewById(R.id.textViewHeader);
        textViewHeader.setText(header);

        TextView textViewBody = findViewById(R.id.textViewNews);
        textViewBody.setText(body);
    }
}
