package com.jpay.videograms.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.jpay.videograms.utils.SyncStatus;

import java.util.Date;

/**
 * Created by anguyen on 12/11/2015.
 */
@SuppressLint("UseSparseArrays")
public class Videograms implements Parcelable, Comparable<Videograms> {
    public String id;
    public String videoPath;
    public String keyframe;
    public Contact contact;
    public boolean isRead;
    public SyncStatus syncStatus;
    public Date createdDate;

    public Videograms() {}

    protected Videograms(Parcel in) {
        id = in.readString();
        videoPath = in.readString();
        keyframe = in.readString();
        contact = (Contact) in.readValue(Contact.class.getClassLoader());
        isRead = in.readByte() != 0x00;
        syncStatus = (SyncStatus) in.readValue(SyncStatus.class.getClassLoader());
        long tmpCreatedDate = in.readLong();
        createdDate = tmpCreatedDate != -1 ? new Date(tmpCreatedDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(videoPath);
        dest.writeString(keyframe);
        dest.writeValue(contact);
        dest.writeByte((byte) (isRead ? 0x01 : 0x00));
        dest.writeValue(syncStatus);
        dest.writeLong(createdDate != null ? createdDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Videograms> CREATOR = new Parcelable.Creator<Videograms>() {
        @Override
        public Videograms createFromParcel(Parcel in) {
            return new Videograms(in);
        }

        @Override
        public Videograms[] newArray(int size) {
            return new Videograms[size];
        }
    };

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Videograms another) {
        if(id.equals(another.id)) {
            return 0;
        }

        return -1;
    }
}
