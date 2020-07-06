package net.smallacademy.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.example.firebasechat.User;
//import com.example.firebasechat.fragments.ChatFragment;
//import com.example.firebasechat.fragments.UserFragment;
import com.google.android.material.tabs.TabLayout;
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

public class Activity extends AppCompatActivity {
    CircleImageView profile;
    TextView tvprofile;
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    ChatFragment chatFragment;
    UserFragment userFragment;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Chat");
        //auto login log out and user name display on toolbr
        profile=findViewById(R.id.profile);
        tvprofile=findViewById(R.id.userName);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    user=dataSnapshot.getValue(User.class);
                    tvprofile.setText(user.getName());
                    if(user.getImage().equals("default"))
                    {
                        profile.setImageResource(R.mipmap.ic_launcher);
                    }
                    else {
                        Glide.with(Activity.this).load(user.getImage()).into(profile);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



         //tablayout part
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        chatFragment=new ChatFragment();
        userFragment=new UserFragment();
        ViewpagerAdapter object = new ViewpagerAdapter(getSupportFragmentManager(), 0);
        object.addFrag(chatFragment,"Chat");
        object.addFrag(userFragment,"Student");
        viewPager.setAdapter(object);


    }
    private static class ViewpagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments=new ArrayList<>();
        List<String> titles=new ArrayList<>();

        public ViewpagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        public void addFrag(Fragment f,String t )
        {
            fragments.add(f);
            titles.add(t);
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent obj=new Intent(Activity.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(obj);

                return true;

        }
        return false;
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





