// Generated by view binder compiler. Do not edit!
package com.example.NoteSquad_TestApp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.NoteSquad_TestApp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegistrationPageBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView AlreadyHaveAccountText;

  @NonNull
  public final Button GoBackToLoginPageButton;

  @NonNull
  public final EditText PasswordRegistration;

  @NonNull
  public final EditText confirmPassword;

  @NonNull
  public final EditText editTextTextEmailAddress;

  @NonNull
  public final Button registerButton;

  @NonNull
  public final TextView textView2;

  private ActivityRegistrationPageBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView AlreadyHaveAccountText, @NonNull Button GoBackToLoginPageButton,
      @NonNull EditText PasswordRegistration, @NonNull EditText confirmPassword,
      @NonNull EditText editTextTextEmailAddress, @NonNull Button registerButton,
      @NonNull TextView textView2) {
    this.rootView = rootView;
    this.AlreadyHaveAccountText = AlreadyHaveAccountText;
    this.GoBackToLoginPageButton = GoBackToLoginPageButton;
    this.PasswordRegistration = PasswordRegistration;
    this.confirmPassword = confirmPassword;
    this.editTextTextEmailAddress = editTextTextEmailAddress;
    this.registerButton = registerButton;
    this.textView2 = textView2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegistrationPageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegistrationPageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_registration_page, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegistrationPageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.AlreadyHaveAccountText;
      TextView AlreadyHaveAccountText = ViewBindings.findChildViewById(rootView, id);
      if (AlreadyHaveAccountText == null) {
        break missingId;
      }

      id = R.id.GoBackToLoginPageButton;
      Button GoBackToLoginPageButton = ViewBindings.findChildViewById(rootView, id);
      if (GoBackToLoginPageButton == null) {
        break missingId;
      }

      id = R.id.PasswordRegistration;
      EditText PasswordRegistration = ViewBindings.findChildViewById(rootView, id);
      if (PasswordRegistration == null) {
        break missingId;
      }

      id = R.id.confirmPassword;
      EditText confirmPassword = ViewBindings.findChildViewById(rootView, id);
      if (confirmPassword == null) {
        break missingId;
      }

      id = R.id.editTextTextEmailAddress;
      EditText editTextTextEmailAddress = ViewBindings.findChildViewById(rootView, id);
      if (editTextTextEmailAddress == null) {
        break missingId;
      }

      id = R.id.registerButton;
      Button registerButton = ViewBindings.findChildViewById(rootView, id);
      if (registerButton == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      return new ActivityRegistrationPageBinding((ConstraintLayout) rootView,
          AlreadyHaveAccountText, GoBackToLoginPageButton, PasswordRegistration, confirmPassword,
          editTextTextEmailAddress, registerButton, textView2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
