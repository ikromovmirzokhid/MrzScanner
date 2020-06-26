package com.hellsayenci.mrzscanner.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.Adapters.BlanksAdapter;
import com.hellsayenci.mrzscanner.Databases.Database;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.pojos.BlankForm;
import com.hellsayenci.mrzscanner.pojos.MrzResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BlanksFragment extends Fragment implements BlanksAdapter.BlankClickListener {

    private NavController navController;
    private BlanksAdapter adapter;
    private Database db;
    private List<BlankForm> blaknList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.blankList)
    RecyclerView blankListView;
    @BindView(R.id.backArrow)
    ImageView backArrow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = Database.getDB();
        blaknList = db.getBankForms();

        return inflater.inflate(R.layout.blank_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

       // ((MainActivity) getActivity()).setToolbar(toolbar);

        navController = Navigation.findNavController(view);

        adapter = new BlanksAdapter(blaknList);
        adapter.setListener(this);
        blankListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        blankListView.setAdapter(adapter);

        backArrow.setOnClickListener(v->{
            navController.popBackStack();
        });


    }

    @Override
    public void setBlankClickListener(String blankName, String relationName) {
        Bundle bundle = new Bundle();
        bundle.putString("blankName", blankName);
        bundle.putString("relationName", relationName);
        navController.navigate(R.id.action_blanksFragment2_to_formsFragment, bundle);
    }
}
