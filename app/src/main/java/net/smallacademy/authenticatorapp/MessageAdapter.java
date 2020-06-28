package net.smallacademy.authenticatorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Chat> chats;
    private String image;
    FirebaseUser user;

    public MessageAdapter(Context context, List<Chat> list, String image) {
        this.context = context;
        chats = list;
        this.image = image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(v);
        }

    }

        @Override
        public void onBindViewHolder (@NonNull ViewHolder holder,final int position){
        Chat chat=chats.get(position);
        holder.show_message.setText(chat.getMessage());
        if(image.equals("default")) {
            holder.image_Profile.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(context).load(image).into(holder.image_Profile);
        }
        }

        @Override
        public int getItemCount () {
            return chats.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image_Profile;
            TextView show_message;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image_Profile = itemView.findViewById(R.id.profilechat);
                show_message = itemView.findViewById(R.id.show_mesg);
            }
        }

        @Override
        public int getItemViewType ( int position){
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (chats.get(position).getSender().equals(user.getUid())) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
        }
}



