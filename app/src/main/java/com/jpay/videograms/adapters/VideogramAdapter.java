package com.jpay.videograms.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jpay.videograms.R;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.utils.SyncStatus;
import com.jpay.videograms.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by anguyen on 12/14/2015.
 */
public class VideogramAdapter extends RecyclerView.Adapter<VideogramAdapter.ViewHolder>{

    private ArrayList<Videograms> videogramses;
    private View.OnClickListener mOnItemClickedListener;

    public VideogramAdapter(View.OnClickListener listener) {
        videogramses = new ArrayList<>();
        this.mOnItemClickedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videograms, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Videograms videograms = videogramses.get(position);

        holder.mainView.setOnClickListener(mOnItemClickedListener);
        ImageLoader.getInstance().displayImage(Utils.getKeyframePath(videograms.keyframe), holder.keyframe);

        if(videograms.syncStatus == SyncStatus.NOT_AVAILABLE || videograms.syncStatus == SyncStatus.WILL_SYNC) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        if(this.videogramses != null) {
            return this.videogramses.size();
        }
        return 0;
    }

    public void notifyDataSetChanged(ArrayList<Videograms> videogramses) {
        this.videogramses = videogramses;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView mainView;
        ImageView keyframe;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = (CardView) itemView.findViewById(R.id.cv_vg_mainview);
            keyframe = (ImageView) itemView.findViewById(R.id.img_keyframe);
        }
    }
}
