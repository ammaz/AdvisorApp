package net.smallacademy.authenticatorapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AddStudentAdapter extends FirestoreRecyclerAdapter<UserModel,AddStudentAdapter.AddStudentViewHolder> {

    private OnItemClickListener listener;
   // UserModel userModel;
    private String batchID;
    //private ArrayList<UserModel> userList;
    private Context context;

    public AddStudentAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, String batchID/*, ArrayList<UserModel> userList*/, Context context) {
        super(options);
        this.batchID = batchID;
      //  this.userList = userList;
        this.context = context;
    }

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
    protected void onBindViewHolder(@NonNull final AddStudentViewHolder holder, int position, @NonNull UserModel model) {
        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
         String ID = snapshot.getReference().getId();
       // String ID = model.getID();
        holder.tvUserName.setText(model.getName());
        holder.tvUserEmail.setText(model.getEmail());
        holder.tvUserStatus.setVisibility(View.GONE);

        if(ID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.tvUserStatus.setVisibility(View.VISIBLE);
            holder.tvUserStatus.setText("Advisor");
        }

        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("users/"+ID+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try{
                    Picasso.get().load(uri).into(holder.imgProfile);
                }catch (Exception e){

                }
            }
        });

    chechIfAlreadyExists(ID,holder);

//        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = fStore.collection("users").document(ID);
//
//
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                userModel = documentSnapshot.toObject(UserModel.class);
//
//            }
//        });


        //holder.tvUserName.setText(ID.);
      //  holder.tvUserEmail.setText(userModel.getEmail());
    }

    private void chechIfAlreadyExists(String ID,final AddStudentViewHolder holder) {

//         CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("users")
//                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Batches").document(batchID)
//                 .collection("Students");

      //  collectionReference.add(ID);


       //  DocumentReference documentReference = collectionReference.document(ID);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Batches").document(batchID).collection("Students")
                .document(ID);

//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    if (documentSnapshot.exists()) {
//                        holder.tvUserStatus.setVisibility(View.VISIBLE);
//                        holder.tvUserStatus.setText(documentReference);
//                    }
//
//                }
//            }
//        });
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    holder.tvUserStatus.setVisibility(View.VISIBLE);
                    AddStudentModel userModel = documentSnapshot.toObject(AddStudentModel.class);
                    holder.tvUserStatus.setText(userModel.getStatus());

                }
            }
        });
    }

    public void deleteStudent( int position) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(getSnapshots().getSnapshot(position).getId()).collection("Batches").document(batchID);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Student Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addStudent(UserModel userModel, final int position) {
        String ID = getSnapshots().getSnapshot(position).getId();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        AddStudentModel studentModel = new AddStudentModel();
        studentModel.setName(userModel.getName());
        studentModel.setEmail(userModel.getEmail());
        studentModel.setStatus("Student");
        studentModel.setUserID(ID);
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Batches").document(batchID).collection("Students");
        collectionReference.document(ID).set(studentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Student added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        FirebaseFirestore.getInstance().collection("Batches").document(batchID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                final Map<String, Object> batch = new HashMap<>();
                batch.put("BatchName", documentSnapshot.toObject(BatchNames.class).getBatchName());
                FirebaseFirestore.getInstance().collection("users").document(getSnapshots().getSnapshot(position).getId())
                        .collection("Batches").document(batchID).set(batch);
            }
        });



    }

    class AddStudentViewHolder extends RecyclerView.ViewHolder{

        TextView tvUserName,tvUserEmail,tvUserStatus;
        ImageView imgProfile;
        public AddStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserStatus = itemView.findViewById(R.id.tvUserStatus);
            imgProfile = itemView.findViewById(R.id.imgProfile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);

                        String ID = getSnapshots().getSnapshot(position).getId();
                        if (!ID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Batches").document(batchID)
                                    .collection("Students").document(ID);

                            FirebaseFirestore.getInstance().collection("Batches").document(batchID)
                                    .collection("Students").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                }
                            });


                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    if (documentSnapshot.exists()) {
                                        //AddStudentModel studentModel = documentSnapshot.toObject(AddStudentModel.class);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Remove Student").setMessage("Remove Student from Batch?")
                                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        deleteStudent( getAdapterPosition());
                                                    }
                                                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Add Student").setMessage("Add student in the Batch?")
                                                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                                                        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                addStudent(documentSnapshot.toObject(UserModel.class), getAdapterPosition());
                                                            }
                                                        });
                                                    }
                                                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                                    }
                                }
                            });

                        }
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
