package com.example.meet.sos;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SettingsListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] options;
    private int[] imageId;

    public SettingsListAdapter(@NonNull Context context, String[] options, int[] imageId) {
        super(context,R.layout.layout_settings_list_item,options);
        this.context = (Activity) context;
        this.options = options;
        this.imageId = imageId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = context.getLayoutInflater().inflate(R.layout.layout_settings_list_item,null,true);

        ImageView optionImage = (ImageView)rowView.findViewById(R.id.settings_list_item_image);
        TextView optionText = (TextView)rowView.findViewById(R.id.settings_list_item_textview);
        optionImage.setImageResource(imageId[position]);
        optionText.setText(options[position]);
        return rowView;
        //return super.getView(position, convertView, parent);
    }
}
