package ru.vivt.applicationmvp.ui.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import ru.vivt.applicationmvp.R;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context mContext;
    private int mResource;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> newsArrayList) {
        super(context, resource, newsArrayList);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.imageViewHeader_list);

        DownloadImage downloadImage = new DownloadImage();
        try {
            String url = Server.getInstance().getPathUrlToDownloadImgNews(getItem(position).getImgPath());
            Bitmap bitmap = downloadImage.execute(url).get();
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textViewTitle = convertView.findViewById(R.id.textViewHeader_list);

        textViewTitle.setText(getItem(position).getTitle());

        return convertView;
    }
}
