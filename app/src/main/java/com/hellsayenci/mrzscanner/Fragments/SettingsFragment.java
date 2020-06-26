package com.hellsayenci.mrzscanner.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.pojos.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsFragment extends Fragment {

    private FirebaseAuth auth;
    private NavController navController;
    private FirebaseFirestore fireabseDB;
    private String userId;
    private User user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;
    @BindView(R.id.fullName)
    EditText fullName;
    @BindView(R.id.jobPlace)
    EditText jobPlace;
    @BindView(R.id.position)
    EditText position;
    @BindView(R.id.phoneNum)
    EditText phoneNum;
    @BindView(R.id.editBtn)
    Button editBtn;
    @BindView(R.id.saveBtn)
    Button saveBtn;
    @BindView(R.id.backArrow)
    ImageView backArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        fireabseDB = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        String jsonUser = getArguments().getString("user");
        if (jsonUser != null) {
            user = new Gson().fromJson(jsonUser, User.class);
        }

        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        //  ((MainActivity) getActivity()).setToolbar(toolbar);

        navController = Navigation.findNavController(view);

        setViews(user);

        editBtn.setOnClickListener(v -> {

            setEditable(true);
            saveBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.GONE);
        });

        saveBtn.setOnClickListener(v -> {
            setEditable(false);
            updateData(view);
            saveBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
        });

        backArrow.setOnClickListener(v -> {
            navController.popBackStack();
        });
    }

    private void updateData(View view) {
        DocumentReference contract = fireabseDB.collection("users").document(userId);
        contract.update("jobPlace", jobPlace.getText().toString());
        contract.update("position", position.getText().toString());
        contract.update("phoneNum", phoneNum.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Snackbar.make(view, "Updated Successfully", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(view, "Sorry, Something went wrong!", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setViews(User user) {
        Glide.with(getActivity()).load(user.getPhoto()).into(profileImage);
        jobPlace.setText(user.getJobPlace());
        position.setText(user.getJobPosition());
        phoneNum.setText(user.getPhoneNum());
        fullName.setText(user.getName() + " " + user.getSurname());
    }

    private void setEditable(boolean isEditable) {
        if (isEditable) {
            jobPlace.setFocusable(true);
            jobPlace.setFocusableInTouchMode(true);
            position.setFocusable(true);
            position.setFocusableInTouchMode(true);
            phoneNum.setFocusable(true);
            phoneNum.setFocusableInTouchMode(true);
        } else {
            jobPlace.setFocusable(false);
            position.setFocusable(false);
            phoneNum.setFocusable(false);
        }
    }

}
