package net.smallacademy.authenticatorapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddStudentAdapter extends FirestoreRecyclerAdapter<UserModel,AddStudentAdapter.AddStudentViewHolder> {

    private OnItemClickListener listener;

    public AddStudentAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @NonNull
    @Override
    public AddStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_student_item,parent,false);
        return new AddStudentAdapter.AddStudentViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull AddStudentViewHolder holder, int position, @NonNull UserModel model) {
        holder.tvUserName.setText(model.getName());
        holder.tvUserEmail.setText(model.getEmail());
    }


    class AddStudentViewHolder extends RecyclerView.ViewHolder{

        TextView tvUserName,tvUserEmail,tvUserStatus;
        public AddStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserStatus = itemView.findViewById(R.id.tvUserStatus);

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
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(AddStudentAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
