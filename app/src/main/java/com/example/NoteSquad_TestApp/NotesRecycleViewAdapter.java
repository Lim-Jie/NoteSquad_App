package com.example.NoteSquad_TestApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NotesRecycleViewAdapter extends RecyclerView.Adapter<NotesRecycleViewAdapter.ViewHolder> {
    private ArrayList<Notes> notes=new ArrayList<>();
    private Context context;


    public NotesRecycleViewAdapter(Context context) {
        this.context=context;
    }

    private OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }

    public interface OnImageClickListener {

        void onImageClick(String imageUrl, String description,boolean isFlagged,int upvotes,int downvotes);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       holder.txtTitle.setText(notes.get(position).getNoteTitle());

       holder.txtTitle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context, notes.get(position).getNoteTitle(), Toast.LENGTH_SHORT).show();
           }
       });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(notes.get(position).getimageUrl(), notes.get(position).getNoteDescription(),notes.get(position).isFlagged(),notes.get(position).getUpvotes(),notes.get(position).getDownvotes());
                }
            }
        });



        Glide.with(context).asBitmap().load(notes.get(position).getimageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private CardView parent;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent=itemView.findViewById(R.id.parent);
            txtTitle=itemView.findViewById(R.id.noteTitle);
            image=itemView.findViewById(R.id.image);

        }
    }
    public void setNotes(ArrayList<Notes> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}
