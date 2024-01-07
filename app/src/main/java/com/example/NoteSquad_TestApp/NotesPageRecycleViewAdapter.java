package com.example.NoteSquad_TestApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class NotesPageRecycleViewAdapter extends RecyclerView.Adapter<NotesPageRecycleViewAdapter.ViewHolder>   implements NotesRecycleViewAdapter.OnImageClickListener{
    private ArrayList<NotePage> notePage=new ArrayList<>();
    private Context context;


    public NotesPageRecycleViewAdapter(Context context) {
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_page_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       NotePage currentNotePage=notePage.get(position);
       holder.subjectTitle.setText(currentNotePage.getSubject());
        NotesRecycleViewAdapter notesAdapter = new NotesRecycleViewAdapter(context);
        notesAdapter.setNotes(currentNotePage.getNotesList());
        notesAdapter.setOnImageClickListener(this);
        holder.notesRecView.setAdapter(notesAdapter);
        holder.notesRecView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return notePage.size();
    }

    @Override
    public void onImageClick(String imageUrl, String description, boolean isFlagged, int upvotes, int downvotes) {
        loadImageDisplayFragment(imageUrl, description,isFlagged,upvotes,downvotes);
    }
    private void loadImageDisplayFragment(String imageUrl, String description,boolean isFlagged, int upvotes, int downvotes) {
        // Create an instance of the fragment to display the image
        NotesDisplayFragment imageDisplayFragment = NotesDisplayFragment.newInstance(imageUrl, description,isFlagged,upvotes,downvotes);

        // Use a FragmentTransaction to replace the current fragment with the image display fragment
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.constraintLayoutFragment, imageDisplayFragment)
                .addToBackStack(null)
                .commit();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView subjectTitle;
        private RecyclerView notesRecView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTitle=itemView.findViewById(R.id.subjectTitle);
           notesRecView=itemView.findViewById(R.id.notesRecView);
        }
    }
    public void setNotePage(ArrayList<NotePage> notePage) {
        this.notePage = notePage;
        notifyDataSetChanged();
    }
}
