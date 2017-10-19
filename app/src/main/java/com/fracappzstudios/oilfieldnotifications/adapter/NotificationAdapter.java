package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.NotificationDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 6/27/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private static final String TAG = NotificationAdapter.class.getSimpleName();

    Context mContext;
    List<NotificationDetail> itemList;

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }

    public NotificationAdapter(Context mContext, List<NotificationDetail> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            NotificationDetail notificationDetail = itemList.get(position);
            holder.tv_heading.setText(notificationDetail.getGroupName());
            holder.tv_msg.setText(notificationDetail.getEventName());
            holder.tv_date_time.setText(notificationDetail.getCreateDate().substring(0,10));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null)
                        onItemClickListener.onItemClick(v,position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_heading)
        TextView tv_heading;
        @BindView(R.id.tv_msg)
        TextView tv_msg;
        @BindView(R.id.tv_date_time)
        TextView tv_date_time;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

