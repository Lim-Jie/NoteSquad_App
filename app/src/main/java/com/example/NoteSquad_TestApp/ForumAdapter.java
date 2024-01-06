package com.example.NoteSquad_TestApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {
    private List<Forum> forumList;
    private OnItemClickListener listener;
    private static Context context;

    public ForumAdapter(Context context, List<Forum> forumList, OnItemClickListener listener) {
        this.context = context;
        this.forumList = forumList;
        this.listener = listener;
    }

    public void setData(List<Forum> newData) {
        forumList.clear();
        forumList.addAll(newData);
    }


    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_list_item, parent, false);
        return new ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        Forum forum = forumList.get(position);
        holder.bind(forum, listener);
    }

    @Override
    public int getItemCount() {
        return forumList.size();
    }

    static class ForumViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;

        public ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.forumTitle);
            descriptionTextView = itemView.findViewById(R.id.forumDescription);
        }

        public void bind(final Forum forum, final OnItemClickListener listener) {
            titleTextView.setText(forum.getTitle());
            descriptionTextView.setText(forum.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(forum);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Forum forum);
    }
}
