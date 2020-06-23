package net.smallacademy.authenticatorapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BatchAdapter extends FirebaseRecyclerAdapter<String,BatchAdapter.BatchViewHolder> {

    public BatchAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }


    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_batch_item,parent,false);
        return new BatchViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull BatchViewHolder holder, int position, @NonNull String model) {
        holder.tvRecycler.setText(model);
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecycler;
        public BatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecycler = itemView.findViewById(R.id.tvRecycler);
        }
    }
}
