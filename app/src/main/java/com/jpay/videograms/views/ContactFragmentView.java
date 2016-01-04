package com.jpay.videograms.views;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpay.mvcvm.controllers.JBaseController;
import com.jpay.mvcvm.exceptions.MissingControllerException;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.videograms.R;
import com.jpay.videograms.adapters.ContactAdapter;
import com.jpay.videograms.controllers.ContactController;
import com.jpay.videograms.models.Contact;
import com.jpay.videograms.utils.Utils;

import java.util.ArrayList;

/**
 * Created by anguyen on 12/11/2015.
 */
public class ContactFragmentView extends JBaseFragmentView {

    private RecyclerView mContactListView;
    private ContactAdapter mAdapter;

    @Override
    protected JBaseController setUpController() throws MissingControllerException {
        ContactController controller = new ContactController();
        try {
            controller.onCreate(this);
            return controller;
        } catch (MissingControllerException ex) {
            JLog.printStrackTrace(ex);
        }
        return null;
    }

    public static ContactFragmentView instance() {
        return new ContactFragmentView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.contact_layout, container, false);

        mContactListView = (RecyclerView) view.findViewById(R.id.contact_list);
        mContactListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ContactAdapter(getActivity(), null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(true);
                int position = mContactListView.getChildAdapterPosition(v);
                /* Deselect other contacts */
                for (int i = 0; i < mContactListView.getChildCount(); i++) {
                    if (i != position) {
                        mContactListView.getChildAt(i).setSelected(false);
                    }
                }
                ((ContactController) controller).OnContactClicked(mAdapter.getItem(position));
            }
        });
        mContactListView.setAdapter(mAdapter);

        return view;
    }

    /**
     * Call from controller to refresh contact list
     *
     * @param contacts
     */
    public void refreshContactList(final ArrayList<Contact> contacts) {
        contacts.add(0, Utils.generateAllDefaultContact());
        mContactListView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged(contacts);
                /* Request VG for the default contact when the app is loaded  */
                if (contacts.size() > 0) {
                    ((ContactController) controller).OnLoadVGAtFirstTime(contacts.get(0));
                }
            }
        });
    }

    /**
     * Set selection for default contact UI
     */
    public void setSelectionForDefaultContact() {
        mContactListView.post(new Runnable() {
            @Override
            public void run() {
                if(mContactListView.getChildCount() > 0) {
                    mContactListView.getChildAt(0).setSelected(true);
                }
            }
        });
    }
}
