package com.example.NoteSquad_TestApp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesDisplayFragment extends Fragment {

    ImageView flag;
    ImageView upvote;

    ImageView downvote;
    ImageView shareButton;
    ImageView editButton;
 TextView numOfUpvotes;
 TextView numOfDownvotes;



    public static NotesDisplayFragment newInstance(String imageUrl, String description, boolean isFlagged, int upvotes, int downvotes) {
        // Create a new instance of the fragment with arguments
        NotesDisplayFragment fragment = new NotesDisplayFragment();
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        args.putString("description", description);
        args.putBoolean("isFlagged", isFlagged);
        args.putInt("upvotes", upvotes);
        args.putInt("downvotes", downvotes);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_display, container, false);

        // Retrieve image URL and description from arguments
        String imageUrl = getArguments().getString("imageUrl", "");
        String description = getArguments().getString("description", "");

        int upvotes = getArguments().getInt("upvotes", 0);
        int downvotes = getArguments().getInt("downvotes", 0);

        numOfUpvotes = view.findViewById(R.id.numOfUpvvotes);
        numOfDownvotes = view.findViewById(R.id.numOfDownvotes);

        // Load initial upvotes and downvotes values
        numOfUpvotes.setText(String.valueOf(upvotes));
        numOfDownvotes.setText(String.valueOf(downvotes));

        // Load and display the image using an ImageView
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(imageUrl).into(imageView);


        flag = view.findViewById(R.id.flag);
        checkFlagInFirestore(imageUrl);


        upvote = view.findViewById(R.id.upvote);
        checkupvoted(imageUrl);

        downvote=view.findViewById(R.id.downvote);
        checkDownvoted(imageUrl);
        // Set up the description overlay
        setUpDescriptionOverlay(view, description);
        shareButton = view.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNote();
            }
        });
        editButton=view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPhotoEditor(imageUrl);
            }
        });


        return view;
    }
    private void openPhotoEditor(String imageUrl) {
        Log.d("YourTag", "Downloading image from URL: " + imageUrl);

        // Use Glide to download the image locally
        Glide.with(requireContext())
                .asFile()
                .load(imageUrl)
                .into(new CustomTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        // Once the image is downloaded, open it for editing
                        Log.d("YourTag", "Image downloaded successfully. File path: " + resource.getAbsolutePath());
                        openImageForEditing(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup if needed
                        Log.d("YourTag", "Image load cleared. Placeholder: " + placeholder);
                    }
                });
    }

    private void openImageForEditing(File imageFile) {
        // Use FileProvider to get a content URI for the image file
        Uri imageUri = FileProvider.getUriForFile(requireContext(),
                "com.example.NoteSquad_TestApp.fileprovider", // Replace with your FileProvider authority
                imageFile);

        // Create an intent to open the image for editing
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        editIntent.setDataAndType(imageUri, "image/*");
        editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Verify that the intent can be resolved to avoid crashes
        if (editIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(Intent.createChooser(editIntent, "Open with"));
        } else {
            // Handle the case where no app is available to handle the edit action
            Toast.makeText(requireContext(), "No app available to edit photos", Toast.LENGTH_SHORT).show();
        }
    }
    private void shareNote() {
        // Retrieve image URL and description from arguments
        String imageUrl = getArguments().getString("imageUrl", "");
        String description = getArguments().getString("description", "");

        // Create a share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this note!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Note Description: " + description + "\nImage URL: " + imageUrl);

        // Start the share activity
        startActivity(Intent.createChooser(shareIntent, "Share Note"));
    }
    private void setUpDescriptionOverlay(View view, String description) {
        RelativeLayout descriptionOverlay = view.findViewById(R.id.descriptionOverlay);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);

        // Set initial visibility
        descriptionOverlay.setVisibility(View.VISIBLE);

        // Set description text
        descriptionTextView.setText(description);

        // Set click listener for the image to toggle description visibility
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of description overlay
                if (descriptionOverlay.getVisibility() == View.VISIBLE) {
                    descriptionOverlay.setVisibility(View.GONE);
                } else {
                    descriptionOverlay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateFlagInFirestore(String imageUrl) {
        // Get the reference to the Firestore collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String postId = document.getId();

                        // Update the flag for the found document
                        firestore.collection("notes").document(postId)
                                .update("isFlagged", true)
                                .addOnSuccessListener(aVoid -> {
                                    // Handle success
                                    Toast.makeText(requireContext(), "Note flagged successfully", Toast.LENGTH_SHORT).show();

                                    // Update the UI immediately
                                    updateUIForFlagged(true,imageUrl); // Pass true for flagged state
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(requireContext(), "Failed to flag the note", Toast.LENGTH_SHORT).show();
                                });

                        // Assuming you want to update only one note, you can break the loop here
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUIForFlagged(boolean isFlagged,String imageUrl) {
        // Update the UI immediately when the flag is changed
        if (isFlagged) {
            flag.setImageResource(R.drawable.baseline_flag_24);
            flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(requireContext(), "You already flagged the note", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            flag.setImageResource(R.drawable.baseline_outlined_flag_24);
            flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update the flag in Firestore
                    updateFlagInFirestore(imageUrl);
                }
            });
        }
    }

    private void updateUpvoteInFirestore(String imageUrl) {
        String userEmail = getEmail();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String postId = document.getId();

                        // Update the upvotedBy array for the found document
                        firestore.collection("notes").document(postId)
                                .update(
                                        "upvotedBy", FieldValue.arrayUnion(userEmail),
                                        "upvotes", FieldValue.increment(1)
                                )
                                .addOnSuccessListener(aVoid -> {
                                    // Handle success
                                    Toast.makeText(requireContext(), "Upvoted successfully", Toast.LENGTH_SHORT).show();
                                    fetchAndUpdateUpvotesDownvotes(imageUrl);
                                    updateUIForUpvoted(true,imageUrl);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(requireContext(), "Failed to upvote the note", Toast.LENGTH_SHORT).show();
                                });

                        // Assuming you want to update only one note, you can break the loop here
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUIForUpvoted(boolean isUpvoted,String imageUrl) {
        // Update the UI immediately when the upvote status is changed
        if (isUpvoted) {
            upvote.setImageResource(R.drawable.baseline_thumb_up_alt_24);
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update the upvote in Firestore
                    removeUpvoteInFirestore(imageUrl);
                }
            });
        } else {
            upvote.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update the upvote in Firestore
                    updateUpvoteInFirestore(imageUrl);
                }
            });
        }
    }

    private void checkFlagInFirestore(String imageUrl) {
        // Get the reference to the Firestore collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Retrieve the value of isFlagged
                        boolean isFlagged = document.getBoolean("isFlagged");
                        if (isFlagged) {
                            flag.setImageResource(R.drawable.baseline_flag_24);
                            flag.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(requireContext(), "You already flagged the note", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            flag.setImageResource(R.drawable.baseline_outlined_flag_24);
                            flag.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateFlagInFirestore(imageUrl);
                                    flag.setImageResource(R.drawable.baseline_flag_24);

                                }
                            });
                        }

                        // Assuming you want to handle only one note, you can break the loop here
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkupvoted(String imageUrl) {
        // Set up initial click listeners
        setUpvoteClickListener(imageUrl);

        // Get the reference to the Firestore collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Retrieve the value of isFlagged
                        List<String> upvotedBy = (List<String>) document.get("upvotedBy");

                        // Update click listeners based on the retrieved information
                        updateClickListenersBasedOnUpvoteCondition(upvotedBy, imageUrl);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void setUpvoteClickListener(String imageUrl) {
        upvote.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUpvoteInFirestore(imageUrl);

            }
        });
    }

    private void updateClickListenersBasedOnUpvoteCondition(List<String> upvotedBy, String imageUrl) {
        if (upvotedBy != null && upvotedBy.contains(getEmail())) {
            upvote.setImageResource(R.drawable.baseline_thumb_up_alt_24);
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeUpvoteInFirestore(imageUrl);

                }
            });
        }
    }


    public String getEmail() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            return account.getEmail();
        }

        return null;
    }


    private void removeUpvoteInFirestore(String imageUrl) {
        String userEmail = getEmail();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String postId = document.getId();

                        // Update the upvotedBy array for the found document
                        firestore.collection("notes").document(postId)
                                .update("upvotedBy", FieldValue.arrayRemove(userEmail),
                                        "upvotes", FieldValue.increment(-1)
                                )

                                .addOnSuccessListener(aVoid -> {
                                    // Handle success
                                    Toast.makeText(requireContext(), "Remove Upvoted successfully", Toast.LENGTH_SHORT).show();

                                    // Update the UI immediately
                                    updateUIForUpvoted(false,imageUrl); // Pass false for not upvoted state
                                    fetchAndUpdateUpvotesDownvotes(imageUrl);

                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(requireContext(), "Failed to remove upvote from the note", Toast.LENGTH_SHORT).show();
                                });

                        // Assuming you want to update only one note, you can break the loop here
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkDownvoted(String imageUrl) {
        // Set up initial click listeners
        setDownvoteClickListener(imageUrl);

        // Get the reference to the Firestore collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Retrieve the value of downvotedBy
                        List<String> downvotedBy = (List<String>) document.get("downvotedBy");

                        // Update click listeners based on the retrieved information
                        updateClickListenersBasedOnDownvoteCondition(downvotedBy, imageUrl);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void setDownvoteClickListener(String imageUrl) {
        downvote.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDownvoteInFirestore(imageUrl);
            }
        });
    }

    private void updateClickListenersBasedOnDownvoteCondition(List<String> downvotedBy, String imageUrl) {
        if (downvotedBy != null && downvotedBy.contains(getEmail())) {
            downvote.setImageResource(R.drawable.baseline_thumb_down_alt_24);
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeDownvoteInFirestore(imageUrl);
                }
            });
        }
    }

    private void updateDownvoteInFirestore(String imageUrl) {
        String userEmail = getEmail();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String postId = document.getId();

                        // Update the downvotedBy array for the found document
                        firestore.collection("notes").document(postId)
                                .update("downvotedBy", FieldValue.arrayUnion(userEmail),
                                        "downvotes", FieldValue.increment(1)
                                )
                                .addOnSuccessListener(aVoid -> {
                                    // Handle success
                                    Toast.makeText(requireContext(), "Downvoted successfully", Toast.LENGTH_SHORT).show();
                                    updateUIForDownvoted(true,imageUrl);
                                   fetchAndUpdateUpvotesDownvotes(imageUrl);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(requireContext(), "Failed to downvote the note", Toast.LENGTH_SHORT).show();
                                });

                        // Assuming you want to update only one note, you can break the loop here
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void removeDownvoteInFirestore(String imageUrl) {
        String userEmail = getEmail();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String postId = document.getId();

                        // Update the downvotedBy array for the found document
                        firestore.collection("notes").document(postId)
                                .update("downvotedBy", FieldValue.arrayRemove(userEmail),
                                        "downvotes", FieldValue.increment(-1)
                                )
                                .addOnSuccessListener(aVoid -> {
                                    // Handle success
                                    Toast.makeText(requireContext(), "Remove Downvoted successfully", Toast.LENGTH_SHORT).show();
                                    updateUIForDownvoted(false, imageUrl); // Pass false for not downvoted state
                                    fetchAndUpdateUpvotesDownvotes(imageUrl);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(requireContext(), "Failed to remove downvote from the note", Toast.LENGTH_SHORT).show();
                                });

                        // Assuming you want to update only one note, you can break the loop here
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUIForDownvoted(boolean isDownvoted, String imageUrl) {
        // Update the UI immediately when the downvote status is changed
        if (isDownvoted) {
            downvote.setImageResource(R.drawable.baseline_thumb_down_alt_24);
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update the downvote in Firestore
                    removeDownvoteInFirestore(imageUrl);
                }
            });
        } else {
            downvote.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update the downvote in Firestore
                    updateDownvoteInFirestore(imageUrl);
                }
            });
        }
    }
    private void fetchAndUpdateUpvotesDownvotes(String imageUrl) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("notes")
                .whereEqualTo("imageUrl", imageUrl)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Fetch the latest values of upvotes and downvotes
                        int upvotes = document.getLong("upvotes").intValue();
                        int downvotes = document.getLong("downvotes").intValue();

                        // Update the UI with the latest values
                        numOfUpvotes.setText(String.valueOf(upvotes));
                        numOfDownvotes.setText(String.valueOf(downvotes));

                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to retrieve note information", Toast.LENGTH_SHORT).show();
                });
    }

    private void refreshFragment() {
        NotesDisplayFragment refreshedFragment = NotesDisplayFragment.newInstance(
                getArguments().getString("imageUrl", ""),
                getArguments().getString("description", ""),
                getArguments().getBoolean("isFlagged", false),
                getArguments().getInt("upvotes", 0),
                getArguments().getInt("downvotes", 0)
        );

        getParentFragmentManager().beginTransaction()
                .replace(R.id.constraintLayoutFragment, refreshedFragment)
                .addToBackStack(null)
                .commit();
    }
}





