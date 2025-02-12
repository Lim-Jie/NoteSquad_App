// Generated by view binder compiler. Do not edit!
package com.example.NoteSquad_TestApp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.NoteSquad_TestApp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentNotesDisplayBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final RelativeLayout descriptionOverlay;

  @NonNull
  public final TextView descriptionTextView;

  @NonNull
  public final ImageView downvote;

  @NonNull
  public final ImageView editButton;

  @NonNull
  public final ImageView flag;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final TextView numOfDownvotes;

  @NonNull
  public final TextView numOfUpvvotes;

  @NonNull
  public final ImageView shareButton;

  @NonNull
  public final ImageView upvote;

  private FragmentNotesDisplayBinding(@NonNull FrameLayout rootView,
      @NonNull RelativeLayout descriptionOverlay, @NonNull TextView descriptionTextView,
      @NonNull ImageView downvote, @NonNull ImageView editButton, @NonNull ImageView flag,
      @NonNull ImageView imageView, @NonNull TextView numOfDownvotes,
      @NonNull TextView numOfUpvvotes, @NonNull ImageView shareButton, @NonNull ImageView upvote) {
    this.rootView = rootView;
    this.descriptionOverlay = descriptionOverlay;
    this.descriptionTextView = descriptionTextView;
    this.downvote = downvote;
    this.editButton = editButton;
    this.flag = flag;
    this.imageView = imageView;
    this.numOfDownvotes = numOfDownvotes;
    this.numOfUpvvotes = numOfUpvvotes;
    this.shareButton = shareButton;
    this.upvote = upvote;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentNotesDisplayBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentNotesDisplayBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_notes_display, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentNotesDisplayBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.descriptionOverlay;
      RelativeLayout descriptionOverlay = ViewBindings.findChildViewById(rootView, id);
      if (descriptionOverlay == null) {
        break missingId;
      }

      id = R.id.descriptionTextView;
      TextView descriptionTextView = ViewBindings.findChildViewById(rootView, id);
      if (descriptionTextView == null) {
        break missingId;
      }

      id = R.id.downvote;
      ImageView downvote = ViewBindings.findChildViewById(rootView, id);
      if (downvote == null) {
        break missingId;
      }

      id = R.id.editButton;
      ImageView editButton = ViewBindings.findChildViewById(rootView, id);
      if (editButton == null) {
        break missingId;
      }

      id = R.id.flag;
      ImageView flag = ViewBindings.findChildViewById(rootView, id);
      if (flag == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.numOfDownvotes;
      TextView numOfDownvotes = ViewBindings.findChildViewById(rootView, id);
      if (numOfDownvotes == null) {
        break missingId;
      }

      id = R.id.numOfUpvvotes;
      TextView numOfUpvvotes = ViewBindings.findChildViewById(rootView, id);
      if (numOfUpvvotes == null) {
        break missingId;
      }

      id = R.id.shareButton;
      ImageView shareButton = ViewBindings.findChildViewById(rootView, id);
      if (shareButton == null) {
        break missingId;
      }

      id = R.id.upvote;
      ImageView upvote = ViewBindings.findChildViewById(rootView, id);
      if (upvote == null) {
        break missingId;
      }

      return new FragmentNotesDisplayBinding((FrameLayout) rootView, descriptionOverlay,
          descriptionTextView, downvote, editButton, flag, imageView, numOfDownvotes, numOfUpvvotes,
          shareButton, upvote);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
