package com.example.myapplication.meetingwizard;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.BottomSheetContactSelectBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ParticipantImportContactBottomSheetAdapter extends BottomSheetDialogFragment{
    BottomSheetContactSelectBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    RecycleViewContactList recycleViewContactList;

    RecycleViewContactList.contactListener listener;

    public RecycleViewContactList.contactListener getListener() {
        return listener;
    }

    public void setListener(RecycleViewContactList.contactListener listener) {
        this.listener = listener;
    }

    // Participant List
    ArrayList<Contact> mContacts = new ArrayList<>();

    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // inflating Layout
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_contact_select, null);

        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // setting max height of bottom sheet
        //bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // skip it being collapsable
        bottomSheetBehavior.setSkipCollapsed(true);

        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent, null));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED ){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                setCancelable(slideOffset < MIN_SCROLL_FOR_CLOSURE);
            }
        });

        setCancelable(false);

        // cancel button clicked
        bi.buttonDismiss.setOnClickListener(viewListener -> dismiss());

        // Listen for Text input changes to filter locations
        bi.textInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recycleViewContactList.filter(charSequence.toString());
                if(charSequence == ""){
                    bi.clearTextButton.setVisibility(View.INVISIBLE);
                }
                else{
                    bi.clearTextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Listener to clear Text
        bi.clearTextButton.setOnClickListener(view1 -> bi.textInputSearch.setText(""));

        // check if contact are being allowed to open
        checkPermission();
        return bottomSheet;
    }

    // Build and fills the recycler view
    private void buildRecyclerView(){

        RecyclerView recyclerView = bi.contactsRecyclerView;
        recycleViewContactList = new RecycleViewContactList(
                mContacts,
                listener,
                this.getContext()
        );
        recyclerView.setAdapter(recycleViewContactList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    // Check if user gave permission
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            getContactList();
        }
            else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    // Gets the contact list
    private void getContactList(){
        // Init uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        // sort by asc
        String sort  = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, sort);

        // check if cursor is empty
        if(cursor.getCount() > 0){
            // iterate through the cursor
            Log.i("TAG", "getContactList: " + cursor.getCount());
            while(cursor.moveToNext()){
                // get values
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                        " =?";

                Cursor phoneCursor = getActivity().getApplicationContext().getContentResolver().query(
                        uriPhone, null, selection, new String[]{id}, null);

                if(phoneCursor.moveToNext()){
                    String number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));


                }

                // Maybe add number
                Log.i("TAG", "getContactList: " +mContacts);
                Contact contact = new Contact(name);
                mContacts.add(contact);
                phoneCursor.close();
            }
        }
        cursor.close();
        Log.i("TAG", "getContactList: " + mContacts);
        buildRecyclerView();
    }

    // https://stackoverflow.com/a/63546099
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getContactList();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    @Override
    public void onStart(){
        super.onStart();
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
