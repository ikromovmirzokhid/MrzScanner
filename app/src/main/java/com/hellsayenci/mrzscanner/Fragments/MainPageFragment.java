package com.hellsayenci.mrzscanner.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.pojos.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainPageFragment extends Fragment {

    private NavController navController;
    private FirebaseAuth auth;
    private String currentUserId;
    private ActionBarDrawerToggle mToggle;
    private View header;
    private CircleImageView profileImg;
    private TextView fullName, jobPlacement;
    private FirebaseFirestore firebaseDB;
    private User user;

    @BindView(R.id.nView)
    NavigationView nView;

    @BindView(R.id.main_drawer_layout)
    DrawerLayout dLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scanBtn)
    Button scanBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        return inflater.inflate(R.layout.main_page_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        ((MainActivity) getActivity()).setToolbar(toolbar);

        setNavigationView();

        firebaseDB.collection("users").document(currentUserId).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (documentSnapshot != null) {
                    user = new User(documentSnapshot.getString("name"),
                            documentSnapshot.getString("surname"),
                            documentSnapshot.getString("jobPlace"),
                            documentSnapshot.getString("position"),
                            documentSnapshot.getString("phoneNum"),
                            documentSnapshot.getString("photo"));

                    if (user != null) {
                        if (getActivity() != null)
                            Glide.with(getActivity()).load(user.getPhoto()).into(profileImg);
                        fullName.setText(user.getName() + " " + user.getSurname());
                        jobPlacement.setText(user.getJobPosition() + " at " + user.getJobPlace());
                    }
                } else
                    Toast.makeText(getActivity(), "documentSnapshot is null", Toast.LENGTH_SHORT).show();
            }

        });

        navController = Navigation.findNavController(view);

        scanBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_mainPageFragment_to_cameraFragment);
        });

        nView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home) {
                    dLayout.closeDrawers();
                    if (navController.getCurrentDestination().getId() == R.id.blanksFragment2 || navController.getCurrentDestination().getId() == R.id.settingsFragment) {
                        navController.popBackStack();
                    }
                } else if (menuItem.getItemId() == R.id.newBlank) {
                    dLayout.closeDrawers();
                    Snackbar.make(view, "Comming Soon", Snackbar.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.blanks) {
                    dLayout.closeDrawers();
                    if (navController.getCurrentDestination().getId() == R.id.mainPageFragment) {
                        navController.navigate(R.id.action_mainPageFragment_to_blanksFragment2);
                    } else if (navController.getCurrentDestination().getId() == R.id.settingsFragment) {
                        navController.navigate(R.id.action_settingsFragment_to_blanksFragment2);
                    }
                } else if (menuItem.getItemId() == R.id.settings) {
                    dLayout.closeDrawers();
                    if (navController.getCurrentDestination().getId() == R.id.mainPageFragment) {
                        String jsonUser = new Gson().toJson(user);
                        Bundle bundle = new Bundle();
                        bundle.putString("user", jsonUser);
                        navController.navigate(R.id.action_mainPageFragment_to_settingsFragment, bundle);
                    } else if (navController.getCurrentDestination().getId() == R.id.settingsFragment) {
                        navController.navigate(R.id.action_blanksFragment2_to_settingsFragment);
                    }
                } else if (menuItem.getItemId() == R.id.logOut) {
                    dLayout.closeDrawers();
                    navController.navigate(R.id.action_mainPageFragment_to_loginPageFragment2);
                    auth.signOut();
                }
                menuItem.setChecked(false);
                nView.getMenu().getItem(0).setChecked(true);
                return true;
            }
        });


    }

    private void setNavigationView() {
        nView.getMenu().getItem(0).setChecked(true);
        header = nView.getHeaderView(0);
        setHeaderView(header);

        mToggle = new ActionBarDrawerToggle(getActivity(), dLayout, R.string.open, R.string.close);
        dLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setHeaderView(View header) {
        profileImg = header.findViewById(R.id.profileImage);
        fullName = header.findViewById(R.id.fullName);
        jobPlacement = header.findViewById(R.id.jobPlacement);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
