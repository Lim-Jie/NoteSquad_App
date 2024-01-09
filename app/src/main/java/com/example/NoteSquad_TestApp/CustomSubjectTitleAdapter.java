package com.example.NoteSquad_TestApp;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSubjectTitleAdapter extends BaseAdapter {
    Context context;
    int icons[];
    String[] subjectTitle;
    LayoutInflater inflter;

    public CustomSubjectTitleAdapter(Context applicationContext, int[] icons, String[] subjectTitle) {
        this.context = applicationContext;
        this.icons = icons;
        this.subjectTitle=subjectTitle;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_subject_title, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView SubjectTitle = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(icons[i]);
        SubjectTitle.setText(subjectTitle[i]);
        return view;
    }
}