package net.smallacademy.authenticatorapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter< UserAdapter.ViewHolder> {

    Context context;
    List<User>user;
    boolean ischat;
    String Last_mesg;
    public UserAdapter(Context context, List<User>list,boolean ischat)
    {
        this.context=context;
        user=list;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvusername.setText(user.get(position).getName());
        //Toast.makeText(context, " yr yari "+  FirebaseDatabase.getInstance().getReference("Chats"), Toast.LENGTH_SHORT).show();
        holder.tvid.setText(user.get(position).getEmail());
        if(user.get(position).getImage().equals("default"))
        {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(context).load(user.get(position).getImage()).into(holder.image);
        }
        if(ischat)
        {
            Lastmesg(user.get(position).getId(),holder.tvLastmesg);
        }
        else {
            holder.tvLastmesg.setVisibility(View.GONE);
        }
        if(ischat)
        {
            if(user.get(position).getStuts().equals("online"))
            {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj=new Intent(context, MessageActivity.class);
                obj.putExtra("userid",user.get(position).getId());
               // Toast.makeText(context, " yr "+user.get(position).getId(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(context, " yr "+user.get(position).getEmail(), Toast.LENGTH_SHORT).show();
                context.startActivity(obj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image,img_on,img_off;
        TextView tvusername,tvid,tvLastmesg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.profileImage);
            tvusername=itemView.findViewById(R.id.tvuser);
            tvid=itemView.findViewById(R.id.tvid);
            img_off=itemView.findViewById(R.id.img_off);
            img_on=itemView.findViewById(R.id.img_on);
            tvLastmesg=itemView.findViewById(R.id.tvlastmesg);
        }
    }

    //cheaking for last mesg
    public void Lastmesg(final String userid, final TextView last_mesg)
    {
        Last_mesg="default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat =snapshot.getValue(Chat.class);
                    if(chat.getReciver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                     chat.getReciver().equals(userid)&& chat.getSender().equals(firebaseUser.getUid()))
                    {
                        Last_mesg=chat.getMessage();
                    }
                }
                switch (Last_mesg)
                {
                    case "default":
                        last_mesg.setText("No Message");
                        break;
                    default:
                        last_mesg.setText(Last_mesg);
                        break;
                }
                Last_mesg="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
