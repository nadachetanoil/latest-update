package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.database.DatabaseHandler;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 6/29/2017.
 */

public class SearchPeopleDirAdapter extends RecyclerView.Adapter<SearchPeopleDirAdapter.ViewHolder>
{
    private static final String TAG = SearchPeopleDirAdapter.class.getSimpleName();

    Context mContext;
    List<PeopelDirdetail> itemList;
    DatabaseHandler db;

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view , int position,PeopelDirdetail peopelDirdetail);
    }

    public SearchPeopleDirAdapter(Context mContext, List<PeopelDirdetail> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        db = new DatabaseHandler(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_dir_people,parent,false);
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

             if(peopelDirdetail.getIs_delete().equals("true"))
             {
                 holder.tv_add.setText("Added");
                 holder.tv_add.setClickable(false);
             }
             else
             {
                 holder.tv_add.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         peopelDirdetail.setIs_delete("true");
                         db.open();
                         db.addPeopelDir(peopelDirdetail,peopelDirdetail.getUser_id());
                         db.close();
                         if(onItemClickListener != null)
                         {
                             onItemClickListener.onItemClick(v,position,peopelDirdetail);
                         }
                         itemList.remove(peopelDirdetail);
                     }
                 });
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

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_user_profile) ImageView iv_user_profile;
        @BindView(R.id.tv_username) TextView tv_username;
        @BindView(R.id.tv_company_name)TextView tv_company_name;
        @BindView(R.id.tv_add)TextView tv_add;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
