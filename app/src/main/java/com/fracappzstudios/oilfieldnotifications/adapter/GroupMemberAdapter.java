package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 7/12/2017.
 */

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder>
{
    private static final String TAG = GroupMemberAdapter.class.getSimpleName();

    Context mContext;
    List<PeopelDirdetail> itemList;
    LayoutInflater mLayoutInflater;
    public SearchPeopleDirAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view , int position,PeopelDirdetail peopelDirdetail);
    }

    public GroupMemberAdapter(Context mContext, List<PeopelDirdetail> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mLayoutInflater.inflate(R.layout.row_group_member,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try
        {
            final PeopelDirdetail peopelDirdetail = itemList.get(position);

            holder.tv_username.setText(peopelDirdetail.getFirstName()+" "+peopelDirdetail.getLastName());
            holder.tv_company_name.setText(peopelDirdetail.getCompanyName());
            Picasso.with(mContext)
                    .load(Urls.PEOPLE_IMAGE_URL+peopelDirdetail.getProfileImage())
                    .placeholder(R.drawable.ic_user_profile)
                    .error(R.drawable.ic_user_profile)
                    .into(holder.iv_user_profile);

            holder.tv_username.setTypeface(Share.Font.bold);
            holder.tv_company_name.setTypeface(Share.Font.thin_regular);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null)
                    {
                        onItemClickListener.onItemClick(v,position,peopelDirdetail);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null?itemList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_user_profile) ImageView iv_user_profile;
        @BindView(R.id.tv_username) TextView tv_username;
        @BindView(R.id.tv_company_name)TextView tv_company_name;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.itemView = itemView;
        }
    }

    public SearchPeopleDirAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(SearchPeopleDirAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
