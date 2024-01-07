package com.example.NoteSquad_TestApp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Submit_Notes extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SELECT_IMAGE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private FirebaseFirestore firestore;
    private StorageReference storageRef;
    private ImageView photoImageView;
    private Switch publicSwitch;
    private EditText descptSubmit;
    private Spinner subjectSubmit;
    private EditText noteTitleSubmit;
    private Button uploadButton;
   private Uri selectedImageUri;
   private  Bitmap imageBitmap;
   private  String selectedSubject;
   private ImageView nails;
    private String[] subjects={"General","Mathematics","Physics","Biology","Computer Science","Economics","English"};
    private int icons[]={R.drawable.general,R.drawable.math,R.drawable.physics,R.drawable.biology,R.drawable.cs,R.drawable.economics,R.drawable.english};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        View view = inflater.inflate(R.layout.fragment_submit_notes, container, false);

        photoImageView = view.findViewById(R.id.photoImageView);
        publicSwitch = view.findViewById(R.id.switch1);
        descptSubmit=view.findViewById(R.id.descriptionSubmit);
        subjectSubmit=view.findViewById(R.id.subjectTitleSubmit);
        noteTitleSubmit=view.findViewById(R.id.noteTitleSubmit);
        uploadButton=view.findViewById(R.id.uploadButton);

        Button selectPhotoButton = view.findViewById(R.id.selectPhotoButton);
        nails=view.findViewById(R.id.nails);
        nails.setVisibility(View.INVISIBLE);



        CustomSubjectTitleAdapter customAdapter=new CustomSubjectTitleAdapter(getContext(),icons,subjects);
        subjectSubmit.setAdapter(customAdapter);
        subjectSubmit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedSubject = subjects[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();

            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImageUri!=null){
                    uploadNoteToFirebase(selectedImageUri);
                }else if(imageBitmap!=null){
                    uploadNoteToFirebase(imageBitmap);
                }else{
                    Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    private void showImagePickerDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                return;
            }
        }

        showOptionsDialog();
    }


    private void showOptionsDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                dispatchTakePictureIntent();

            } else if (options[item].equals("Choose from Gallery")) {
                dispatchSelectFromGalleryIntent();


            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startForResult.launch(takePictureIntent);

    }

    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getExtras() != null) {
                                // Get the captured image from the data
                                Bundle extras = data.getExtras();
                                imageBitmap = (Bitmap) extras.get("data");
                                selectedImageUri = null;
                                displayCapturedImage(imageBitmap);
                                nails.setVisibility(View.VISIBLE);
                            }
                        }
                    });
    private final ActivityResultLauncher<String> getContent =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    result -> {
                        if (result != null) {
                            selectedImageUri = result;
                            imageBitmap = null;
                            displaySelectedImage(selectedImageUri);
                            nails.setVisibility(View.VISIBLE);
                        }
                    });

    private void dispatchSelectFromGalleryIntent() {
        getContent.launch("image/*");

    }






    private void displaySelectedImage(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(photoImageView);
    }

    private void displayCapturedImage(Bitmap imageBitmap) {
        Glide.with(this)
                .load(imageBitmap)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(photoImageView);
    }
    private void uploadNoteToFirebase(Uri imageUri) {
        String userEmail = getEmail();

        String noteTitle = noteTitleSubmit.getText().toString();
        String noteDescription = descptSubmit.getText().toString();
        boolean isPublic = publicSwitch.isChecked();
        boolean isFlagged=false;
        int upvotes=0;
        int downvotes=0;


        if ( TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription)) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // Upload image to Firebase Storage
        String noteId = firestore.collection("notes").document().getId();
        String imagePath = "images/" + noteId + ".jpg";
        StorageReference imageRef = storageRef.child(imagePath);

        UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();

                // Store note details in Firestore
                Map<String, Object> noteData = new HashMap<>();
                noteData.put("userEmail", userEmail);
                noteData.put("subjectTitle", selectedSubject);
                noteData.put("noteTitle", noteTitle);
                noteData.put("noteDescription", noteDescription);
                noteData.put("isPublic", isPublic);
                noteData.put("imageUrl", imageUrl);
                noteData.put("isFlagged", isFlagged);
               noteData.put("upvotes", upvotes);
                noteData.put("downvotes", downvotes);
                noteData.put("upvotedBy", Arrays.asList());
                noteData.put("downvotedBy", Arrays.asList());

                // Programmatically create a new document in the "notes" collection
                firestore.collection("notes").document(noteId)
                        .set(noteData)
                        .addOnSuccessListener(aVoid -> {
                            // Note details stored successfully

                            Toast.makeText(requireContext(), "Note uploaded successfully", Toast.LENGTH_SHORT).show();
                            incrementContributions();
                            new Handler().postDelayed(() -> loadNoteShareFragment(), 1000);
                        })
                        .addOnFailureListener(e -> {
                            // Handle note details storage failure
                            Toast.makeText(requireContext(), "Failed to upload note details", Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            // Handle image upload failure
            Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadNoteToFirebase(Bitmap imageBitmap) {
        String userEmail = getEmail();

        String noteTitle = noteTitleSubmit.getText().toString();   // Use the entered note title
        String noteDescription = descptSubmit.getText().toString(); // Use the entered note description
        boolean isPublic = publicSwitch.isChecked();
        boolean isFlagged=false;
        int upvotes=0;
        int downvotes=0;



        // Check if EditText fields are filled
        if ( TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription) ) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image to Firebase Storage
        String noteId = firestore.collection("notes").document().getId();
        String imagePath = "images/" + noteId + ".jpg";
        StorageReference imageRef = storageRef.child(imagePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();

                // Store note details in Firestore
                Map<String, Object> noteData = new HashMap<>();
                noteData.put("userEmail", userEmail);
                noteData.put("subjectTitle", selectedSubject);
                noteData.put("noteTitle", noteTitle);
                noteData.put("noteDescription", noteDescription);
                noteData.put("isPublic", isPublic);
                noteData.put("imageUrl", imageUrl);
                noteData.put("isFlagged", isFlagged);
                noteData.put("upvotes", upvotes);
                noteData.put("downvotes", downvotes);
                noteData.put("upvotedBy", Arrays.asList());
                noteData.put("downvotedBy", Arrays.asList());
                // Programmatically create a new document in the "notes" collection
                firestore.collection("notes").document(noteId)
                        .set(noteData)
                        .addOnSuccessListener(aVoid -> {
                            // Note details stored successfully
                            Toast.makeText(requireContext(), "Note uploaded successfully", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(() -> loadNoteShareFragment(), 1000);
                        })
                        .addOnFailureListener(e -> {
                            // Handle note details storage failure
                            Toast.makeText(requireContext(), "Failed to upload note details", Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            // Handle image upload failure
            Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            showOptionsDialog();
                        }
                    });
    public String getEmail(){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account!=null){
            return account.getEmail();}

        return null;
    }

    private void loadNoteShareFragment() {
        // Create an instance of the submit_notes fragment
        noteShareFragment NoteShareFragment = new noteShareFragment();

        // Use a FragmentTransaction to replace the current fragment with submit_notes
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.constraintLayoutFragment,NoteShareFragment)
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();
    }
    private void incrementContributions() {
        String userEmail = getEmail();
        if (userEmail != null) {
            // Get the reference to the user's document in the "Users" collection
            DocumentReference userRef = firestore.collection("Users").document(userEmail);

            // Update the "contributions" field by incrementing its current value
            userRef.update("contributions", FieldValue.increment(1))
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Contributions incremented"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error incrementing contributions", e));
        }
    }

}
