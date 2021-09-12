package ru.vivt.applicationmvp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.WriterException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ru.vivt.applicationmvp.ActivityNews;
import ru.vivt.applicationmvp.MainActivity;
import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentHomeBinding;
import ru.vivt.applicationmvp.ui.repository.News;
import ru.vivt.applicationmvp.ui.repository.NewsAdapter;
import ru.vivt.applicationmvp.ui.repository.ObserverArrayNews;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        homeViewModel.getQrCode().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                /*create qr code*/
                ImageView image = binding.imageViewQrCode;
                WindowManager manager = (WindowManager) binding.getRoot().getContext().getSystemService(binding.getRoot().getContext().WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int dimen = Math.max(point.x, point.y) * 3 / 4;
                QRGEncoder qrgEncoder = new QRGEncoder(s, null, QRGContents.Type.TEXT, dimen);
                try {
                    Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                    image.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Log.e("Qr code error", e.toString());
                }
            }
        });

        homeViewModel.getNews().observe(getViewLifecycleOwner(), new ObserverArrayNews(binding.getRoot().getContext(), binding.dynamickList));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}