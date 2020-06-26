package com.hellsayenci.mrzscanner.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.Adapters.BlankFormsAdapter;
import com.hellsayenci.mrzscanner.Databases.Database;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.pojos.BankClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class FormsFragment extends Fragment {

    private NavController navController;
    private String blankName, relationName;
    private Database db;
    private List<BankClient> clients;
    private BlankFormsAdapter adapter;
    private Bundle bundle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.formList)
    RecyclerView formListView;
    @BindView(R.id.binocularIcon)
    CircleImageView binocularIcon;
    @BindView(R.id.noMessageTitle)
    TextView noMessageTitle;
    @BindView(R.id.noDataMessage)
    TextView noDataMessage;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.toolbarText)
    TextView toolbartext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        blankName = getArguments().getString("blankName");
        relationName = getArguments().getString("relationName");

        db = Database.getDB();
        clients = db.getBankClients(relationName);

        bundle = new Bundle();

        return inflater.inflate(R.layout.forms_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        navController = Navigation.findNavController(view);

        //((MainActivity) getActivity()).setToolbar(toolbar);

        // toolbar.setTitle(blankName);

        toolbartext.setText(blankName);

        adapter = new BlankFormsAdapter(clients);

//        if (clients.size() == 0 || clients.get(0)==null) {
//            clientsNotFound(true);
//        } else
//            clientsNotFound(false);

        if (clients.size() == 0) {
            clientsNotFound(true);
        } else
            clientsNotFound(false);


        add.setOnClickListener(v -> navController.navigate(R.id.action_formsFragment_to_mainPageFragment));

        adapter.setListener(client -> {
            String finalResult = new Gson().toJson(client);
            bundle.putString("finalResult", finalResult);
            bundle.putString("blankType", relationName);
            bundle.putString("blankTypeText", blankName);
            navController.navigate(R.id.action_formsFragment_to_personInfosFragment, bundle);
        });

        backArrow.setOnClickListener(v -> {
            navController.popBackStack();
        });

    }

    private void clientsNotFound(boolean b) {
        if (b) {
            formListView.setVisibility(View.GONE);
            binocularIcon.setVisibility(View.VISIBLE);
            noDataMessage.setVisibility(View.VISIBLE);
            noMessageTitle.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
        } else {
            formListView.setVisibility(View.VISIBLE);
            binocularIcon.setVisibility(View.GONE);
            noDataMessage.setVisibility(View.GONE);
            noMessageTitle.setVisibility(View.GONE);
            add.setVisibility(View.GONE);

            formListView.setLayoutManager(new LinearLayoutManager(getActivity()));
            formListView.setAdapter(adapter);
        }
    }


}
