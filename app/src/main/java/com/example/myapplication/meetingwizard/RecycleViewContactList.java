package com.example.myapplication.meetingwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class RecycleViewContactList extends RecyclerView.Adapter<RecycleViewContactList.ViewHolder>{

    // Content
    private final ArrayList<Contact> mContact;
    private final ArrayList<Contact> mContactCopy = new ArrayList<>();
    private final Context mContext;
    contactListener listener;

    public RecycleViewContactList(ArrayList<Contact> mContact, contactListener listener, Context mContext) {
        this.mContact = mContact;
        this.mContext = mContext;
        this.listener = listener;
        mContactCopy.addAll(mContact);
    }

    public interface contactListener{
            void onContactSelected(String name);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Contact contact = mContact.get(holder.getAdapterPosition());
            holder.contactName.setText(contact.getName());

            // Set on click listener on the container
            holder.contactContainer.setOnClickListener(containerView -> {
                listener.onContactSelected(contact.getName());
            });
            }

    @Override
    public int getItemCount() {
            return mContact.size();
            }

    // Filter the Contacts
    // https://stackoverflow.com/a/37562572
    public void filter(String text) {
        mContact.clear();
        if (text.isEmpty()) {
            mContact.addAll(mContactCopy);
        } else {
            text = text.toLowerCase();
            for (Contact item : mContactCopy) {
                if (item.getName().toLowerCase().contains(text)) {
                    mContact.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_contact_item,
            parent, false);
            return new ViewHolder(view);
            }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        RelativeLayout contactContainer;

        public ViewHolder(View view){
            super(view);
            contactName = view.findViewById(R.id.contact_Name);
            contactContainer = view.findViewById(R.id.contact_container);
        }
    }
}
