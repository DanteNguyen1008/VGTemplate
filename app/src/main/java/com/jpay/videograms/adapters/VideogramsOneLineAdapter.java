package com.jpay.videograms.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpay.videograms.R;
import com.jpay.videograms.interfaces.OnVideogramsClickListener;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.utils.SyncStatus;
import com.jpay.videograms.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by anguyen on 12/15/2015.
 */
public class VideogramsOneLineAdapter extends RecyclerView.Adapter<VideogramsOneLineAdapter.ViewHolder> {

    private ArrayList<Videograms> mVideogramses;
    private OnVideogramsClickListener mOnItemClickedListener;
    private DisplayImageOptions mDisplayImageOption;
    private Context mContext;

    public VideogramsOneLineAdapter(Context context, OnVideogramsClickListener listener) {
        mVideogramses = new ArrayList<>();
        this.mOnItemClickedListener = listener;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_videogram_horizontal, null);
        mDisplayImageOption = Utils.getDisplayImageOptionWithRoundedImage((int) mContext.getResources().getDimension(R.dimen.rounded_photo_corner_radius));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Videograms videograms = mVideogramses.get(position);
        ImageLoader.getInstance().displayImage(Utils.getKeyframePath(videograms.keyframe), holder.keyframe, mDisplayImageOption);
        holder.txtSenderName.setText(videograms.contact.name);
        holder.txtDate.setText(Utils.parseCreatedDate(videograms.createdDate));

        /*handle displaying of keyframe and user info */
        if (videograms.syncStatus == SyncStatus.DELETED) {
            holder.keyframe.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

            holder.txtSenderName.setTextColor(holder.txtSenderName.getContext().getResources().getColor(R.color.disable_normal_gray_text));
            holder.txtDate.setTextColor(holder.txtDate.getContext().getResources().getColor(R.color.disable_normal_gray_text));

        } else {
            holder.keyframe.setColorFilter(holder.mainColorFilter);

            holder.txtSenderName.setTextColor(Color.WHITE);
            holder.txtDate.setTextColor(holder.txtDate.getContext().getResources().getColor(R.color.normal_gray_text));
        }

        /* handle of actions*/
        switch (videograms.syncStatus) {
            case ON_DEVICE:
                holder.txtStatus.setVisibility(View.GONE);
                holder.txtRemove.setVisibility(View.VISIBLE);
                holder.txtUndo.setVisibility(View.GONE);
                holder.txtSync.setVisibility(View.GONE);

                holder.keyframe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickedListener != null) {
                            mOnItemClickedListener.onKeyframeClicked(videograms, position);
                        }
                    }
                });

                handleReadIcon(videograms, holder);
                break;

            case NOT_AVAILABLE:
                holder.txtStatus.setVisibility(View.GONE);
                holder.txtRemove.setVisibility(View.GONE);
                holder.txtUndo.setVisibility(View.GONE);
                holder.txtSync.setVisibility(View.VISIBLE);

                holder.keyframe.setOnClickListener(null);
                handleReadIcon(videograms, holder);
                break;

            case DELETED:
                holder.txtStatus.setVisibility(View.VISIBLE);
                holder.txtStatus.setText("Removed");
                holder.txtStatus.setTextColor(Color.WHITE);

                holder.txtRemove.setVisibility(View.GONE);
                holder.txtUndo.setVisibility(View.VISIBLE);
                holder.txtSync.setVisibility(View.GONE);

                holder.keyframe.setOnClickListener(null);
                holder.iconReadStatus.setVisibility(View.GONE);
                break;

            case WILL_SYNC:
                holder.txtStatus.setVisibility(View.VISIBLE);
                holder.txtStatus.setText("Will Sync");
                holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.green_will_sync));
                holder.txtRemove.setVisibility(View.GONE);
                holder.txtUndo.setVisibility(View.VISIBLE);
                holder.txtSync.setVisibility(View.GONE);

                holder.keyframe.setOnClickListener(null);
                handleWillSyncIcon(holder);
                break;
        }

        holder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickedListener != null) {
                    mOnItemClickedListener.onRemoveButtonClicked(videograms, position);
                }
            }
        });

        holder.txtUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickedListener != null) {
                    mOnItemClickedListener.onUndoButtonClicked(videograms, position);
                }
            }
        });

        holder.txtSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickedListener != null) {
                    mOnItemClickedListener.onSyncButtonClicked(videograms, position);
                }
            }
        });


    }

    private void handleReadIcon(Videograms videograms, ViewHolder holder) {
        if(!videograms.isRead) {
            holder.iconReadStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_new));
            holder.iconReadStatus.setVisibility(View.VISIBLE);
        } else {
            holder.iconReadStatus.setVisibility(View.GONE);
        }
    }

    private void handleWillSyncIcon(ViewHolder holder) {
        holder.iconReadStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_willsync));
        holder.iconReadStatus.setVisibility(View.VISIBLE);
    }



    @Override
    public int getItemCount() {
        if (this.mVideogramses != null) {
            return this.mVideogramses.size();
        }
        return 0;
    }

    public void notifyDataSetChanged(ArrayList<Videograms> videogramses) {
        this.mVideogramses = videogramses;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSenderName, txtDate, txtRemove, txtUndo, txtSync, txtStatus;
        ImageView keyframe, iconReadStatus;

        ColorFilter mainColorFilter;

        public ViewHolder(View itemView) {
            super(itemView);
            keyframe = (ImageView) itemView.findViewById(R.id.img_keyframe);
            iconReadStatus = (ImageView) itemView.findViewById(R.id.ic_read_status);
            txtSenderName = (TextView) itemView.findViewById(R.id.txt_sender_name);
            txtDate = (TextView) itemView.findViewById(R.id.txt_created_date);

            txtRemove = (TextView) itemView.findViewById(R.id.txt_remove);
            txtUndo = (TextView) itemView.findViewById(R.id.txt_undo);
            txtSync = (TextView) itemView.findViewById(R.id.txt_sync);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_action_status);

            mainColorFilter = keyframe.getColorFilter();
        }
    }
}
