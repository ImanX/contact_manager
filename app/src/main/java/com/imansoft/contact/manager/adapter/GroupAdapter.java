package com.imansoft.contact.manager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imansoft.contact.manager.GroupActivity;
import com.imansoft.contact.manager.R;
import com.imansoft.contact.manager.models.Group;


import java.util.List;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public abstract class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GPViewHolder>{


    private List<Group> list;
    private Context context;

    public GroupAdapter(List<Group> list){
        this.list = list;
    }

    public List<Group> getList() {
        return this.list;
    }


    public abstract void onDeleteGroup(int gpID);


    @Override
    public GPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new GPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gp_item,null));
    }

    @Override
    public void onBindViewHolder(GPViewHolder holder, int position) {
        final Group item = this.list.get(position);
        holder.txtName.setText(item.getName());
        holder.txtMemberCount.setText(String.format("تعداد اعضای %s" , item.getCountOfMember()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             GroupActivity.start(context,item);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onDeleteGroup(item.getId());
            }
        });

    }

    public void deleteItem(int id){
        for (Group gp : list) {
            if (gp.getId() == id){
                list.remove(gp);
                 notifyDataSetChanged();
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class GPViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout;
        TextView txtName;
        TextView txtMemberCount;
        ImageView imgDelete;

        public GPViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout_root);
            txtName = itemView.findViewById(R.id.txt_name);
            txtMemberCount = itemView.findViewById(R.id.txt_member_count);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }


    public interface OnDeleteGroupListener{
        void onDelete(int gpID);
    }
}
