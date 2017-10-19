package com.fracappzstudios.oilfieldnotifications.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harshad on 7/2/2017.
 */

public class SelectMemberAdapter extends RecyclerView.Adapter<SelectMemberAdapter.ViewHolder>
{
    private static final  String TAG = SelectMemberAdapter.class.getSimpleName();

    Context mContext;
    List<PeopelDirdetail> itemList;
    public Map<String,Boolean> selectedMember;

    public SelectMemberAdapter(Context mContext, List<PeopelDirdetail> itemList,Map<String,Boolean> selectedMember) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.selectedMember = selectedMember;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_member,parent,false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try
        {

            final PeopelDirdetail peopelDirdetail = itemList.get(position);

            holder.tv_username.setText(peopelDirdetail.getFirstName()+" "+peopelDirdetail.getLastName());
            holder.tv_email.setText(peopelDirdetail.getEmail());


            if(peopelDirdetail.is_select())
            {
                holder.iv_check.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.iv_check.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if(peopelDirdetail.is_select())
                    {
                        itemList.get(position).setIs_select(false);
                        holder.iv_check.setVisibility(View.GONE);
                    }
                    else
                    {
                        itemList.get(position).setIs_select(true);
                        holder.iv_check.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
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
        return itemList!= null?itemList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_username)TextView tv_username;
        @BindView(R.id.tv_email)TextView tv_email;
        @BindView(R.id.iv_check)ImageView iv_check;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.itemView = itemView;
        }
    }

    public String getSelectedMembers()
    {
        StringBuilder memberlist = new StringBuilder();
        if(itemList != null && itemList.size() > 0)
        {
            for (int index=0;index<itemList.size();index++)
            {
                if (!itemList.get(index).getMemberId().equals(SharedPrefs.getString(mContext,SharedPrefs.USER_ID)))
                {
                    if(memberlist.length() == 0 && itemList.get(index).is_select())
                        memberlist.append(itemList.get(index).getMemberId());
                    else if(itemList.get(index).is_select())
                        memberlist.append(","+itemList.get(index).getMemberId());
                }
            }
        }
        return memberlist.toString();
    }

    public  Map<String,Boolean> getItemList()
    {
        selectedMember = new HashMap<>();
        if(itemList != null && itemList.size() > 0)
        {
            for (int index=0;index<itemList.size();index++)
            {
                if(itemList.get(index).is_select())
                    selectedMember.put(itemList.get(index).getMemberId(),true);
                else
                    selectedMember.put(itemList.get(index).getMemberId(),false);
            }
        }
        return selectedMember;
    }

}
