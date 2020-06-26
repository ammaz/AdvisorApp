package net.smallacademy.authenticatorapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BatchNameAdapter extends FirestoreRecyclerAdapter<BatchNames, BatchNameAdapter.BatchNameViewHolder> {


    public BatchNameAdapter(@NonNull FirestoreRecyclerOptions<BatchNames> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BatchNameViewHolder holder, int position, @NonNull BatchNames model) {
        holder.tvRecycler.setText(model.getBatchName());
    }

    @NonNull
    @Override
    public BatchNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_batch_item,parent,false);
        return new BatchNameViewHolder(view);
    }

    class BatchNameViewHolder extends RecyclerView.ViewHolder{

        TextView tvRecycler;
        public BatchNameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecycler = itemView.findViewById(R.id.tvRecycler);

        }
    }
}
