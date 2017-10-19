package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.ProfileActivity;
import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 6/27/2017.
 */

public class PeopelDirAdapter extends RecyclerView.Adapter<PeopelDirAdapter.ViewHolder>
{
    private static final String TAG = PeopelDirAdapter.class.getSimpleName();

    Context mContext;
    List<PeopelDirdetail> itemList;

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view , int position,PeopelDirdetail peopelDirdetail);
    }

    public PeopelDirAdapter(Context mContext, List<PeopelDirdetail> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dir_people,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try
        {
            final PeopelDirdetail peopelDirdetail = itemList.get(position);
            holder.tv_username.setText(""+peopelDirdetail.getFirstName()+" "+peopelDirdetail.getLastName());
            holder.tv_company_name.setText(""+peopelDirdetail.getCompanyName());

            holder.tv_username.setTypeface(Share.Font.bold);
            holder.tv_company_name.setTypeface(Share.Font.regular);
            Picasso.with(mContext)
                    .load(Urls.PEOPLE_IMAGE_URL+peopelDirdetail.getProfileImage())
                    .placeholder(R.drawable.ic_user_profile)
                    .error(R.drawable.ic_user_profile)
                    .into(holder.iv_user_profile);

            Log.e(TAG,""+peopelDirdetail.getIs_delete());
            if(peopelDirdetail.getIs_delete().equals("true") || peopelDirdetail.getIs_delete().equals("temp"))
            {
                holder.tv_delete.setEnabled(true);
                holder.tv_delete.setBackgroundResource(R.drawable.roundshape_btn);
                holder.tv_delete.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
            else
            {
                holder.tv_delete.setEnabled(false);
                holder.tv_delete.setBackgroundResource(R.drawable.roundshape_gray_btn);
                holder.tv_delete.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
            }
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null)
                        onItemClickListener.onItemClick(v,position,peopelDirdetail);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                    profileIntent.putExtra("peopelDirdetail",peopelDirdetail);
                    mContext.startActivity(profileIntent);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() { return itemList != null?itemList.size():0;    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_username)TextView tv_username;
        @BindView(R.id.tv_company_name)TextView tv_company_name;
        @BindView(R.id.tv_delete)TextView tv_delete;
        @BindView(R.id.iv_user_profile)ImageView iv_user_profile;

        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.itemView = itemView;
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
