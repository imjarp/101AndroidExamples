package com.example.jarp.aidlcallback;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JARP on 4/7/15.
 */
public class CustomData implements Parcelable {

    public static  final Creator<CustomData> CREATOR = new Creator<CustomData>() {

        @Override
        public CustomData createFromParcel(Parcel source) {
            CustomData customData = new CustomData();
            customData.mName = source.readString();
            customData.mReferences = new ArrayList<>();
            source.readStringList(customData.mReferences);
            customData.mCreated = new Date(source.readLong());
            return customData;
        }

        @Override
        public CustomData[] newArray(int size) {
            return new CustomData[size];
        }
    };


    private String mName ;
    private List<String> mReferences;
    private Date mCreated;

    public CustomData() {
        this.mName = "";
        this.mReferences =new ArrayList<>();
        this.mCreated = new Date();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mName);
        dest.writeStringList(mReferences);
        dest.writeLong(mCreated.getTime());


    }
}
