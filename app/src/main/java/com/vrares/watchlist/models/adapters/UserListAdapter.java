package com.vrares.watchlist.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.activities.Henson;
import com.vrares.watchlist.models.pojos.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<User> usersList;

    public UserListAdapter(Context context, ArrayList<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.MyViewHolder holder, int position) {
        final User user = usersList.get(position);
        Glide.with(context)
                .load(user.getPicture())
                .into(holder.civPicture);
        holder.tvName.setText(user.getFullname());
        holder.tvEmail.setText(user.getEmail());
        if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.btnFriend.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Henson.with(context)
                        .gotoUserProfileActivity()
                        .user(user)
                        .build();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_users_picture)CircleImageView civPicture;
        @BindView(R.id.item_users_name)TextView tvName;
        @BindView(R.id.item_users_email)TextView tvEmail;
        @BindView(R.id.item_users_btn_friend)Button btnFriend;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
