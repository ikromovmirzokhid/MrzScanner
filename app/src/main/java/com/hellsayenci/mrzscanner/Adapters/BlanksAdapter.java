package com.hellsayenci.mrzscanner.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.pojos.BlankForm;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlanksAdapter extends RecyclerView.Adapter<BlanksAdapter.ViewHolder> {
    private List<BlankForm> blankList;
    private BlankClickListener listener;

    public BlanksAdapter(List<BlankForm> blankList) {
        this.blankList = blankList;
    }

    public void setListener(BlankClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BlanksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlanksAdapter.ViewHolder holder, int position) {
        BlankForm blankForm = blankList.get(position);

        holder.blankTitle.setText(blankForm.getFormName());
        Glide.with(holder.itemView).load(blankForm.photoOfBlank).into(holder.blankImg);
        holder.itemView.setOnClickListener(v -> {
            listener.setBlankClickListener(blankForm.getFormName(), blankForm.getRelationName());
        });
    }

    @Override
    public int getItemCount() {
        return blankList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.blankImg)
        ImageView blankImg;
        @BindView(R.id.blankTitle)
        TextView blankTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface BlankClickListener {
        void setBlankClickListener(String blankName, String relationName);
    }
}
