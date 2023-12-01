package com.example.NoteSquad_TestApp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AddForumFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public AddForumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_forum, container, false);
        final EditText editTextForumTitle = view.findViewById(R.id.editTextForumTitle);
        final EditText editTextForumDescription = view.findViewById(R.id.editTextForumDescription);
        Button btnSubmitForum = view.findViewById(R.id.btnSubmitForum);

        btnSubmitForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextForumTitle.getText().toString().trim();
                String description = editTextForumDescription.getText().toString().trim();

                if (!title.isEmpty() && !description.isEmpty()) {
                    addForumToFirebase(title, description);

                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            }
        });

        return view;
    }

    private void addForumToFirebase(String title, String description) {
        if (getContext() != null) {
            GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(getContext());

            if (user != null) {
                String email = user.getEmail();
                db.collection("Users").whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                String username = (String) documentSnapshot.get("username");

                                Map<String, Object> forumData = new HashMap<>();
                                forumData.put("title", title);
                                forumData.put("description", description);
                                forumData.put("userName", username);
                                forumData.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("Forums")
                                        .add(forumData)
                                        .addOnSuccessListener(documentReference -> {
                                            if (getContext() != null) {
                                                Toast.makeText(getContext(), "Forum post added", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            if (getContext() != null) {
                                                Toast.makeText(getContext(), "Save failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                        });
            }
        }
    }
}
