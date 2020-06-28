package net.smallacademy.authenticatorapp;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> users;

    List<String> chatuser;
    FirebaseUser user;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView=v.findViewById(R.id.viewchat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        user= FirebaseAuth.getInstance().getCurrentUser();
        chatuser=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatuser.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chating=snapshot.getValue(Chat.class);
                    assert chating != null;
                    if(chating.getSender().equals(user.getUid()))
                    {
                        chatuser.add(chating.getReciver());
                    }
                    if(chating.getReciver().equals(user.getUid()))
                    {
                        chatuser.add(chating.getSender());
                    }
                }
                readchat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


     return v;

    }

  private void readchat() {
        users=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User obj=snapshot.getValue(User.class);
                    for(String userid:chatuser)
                    {
                        assert obj != null;
                        if(obj.getId().equals(userid))
                        {
                            boolean hy=false;
                            for(User obj1:users){
                                if(obj1.getId().equals(obj.getId())){
                                    hy=true;
                                    break;
                                }
                            }
                            if(!hy)
                            users.add(obj);
                        }
                    }
                }
                userAdapter=new UserAdapter(getContext(),users,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
