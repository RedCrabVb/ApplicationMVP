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
        String header = "None";
        String body = "Text not found";
        String imgPath = "example.png";
        if (b != null) {
            header = b.getString("header");
            body = b.getString("body");
            imgPath = b.getString("imgPath");
        }

        TextView textViewHeader = findViewById(R.id.textViewHeader_list);
        textViewHeader.setText(header);

        TextView textViewBody = findViewById(R.id.textViewNews);
        textViewBody.setText(body);

        ImageView imageViewNews = findViewById(R.id.imageViewNews);

        try {
            imageViewNews.setImageBitmap(new DownloadImage().execute(Server.getInstance().getPathUrlToDownloadImgNews(imgPath)).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
