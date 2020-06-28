package net.smallacademy.authenticatorapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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

import net.smallacademy.authenticatorapp.R;
import net.smallacademy.authenticatorapp.User;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter;
    List<User> users;
    RecyclerView.LayoutManager manager;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    SearchView srch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView=v.findViewById(R.id.userlist);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        users=new ArrayList<>();
        Readuser();
        srch=v.findViewById(R.id.srchuser);
        return v;


    }


    private void Readuser() {
    firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    reference= FirebaseDatabase.getInstance().getReference("Users");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            users.clear();
            for(DataSnapshot snapshot: dataSnapshot.getChildren())
            {
                User user=snapshot.getValue(User.class);
                assert user != null;
                if(!user.getId().equals(firebaseUser.getUid()))
                {
                    users.add(user);
                }
            }
            adapter=new UserAdapter(getContext(),users,false);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }

    @Override
    public void onStart() {
        super.onStart();

        srch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Searchbyid(newText);
                return true;
            }
        });
    }

    private void Searchbyid(String s) {
        ArrayList<User> mylist=new ArrayList<>();
       for(User user:users)
       {
           if(user.getEmail().toLowerCase().contains(s.toLowerCase()))
           {
               mylist.add(user);
           }
       }
       adapter=new UserAdapter(this.getContext(),mylist,false);
       recyclerView.setAdapter(adapter);
    }
}