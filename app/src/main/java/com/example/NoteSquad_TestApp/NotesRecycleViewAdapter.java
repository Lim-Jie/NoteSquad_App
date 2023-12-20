package com.example.NoteSquad_TestApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.txtName.setText(notes.get(position).getName());
       holder.txtEmail.setText(notes.get(position).getEmail());
       holder.txtName.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context, notes.get(position).getName(), Toast.LENGTH_SHORT).show();
           }
       });
        Glide.with(context).asBitmap().load(notes.get(position).getImageURL())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName,txtEmail;
        private CardView parent;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.textName);
            parent=itemView.findViewById(R.id.parent);
            txtEmail=itemView.findViewById(R.id.textEmail);
            image=itemView.findViewById(R.id.image);
        }
    }
    public void setNotes(ArrayList<Notes> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}
