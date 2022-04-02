package ru.vivt.applicationmvp.ui.repository;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ru.vivt.applicationmvp.R;

public class TestAdapter extends ArrayAdapter<Test> {
    private Context mContext;
    private int mResource;

    public TestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Test> testsArrayList) {
        super(context, resource, testsArrayList);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView textViewTitle = convertView.findViewById(R.id.textViewHeader_list);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescription_list);

        textViewTitle.setText(getItem(position).getHeader());
        textViewDescription.setText(getItem(position).getDescription());

        if (!getItem(position).isActive())  {
            LinearLayout linearLayout = convertView.findViewById(R.id.item_test);
            linearLayout.setBackgroundColor(Color.GRAY);
        }

        return convertView;
    }
}
