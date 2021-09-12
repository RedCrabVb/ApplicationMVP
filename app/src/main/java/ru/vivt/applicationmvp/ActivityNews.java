package ru.vivt.applicationmvp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

        TextView textViewHeader = findViewById(R.id.textViewHeader);
        textViewHeader.setText(header);

        TextView textViewBody = findViewById(R.id.textViewNews);
        textViewBody.setText(body);

        ImageView imageViewNews = findViewById(R.id.imageViewNews);

        DownloadImage downloadImage = new DownloadImage();
        try {
            String url = Server.getInstance().getPathUrlToDownloadImgNews(imgPath);
            Bitmap bitmap = downloadImage.execute(url).get();
            imageViewNews.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }
}
