package com.vrares.watchlist.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.activities.Henson;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.models.pojos.Watcher;
import com.vrares.watchlist.models.utils.DateFormatUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class WatchersAdapter extends RecyclerView.Adapter<WatchersAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Watcher> watcherList;

    public WatchersAdapter(Context context, ArrayList<Watcher> watcherList) {
        this.context = context;
        this.watcherList = watcherList;
    }

    @Override
    public WatchersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watchers_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WatchersAdapter.MyViewHolder holder, int position) {
        final Watcher watcher = watcherList.get(position);
        Glide.with(context)
                .load(watcher.getPicture())
                .into(holder.civPicture);
        if(!watcher.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.tvName.setText(watcher.getFullname());
        } else {
            holder.tvName.setText("You");
        }

        DateFormatUtil dateFormatUtil = new DateFormatUtil("dd-MMM-yyyy HH:mm");
        holder.tvDate.setText(dateFormatUtil.getFormatDate(watcher.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = watcher.getFullname();
                String[] userNameSplitted = userName.split(" ");
                User user = new User(userNameSplitted[0], userNameSplitted[1], watcher.getEmail(), watcher.getPicture());
                user.setId(watcher.getId());
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
        return watcherList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civ_watcher_picture)CircleImageView civPicture;
        @BindView(R.id.tv_watchers_name)TextView tvName;
        @BindView(R.id.tv_watchers_date)TextView tvDate;
        @BindView(R.id.tv_watchers_location)TextView tvLocation;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
