package com.hellsayenci.mrzscanner.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginPageFragment extends Fragment {

    private FirebaseAuth auth;
    private NavController navController;
    private ProgressDialog dialog;
    @BindView(R.id.signIn)
    Button logInBtn;
    @BindView(R.id.passwordEditText)
    TextInputEditText passwordEditText;
    @BindView(R.id.loginEditText)
    TextInputEditText loginEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();

        return inflater.inflate(R.layout.login_page_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        navController = Navigation.findNavController(view);

        dialog = new ProgressDialog(getActivity());


        logInBtn.setOnClickListener(v -> {
            dialog.setTitle("Logging in, Please wait!");
            dialog.show();
            if (loginEditText.getText().toString().equals("")) {
                loginEditText.setError("Fill login field");
            } else {
                if (passwordEditText.getText().toString().equals(""))
                    passwordEditText.setError("Fill password field");
                else
                    auth.signInWithEmailAndPassword(loginEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.cancel();
                                if (navController.getCurrentDestination().getId() == R.id.loginPageFragment) {
                                    navController.navigate(R.id.action_loginPageFragment_to_mainPageFragment);
                                    Snackbar.make(view, "User Logged In", Snackbar.LENGTH_SHORT).show();
                                }
                            } else{
                                dialog.cancel();
                                Snackbar.make(view, "Incorrect email or password", Snackbar.LENGTH_SHORT).show();}
                        }
                    });
            }
        });

    }
}
