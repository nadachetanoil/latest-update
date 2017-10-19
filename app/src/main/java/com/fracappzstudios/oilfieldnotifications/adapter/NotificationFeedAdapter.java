package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.EventDetailsActivity;
import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.Eventdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 7/30/2017.
 */

public class NotificationFeedAdapter  extends RecyclerView.Adapter<NotificationFeedAdapter.ViewHolder>
{
    private static final String TAG = NotificationFeedAdapter.class.getSimpleName();

    Context mContext;
    List<Eventdetail> itemLists;

    public NotificationFeedAdapter(Context mContext, List<Eventdetail> itemLists) {
        this.mContext = mContext;
        this.itemLists = itemLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_feed,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try
        {
            final Eventdetail eventdetail = itemLists.get(position);
            holder.tv_event_name.setText(""+eventdetail.getEventName());
            holder.tv_group_name.setText(""+eventdetail.getGroupName());
            holder.tv_date_time.setText(""+eventdetail.getCreateDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent eventDetailIntent = new Intent(mContext, EventDetailsActivity.class);
                    Share.eventdetail = eventdetail;
                    mContext.startActivity(eventDetailIntent);
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return itemLists != null?itemLists.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_event_name) TextView tv_event_name;
        @BindView(R.id.tv_group_name) TextView tv_group_name;
        @BindView(R.id.tv_date_time) TextView tv_date_time;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.itemView = itemView;
        }
    }
}
