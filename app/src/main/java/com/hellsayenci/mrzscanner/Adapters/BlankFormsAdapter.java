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
import com.hellsayenci.mrzscanner.pojos.BankClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlankFormsAdapter extends RecyclerView.Adapter<BlankFormsAdapter.ViewHolder> {
    private List<BankClient> clients;
    private OnClientItemClickListener listener;

    public void setListener(OnClientItemClickListener listener) {
        this.listener = listener;
    }

    public BlankFormsAdapter(List<BankClient> clients) {
        this.clients = clients;
    }

    @NonNull
    @Override
    public BlankFormsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.form_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlankFormsAdapter.ViewHolder holder, int position) {
        BankClient client = clients.get(position);

        if (client != null) {
            holder.personName.setText(client.getFirstName() + " " + client.getLastName());
            //Glide.with(holder.itemView).load(client.getPersonPhoto()).into(holder.personPic);
            holder.addedDate.setText(client.getOpenedDate());

            if (client.getGender().equals("F"))
                Glide.with(holder.itemView).load(R.drawable.women).into(holder.personPic);
            else
                Glide.with(holder.itemView).load(R.drawable.men).into(holder.personPic);

            holder.itemView.setOnClickListener(v -> listener.setOnClientItemListener(client));
        }
//        else
//            Toast.makeText(holder.itemView.getContext(), "Client is null", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.personPic)
        ImageView personPic;
        @BindView(R.id.personName)
        TextView personName;
        @BindView(R.id.addedDate)
        TextView addedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClientItemClickListener {
        void setOnClientItemListener(BankClient client);
    }
}
