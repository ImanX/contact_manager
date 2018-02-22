package com.imansoft.contact.manager.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.imansoft.contact.manager.R;
import com.imansoft.contact.manager.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> list;
    private List<Contact> selectedList;
    private List<Contact> filter;
    private List<Contact> listHolder;
    private boolean       isSelectable;

    public ContactAdapter(List<Contact> list, boolean isSelectable) {
        this.list = list;
        this.listHolder = list;
        this.filter = new ArrayList<>();
        this.selectedList = new ArrayList<>();
        this.isSelectable = isSelectable;
    }

    public void search(String name){

        if (name.equals("")){
            this.list = listHolder;
            notifyDataSetChanged();
        }

        if (name.length() < 3){
            return;
        }

        for (Contact item : listHolder) {
            String name1 = item.getName().toLowerCase();
            String name2 = name.toLowerCase();
            if (name1.contains(name2)) {
                filter.add(item);
            }
        }


        this.list = filter;
        notifyDataSetChanged();
        filter = new ArrayList<>();

    }

    public List<Contact> getSelectedList() {
        return selectedList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, null));
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        final Contact contact = this.list.get(position);
        holder.txtName.setText(contact.getName());
        holder.txtNumber.setText(contact.getNumber());

        if (!this.isSelectable) {
            holder.chk.setVisibility(View.INVISIBLE);
            return;
        }

        holder.chk.setChecked(this.selectedList.contains(contact));
        holder.chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.chk.isChecked()) {
                    selectedList.add(contact);
                } else {
                    selectedList.remove(contact);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtNumber;
        CheckBox chk;

        public ContactViewHolder(View view) {
            super(view);

            this.txtName = view.findViewById(R.id.txt_name);
            this.txtNumber = view.findViewById(R.id.txt_number);
            this.chk = view.findViewById(R.id.chk_contact);

        }


    }
}
