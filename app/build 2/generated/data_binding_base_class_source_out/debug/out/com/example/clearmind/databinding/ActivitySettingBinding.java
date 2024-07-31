// Generated by view binder compiler. Do not edit!
package com.example.clearmind.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.clearmind.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySettingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button changePswd;

  @NonNull
  public final Button deleteAccount;

  @NonNull
  public final Button logout;

  @NonNull
  public final TextView welcomeTitle;

  private ActivitySettingBinding(@NonNull ConstraintLayout rootView, @NonNull Button changePswd,
      @NonNull Button deleteAccount, @NonNull Button logout, @NonNull TextView welcomeTitle) {
    this.rootView = rootView;
    this.changePswd = changePswd;
    this.deleteAccount = deleteAccount;
    this.logout = logout;
    this.welcomeTitle = welcomeTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySettingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySettingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_setting, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySettingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.change_pswd;
      Button changePswd = ViewBindings.findChildViewById(rootView, id);
      if (changePswd == null) {
        break missingId;
      }

      id = R.id.delete_account;
      Button deleteAccount = ViewBindings.findChildViewById(rootView, id);
      if (deleteAccount == null) {
        break missingId;
      }

      id = R.id.logout;
      Button logout = ViewBindings.findChildViewById(rootView, id);
      if (logout == null) {
        break missingId;
      }

      id = R.id.welcome_title;
      TextView welcomeTitle = ViewBindings.findChildViewById(rootView, id);
      if (welcomeTitle == null) {
        break missingId;
      }

      return new ActivitySettingBinding((ConstraintLayout) rootView, changePswd, deleteAccount,
          logout, welcomeTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
