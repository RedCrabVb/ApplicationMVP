package ru.vivt.applicationmvp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import ru.vivt.applicationmvp.ui.repository.DownloadImage;
import ru.vivt.applicationmvp.ui.repository.Server;

public class ActivityNews extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);

        Bundle b = getIntent().getExtras();
        String header = b.getString("header");
        String body = b.getString("body");
        String imgPath = b.getString("imgPath");

        TextView textViewHeader = findViewById(R.id.textViewHeader_list);
        textViewHeader.setText(header);

        TextView textViewBody = findViewById(R.id.textViewNews);
        textViewBody.setText(body);

        ImageView imageViewNews = findViewById(R.id.imageViewNews);
        new DownloadImage(imageViewNews).execute(Server.getInstance().getPathUrlToDownloadImgNews(imgPath));
    }
}
