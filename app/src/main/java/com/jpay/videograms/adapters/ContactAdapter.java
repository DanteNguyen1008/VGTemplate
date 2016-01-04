package com.jpay.videograms.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpay.videograms.R;
import com.jpay.videograms.models.Contact;

import java.util.ArrayList;

/**
 * Created by anguyen on 12/11/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    private ArrayList<Contact> mContacts;
    private Context mContext;
    private View.OnClickListener mOnItemClickListener;

    public ContactAdapter(Context context, ArrayList contacts, View.OnClickListener listener) {
        this.mContacts = contacts;
        this.mContext = context;
        this.mOnItemClickListener = listener;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder holder, int position) {
        if(position < 0 || position >= mContacts.size()) {
            return;
        }

        holder.tvContactName.setText(mContacts.get(position).name);
        holder.tvContactName.setOnClickListener(mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        if(mContacts != null) {
            return mContacts.size();
        }
        return 0;
    }

    public Contact getItem(int position) {
        if(mContacts != null && position >= 0 && position < mContacts.size()) {
            return mContacts.get(position);
        }
        return null;
    }

    public void notifyDataSetChanged(ArrayList<Contact> contacts) {
        this.mContacts = contacts;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContactName;

        public ViewHolder(View v) {
            super(v);
            this.tvContactName = (TextView) v.findViewById(R.id.tv_contact_name);
        }
    }
}
