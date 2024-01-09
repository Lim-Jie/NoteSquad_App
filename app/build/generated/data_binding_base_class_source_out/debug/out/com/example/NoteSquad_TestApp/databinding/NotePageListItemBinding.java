// Generated by view binder compiler. Do not edit!
package com.example.NoteSquad_TestApp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.NoteSquad_TestApp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NotePageListItemBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout notesPageParent;

  @NonNull
  public final RecyclerView notesRecView;

  @NonNull
  public final TextView subjectTitle;

  private NotePageListItemBinding(@NonNull RelativeLayout rootView,
      @NonNull RelativeLayout notesPageParent, @NonNull RecyclerView notesRecView,
      @NonNull TextView subjectTitle) {
    this.rootView = rootView;
    this.notesPageParent = notesPageParent;
    this.notesRecView = notesRecView;
    this.subjectTitle = subjectTitle;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NotePageListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NotePageListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.note_page_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NotePageListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      RelativeLayout notesPageParent = (RelativeLayout) rootView;

      id = R.id.notesRecView;
      RecyclerView notesRecView = ViewBindings.findChildViewById(rootView, id);
      if (notesRecView == null) {
        break missingId;
      }

      id = R.id.subjectTitle;
      TextView subjectTitle = ViewBindings.findChildViewById(rootView, id);
      if (subjectTitle == null) {
        break missingId;
      }

      return new NotePageListItemBinding((RelativeLayout) rootView, notesPageParent, notesRecView,
          subjectTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}