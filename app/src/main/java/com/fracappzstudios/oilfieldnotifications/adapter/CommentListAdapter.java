package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.AllCommentdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 7/28/2017.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>
{
    private static final String TAG = CommentListAdapter.class.getSimpleName();

    Context mContext;
    List<AllCommentdetail> itemList;

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public CommentListAdapter(Context mContext, List<AllCommentdetail> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try
        {
            AllCommentdetail allCommentdetail = itemList.get(position);
            holder.tv_comment.setText(allCommentdetail.getComment());
            holder.tv_username.setText(allCommentdetail.getCreateByUser().get(0).getFirstName()+" "+allCommentdetail.getCreateByUser().get(0).getLastName());
            Picasso.with(mContext)
                    .load(Urls.PEOPLE_IMAGE_URL+allCommentdetail.getCreateByUser().get(0).getProfileImage())
                    .placeholder(R.drawable.ic_user_profile)
                    .error(R.drawable.ic_user_profile)
                    .into(holder.iv_user_img);
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null)
                        onItemClickListener.onItemClick(v,position);
                }
            });

            holder.tv_delete.setTypeface(Share.Font.regular);
            holder.tv_username.setTypeface(Share.Font.regular);
            holder.tv_comment.setTypeface(Share.Font.thin_medium);
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return itemList!=null?itemList.size():0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_username) TextView tv_username;
        @BindView(R.id.tv_comment) TextView tv_comment;
        @BindView(R.id.tv_delete) TextView tv_delete;
        @BindView(R.id.iv_user_img)ImageView iv_user_img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public boolean removeItem(int position)
    {
        boolean is_delete = false;
       try {
           if(itemList != null && itemList.size() >= position)
           {
               itemList.remove(position);
               notifyDataSetChanged();
           }
       }
       catch (Exception e){e.printStackTrace();}
        return is_delete;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
