package com.jpay.videograms.views;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpay.mvcvm.controllers.JBaseController;
import com.jpay.mvcvm.exceptions.MissingControllerException;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.videograms.R;
import com.jpay.videograms.adapters.VideogramsOneLineAdapter;
import com.jpay.videograms.controllers.VideogramsListController;
import com.jpay.videograms.interfaces.OnVideogramsClickListener;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.ui.HorizontalSpaceItemDecoration;
import com.jpay.videograms.utils.SyncStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anguyen on 12/10/2015.
 */
public class VideogramsListFragmentView extends JBaseFragmentView {
    private static final String TAG = VideogramsListFragmentView.class.getSimpleName();
    private static final String EXTRA_INIT_VIEW_VG_SCREEN_TYPE = "extra.init.view.vg.screen.type";

    private RecyclerView mVGGridView;
    private VideogramsOneLineAdapter mVideogramAdapter;

    @Override
    protected JBaseController setUpController() throws MissingControllerException {
        VideogramsListController controller = new VideogramsListController();
        try {
            controller.onCreate(this);
        } catch (MissingControllerException ex) {
            JLog.printStrackTrace(ex);
        }
        return controller;
    }

    /**
     * create new instance of videogram list fragment based on screen type :
     * <p/>
     * ON_DEVICE OR NOT_AVAIALBLE
     *
     * @param screenType
     * @return
     */
    public static VideogramsListFragmentView instance(SyncStatus screenType) {
        Bundle data = new Bundle();
        data.putInt(EXTRA_INIT_VIEW_VG_SCREEN_TYPE, screenType.ordinal());
        VideogramsListFragmentView view = new VideogramsListFragmentView();
        view.setArguments(data);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            SyncStatus screenType = SyncStatus.values()[getArguments().getInt(EXTRA_INIT_VIEW_VG_SCREEN_TYPE)];
            ((VideogramsListController) controller).initVariables(screenType);
        }

        View view = inflater.inflate(R.layout.videograms_list_layout, container, false);

        mVGGridView = (RecyclerView) view.findViewById(R.id.rlv_vg_gridview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mVGGridView.addItemDecoration(new HorizontalSpaceItemDecoration((int) getResources().getDimension(R.dimen.oneline_vg_decoration_space)));
        mVGGridView.setLayoutManager(layoutManager);

        mVideogramAdapter = new VideogramsOneLineAdapter(getActivity(), new OnVideogramsClickListener() {
            @Override
            public void onKeyframeClicked(Videograms videograms, int position) {
                /*Move to videoplayer screen*/
                ((VideogramsListController) controller).onVideogramsClicked(videograms, position);
            }

            @Override
            public void onRemoveButtonClicked(Videograms removeVideograms, int position) {
                /* Locally Remove the video */
                ((VideogramsListController) controller).onRemoveButtonClicked(removeVideograms, position);
            }

            @Override
            public void onSyncButtonClicked(Videograms syncVideograms, int position) {
                /* Move the video the ready to download */
                ((VideogramsListController) controller).onSyncButtonClicked(syncVideograms, position);
            }

            @Override
            public void onUndoButtonClicked(Videograms undoVideograms, int position) {
                /* move locally deleted Video back */
                ((VideogramsListController) controller).onUndoButtonClicked(undoVideograms, position);
            }
        });

        mVGGridView.setAdapter(mVideogramAdapter);
        return view;
    }

    public void refreshVideograms(final List<Videograms> videogrames) {
        mVGGridView.post(new Runnable() {
            @Override
            public void run() {
                if (videogrames == null || videogrames.size() == 0) {
                    mVGGridView.setVisibility(View.GONE);
                } else {
                    mVideogramAdapter.notifyDataSetChanged((ArrayList) videogrames);
                }
            }
        });
    }
}
