package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.fracappzstudios.oilfieldnotifications.EventDetailsActivity;
import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.Eventdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 7/9/2017.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>
{
    private static final String TAG = EventListAdapter.class.getSimpleName();

    Context mContext;
    List<Eventdetail> itemList;
    private LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private boolean is_visible_delete = false;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public EventListAdapter(Context mContext, List<Eventdetail> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        mInflater = LayoutInflater.from(mContext);
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.row_event,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try
        {
            if (itemList != null && 0 <= position && position < itemList.size())
            {
                final Eventdetail eventdetail = itemList.get(position);
                holder.tv_event_name.setText(eventdetail.getEventName());
                holder.tv_group_name.setText(eventdetail.getGroupName());
                holder.tv_group_date.setText(eventdetail.getCreateDate());


                holder.detail_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent eventDetailsIntent = new Intent(mContext, EventDetailsActivity.class);
                    Share.eventdetail = eventdetail;
                    mContext.startActivity(eventDetailsIntent);
                    }
                });

                holder.bind(eventdetail);
                if (!is_visible_delete)
                    holder.swipeLayout.setLockDrag(true);
                else
                    binderHelper.bind(holder.swipeLayout, eventdetail.getEventId());
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

        @BindView(R.id.tv_event_name)TextView tv_event_name;
        @BindView(R.id.tv_group_name) TextView tv_group_name;
        @BindView(R.id.tv_group_date) TextView tv_group_date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Eventdetail eventdetail ) {
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

    public boolean is_visible_delete() {
        return is_visible_delete;
    }

    public void setIs_visible_delete(boolean is_visible_delete) {
        this.is_visible_delete = is_visible_delete;
    }

    public OnItemClickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
}
