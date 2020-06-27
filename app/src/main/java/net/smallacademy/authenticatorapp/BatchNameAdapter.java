package net.smallacademy.authenticatorapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BatchNameAdapter extends FirestoreRecyclerAdapter<BatchNames, BatchNameAdapter.BatchNameViewHolder> {
    private OnItemClickListener listener;

    public BatchNameAdapter(@NonNull FirestoreRecyclerOptions<BatchNames> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BatchNameViewHolder holder, int position, @NonNull BatchNames model) {
        holder.tvRecycler.setText(model.getBatchName());
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
