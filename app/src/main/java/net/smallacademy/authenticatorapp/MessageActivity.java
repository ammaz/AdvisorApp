package net.smallacademy.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile;
    TextView tvname;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Toolbar toolbar;

    ImageButton btnsend;
    EditText mesgsend;
    MessageAdapter myadapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //set linearlayout manager
        recyclerView=findViewById(R.id.view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        btnsend=findViewById(R.id.btnsend);
        //
        Intent obj=getIntent();
        final String id=obj.getStringExtra("userid");
        toolbar=findViewById(R.id.toolbarMesg);
        setSupportActionBar(toolbar);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesg=mesgsend.getText().toString();
                if(!mesg.equals(""))
                {
                    sendmeassge(firebaseUser.getUid(),id,mesg);
                }
                else{
                    Toast.makeText(MessageActivity.this, "You can't send empty message !", Toast.LENGTH_SHORT).show();
                }
                mesgsend.setText("");
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent obj=new Intent(MessageActivity.this,Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(obj);
            }
        });
        profile=findViewById(R.id.profileMesg);
        tvname=findViewById(R.id.userNameMesg);
        btnsend=findViewById(R.id.btnsend);
        mesgsend=findViewById(R.id.message);



        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert id != null;
        reference=FirebaseDatabase.getInstance().getReference("Users").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                tvname.setText(user.getName());
                if(user.getImage().equals("default"))
                {
                    profile.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(MessageActivity.this).load(user.getImage()).into(profile);
                }
                readmesg(firebaseUser.getUid(),id,user.getImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void sendmeassge(String sender,String reciver,String messge)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> map=new HashMap<>();
        map.put("sender",sender);
        map.put("reciver",reciver);
        map.put("message",messge);
        reference.child("Chats").push().setValue(map);
    }
    private void readmesg(final String myid, final String userid, final String images)
    {
        chats=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    assert chat != null;
                    if(chat.getReciver().equals(myid)&&chat.getSender().equals(userid)||
                            chat.getReciver().equals(userid)&&chat.getSender().equals(myid) )
                    {
                        chats.add(chat);
                    }
                    myadapter=new MessageAdapter(MessageActivity.this,chats,images);
                    recyclerView.setAdapter(myadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //stuts online offline
    public void status(String status){
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> map=new HashMap<>();
        map.put("stuts",status);
        reference.updateChildren(map);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }
    @Override
    protected void onPause() {
        super.onPause();
        status("offline");

    }

}