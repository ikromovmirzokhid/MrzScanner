package com.hellsayenci.mrzscanner.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.hellsayenci.mrzscanner.R;


public class EmptyFragment extends Fragment {

    private NavController navController;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();


        return inflater.inflate(R.layout.empty_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        authStateListener = firebaseAuth -> {
            if (auth.getCurrentUser() == null) {
                if (navController.getCurrentDestination().getId() == R.id.emptyFragment)
                    navController.navigate(R.id.action_emptyFragment_to_loginPageFragment);
            } else {
                if (navController.getCurrentDestination().getId() == R.id.emptyFragment)
                    navController.navigate(R.id.action_emptyFragment_to_mainPageFragment);
            }
        };

        auth.addAuthStateListener(authStateListener);


    }
}
