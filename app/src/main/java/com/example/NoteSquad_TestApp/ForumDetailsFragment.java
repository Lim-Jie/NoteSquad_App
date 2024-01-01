package com.example.NoteSquad_TestApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ForumDetailsFragment extends Fragment {
    private Forum forum;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewNoComments;
    private TextView textViewLikes;
    private RecyclerView commentsRecyclerView;
    private EditText editTextComment;
    private Button btnSubmitComment;
    private Button btnLike;
    private Button btnDislike;
    private ImageButton btnDelete;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<ForumComment> commentsList = new ArrayList<>();
    private ForumCommentsAdapter forumCommentsAdapter;

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public static ForumDetailsFragment newInstance(Forum forum) {
        ForumDetailsFragment fragment = new ForumDetailsFragment();
        fragment.setForum(forum);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_details, container, false);
        textViewTitle = view.findViewById(R.id.forumDetailsTitle);
        textViewDescription = view.findViewById(R.id.forumDetailsDescription);
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        textViewNoComments = view.findViewById(R.id.textViewNoComments);
        textViewLikes = view.findViewById(R.id.textViewLikes);
        editTextComment = view.findViewById(R.id.editTextComment);
        btnSubmitComment = view.findViewById(R.id.btnSubmitComment);
        btnLike = view.findViewById(R.id.btnLike);
        btnDislike = view.findViewById(R.id.btnDislike);
        btnDelete = view.findViewById(R.id.btnDelete);

        getUsername(new Callback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals(forum.getUsername())) {
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                btnDelete.setVisibility(View.INVISIBLE);
            }
        });

        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        forumCommentsAdapter = new ForumCommentsAdapter(commentsList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(forumCommentsAdapter);
        loadComments();

        int likesCount = forum.getOriginalLikes();
        final boolean[] isVoted = {false};

        if (forum != null) {
            textViewTitle.setText(forum.getTitle());
            textViewDescription.setText(forum.getDescription());
            textViewLikes.setText(String.valueOf(likesCount) + " Likes");
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoted[0] == false) {
                    int newLikesCount = forum.getNewLikes() + 1;

                    updateLikesCount(newLikesCount);

                    forum.setOriginalLikes(newLikesCount);
                    forum.setNewLikes(newLikesCount);

                    textViewLikes.setText(String.valueOf(newLikesCount) + " Likes");
                    isVoted[0] = true;
                } else {
                    Toast.makeText(getContext(), "Already voted", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newLikesCount = forum.getNewLikes() - 1;

                if (isVoted[0] == true) {
                    Toast.makeText(getContext(), "Already voted", Toast.LENGTH_SHORT).show();
                } else if (newLikesCount < 0) {
                    Toast.makeText(getContext(), "Cannot less than 0", Toast.LENGTH_SHORT).show();
                } else {
                    updateLikesCount(newLikesCount);

                    forum.setOriginalLikes(newLikesCount);
                    forum.setNewLikes(newLikesCount);

                    textViewLikes.setText(String.valueOf(newLikesCount) + " Likes");

                    isVoted[0] = true;
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteForum();
            }
        });


        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCommentText = editTextComment.getText().toString().trim();

                if (!newCommentText.isEmpty()) {
                    ForumComment newComment = new ForumComment();
                    getUsername(new Callback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            newComment.setForumCommentUsername(result);
                            newComment.setForumCommentText(newCommentText);
                            newComment.setForumCommentTimestamp(new Timestamp(new Date()));
                            addComment(newComment);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            newComment.setForumCommentUsername("Anonymous");
                            newComment.setForumCommentText(newCommentText);
                            newComment.setForumCommentTimestamp(new Timestamp(new Date()));
                            addComment(newComment);
                        }
                    });

                    editTextComment.setText("");
                }
            }
        });

        return view;
    }

    private void updateLikesCount(int newLikesCount) {
        if (forum != null) {
            Map<String, Object> update = new HashMap<>();
            update.put("likes", newLikesCount);

            db.collection("Forums")
                    .document(forum.getForumId())
                    .update(update)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Liked!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to like. Try again later.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void loadComments() {
        if (forum != null) {
            db.collection("Forums")
                    .document(forum.getForumId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            List<ForumComment> retrievedComments = documentSnapshot.toObject(Forum.class).getComments();
                            if (retrievedComments != null) {
                                commentsList.clear();
                                commentsList.addAll(retrievedComments);
                                forumCommentsAdapter.notifyDataSetChanged();
                                textViewNoComments.setVisibility(commentsList.isEmpty() ? View.VISIBLE : View.GONE);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to load comments. Try again later.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void addComment(ForumComment comment) {
        if (forum != null) {
            Map<String, Object> update = new HashMap<>();
            update.put("comments", FieldValue.arrayUnion(comment));

            db.collection("Forums")
                    .document(forum.getForumId())
                    .update(update)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                        loadComments();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to add comment. Try again later.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void deleteForum() {
        if (forum != null) {
            db.collection("Forums")
                    .document(forum.getForumId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Forum deleted successfully", Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to delete forum. Try again later.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void getUsername(Callback<String> callback) {

        if (getContext() != null) {
            GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(getContext());

            if (user != null) {
                String email = user.getEmail();
                db.collection("Users").whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                callback.onSuccess((String) documentSnapshot.get("username"));
                            }
                        })
                        .addOnFailureListener(e -> {
                            callback.onFailure(e.getMessage());
                        });
            } else {
                Toast.makeText(getContext(), "No Context.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(String errorMessage);
    }


}
