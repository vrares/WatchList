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
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.activities.Henson;
import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.models.pojos.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestPendingAdapter extends RecyclerView.Adapter<FriendRequestPendingAdapter.MyViewHolder> implements FriendRequestPendingAdapterCallback{

    private ArrayList<User> pendingList;
    private Context context;
    private User receivingUser;

    private DatabaseHelper databaseHelper = new DatabaseHelper();

    public FriendRequestPendingAdapter(ArrayList<User> pendingList, Context context, User localUser) {
        this.pendingList = pendingList;
        this.context = context;
        this.receivingUser = localUser;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final User user = pendingList.get(position);
        Glide.with(context)
                .load(user.getPicture())
                .into(holder.civPicture);
        holder.tvName.setText(user.getFullname());

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

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(user, position, receivingUser, holder);
            }
        });

        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineRequest(user, position, receivingUser, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    private void acceptRequest(User user, int position, User receivingUser, MyViewHolder holder) {
        databaseHelper.acceptRequest(user, position, receivingUser, holder, this);
    }

    private void declineRequest(User user, int position, User receivingUser, MyViewHolder holder) {
        databaseHelper.declineRequest(user, position, receivingUser, holder, this);
    }

    @Override
    public void onRequestFinished(User user, int position, User receivingUser, MyViewHolder holder) {
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_pending_picture)CircleImageView civPicture;
        @BindView(R.id.item_pending_name)TextView tvName;
        @BindView(R.id.item_pending_accept)Button btnAccept;
        @BindView(R.id.item_pending_decline)Button btnDecline;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
