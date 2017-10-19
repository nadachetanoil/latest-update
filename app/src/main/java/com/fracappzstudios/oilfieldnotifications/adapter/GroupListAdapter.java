package com.fracappzstudios.oilfieldnotifications.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.fracappzstudios.oilfieldnotifications.GroupViewActivity;
import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.Groupdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 6/30/2017.
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder>
{
    private static final String TAG = GroupListAdapter.class.getSimpleName();

    Context mContext;
    List<Groupdetail> itemList;
    private LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    private OnItemClickListener mListener;
    private boolean is_visible_delete = false;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public GroupListAdapter(Context mContext, List<Groupdetail> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        mInflater = LayoutInflater.from(mContext);
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.row_group,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try
        {
            if (itemList != null && 0 <= position && position < itemList.size())
            {
                final Groupdetail groupdetail = itemList.get(position);

                holder.tv_group_date.setText(""+groupdetail.getCreateDate());
                holder.tv_group_name.setText(""+groupdetail.getGroupName());

                holder.tv_group_date.setTypeface(Share.Font.regular);
                holder.tv_group_name.setTypeface(Share.Font.bold);

                Picasso.with(mContext)
                        .load(Urls.GROUP_IMAGE_URL+groupdetail.getGroupImage())
                        .placeholder(R.drawable.ic_group)
                        .error(R.drawable.ic_group)
                        .into(holder.iv_group);

                holder.bind(groupdetail);

                holder.detail_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent groupviewIntent = new Intent(mContext, GroupViewActivity.class);
                        Share.groupdetail = groupdetail;
                        mContext.startActivity(groupviewIntent);
                    }
                });

                if (!is_visible_delete)
                    holder.swipeLayout.setLockDrag(true);
                else
                    binderHelper.bind(holder.swipeLayout, groupdetail.getGroupId());
            }
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
    public void remove(int position) {
        if(itemList != null && itemList.size() > position)
        {
            itemList.remove(position);
            notifyItemRemoved(position);
        }

    }

    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.swipe_layout) SwipeRevealLayout swipeLayout;
        @BindView(R.id.delete_layout) View deleteLayout;
        @BindView(R.id.detail_layout) FrameLayout detail_layout;

        @BindView(R.id.iv_group)ImageView iv_group;
        @BindView(R.id.tv_group_name)TextView tv_group_name;
        @BindView(R.id.tv_group_date)TextView tv_group_date;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.itemView = itemView;
        }

        public void bind(Groupdetail groupdetail) {
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        if (v != null && mListener != null ) {
                            mListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public OnItemClickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public boolean is_visible_delete() {
        return is_visible_delete;
    }

    public void setIs_visible_delete(boolean is_visible_delete) {
        this.is_visible_delete = is_visible_delete;
    }
}
