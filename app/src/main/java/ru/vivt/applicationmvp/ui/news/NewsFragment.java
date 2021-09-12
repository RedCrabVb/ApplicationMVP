package ru.vivt.applicationmvp.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;

import ru.vivt.applicationmvp.ActivityNews;
import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentNewsBinding;
import ru.vivt.applicationmvp.ui.repository.News;
import ru.vivt.applicationmvp.ui.repository.NewsAdapter;

public class NewsFragment extends Fragment {

    private NewsViewModel dashboardViewModel;
    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ListView listView = binding.listNews;
        ArrayAdapter<News> arrayAdapter = new NewsAdapter(binding.getRoot().getContext(), R.layout.list_news,
                new ArrayList<>(Arrays.asList(dashboardViewModel.getNews())));
        listView.setAdapter(arrayAdapter);

        /*show news list
        * dry - error
        * */
        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("click item + " + position);

            Intent intent = new Intent(binding.getRoot().getContext(), ActivityNews.class);
            Bundle bundle = new Bundle();
            News news = dashboardViewModel.getNews()[position];
            bundle.putString("header", news.getTitle());
            bundle.putString("body", news.getBody());
            bundle.putString("imgPath", news.getImgPath());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}