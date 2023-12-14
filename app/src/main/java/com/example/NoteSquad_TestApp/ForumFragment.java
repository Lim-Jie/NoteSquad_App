package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ForumFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView forumRecView;
    private ForumAdapter adapter;
    private ProgressBar loadingIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        FloatingActionButton fabAddForum = view.findViewById(R.id.addForumFAB);
        fabAddForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.constraintLayoutFragment, new AddForumFragment());
                transaction.commit();
                transaction.addToBackStack(null);

            }
        });

        loadingIndicator = view.findViewById(R.id.loadingIndicator);

        forumRecView = view.findViewById(R.id.forumRecView);
        forumRecView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ForumAdapter(getContext(), new ArrayList<>(), new ForumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Forum forum) {
                if (getActivity() != null) {
                    ForumDetailsFragment detailsFragment = ForumDetailsFragment.newInstance(forum);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.constraintLayoutFragment, detailsFragment);
                    transaction.commit();
                }
            }
        });
        forumRecView.setAdapter(adapter);

        fetchData();

        return view;
    }

    private void fetchData() {
        loadingIndicator.setVisibility(View.VISIBLE);
        db.collection("Forums").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                loadingIndicator.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    List<Forum> forumList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Forum forum = new Forum(document.getData());
                        forumList.add(forum);
                    }

                    updateAdapter(forumList);
                } else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Error getting documents:", String.valueOf(task.getException()));
                }
            }
        });

    }

    private void updateAdapter(List<Forum> forumList) {
        ForumAdapter adapter = (ForumAdapter) forumRecView.getAdapter();
        if (adapter != null) {
            adapter.setData(forumList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }
}