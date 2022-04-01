package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.data.ParticipantData;

public class DialogUserInfoViewCreator {

    public static View createView(Context context, ParticipantData p, boolean editButtonVisible) {
        View alertDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_user_info, null);

        // Probably can be simplified with Databinding
        TextView tvName, tvEmail, tvStatus;
        ImageButton btnEdit;
        ConstraintLayout emailContainer;

        tvName = alertDialogView.findViewById(R.id.dialog_user_info_topbar_text);
        btnEdit = alertDialogView.findViewById(R.id.dialog_user_info_topbar_edit_button);
        tvEmail = alertDialogView.findViewById(R.id.dialog_user_info_content_email_text);
        tvStatus = alertDialogView.findViewById(R.id.dialog_user_info_content_status_text);
        emailContainer = alertDialogView.findViewById(R.id.dialog_user_info_content_email);
        tvName.setText(p.getName());
        if (!editButtonVisible) {
            btnEdit.setVisibility(View.INVISIBLE);
        }
        tvEmail.setText(p.getEmail());
        tvStatus.setText(p.getStatus());
        // Open Mail App
        emailContainer.setOnClickListener(v -> {
            try {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{p.getEmail()});
                context.startActivity(Intent.createChooser(intent, "Sende Mail..."));
            } catch (Exception e) {
                Toast.makeText(context,"Kein unterstützter Email-Client gefunden...",Toast.LENGTH_SHORT).show();
            }
        });
        return alertDialogView;
    }
}
