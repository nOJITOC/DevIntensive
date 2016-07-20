package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 14.07.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private static final String TAG = ConstantManager.TAG_PREFIX+" UsersAdapter";
    List<User> mUsers;
    Context mContext;
    UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<User> users, UserViewHolder.CustomClickListener customClickListener) {
        this.mUsers = users;
        this.mCustomClickListener = customClickListener;

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);

        return new UserViewHolder(convertView,mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final User user = mUsers.get(position);
        final String userPhoto;
        if(user.getPhoto().isEmpty()){
            Log.e(TAG, "onBindViewHolder: user with name " +user.getFullName() +"do not have image");
            userPhoto="null";
        }else{
            userPhoto=user.getPhoto();
        }
        Glide.with(mContext)
                .load(userPhoto)
                .error(holder.mDummy)
                .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                .placeholder(holder.mDummy)
                .centerCrop()
                .fitCenter()
                .into(holder.userPhoto);
//        DataManager.getInstance().getPicasso()
//                .load(userPhoto)
//                .error(holder.mDummy)
//                .placeholder(holder.mDummy)
//                .fit()
//                .centerCrop()
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(holder.userPhoto, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.e(TAG, " load from cache");
//                    }
//
//                    @Override
//                    public void onError() {
//                        DataManager.getInstance().getPicasso()
//                                .load(userPhoto)
//                                .error(holder.mDummy)
//                                .placeholder(holder.mDummy)
//
//                                .into(holder.userPhoto, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//                                        Log.e(TAG, " load from cache");
//                                    }
//
//                                    @Override
//                                    public void onError() {
//                                        Log.e(TAG, "Could not fetch image");
//
//                                    }
//                                });
//                    }
//                });
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProjects()));
        if (user.getBio() == null || user.getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getBio());
        }



    }
    public void setUsers(List<User> models) {
        mUsers = new ArrayList<>(models);
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected AspectRatioImageView userPhoto;
        protected TextView mFullName, mRating, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        protected Drawable mDummy;

        private CustomClickListener mListener;

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            this.mListener = customClickListener;
            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.rating_txt);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_txt);
            mProjects = (TextView) itemView.findViewById(R.id.projects_txt);
            mBio = (TextView) itemView.findViewById(R.id.bio_txt);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_btn);

            mDummy = userPhoto.getContext().getResources().getDrawable(R.drawable.user_bg);
            mShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener!=null){
                mListener.onUserItemClick(getAdapterPosition());
            }
        }

        public interface CustomClickListener {

            void onUserItemClick(int position);
        }
    }
}
