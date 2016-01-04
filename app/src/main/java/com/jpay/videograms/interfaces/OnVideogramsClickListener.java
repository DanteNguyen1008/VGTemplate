package com.jpay.videograms.interfaces;

import com.jpay.videograms.models.Videograms;

/**
 * Created by anguyen on 12/16/2015.
 */
public interface OnVideogramsClickListener {
    void onKeyframeClicked(Videograms videograms, int position);
    void onRemoveButtonClicked(Videograms removeVideograms, int position);
    void onSyncButtonClicked(Videograms syncVideograms, int position);
    void onUndoButtonClicked(Videograms undoVideograms, int position);
}
