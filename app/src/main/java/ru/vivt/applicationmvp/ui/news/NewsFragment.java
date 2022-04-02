package ru.vivt.applicationmvp.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import ru.vivt.applicationmvp.databinding.FragmentNewsBinding;
import ru.vivt.applicationmvp.ui.repository.Server;

public class NewsFragment extends Fragment {

    private NewsViewModel dashboardViewModel;
    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        try {
            JsonObjectRequest request = Server.getInstance().getNewsRequest(response -> {
                try {
                    binding.webNew.loadUrl(response.getString("News"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        dashboardViewModel.getNewsLink().observe(getViewLifecycleOwner(), s -> {
//            System.out.println(s);
//            binding.webNew.loadUrl(s);
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}