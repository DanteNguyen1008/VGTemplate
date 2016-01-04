package com.jpay.videograms.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anguyen on 12/11/2015.
 */
@SuppressLint("UseSparseArrays")
public class Contact implements Parcelable {
    public String id;
    public String name;
    /** This field's not handled in Parcelable */
    public HashMap<Integer, Videograms> videogrames;

    public List<Videograms> getVideogrames() {
        List<Videograms> outList = new ArrayList<Videograms>();

        for (Map.Entry<Integer, Videograms> entry : this.videogrames.entrySet()) {
            Videograms videograms = entry.getValue();
            videograms.contact = this;
            outList.add(videograms);
        }

        return outList;
    }

    /**
     * Update new videograms to contact list
     *
     * @param videograms
     * @return true if the new videogram is existed in the current contact list and able to update, otherwise return false
     */
    public boolean updateVGInContact(Videograms videograms) {
        for (Map.Entry<Integer, Videograms> entry : this.videogrames.entrySet()) {
            Videograms _videograms = entry.getValue();

            if(videograms.compareTo(_videograms) == 0) {
                /* set new value */
                entry.setValue(videograms);
                return true;
            }
        }

        return false;
    }

    public Contact() {}

    protected Contact(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
