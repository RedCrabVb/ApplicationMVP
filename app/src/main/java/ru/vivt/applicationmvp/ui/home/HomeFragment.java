package ru.vivt.applicationmvp.ui.home;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ru.vivt.applicationmvp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getQrCode().observe(getViewLifecycleOwner(), s -> {
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
        });

        homeViewModel.getNewsLink().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                System.out.println(s);
                binding.webView.loadUrl(s);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}