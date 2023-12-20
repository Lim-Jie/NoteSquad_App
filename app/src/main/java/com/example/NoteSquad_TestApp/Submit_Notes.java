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
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private EditText subjectSubmit;
    private EditText noteTitleSubmit;
    private Button uploadButton;
   private Uri selectedImageUri;
   private  Bitmap imageBitmap;


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

    private void dispatchSelectFromGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startForResult.launch(intent);
    }
    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null && result.getData().getData() != null) {
                                // Image from gallery
                                selectedImageUri = result.getData().getData();
                                imageBitmap=null;
                                displaySelectedImage(selectedImageUri);
                            } else {
                                // Image from camera
                                Bundle extras = result.getData().getExtras();
                                if (extras != null) {
                                  imageBitmap = (Bitmap) extras.get("data");
                                  selectedImageUri=null;
                                    displayCapturedImage(imageBitmap);
                                }
                            }
                        }
                    });





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
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userEmail = currentUser.getEmail();

            } else {
                Toast.makeText(requireContext(), "No User", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String subjectTitle = subjectSubmit.getText().toString();
        String noteTitle = noteTitleSubmit.getText().toString();
        String noteDescription = descptSubmit.getText().toString();
        boolean isPublic = publicSwitch.isChecked();


        if (TextUtils.isEmpty(subjectTitle) || TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription)) {
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
                //noteData.put("userEmail", userEmail);
                noteData.put("subjectTitle", subjectTitle);
                noteData.put("noteTitle", noteTitle);
                noteData.put("noteDescription", noteDescription);
                noteData.put("isPublic", isPublic);
                noteData.put("imageUrl", imageUrl);

                // Programmatically create a new document in the "notes" collection
                firestore.collection("notes").document(noteId)
                        .set(noteData)
                        .addOnSuccessListener(aVoid -> {
                            // Note details stored successfully
                            Toast.makeText(requireContext(), "Note uploaded successfully", Toast.LENGTH_SHORT).show();
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
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String subjectTitle = subjectSubmit.getText().toString();  // Use the entered subject title
        String noteTitle = noteTitleSubmit.getText().toString();   // Use the entered note title
        String noteDescription = descptSubmit.getText().toString(); // Use the entered note description
        boolean isPublic = publicSwitch.isChecked();




        // Check if EditText fields are filled
        if (TextUtils.isEmpty(subjectTitle) || TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription)) {
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
                noteData.put("subjectTitle", subjectTitle);
                noteData.put("noteTitle", noteTitle);
                noteData.put("noteDescription", noteDescription);
                noteData.put("isPublic", isPublic);
                noteData.put("imageUrl", imageUrl);

                // Programmatically create a new document in the "notes" collection
                firestore.collection("notes").document(noteId)
                        .set(noteData)
                        .addOnSuccessListener(aVoid -> {
                            // Note details stored successfully
                            Toast.makeText(requireContext(), "Note uploaded successfully", Toast.LENGTH_SHORT).show();
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

}
